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
import org.w3c.dom.DOMException;
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
    HashMap<String, MethodInfo> m_methodInfo;
    private HashSet<String> m_classNames;
    Document document;

    public SrcMLProcessor() {
        m_classInfo = new HashMap<String, ClassInfo>();
        m_memberInfo = new HashMap<String, MemberInfo>();
        m_methodInfo = new HashMap<String, MethodInfo>();
        m_classNames = new HashSet<String>();
    }

    public void process(String[] args) throws SAXException, IOException {
        try {
            DocumentBuilderFactory builderFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            document = builder.parse(new FileInputStream(args[0]));

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList classNames = fillClassDetails(xPath);
            for (int i=0; i<classNames.getLength(); ++i) {
                String className = classNames.item(i).getTextContent();
                fillMemberDetails(className, xPath, m_classInfo.get(className));
            }         
        } catch (XPathExpressionException ex) {
            Logger.getLogger(SrcMLProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException e) {
            Logger.getLogger(SrcMLProcessor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private NodeList fillClassDetails(XPath xPath) throws DOMException, XPathExpressionException {
        NodeList classNames = (NodeList) xPath.compile("/unit/class/name").evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < classNames.getLength(); ++i) {
            String className = classNames.item(i).getTextContent();
            ClassInfo classInfo = new ClassInfo();
            classInfo.setM_className(className);
            m_classNames.add(className);
            m_classInfo.put(className, classInfo);
        }
        return classNames;
    }

    private void fillMemberDetails(String className, XPath xPath, ClassInfo classInfo) throws DOMException, XPathExpressionException {
        ArrayList<String> members = new ArrayList<String>();
        String memberXPath = "/unit/class[name = \'" + className+ "\']/block/*/decl_stmt/decl/name";
        NodeList memberNames = (NodeList) xPath.compile(memberXPath).evaluate(document,XPathConstants.NODESET);
        
        for (int j = 0; j < memberNames.getLength(); ++j)
        {
            String varName = memberNames.item(j).getTextContent();
            MemberInfo memInfo = new MemberInfo();
            members.add(varName);
            String specifier = (String) xPath.compile("/unit/class[name=\'"+className+"\']/block/*/decl_stmt/decl[name=\'"+varName+"\']/type/specifier").evaluate(document,XPathConstants.STRING);
            if(specifier.equals("const")) {
                memInfo.setIsConst(true);
            }
            m_memberInfo.put(varName, memInfo);
        }
        String[] mem = new String[members.size()];
        members.toArray(mem);
        classInfo.setM_members(mem);
        
        ArrayList<String> methodList = new ArrayList<String>();
        
        String methodXPath = "/unit/class[name = \'" + className+ "\']/block/*/function_decl/name";
        NodeList methods = (NodeList) xPath.compile(methodXPath).evaluate(document,XPathConstants.NODESET);
        
        for(int k = 0; k < methods.getLength(); ++k)
        {
            String methodName = methods.item(k).getTextContent();
            MethodInfo methodInfo = new MethodInfo();
            methodList.add(methodName);
            String specifier = (String) xPath.compile("/unit/class[name=\'"+className+"\']/block/*/function_decl[name=\'"+methodName+"\']/specifier").evaluate(document,XPathConstants.STRING);
            methodInfo.setIsConst(false);
            if(specifier.equals("const"))
            {
               methodInfo.setIsConst(true);
            } 
            m_methodInfo.put(methodName, methodInfo);
        }
        
        String methodDefXPath = "/unit/class[name = \'" + className+ "\']/block/*/function/name";
        NodeList methodsDef = (NodeList) xPath.compile(methodDefXPath).evaluate(document,XPathConstants.NODESET);
        
        for(int k = 0; k < methodsDef.getLength(); ++k)
        {
          String methodName = methodsDef.item(k).getTextContent();
          methodList.add(methodName);
          MethodInfo methodInfo = new MethodInfo();
          String specifier = (String) xPath.compile("/unit/class[name=\'"+className+"\']/block/*/function[name=\'"+methodName+"\']/specifier").evaluate(document,XPathConstants.STRING);
          methodInfo.setIsConst(false);
          if(specifier.equals("const"))
          {
             methodInfo.setIsConst(true);
          }
          m_methodInfo.put(methodName, methodInfo);
        }
        
        classInfo.setM_methodNames(methodList);
    }
 
    @Override
    public HashSet<String> getAllClassNames() {
        return m_classNames;
    }

    @Override
    public HashSet<String> getAllMemberInClass(String classNameToFind) {
        String[] members = m_classInfo.get(classNameToFind).getM_members();
        HashSet<String> updatedMembers = new HashSet<String>();
        updatedMembers.addAll(Arrays.asList(members));
        return updatedMembers;
    }

    @Override
    public ClassInfo getClassInfo(String className) {
        return m_classInfo.get(className);
    }

    @Override
    public MemberInfo getMemberInfo(String memberName) {
        return m_memberInfo.get(memberName);
    }
    
    @Override
    public MethodInfo getMethodInfo(String methodName)
    {
        return m_methodInfo.get(methodName);
    }
    
    @Override
    public HashSet<String> getAllMethodsInClass(String classNameToFind) {
        ArrayList<String> methodNames = m_classInfo.get(classNameToFind).getM_methodNames();
        HashSet<String> hset = new HashSet<String>();
        for( String str: methodNames) {
            hset.add(str);
        }
        return hset;
    }
}
