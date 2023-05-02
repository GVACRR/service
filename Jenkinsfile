pipeline {
    agent any
    environment {
        DOCKER_CREDENTIALS = credentials("docker-hub-credentials")
        GITHUB_TOKEN = credentials("github_token")
    }

    stages {
        stage('Build & Test') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Tag image') {
            steps {
                script {
                   sh([script: 'git fetch --tag', returnStdout: true]).trim()
                   env.MAJOR_VERSION = sh([script: 'git tag | sort --version-sort | tail -1 | cut -d . -f 1', returnStdout: true]).trim()
                   env.MINOR_VERSION = sh([script: 'git tag | sort --version-sort | tail -1 | cut -d . -f 2', returnStdout: true]).trim()
                   env.PATCH_VERSION = sh([script: 'git tag | sort --version-sort | tail -1 | cut -d . -f 3', returnStdout: true]).trim()
                   env.IMAGE_TAG = "${env.MAJOR_VERSION}.\$((${env.MINOR_VERSION} + 1)).${env.PATCH_VERSION}"
                }
            sh "docker build -t $DOCKER_CREDENTIALS_USR/hello-img:${env.IMAGE_TAG} ."

            sh "docker login docker.io -u $DOCKER_CREDENTIALS_USR -p $DOCKER_CREDENTIALS_PSW"
            sh "docker push $DOCKER_CREDENTIALS_USR/hello-img:${env.IMAGE_TAG}"

            sh "git tag ${env.IMAGE_TAG}"
            sh "git push https://$GITHUB_TOKEN@github.com/GVACRR/service.git ${env.IMAGE_TAG}"
          }
        }

        stage('Start the container and run integration tests') {
            steps {
                sh "IMAGE_TAG=${env.IMAGE_TAG} DOCKER_HUB_USERNAME=$DOCKER_CREDENTIALS_USR docker-compose up -d hello"
                sh './gradlew testIT'
                sh './gradlew testE2E'
            }
        }
    }
}