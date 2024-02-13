# Startup
Running CICD
```shell
docker compose up -d
```

Get the initial admin password by running
```shell
docker compose exec jenkins sh -c 'cat /var/jenkins_home/secrets/initialAdminPassword'
```

Go to `http://<DOCKER_HOST>:8080/blue` and enter the password from previous step

# References
Installing compose standalone 
- https://docs.docker.com/compose/install/standalone/

Working with Jenkins plugins
- https://github.com/fabric8io/jenkins-docker/blob/master/plugins.txt
- https://hub.docker.com/r/jenkinsci/blueocean
- https://hub.docker.com/r/jenkins/jenkins
- https://stackoverflow.com/questions/9815273/how-to-get-a-list-of-installed-jenkins-plugins-with-name-and-version-pair
- https://gist.github.com/noqcks/d2f2156c7ef8955619d45d1fe6daeaa9

Installing Jenkins with Docker
- https://www.jenkins.io/doc/book/installing/docker/

# ECS and ECR
- https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ECS_AWSCLI_Fargate.html
- https://docs.aws.amazon.com/AmazonECS/latest/developerguide/creating-resources-with-cloudformation.html
- https://docs.aws.amazon.com/AmazonECS/latest/developerguide/example_task_definitions.html#example_task_definition-iam
- https://docs.aws.amazon.com/AmazonECS/latest/developerguide/AWS_Fargate.html
- https://github.com/aws-samples/ecs-refarch-cloudformation

# AWS Configs
- https://docs.aws.amazon.com/cli/latest/userguide/cli-usage-pagination.html#cli-usage-pagination-nopaginate