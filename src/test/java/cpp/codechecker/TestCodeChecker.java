package cpp.codechecker;

import cpp.codechecker.CodeChecker;
import cpp.codechecker.InvalidFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Unit test for simple App.
 */
public class TestCodeChecker
{
    private CodeChecker codeChecker;
    private XMLProcessor xmlProcessor;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestCodeChecker() {
        codeChecker = new CodeChecker();
        xmlProcessor = new XMLProcessor();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = FileNotFoundException.class)
    public void NonExistingSuite_shouldThrowNonExistingSuite() throws Exception, Throwable {
        String[] args = new String[10];
        args[0] = "InvalidFile.xml";
        codeChecker.main(args);
    }
    
    @Test(expected = InvalidFile.class)
    public void InvalidXML_shouldThrowInvalidFile() throws Exception {
        String[] args = new String[10];
        args[0] = "src\\test\\java\\cpp\\codechecker\\ZeroKb.xml";
        codeChecker.main(args);
    }
        
    @Test
    public void TestGetClassNames() throws Exception {
        CleanFileProcessor processor = new CleanFileProcessor();
        ArrayList<String> classNames = processor.getClassNames("Calculation.cpp");
        assert (classNames.contains("Program"));
        assert (classNames.contains("Calculation"));
        assert (classNames.contains("Dummy"));
    }
    
    @Test
    public void ValidXML_should_get_proper_class_names() throws SAXException, IOException
    {
        xmlProcessor.process(getXMLFile());
        HashSet<String> classNames = xmlProcessor.getAllClassNames();
        assert(classNames.contains("Base"));
    }
    
    @Test
    public void getMembers_should_give_all_members() throws SAXException, IOException
    {
        xmlProcessor.process(getXMLFile());
        HashSet<String> members = xmlProcessor.getAllMembers("Base");
        assert(members.contains("isaac"));
        assert(members.contains("partha"));
        assert(members.contains("nsn"));
    }
    
    private String[] getXMLFile() {
        String[] args = new String[10];
        args[0] = "output.xml";
        return args;
    }    
}
