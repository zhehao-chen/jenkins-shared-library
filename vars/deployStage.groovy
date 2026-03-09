def call(String environment, String imageName, String imageTag) {
    echo "=== Deploy Stage: ${environment} | image: ${imageName}:${imageTag} ==="

    def namespace = environment  // dev, staging, prod

    sh """
        # Replace IMAGE_TAG_PLACEHOLDER with the actual image tag
        sed -i.bak 's|IMAGE_TAG_PLACEHOLDER|${imageTag}|g' k8s/deployment.yaml

        # Apply all manifests in the k8s directory
        kubectl apply -f k8s/ --namespace=${namespace}

        # Wait for rollout to complete (timeout 3 minutes)
        kubectl rollout status deployment/${imageName.split('/')[1]} \
            --namespace=${namespace} \
            --timeout=180s

        # Restore original file so workspace stays clean for next run
        mv k8s/deployment.yaml.bak k8s/deployment.yaml
    """

    echo "Successfully deployed ${imageName}:${imageTag} to ${environment}"
}
