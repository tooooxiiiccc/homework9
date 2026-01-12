pipeline {
    agent any
    
    parameters {
        choice(
            name: 'TAG',
            choices: ['smoke', 'regress', 'all'],
            description: 'Выберите набор тестов для запуска'
        )
        string(
            name: 'PARALLEL_THREADS',
            defaultValue: '4',
            description: 'Количество параллельных потоков'
        )
    }
    
    environment {
        PARALLEL_THREADS = "${params.PARALLEL_THREADS}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    if (env.GIT_BRANCH) {
                        checkout scm
                    } else {
                        echo 'Используется текущая рабочая директория'
                    }
                }
            }
        }
        
        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }
        
        stage('Tests') {
            steps {
                script {
                    def tagFilter = params.TAG == 'all' ? '' : "-Ptag=${params.TAG}"
                    
                    sh """
                        ./gradlew test ${tagFilter} \
                            -Dparallel.threads=${PARALLEL_THREADS}
                    """
                }
            }
        }
        
        stage('Allure Report') {
            steps {
                script {
                    sh './gradlew allureReport'
                }
            }
            post {
                always {
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'build/allure-results']]
                    ])
                }
            }
        }
    }
    
    post {
        always {
            archiveArtifacts artifacts: 'build/allure-results/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'build/reports/**/*', allowEmptyArchive: true
        }
        failure {
            echo 'Тесты завершились с ошибками'
        }
        success {
            echo 'Все тесты прошли успешно'
        }
    }
}

