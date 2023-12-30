FROM nginx:latest

RUN rm -rf /etc/nginx/conf.d/default.conf

COPY ./conf.d/default.conf  /etc/nginx/conf.d/default.conf
COPY ./conf.d/nginx.conf  /etc/nginx/nginx.conf

VOLUME ["/data", "/etc/nginx", "/var/log/nginx"]

WORKDIR /etc/nginx

EXPOSE 80