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
        node("principal"){
            checkoutApi("tests")
            runner 'cd tests && mvn test'
        }
    }
}
SonarQube:{
    stage("SonarQube"){
        node("principal"){
            checkoutTesting("tests")
            runner 'cd tests && mvn clean verify sonar:sonar -Dsonar.login=%token%'
        }
    }
}

}