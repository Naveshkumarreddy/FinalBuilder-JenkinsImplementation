package com.example

class LockWorkspace {
    def lockWorkspace(String scmPath, String scmUrl, String username, String password, String workspaceName) {
        println "Locking workspace: ${workspaceName}"
        def command = "\"${scmPath}\\scm.exe\" workspace propertyset -r \"${scmUrl}\" -u ${username} -P ${password} ownedby Locked_For_Build ${workspaceName}"
        def process = command.execute()
        try {
            process.waitFor()

            if (process.exitValue() == 0) {
                println "Workspace ${workspaceName} locked successfully."
            } else {
                println "Failed to lock workspace: ${process.errorStream.text}"
                throw new RuntimeException("Failed to lock workspace.")
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