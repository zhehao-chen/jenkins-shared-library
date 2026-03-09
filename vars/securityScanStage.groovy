def call(Map config = [:]) {
    stage('Security Scan') {
        sh 'echo "Running security scan..."'
    }
}
