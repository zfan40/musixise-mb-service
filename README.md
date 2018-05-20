# musixise-mb-service

启动方法

mvn -Pprod clean package  -Dmaven.test.skip=true -X

BUILD_ID=dontKillMe /var/lib/jenkins/spring-boot-jenkins/api-deploy.sh dev 8082 musixise-new application-localhost.yml
