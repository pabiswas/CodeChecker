package cpp.codechecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.SAXException;

public class CodeChecker {

    public static ApplicationContext context;
    private static IXMLProcessor processor;

    public static void main(String[] xmlFile) throws FileNotFoundException, SAXException, IOException {
        if (!isParsedXmlPresent(xmlFile)) {
            return;
        }
        context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        processor = (IXMLProcessor) context.getBean("IXMLProcessor");
        processor.process(xmlFile);
        CodeChecker codeChecker = new CodeChecker();
        codeChecker.startChecker();
    }

    private static boolean isParsedXmlPresent(String[] args) throws FileNotFoundException {
        boolean returnValue = false;
        if (args.length < 1) {
            System.out.println("Usage: java -jar xmlProcessing <SuiteName>");
        } else {
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

    private void startChecker() {
        try {
            RuleConfigReader ruleReader = new RuleConfigReader();
            final String ruleClassPackage = "cpp.codechecker.rules";
            IPrinter printer = (IPrinter)context.getBean("IPrinter");
            
            Class[] resource = new Class[2];
            resource[0] = IXMLProcessor.class;
            resource[1] = IPrinter.class;

            while (ruleReader.isNextRuleAvailable()) {
                String ruleName = ruleReader.getNextRule();
                Class reflectedClass = Class.forName(ruleClassPackage + "." + ruleName);

                Method method = reflectedClass.getDeclaredMethod("runRule", resource);
                Object reflectedObject = reflectedClass.newInstance();
                method.invoke(reflectedObject, processor, printer);
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(CodeChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
