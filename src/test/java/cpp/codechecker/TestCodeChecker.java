package cpp.codechecker;

import cpp.codechecker.InvalidFile;
import cpp.codechecker.CodeChecker;
import java.io.FileNotFoundException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class TestCodeChecker
{
    private CodeChecker codeChecker;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestCodeChecker() {
        codeChecker = new CodeChecker();
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
        args[0] = "ZeroKb.xml";
        codeChecker.main(args);
    }
}
