#!groovy
pipeline {
//    agent {
//        docker {
//            image 'eclipse-temurin:18.0.2.1_1-jdk'
//            args '--network ci --mount type=volume,source=ci-maven-home,target=/root/.m2'
//        }
//    }

    environment {
        PATH = '/usr/local/bin'
        ORG_NAME = 'ufoalan'
        NAME_SERVICE_APP_NAME = 'name-service'
        GREETINGS_SERVICE_APP_NAME = 'greetings-service'
        APP_VERSION = '0.4.0'
        APP_CONTEXT_ROOT = 'camel'
        APP_NAME_SERVICE_LISTENING_PORT = '8088'
        APP_GREETINGS_SERVICE_LISTENING_PORT = '8089'
        TEST_NAME_SERVICE_CONTAINER_NAME = "ci-${NAME_SERVICE_APP_NAME}-${BUILD_NUMBER}"
        TEST_GREETINGS_SERVICE_CONTAINER_NAME = "ci-${GREETINGS_SERVICE_APP_NAME}-${BUILD_NUMBER}"
        DOCKER_HUB = credentials("${ORG_NAME}")
    }

    stages {
/*
        stage('Prepare Environment') {
            steps {
                script {
                    qualityGates = readYaml file: 'quality-gates.yaml'
                }
            }
*/
        }

        stage('Compile') {
            steps {
                echo '-=- compiling project -=-'
                sh './mvnw clean compile'
            }
        }

        stage('Unit tests') {
            steps {
                echo '-=- execute unit tests -=-'
//                sh './mvnw test org.jacoco:jacoco-maven-plugin:report'
//                junit 'target/surefire-reports/*.xml'
//                jacoco execPattern: 'target/jacoco.exec'
            }
        }

/*
        stage('Mutation tests') {
            steps {
                echo '-=- execute mutation tests -=-'
                sh './mvnw org.pitest:pitest-maven:mutationCoverage'
            }
        }
*/

        stage('Package') {
            steps {
                echo '-=- packaging project -=-'
                sh './mvnw package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Name Service Docker image') {
            steps {
                echo '-=- build name service Docker image -=-'
                sh "${PATH}/docker build -t ${ORG_NAME}/${NAME_SERVICE_APP_NAME}:${APP_VERSION} -t ${ORG_NAME}/${NAME_SERVICE_APP_NAME}:${APP_VERSION} ./name-service"
            }
        }

        stage('Run Name Service Docker image') {
            steps {
                echo '-=- run name service Docker image -=-'
                sh "${PATH}/docker run --name ${TEST_NAME_SERVICECONTAINER_NAME} --detach --rm --network ci -p8088:8080 ${ORG_NAME}/${NAME_SERVICEAPP_NAME}:${APP_VERSION}"
//                sh 'curl http://localhost:8080/camel/name
            }
        }

        stage('Build Greetings Service Docker image') {
            steps {
                echo '-=- build greetings service Docker image -=-'
                sh "${PATH}/docker build -t ${ORG_NAME}/${GREETINGS_SERVICE_APP_NAME}:${APP_VERSION} -t ${ORG_NAME}/${GREETINGS_SERVICE_APP_NAME}:${APP_VERSION} ./greetings-service"
            }
        }

        stage('Run Greetings Service Docker image') {
            steps {
                echo '-=- run greetingsservice Docker image -=-'
                sh "${PATH}/docker run --name ${TEST_GREETINGS_SERVICE_CONTAINER_NAME} --detach --rm --network ci -p8089:8080 ${ORG_NAME}/${GREETINGS_SERVICE_APP_NAME}:${APP_VERSION}"
            }
        }

        stage('Integration tests') {
            steps {
                echo '-=- execute integration tests -=-'
                sh "curl --retry 5 --retry-connrefused --connect-timeout 5 --max-time 5 http://${TEST_NAME_SERVICE_CONTAINER_NAME}:${APP_NAME_SERVICE_LISTENING_PORT}/${APP_CONTEXT_ROOT}/actuator/health"
                sh "curl --retry 5 --retry-connrefused --connect-timeout 5 --max-time 5 http://${TEST_NAME_SERVICE_CONTAINER_NAME}:${APP_NAME_SERVICE_LISTENING_PORT}/${APP_CONTEXT_ROOT}/name"
                sh "curl --retry 5 --retry-connrefused --connect-timeout 5 --max-time 5 http://${TEST_GREETINGS_SERVICE_CONTAINER_NAME}:${APP_GREETINGS_SERVICE_LISTENING_PORT}/${APP_CONTEXT_ROOT}/actuator/health"
                sh "curl --retry 5 --retry-connrefused --connect-timeout 5 --max-time 5 http://${TEST_GRETTINGS_SERVICE_CONTAINER_NAME}:${APP_GREETINGS_SERVICE_LISTENING_PORT}/${APP_CONTEXT_ROOT}/greetings"
//                sh "./mvnw failsafe:integration-test failsafe:verify -DargLine=\"-Dtest.selenium.hub.url=http://selenium-hub:4444/wd/hub -Dtest.target.server.url=http://${TEST_CONTAINER_NAME}:${APP_LISTENING_PORT}/${APP_CONTEXT_ROOT}\""
//                sh "java -jar target/dependency/jacococli.jar dump --address ${TEST_CONTAINER_NAME} --port 6300 --destfile target/jacoco-it.exec"
//                sh 'mkdir target/site/jacoco-it'
//                sh 'java -jar target/dependency/jacococli.jar report target/jacoco-it.exec --classfiles target/classes --xml target/site/jacoco-it/jacoco.xml'
//                junit 'target/failsafe-reports/*.xml'
//                jacoco execPattern: 'target/jacoco-it.exec'
            }
        }

        stage('Performance tests') {
            steps {
                echo '-=- execute performance tests -=-'
/*
                sh "./mvnw jmeter:configure@configuration jmeter:jmeter jmeter:results -Djmeter.target.host=${TEST_CONTAINER_NAME} -Djmeter.target.port=${APP_LISTENING_PORT} -Djmeter.target.root=${APP_CONTEXT_ROOT}"
                perfReport(
                    sourceDataFiles: 'target/jmeter/results/*.csv',
                    errorUnstableThreshold: qualityGates.performance.throughput.error.unstable,
                    errorFailedThreshold: qualityGates.performance.throughput.error.failed,
                    errorUnstableResponseTimeThreshold: qualityGates.performance.throughput.response.unstable)
*/
            }
        }

        // stage('Web page performance analysis') {
        //     steps {
        //         echo '-=- execute web page performance analysis -=-'
        //         sh 'apt-get update'
        //         sh 'apt-get install -y gnupg'
        //         sh 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" | tee -a /etc/apt/sources.list.d/google.list'
        //         sh 'curl -sL https://dl.google.com/linux/linux_signing_key.pub | apt-key add -'
        //         sh 'curl -sL https://deb.nodesource.com/setup_10.x | bash -'
        //         sh 'apt-get install -y nodejs google-chrome-stable'
        //         sh 'npm install -g lighthouse@5.6.0'
        //         sh "lighthouse http://${TEST_CONTAINER_NAME}:${APP_LISTENING_PORT}/${APP_CONTEXT_ROOT}/hello --output=html --output=csv --chrome-flags=\"--headless --no-sandbox\""
        //         archiveArtifacts artifacts: '*.report.html'
        //         archiveArtifacts artifacts: '*.report.csv'
        //     }
        // }

/*
        stage('Dependency vulnerability scan') {
            steps {
                echo '-=- run dependency vulnerability tests -=-'
                sh './mvnw dependency-check:check'
                dependencyCheckPublisher(
                    failedTotalCritical: qualityGates.security.dependencies.critical.failed,
                    unstableTotalCritical: qualityGates.security.dependencies.critical.unstable,
                    failedTotalHigh: qualityGates.security.dependencies.high.failed,
                    unstableTotalHigh: qualityGates.security.dependencies.high.unstable,
                    failedTotalMedium: qualityGates.security.dependencies.medium.failed,
                    unstableTotalMedium: qualityGates.security.dependencies.medium.unstable)
                script {
                    if (currentBuild.result == 'FAILURE') {
                        error('Dependency vulnerabilities exceed the configured threshold')
                    }
                }
            }
        }

        stage('Code inspection & quality gate') {
            steps {
                echo '-=- run code inspection & check quality gate -=-'
                withSonarQubeEnv('ci-sonarqube') {
                    sh './mvnw sonar:sonar'
                }
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
*/

        stage('Push Name Service Docker image') {
            steps {
                echo '-=- push Docker image -=-'
                withDockerRegistry([ credentialsId: "${ORG_NAME}", url: "" ]) {
                    sh "${PATH}/docker tag ${ORG_NAME}/${NAME_SERVICE_APP_NAME}:${APP_VERSION} ${ORG_NAME}/${NAME_SERVICE_APP_NAME}:latest"
                    sh "${PATH}/docker push ${ORG_NAME}/${NAME_SERVICE_APP_NAME}:${APP_VERSION}"
                    sh "${PATH}/docker push ${ORG_NAME}/${NAME_SERVICE_APP_NAME}:latest"
                }
            }
        }

        stage('Push Greetings Service Docker image') {
            steps {
                echo '-=- push Greetings Service Docker image -=-'
                withDockerRegistry([ credentialsId: "${ORG_NAME}", url: "" ]) {
                    sh "${PATH}/docker tag ${ORG_NAME}/${GREETINGS_SERVICE_APP_NAME}:${APP_VERSION} ${ORG_NAME}/${GREETINGS_SERVICE_APP_NAME}:latest"
                    sh "${PATH}/docker push ${ORG_NAME}/${GREETINGS_SERVICE_APP_NAME}:${APP_VERSION}"
                    sh "${PATH}/docker push ${ORG_NAME}/${GREETINGS_SERVICE_APP_NAME}:latest"
                }
            }
        }
    }

    post {
        always {
            echo '-=- remove deployment -=-'
            sh "${PATH}/docker stop ${TEST_NAME_SERVICE_CONTAINER_NAME}"
            sh "${PATH}/docker stop ${TEST_GREETINGS_SERVICE_CONTAINER_NAME}"
            sh "${PATH}/docker rm ${TEST_NAME_SERVICE_CONTAINER_NAME}"
            sh "${PATH}/docker rm ${TEST_GREETINGS_SERVICE_CONTAINER_NAME}"
        }
    }
}
