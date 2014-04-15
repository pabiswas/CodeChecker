/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpp.codechecker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author pabiswas
 */
public class SrcMLProcessor implements IXMLProcessor {

    HashMap<String, ClassInfo> m_classInfo;
    HashMap<String, MemberInfo> m_memberInfo;
    private HashSet<String> m_classNames;

    public SrcMLProcessor() {
        m_classInfo = new HashMap<String, ClassInfo>();
        m_memberInfo = new HashMap<String, MemberInfo>();
        m_classNames = new HashSet<String>();
    }

    public void process(String[] args) throws SAXException, IOException {
        try {
            DocumentBuilderFactory builderFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new FileInputStream(args[0]));

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList classNames = (NodeList) xPath.compile("/unit/class/name").evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < classNames.getLength(); ++i) {
                String className = classNames.item(i).getTextContent();
                
                ClassInfo classInfo = new ClassInfo();
                classInfo.setM_className(className);
                ArrayList<String> members = new ArrayList<String>();
                String memberXPath = "/unit/class[name = \'" + className+ "\']/block/*/decl_stmt/decl/name";
                NodeList memberNames = (NodeList) xPath.compile(memberXPath).evaluate(document,XPathConstants.NODESET);

                for (int j = 0; j < memberNames.getLength(); ++j)
                {
                    String varName = memberNames.item(j).getTextContent();
                    MemberInfo memInfo = new MemberInfo();
                    
                    members.add(varName);
                    
                    String specifier = (String) xPath.compile("/unit/class[name=\'"+className+"\']/block/*/decl_stmt/decl[name=\'"+varName+"\']/../decl/type/specifier").evaluate(document,XPathConstants.STRING);
                    if(specifier.equals("const")) {
                        memInfo.setIsConst(true);
                    }
                    m_memberInfo.put(varName, memInfo);
                }
                String[] mem = new String[members.size()];
                members.toArray(mem);
                classInfo.setM_members(mem);
                m_classNames.add(className);
                
                String methodXPath = "/unit/class[name = \'" + className+ "\']/block/*/function_decl/name";
                ArrayList<String> methodList = new ArrayList<String>();
                NodeList methods = (NodeList) xPath.compile(methodXPath).evaluate(document,XPathConstants.NODESET);
                for(int k = 0; k < methods.getLength(); ++k)
                {
                    String methodName = methods.item(k).getTextContent();
                    methodList.add(methodName);                    
                }
                classInfo.setM_methodNames(methodList);
                m_classInfo.put(className, classInfo);
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(SrcMLProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException e) {
            Logger.getLogger(SrcMLProcessor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public HashSet<String> getAllClassNames() {
        return m_classNames;
    }

    public HashSet<String> getAllMemberInClass(String classNameToFind) {
        String[] members = m_classInfo.get(classNameToFind).getM_members();
        HashSet<String> updatedMembers = new HashSet<String>();
        updatedMembers.addAll(Arrays.asList(members));
        return updatedMembers;
    }

    public ClassInfo getClassInfo(String className) {
        return m_classInfo.get(className);
    }

    public MemberInfo getMemberInfo(String memberName) {
        return m_memberInfo.get(memberName);
    }
    
    public HashSet<String> getAllMethodsInClass(String classNameToFind) {
        ArrayList<String> methodNames = m_classInfo.get(classNameToFind).getM_methodNames();
        HashSet<String> hset = new HashSet<String>();
        for( String str: methodNames) {
            hset.add(str);
        }
        return hset;
    }
}
