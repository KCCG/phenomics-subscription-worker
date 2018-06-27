```

___  _  _ ____ _  _ ____ _  _ _ ____ ____    ____ _  _ ___  ____ ____ ____ _ ___  ___ _ ____ _  _    _ _ _ ____ ____ _  _ ____ ____
|__] |__| |___ |\ | |  | |\/| | |    [__     [__  |  | |__] [__  |    |__/ | |__]  |  | |  | |\ |    | | | |  | |__/ |_/  |___ |__/
|    |  | |___ | \| |__| |  | | |___ ___]    ___] |__| |__] ___] |___ |  \ | |     |  | |__| | \|    |_|_| |__| |  \ | \_ |___ |  \


```

Introduction
============
Phenmomic-Subscriptoion-Worker is a Java8 Cron Application. It is responsible to fetch the latest articles for subscribed users and dispatch them using Phenomics-Notification.

Following are key components for this service:

**Runner**
* Runs every day and checks the valid subscriptions.
* Query search engine and fetches articles matching the subscription query criteria.
* Generates a comprehensive email and dispatch it to Phenomics-Notification.

**Interfaces**

There are no interfaces as this runs in scheduled container on ECS.


Setup
=====
1. The build tool is gradle to keep dependencies clean and build process simple.
2. This is a dockerized application. Gradle is used to produce Docker image. This makes Docker container very lean and no port bindings are given at this stage. This is to facilitate deployment on ECS.

Deployment
==========
1. DevOps code contains script to build docker image, tag it and publish it to ECR.
2. DevOps requires AWS CLI and credentials in environment variables.


Install in Intellij
===================
This project uses Lombok and you need to install the following to get it to work:
* Lombok plugin
* Enable Annotation processing by going to:
Preferences -> Build, execution, deployment, compiler, Annotation Processors
Creating a profile for the project and enabling it

https://www.jetbrains.com/help/idea/2016.1/configuring-annotation-processing.html

Rebuild and then you should be good to go.


