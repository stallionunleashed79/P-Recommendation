FROM amazoncorretto:11.0.7@sha256:c97a1d8c218c5e0c25ff71c1791d72c55734c1366b51c6308e6320c2da924530

RUN yum install -y python3-pip openssl && pip3 install awscli && yum update -y
RUN mkdir /home/app && mkdir /home/app/secrets

WORKDIR /home/app

COPY target/reco.jar /home/app/reco.jar
COPY scripts/startup.sh /home/app/startup.sh

EXPOSE 8080
EXPOSE 443

ENTRYPOINT ["/bin/bash"]
CMD ["/home/app/startup.sh"]