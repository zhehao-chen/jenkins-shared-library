def call(String environment, String imageName, String imageTag) {
    echo "=== Deploy Stage: ${environment} | image: ${imageName}:${imageTag} ==="

    def namespace = environment  // dev, staging, prod

    // NodePort offset per environment: dev=0, staging=+10, prod=+20
    def nodePortOffset = environment == 'staging' ? 10 : environment == 'prod' ? 20 : 0

    sh """
        # Replace IMAGE_TAG_PLACEHOLDER, namespace, and NodePorts in deployment.yaml
        sed -i.bak \
            -e 's|IMAGE_TAG_PLACEHOLDER|${imageTag}|g' \
            -e 's|namespace: dev|namespace: ${namespace}|g' \
            k8s/deployment.yaml

        # Shift NodePorts for staging (+10) and prod (+20)
        if [ ${nodePortOffset} -gt 0 ]; then
            for port in 30000 30001 30002; do
                newPort=\$((port + ${nodePortOffset}))
                sed -i '' "s|nodePort: \${port}|nodePort: \${newPort}|g" k8s/deployment.yaml
            done
        fi

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
