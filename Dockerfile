#!/bin/bash
FROM fedora

# install firefox
RUN dnf install -y firefox
    
# install dependancies
RUN dnf install -y libcanberra-gtk3 PackageKit-gtk3-module \
	dbus dbus-devel dbus-x11 java
RUN dbus-uuidgen --ensure

# make uid and gid match inside and outside the container
# replace 1000 with your gid/uid, find them by running
# the id command
RUN export uid=1000 gid=1000 && \
	mkdir -p /home/firefox && \
	echo "firefox:x:${uid}:${gid}:Developer,,,:/home/firefox:/bin/bash" >> /etc/passwd  && \
	echo "firefox:x:${uid}:" >> /etc/group  && \
	echo "firefox ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers  && \
	chmod 0440 /etc/sudoers  && \
	chown ${uid}:${gid} -R /home/firefox

#remove cache from the image to shrink it a bit
RUN dnf clean all

# set up and run firefox
USER firefox
ENV HOME /home/firefox
    
ENV PATH="/usr/bin/firefox:${PATH}"    

ADD build/libs/*.jar app.jar
ADD geckodriverLinux geckodriver

ENV JAVA_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dwebdriver.gecko.driver=geckodriver -jar /app.jar" ]

# HEALTHCHECK CMD curl -f http://localhost:8080/health || exit 1;