node {
   emailext mimeType: 'text/html',
                 subject: "[Jenkins]${currentBuild.fullDisplayName}",
                 to: "pavan.kumar@systemizerinc.com",
                 body: '''<a href="Please go to console output of ${BUILD_URL}input to Approve or Abort.</a>''', '''<a href="${BUILD_URL}input">click to approve</a>'''
}
pipeline {
    agent any
    tools {
        maven 'M2_HOME'
        }    
    stages {
        stage ('Checkout SCM'){
            steps {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'git', url: 'https://github.com/rmspavan/jenkins-ansible.git']]])
                }
        }
        stage('Build') {
            steps {
                echo 'Build Successfully'
            }
        }
        stage('Waiting for Approvals') {
            input {
                message "Should we continue?"
                ok "Yes"
            }
            when {
                expression { user == 'hardCodeApproverJenkinsId'}
            }
            steps {
                echo 'Approved Successfully'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deployed successfully'
            }
        }
    }
}