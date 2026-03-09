def call(Map config = [:]) {
    stage('Container Push') {
        sh 'echo "Pushing container image..."'
    }
}
