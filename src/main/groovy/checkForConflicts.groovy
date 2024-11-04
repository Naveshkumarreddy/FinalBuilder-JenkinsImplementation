class CheckForConflicts {
    def checkForConflicts(String scmPath, String viewPath) {
        println 'Checking for local conflicts...'
        def command = "\"${scmPath}\\scm.exe\" status"
        def process = command.execute(null, new File(viewPath))
        def outputFile = new File("${viewPath}\\Release\\bin\\Local_Conflicts_status.txt")

        try {
            outputFile.withWriter { writer ->
                writer << process.text
            }

            def conflictsFound = false
            outputFile.eachLine { line ->
                if (line == "Local conflicts:") {
                    conflictsFound = true
                }
            }

            if (conflictsFound) {
                println "There is a Local conflict in Build Workspace, Please resolve the conflicts."
                System.exit(1)
            } else {
                println "No conflicts Found in Build Workspace."
                System.exit(0)
            }
        } catch (IOException e) {
            println "IOException occurred: ${e.message}"
            throw new RuntimeException("Failed to execute command.", e)
        } catch (InterruptedException e) {
            println "InterruptedException occurred: ${e.message}"
            throw new RuntimeException("Command execution interrupted.", e)
        } finally {
            process.inputStream.close()
            process.errorStream.close()
            process.outputStream.close()
        }
    }
}