#!/bin/sh
# https://www.youtube.com/watch?v=pMO26j2OUME


docker run \
   --publish 8080:8080 \
   -p 50000:50000 \
   -d \
   -v jenkins_home:/var/jenkins_home \
   jenkins/jenkins:lts




# After, run 'docker logs <container-id>' and look for the password it gives
# Then, go to localhost:8080 (or your port) and put that in
 
