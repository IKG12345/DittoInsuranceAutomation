pipeline 
{
    agent any

    tools {
        jdk 'Java 22' 
        maven 'Maven 3.9.6' 
    }

    stages 
    {
        stage('Checkout') 
        {
            steps 
            {
                git branch: 'main', url: 'https://github.com/IKG12345/DittoInsuranceAutomation.git'
            }
        }

        stage('Build & Test') 
        {
            steps 
            {
                sh 'mvn clean test'
            }
        }
    }
}