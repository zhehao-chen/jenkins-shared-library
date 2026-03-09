def call(Map config = [:]) {
    stage('Test') {
        sh 'echo "Running tests..."'
    }
}
