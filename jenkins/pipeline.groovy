pipeline {
    agent any

    stages {
        stage('Execute Gradle') {
            steps {
                git branch: 'master', url: 'https://github.com/SimEunJu/RoomEscapeBack.git'
                withCredentials([
                        file(credentialsId: 'application-security', variable: 'SECURITY_PROPERTIES'),
                        file(credentialsId: 'application-credentials', variable: 'CREDENTIALS_PROPERTIES')
                ]) {
                    sh 'cp \$SECURITY_PROPERTIES ./src/main/resources/application-security.properties'
                    sh 'cp \$CREDENTIALS_PROPERTIES ./src/main/resources/application-credentials.properties'
                    sh 'chmod 644 ./src/main/resources/application-security.properties'
                    sh 'chmod 644 ./src/main/resources/application-credentials.properties'
                }
                sh 'chmod +x gradlew'
                sh './gradlew build -x test'
                sh 'cp ./build/libs/*.jar ./build/libs/deploy/app.jar'
                sh 'java -jar ./build/libs/deploy/app.jar'
            }
        }
    }
}