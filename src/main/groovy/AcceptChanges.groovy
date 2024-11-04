class AcceptChanges {
    def acceptChanges(String scmPath, String scmUrl, String username, String password, String workspace, String stream) {
        println "Accepting changes for Workspace: ${workspace}, Stream: ${stream}"
        def command = "\"${scmPath}\\scm.exe\" accept -o -r \"${scmUrl}\" -u ${username} -P ${password} -N -s ${stream} -t ${workspace}"
        def process = command.execute()

        try {
            process.waitFor()

            if (process.exitValue() == 0) {
                println "Changes accepted for workspace ${workspace} from stream ${stream}."
            } else {
                println "Failed to accept changes: ${process.errorStream.text}"
                throw new RuntimeException("Failed to accept changes.")
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