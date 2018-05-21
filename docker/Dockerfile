FROM java:8
MAINTAINER Daryll  
ADD ["https://github.com/Nyahon/PRO/blob/docker_server/server/blob/server_jar/server.jar?raw=true", "/opt/"]
EXPOSE 2613
ENTRYPOINT ["java","-jar", "/opt/server.jar"]
