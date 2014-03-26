package cpp.codechecker;

import java.io.File;
import java.io.FileNotFoundException;

public class CodeChecker {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Current working directory : " + System.getProperty("user.dir"));
        if(!isParsedXmlPresent(args))
            return;
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
