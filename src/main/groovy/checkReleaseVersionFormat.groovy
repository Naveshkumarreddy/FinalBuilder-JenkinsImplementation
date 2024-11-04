class CheckReleaseVersionFormat {
    def checkReleaseVersionFormat(String viewPath) {
        println 'Checking release version format...'
        def releaseFilePath
        def installProduct = System.getenv("INSTALL_PRODUCT")
        if (installProduct == "NPD" || installProduct == "LNG") {
            releaseFilePath = "${viewPath}/Release/FactoryConfig/Release.txt"
        } else {
            releaseFilePath = "${viewPath}/Deployment/Release.txt"
        }

        def releaseFile = new File(releaseFilePath)
        if (!releaseFile.exists()) {
            throw new RuntimeException("Release file not found: ${releaseFilePath}")
        }

        def strRelease = releaseFile.text.trim()
        def errorLogDir = new File("${viewPath}/Release_Management/Build_Scripts/ReleaseVersionFormatErrorLog")
        if (!errorLogDir.exists()) {
            errorLogDir.mkdirs()
        }

        def errorLogFile = new File("${viewPath}/Release_Management/Build_Scripts/ReleaseVersionFormatErrorLog/ErrorReleaseFormat.txt")
        if (errorLogFile.exists()) {
            errorLogFile.delete()
        }

        def errorMsg = """Example release number formats:
V1.3.3
V1.3.3B12
V1.4.7P11
V1.4.7P01B11
V1.3.3.1_DEV_B10
V1.3.3.1_Radial_P12
V1.3.3.1_200_P01B10"""

        def logError = { msg ->
            errorLogFile.withWriter { writer ->
                writer.writeLine(msg)
            }
            throw new RuntimeException(msg)
        }

        if (strRelease.length() > 20) {
            logError("ERROR: Release number exceeds 20 characters which is a wrong format. Release number should not exceed 20 characters.\n${errorMsg}")
        } else if (!strRelease.contains("_")) {
            if (!strRelease.startsWith("V")) {
                logError("ERROR: Release number does not start with 'V' which is a wrong format. Release number must start with 'V'.\n${errorMsg}")
            }
            strRelease[1..-1].each {
                if (!(it.isDigit() || it == '.' || it == 'B' || it == 'P')) {
                    logError("ERROR: Only numbers, dot(.), alphabets 'B' and 'P' are allowed after the character 'V'. No other special characters or alphabets are allowed.\n${errorMsg}")
                }
            }
        } else {
            def parts = strRelease.split("_")
            if (parts.size() != 3) {
                logError("ERROR: Release number format should have two underscores(_) or no underscore.\n${errorMsg}")
            }
            if (!parts[0].startsWith("V")) {
                logError("ERROR: Release number does not start with 'V' which is a wrong format. Release number must start with 'V'.\n${errorMsg}")
            }
            parts[0][1..-1].each {
                if (!(it.isDigit() || it == '.')) {
                    logError("ERROR: Only numbers and dot(.) are allowed after character 'V' in the first part(before first underscore '_') of release number. No other special characters or alphabets are allowed.\n${errorMsg}")
                }
            }
            if (parts[1].isEmpty()) {
                logError("ERROR: The second part(between first and second underscore '_') of release number should not be empty.\n${errorMsg}")
            }
            def thirdPart = parts[2]
            if (!(thirdPart.startsWith("B") || thirdPart.startsWith("P"))) {
                logError("ERROR: Release number does not start with 'P' or 'B' in the third part(after second underscore '_') of release number which is a wrong format. Release number must start with 'P' or 'B' in the third part of release number.\n${errorMsg}")
            }
            thirdPart[1..-1].each {
                if (!(it.isDigit() || (thirdPart.startsWith("P") && it == 'B'))) {
                    logError("ERROR: Only numbers and alphabet 'B' are allowed after character '${thirdPart[0]}' in the third part(after second underscore '_') of release number. No other special characters or alphabets are allowed.\n${errorMsg}")
                }
            }
        }
        println 'Release version format check completed successfully.'
    }
}