pipeline {

    agent none

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout') {
            agent any
            steps {
                checkout scm
            }
        }

        stage('Build Services (Maven)') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17'
                    args '--memory=1g --memory-swap=1g'
                }
            }

            parallel {

                stage('Anggota Service') {
                    steps {
                        dir('anggota-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }

                stage('Buku Service') {
                    steps {
                        dir('buku-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }

                stage('Peminjaman Command') {
                    steps {
                        dir('peminjaman-command-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }

                stage('Peminjaman Query') {
                    steps {
                        dir('peminjaman-query-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }

                stage('Pengembalian Service') {
                    steps {
                        dir('pengembalian-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }

                stage('RabbitMQ Peminjaman') {
                    steps {
                        dir('rabbitmq-peminjaman-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }

                stage('RabbitMQ Pengembalian') {
                    steps {
                        dir('rabbitmq-pengembalian-service') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }

                stage('API Gateway') {
                    steps {
                        dir('api-gateway-pustaka') {
                            sh 'chmod +x mvnw'
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Docker Build') {
            agent any
            steps {
                sh 'docker compose build'
            }
        }

        stage('Deploy') {
            agent any
            steps {
                sh 'docker compose up -d'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}

