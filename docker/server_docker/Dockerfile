FROM java:8
MAINTAINER Daryll  
ADD ["https://github.com/Nyahon/PRO/blob/docker_server/server/blob/server_jar/server.jar?raw=true", "/opt/"]
ADD ["https://github.com/Nyahon/PRO/blob/prod_server/server/gaps_global_S2_2017_2018.ics?raw=true" "/opt/server"]
EXPOSE 2613
ENTRYPOINT ["java","-jar", "/opt/server.jar"]
