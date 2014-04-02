package cpp.codechecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.SAXException;

public class CodeChecker {

    public static void main(String[] xmlFile) throws FileNotFoundException, SAXException, IOException {
        if(!isParsedXmlPresent(xmlFile))
            return;
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        IXMLProcessor processor = (IXMLProcessor) context.getBean("IXMLProcessor");
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
