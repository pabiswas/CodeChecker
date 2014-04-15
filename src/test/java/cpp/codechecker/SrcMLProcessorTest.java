/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpp.codechecker;

import java.io.IOException;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author pabiswas
 */
public class SrcMLProcessorTest {
    private IXMLProcessor xmlProcessor;
    
    public SrcMLProcessorTest() {
        xmlProcessor = new SrcMLProcessor();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws SAXException, IOException {
        xmlProcessor.process(getXMLFile());
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getAllClassNames method, of class SrcMLProcessor.
     */
    @Test
    public void testGetAllClassNames() throws SAXException, IOException {
        HashSet<String> classNames = xmlProcessor.getAllClassNames();
        assert(classNames.contains("Test"));
        assert(classNames.contains("Test1"));
    }
    
    @Test
    public void getMembers_should_give_all_members() throws SAXException, IOException
    {
        HashSet<String> members = xmlProcessor.getAllMemberInClass("Test");
        assert(members.contains("i"));
        assert(members.contains("partha"));
        assert(members.contains("m_IsTrue"));
    }
    
    @Test
    public void getAllMethodsInClass_should_give_all_methods()
    {
        HashSet<String> methods = xmlProcessor.getAllMethodsInClass("Test");
        assert(methods.contains("foo"));
    }
    
    @Test
    public void checkContantMembers()
    {
        assert(!xmlProcessor.getMemberInfo("i").isIsConst());
        assert(!xmlProcessor.getMemberInfo("partha").isIsConst());
        assert(xmlProcessor.getMemberInfo("m_IsTrue").isIsConst());
    }
    
    private String[] getXMLFile() {
        String[] args = new String[10];
        args[0] = "SrcML_Two_Class.xml";
        return args;
    }
}