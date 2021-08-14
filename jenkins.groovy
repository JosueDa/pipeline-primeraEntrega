def checkoutTesting = { String subFolder ->
    checkout([
            $class      : "GitSCM",
            branches    : [[name: "*/main"]],
            extensions  : [[ $class: "RelativeTargetDirectory",
                             relativeTargetDir: subFolder
                           ]],
            userRemoteConfigs: [[
                                        url : 'git@github.com:JosueDa/pipeline-primeraEntrega.git',
                                        credentialsId: "jenkins"
                                ]]
    ])
}

def runner = { commandToExecute -> isUnix() ? sh(commandToExecute) : bat(commandToExecute) }


unitTest:{
    stage("Unit Testing"){
        node("NewNode"){
            checkoutApi("tests")
            runner 'cd tests && mvn test'
        }
    }
}
SonarQube:{
    stage("SonarQube"){
        node("NewNode"){
            checkoutTesting("tests")
            runner 'cd tests && mvn clean verify sonar:sonar -Dsonar.login=8b63209fb27d32eed2fde95b5ee712cfd0fc179b'
        }
    }
}

}