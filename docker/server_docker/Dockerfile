FROM java:8
MAINTAINER Daryll  
ADD ["https://github.com/Nyahon/PRO/tree/prod_server/server/blob/server_jar?raw=true", "/opt/"]
ADD ["https://raw.githubusercontent.com/Nyahon/PRO/prod_server/server/gaps_global_S2_2017_2018.ics?raw=true", "/opt/server/"]
EXPOSE 2613
ENTRYPOINT ["java","-jar", "/opt/server.jar"]
