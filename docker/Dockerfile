FROM java:openjdk-8-jdk
MAINTAINER Heiner Peuser <heiner.peuser@weweave.net>

ENV LANG C.UTF-8
ENV HOME /root
ENV DEBIAN_FRONTEND noninteractive
ENV TW_HOME /opt/tubewarder

RUN apt-get update && apt-get install -y \
    mysql-server \
    supervisor

ADD my.cnf /etc/mysql/
ADD supervisord.conf /etc/supervisor/conf.d/supervisord.conf

RUN useradd -d $TW_HOME tubewarder
RUN mkdir -p $TW_HOME
ADD run.sh $TW_HOME
ADD tubewarder-swarm.jar $TW_HOME
RUN chown -R tubewarder:tubewarder $TW_HOME

EXPOSE 8080

RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

ENTRYPOINT ["bin/bash", "/opt/tubewarder/run.sh"]
