class RunAllScripts {

    Properties properties = new Properties()

    RunAllScripts() {
        File propertiesFile = new File('src/main/resources/config.properties')
        if (propertiesFile.exists()) {
            properties.load(propertiesFile.newInputStream())
            println "Properties loaded: ${properties}"
        } else {
            println "Properties file not found: ${propertiesFile.absolutePath}"
        }
    }

    // Create instances of the classes
    def loginToRTC = new LoginToRTC()
    def lockWorkspace = new LockWorkspace()
    def checkForConflicts = new CheckForConflicts()
    def acceptChanges = new AcceptChanges()
    def checkReleaseVersionFormat = new CheckReleaseVersionFormat()
    def generateGUIHeaderFile = new GenerateGUIHeaderFile()

    // Define parameters for the methods
    def scmPath
    def scmUrl
    def username
    def password
    def repositoryName
    def workspaceName
    def stream
    def viewPath

    void assignProperties() {
        scmPath = properties.getProperty('scmPath')
        scmUrl = properties.getProperty('scmUrl')
        username = properties.getProperty('username')
        password = properties.getProperty('password')
        repositoryName = properties.getProperty('repositoryName')
        workspaceName = properties.getProperty('workspaceName')
        stream = properties.getProperty('stream')
        viewPath = properties.getProperty('viewPath')
    }

    void runAll() {
        assignProperties()
        try {
            println "Starting the RTC login process..."
            println "scmPath: ${scmPath}"
            println "scmUrl: ${scmUrl}"
            println "username: ${username}"
            println "password: ${password}"
            println "repositoryName: ${repositoryName}"
            loginToRTC.loginToRTC(scmPath, scmUrl, username, password, repositoryName)
        } catch (Exception e) {
            println "Error during login: ${e.message}"
            return // Exit the script if login fails
        }

        try {
            println "Locking the workspace..."
            lockWorkspace.lockWorkspace(scmPath, scmUrl, username, password, workspaceName)
        } catch (Exception e) {
            println "Error during locking workspace: ${e.message}"
            return // Exit if workspace locking fails
        }

        try {
            println "Checking for local conflicts..."
            checkForConflicts.checkForConflicts(scmPath, viewPath) // Pass both arguments
        } catch (Exception e) {
            println "Error during conflict check: ${e.message}"
            return // Exit if conflict check fails
        }

        try {
            println "Accepting changes from the stream..."
            acceptChanges.acceptChanges(scmPath, scmUrl, username, password, workspaceName, stream)
        } catch (Exception e) {
            println "Error during accepting changes: ${e.message}"
            return
        }

        try {
            println "Checking the release version format..."
            checkReleaseVersionFormat.checkReleaseVersionFormat(viewPath)
        } catch (Exception e) {
            println "Error during release version format check: ${e.message}"
            return // Exit if version format check fails
        }

        try {
            println "Generating GUI header file..."
            generateGUIHeaderFile.generateHeaderFile(viewPath)
        } catch (Exception e) {
            println "Error during GUI header file generation: ${e.message}"
            return // Exit if GUI header file generation fails
        }

        println "All operations completed successfully."
    }

    static void main(String[] args) {
        def runAllScripts = new RunAllScripts()
        runAllScripts.runAll()
    }
}