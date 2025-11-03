package com.example.ecommerce.service;

import com.example.ecommerce.search.ProductDocument;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProductSearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public List<ProductDocument> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        // Step 1: Normalize keyword
        String cleanedKeyword = keyword.toLowerCase(Locale.ROOT).trim();

        // Step 2: Extract numeric price range
        PriceRange priceRange = extractPriceRange(cleanedKeyword);

        // Step 3: Remove numeric/price phrases from keyword
        String textQuery = cleanedKeyword
                .replaceAll("(under|below|less than|upto|underneath|over|above|greater than|between)\\s*\\d+(\\s*(and|to)\\s*\\d+)?", "")
                .replaceAll("\\d+", "")
                .trim();

        // Step 4: Build query
        var queryBuilder = new NativeQueryBuilder()
                .withQuery(q -> q
                        .multiMatch(m -> m
                                .fields("name", "categoryName")
                                .query(textQuery)
                                .fuzziness("AUTO")
                                .operator(Operator.Or)
                        )
                );

        // Step 5: Apply price filter(s)
        if (priceRange.hasRange()) {
            queryBuilder.withFilter(f -> f
                    .range(r -> r
                            .field("discountedPrice")
                            .gte(priceRange.getMin() > 0 ? JsonData.of(priceRange.getMin()) : null)
                            .lte(priceRange.getMax() > 0 ? JsonData.of(priceRange.getMax()) : null)
                    )
            );
        }

        var query = queryBuilder.build();

        // Step 6: Execute search
        SearchHits<ProductDocument> hits = elasticsearchOperations.search(query, ProductDocument.class);

        // Step 7: Return results
        return hits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }

    /**
     * Extracts a price range from phrases like:
     * "under 20000", "above 10000", "between 20000 and 50000"
     */
    private PriceRange extractPriceRange(String keyword) {
        double min = 0, max = 0;

        // Pattern for "between X and Y" or "between X to Y"
        Pattern betweenPattern = Pattern.compile("between\\s*(\\d+)\\s*(and|to)\\s*(\\d+)");
        Matcher betweenMatcher = betweenPattern.matcher(keyword);
        if (betweenMatcher.find()) {
            min = Double.parseDouble(betweenMatcher.group(1));
            max = Double.parseDouble(betweenMatcher.group(3));
            return new PriceRange(min, max);
        }

        // Pattern for "under", "below", "less than", etc.
        Pattern underPattern = Pattern.compile("(under|below|less than|upto|underneath)\\s*(\\d+)");
        Matcher underMatcher = underPattern.matcher(keyword);
        if (underMatcher.find()) {
            max = Double.parseDouble(underMatcher.group(2));
            return new PriceRange(0, max);
        }

        // Pattern for "above", "over", "greater than"
        Pattern abovePattern = Pattern.compile("(above|over|greater than)\\s*(\\d+)");
        Matcher aboveMatcher = abovePattern.matcher(keyword);
        if (aboveMatcher.find()) {
            min = Double.parseDouble(aboveMatcher.group(2));
            return new PriceRange(min, 0);
        }

        return new PriceRange(0, 0);
    }

    /**
     * Simple helper record for min/max price
     */
    private static class PriceRange {
        private final double min;
        private final double max;

        public PriceRange(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public boolean hasRange() {
            return min > 0 || max > 0;
        }
    }
}
