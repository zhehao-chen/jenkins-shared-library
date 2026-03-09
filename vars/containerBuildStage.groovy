def call(String imageName, String imageTag, String envTag) {
    echo "=== Container Build Stage ==="
    sh "docker build -t ${imageName}:${imageTag} -t ${imageName}:${envTag} ."
}
