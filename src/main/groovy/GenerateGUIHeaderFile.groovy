class GenerateGUIHeaderFile {
    def generateHeaderFile(String viewPath) {
        println 'Generating GUI header file...'
        def filename = "${viewPath}/Src/Common_UI/Include/ProductType_GUI.h"
        def productType = System.getenv("OPTIMA_PRODUCT")

        def file = new File(filename)
        file.withWriter { writer ->
            writer.writeLine("//************************************//")
            writer.writeLine("//This is a generated file do not edit//")
            writer.writeLine("//************************************//")
            writer.writeLine("")
            writer.writeLine("#ifndef ProductType_GUI_H")
            writer.writeLine("#define ProductType_GUI_H")
            writer.writeLine("")
            if (productType.equalsIgnoreCase("NPD")) {
                writer.writeLine("#define PurionH")
            }
            if (productType.equalsIgnoreCase("OHE")) {
                writer.writeLine("#define PurionXE")
            }
            if (productType.equalsIgnoreCase("LNG")) {
                writer.writeLine("#define PurionM")
            }
            writer.writeLine("")
            writer.writeLine("#endif")
        }
        println 'GUI header file generated successfully.'
    }
}