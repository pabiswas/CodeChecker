package cpp.codechecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.xml.sax.SAXException;

public class CodeChecker {

    public static void main(String[] xmlFile) throws FileNotFoundException, SAXException, IOException {
        if(!isParsedXmlPresent(xmlFile))
            return;
        XMLProcessor processor = new XMLProcessor();
        processor.process(xmlFile);
    }

    private static boolean isParsedXmlPresent(String[] args) throws FileNotFoundException {
        boolean returnValue = false;
        if (args.length < 1) 
        {
            System.out.println("Usage: java -jar xmlProcessing <SuiteName>");
        } 
        else 
        {
            returnValue = checkFileValidity(new File(args[0]));
        }

        return returnValue;
    }

    private static boolean checkFileValidity(File fileName) throws FileNotFoundException {
        if (!fileName.exists()) {
            throw new FileNotFoundException();
        }
        if (fileName.length() == 0) {
            throw new InvalidFile();
        }
        
        System.out.println("Parsed XML name : " + fileName);
        return true;
    }
}
