# Build stage
FROM node:20-alpine AS build
WORKDIR /app

# Copy package files
COPY frontend-react/package*.json ./
RUN npm install

# Copy source code
COPY frontend-react/ .

# Build
RUN npm run build

# Run stage
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY frontend-react/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
