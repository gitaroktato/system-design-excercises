# Startup
Installing plugins
```shell
docker compose cp jenkins/plugins.txt jenkins-blueocean:/var/jenkins_home
docker compose exec jenkins-blueocean sh -c 'jenkins-plugin-cli -f ~/plugins.txt'
```

Get the initial admin password by running
```shell
docker compose exec jenkins-blueocean sh -c 'cat /var/jenkins_home/secrets/initialAdminPassword'
```

Go to `http://<DOCKER_HOST>:8080/blue` and enter the password from previous step

# References
Installing compose standalone 
- https://docs.docker.com/compose/install/standalone/

Jenkins plugins
- https://github.com/fabric8io/jenkins-docker/blob/master/plugins.txt
- https://hub.docker.com/r/jenkinsci/blueocean
- https://hub.docker.com/r/jenkins/jenkins
- https://stackoverflow.com/questions/9815273/how-to-get-a-list-of-installed-jenkins-plugins-with-name-and-version-pair
- https://gist.github.com/noqcks/d2f2156c7ef8955619d45d1fe6daeaa9
