node('codebuild-small') {
  stage('Checkout') {
    checkout scm
  }
  stage('Build') {
    docker.image('maven').inside {
      sh 'mvn clean install'
    }
  }
}