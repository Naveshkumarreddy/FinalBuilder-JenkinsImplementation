package com.example

class LoginToRTC {
    def loginToRTC(String scmPath, String scmUrl, String username, String password, String repositoryName) {
        println 'Logging in to RTC...'
        def command = "\"${scmPath}\\scm.exe\" login -r \"${scmUrl}\" -u ${username} -P ${password} -n ${repositoryName}"
        def process = command.execute()
        try {
            process.waitFor()

            if (process.exitValue() == 0) {
                println 'Login successful.'
            } else {
                println "Login failed: ${process.errorStream.text}"
                throw new RuntimeException("RTC login failed.")
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