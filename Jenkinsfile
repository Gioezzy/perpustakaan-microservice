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
            parallel {
                stage('Anggota Service') {
                    agent any
                    steps {
                        script {
                            docker.image('maven:3.9.6-eclipse-temurin-17').inside {
                                dir('anggota-service') {
                                    sh 'chmod +x mvnw'
                                    sh './mvnw clean package -DskipTests'
                                }
                            }
                        }
                    }
                }
                stage('Buku Service') {
                    agent any
                    steps {
                        script {
                            docker.image('maven:3.9.6-eclipse-temurin-17').inside {
                                dir('buku-service') {
                                    sh 'chmod +x mvnw'
                                    sh './mvnw clean package -DskipTests'
                                }
                            }
                        }
                    }
                }
                stage('Peminjaman Command') {
                    agent any
                    steps {
                        script {
                            docker.image('maven:3.9.6-eclipse-temurin-17').inside {
                                dir('peminjaman-command-service') {
                                    sh 'chmod +x mvnw'
                                    sh './mvnw clean package -DskipTests'
                                }
                            }
                        }
                    }
                }
                stage('Peminjaman Query') {
                    agent any
                    steps {
                        script {
                            docker.image('maven:3.9.6-eclipse-temurin-17').inside {
                                dir('peminjaman-query-service') {
                                    sh 'chmod +x mvnw'
                                    sh './mvnw clean package -DskipTests'
                                }
                            }
                        }
                    }
                }
                stage('Pengembalian Service') {
                    agent any
                    steps {
                        script {
                            docker.image('maven:3.9.6-eclipse-temurin-17').inside {
                                dir('pengembalian-service') {
                                    sh 'chmod +x mvnw'
                                    sh './mvnw clean package -DskipTests'
                                }
                            }
                        }
                    }
                }
                stage('API Gateway') {
                    agent any
                    steps {
                        script {
                            docker.image('maven:3.9.6-eclipse-temurin-17').inside {
                                dir('api-gateway-pustaka') {
                                    sh 'chmod +x mvnw'
                                    sh './mvnw clean package -DskipTests'
                                }
                            }
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
            node('any') {
                cleanWs()
            }
        }
    }
}