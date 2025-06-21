# Stage 1: Build Angular app
FROM node:20-alpine AS build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ .
RUN npm run build -- --configuration=production

# Stage 2: Serve with nginx
FROM nginx:alpine
COPY --from=build /app/dist/retro-vania/browser /usr/share/nginx/html
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf
# COPY dist/retro-vania/browser /usr/share/nginx/html
# COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
