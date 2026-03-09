def call(Map config = [:]) {
    def state = config.state ?: 'success'
    def description = config.description ?: 'Build finished'
    sh "echo \"Notifying GitHub: state=${state}, description=${description}\""
}
