# 基于那个
FROM java:8-jre
# 维修人员
MAINTAINER zhangtao

# 将本地文件夹挂载到当前容器
VOLUME /Users/zhangtao/Documents/deploy/discovery

# 添加 jar 到 docker
ADD ./target/discovery-service-1.0-SNAPSHOT.jar /app/
# 命令
CMD ["java", "-Xmx200m", "-jar", "/app/discovery-service-1.0-SNAPSHOT.jar"]
# 映射端口
EXPOSE 8761