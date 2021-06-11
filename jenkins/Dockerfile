# https://velog.io/@hind_sight/Docker-Jenkins-%EB%8F%84%EC%BB%A4%EC%99%80-%EC%A0%A0%ED%82%A8%EC%8A%A4%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-Spring-Boot-CICD
# https://devops4solutions.com/ci-cd-using-jenkins-and-docker-2/
# https://www.jenkins.io/doc/book/pipeline/docker/

FROM jenkins/jenkins:jdk11

#도커를 실행하기 위한 root 계정으로 전환
USER root

#도커 설치
COPY docker_install.sh /docker_install.sh
RUN chmod +x /docker_install.sh
RUN /docker_install.sh

#설치 후 도커그룹의 jenkins 계정 생성 후 해당 계정으로 변경
RUN groupadd -f docker
RUN usermod -aG docker jenkins
USER jenkins

#docker run -d -p 9090:9090 -v /var/run/docker.sock:/var/run/docker.sock -v /home/ubuntu/jenkins/jenkins_home:/var/jenkins_home -v /home/ubuntu/jenkins/info:/info wndmstla/roomescape-jenkins:21
