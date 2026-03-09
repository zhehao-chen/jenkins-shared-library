def call(String environment, String imageName, String imageTag) {
    echo "=== Deploy Stage: ${environment} | image: ${imageName}:${imageTag} ==="
    echo "Deployed ${imageName}:${imageTag} to ${environment}"
}
