/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpp.codechecker;

import java.io.IOException;
import java.util.HashSet;
import org.xml.sax.SAXException;

/**
 *
 * @author pabiswas
 */
public interface IXMLProcessor {
    public void process(String[] args) throws SAXException, IOException;
    public HashSet<String> getAllClassNames();
    HashSet<String> getAllMemberInClass(String classNameToFind);
    public ClassInfo getClassInfo(String className);
    public MemberInfo getMemberInfo(String methodName);
    HashSet<String> getAllMethodsInClass(String classNameToFind);
}
