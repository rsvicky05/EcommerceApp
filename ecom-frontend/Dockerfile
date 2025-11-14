# ecom-frontend/Dockerfile
# build stage
FROM node:18-alpine AS build
WORKDIR /app

# enable corepack (pnpm)
RUN corepack enable

COPY package.json pnpm-lock.yaml ./
# Install dependencies with pnpm using lockfile
RUN corepack prepare pnpm@latest --activate \
  && pnpm install --frozen-lockfile

COPY . .
# Accept API base URL as build arg (container name 'backend' used in compose)
ARG REACT_APP_API_URL=http://backend:8080
ENV REACT_APP_API_URL=${REACT_APP_API_URL}

RUN pnpm run build

# runtime stage using nginx to serve static files
FROM nginx:stable-alpine
# copy built files
COPY --from=build /app/build /usr/share/nginx/html

# copy custom nginx config
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
