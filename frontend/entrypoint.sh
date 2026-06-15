#!/bin/sh
# env.js schrijven
cat > /usr/share/nginx/html/assets/env.js << EOF
(function(window) {
  window.__env = {
    baseApiUrl: '${API_URL}'
  };
}(this));
EOF

# nginx.conf genereren vanuit template
envsubst '${API_URL} ${ALLOWED_ORIGIN} ${BACKEND_URL}' < /etc/nginx/templates/nginx.conf.template > /etc/nginx/conf.d/default.conf

nginx -g 'daemon off;'
