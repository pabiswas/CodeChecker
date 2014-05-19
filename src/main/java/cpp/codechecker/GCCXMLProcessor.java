package cpp.codechecker;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author pabiswas
 */
public class GCCXMLProcessor implements IXMLProcessor {

    private HashSet<String> m_classNames;
    private DOMParser parser;
    private Document doc;
    private Element root;
    MemberInfo m_memInfo;
    HashMap<String, ClassInfo> m_classInfo;
    HashMap<String, MemberInfo> m_memberInfo;
    HashMap<String, MethodInfo> m_methodInfo;

    public GCCXMLProcessor() {
        m_classNames = new HashSet<String>();
        m_classInfo = new HashMap<String, ClassInfo>();
        m_memberInfo = new HashMap<String, MemberInfo>();
        m_methodInfo = new HashMap<String, MethodInfo>();
    }

    public void process(String[] args) throws SAXException, IOException {
        setUpXmlParser(args[0]);
        NodeList classes = root.getElementsByTagName("Class");

        for (int numOfClasses = 0; numOfClasses < classes.getLength(); ++numOfClasses) {
            NamedNodeMap map = classes.item(numOfClasses).getAttributes();
            String className = map.getNamedItem("name").getTextContent();
            m_classNames.add(className);
        }
    }

    private void setUpXmlParser(String xmlToParse) throws SAXException, IOException {
        parser = new DOMParser();
        parser.parse(xmlToParse);
        doc = parser.getDocument();
        root = doc.getDocumentElement();
        cacheData(doc);
    }

    public HashSet<String> getAllClassNames() {
        return m_classNames;
    }

    public HashSet<String> getAllMemberInClass(String classNameToFind) {
        String[] members = getClassInfo(classNameToFind).getM_members();

        HashSet<String> updatedMembers = new HashSet<String>();
        for (String str : members) {
//            System.out.printf("Member of %s is %s\n", classNameToFind, getName(str));
            updatedMembers.add(getName(str));
        }
        return updatedMembers;
    }

    private void cacheData(Document doc) {
        if (doc.hasChildNodes()) {
            NodeList nodeList = doc.getChildNodes();
            for (int count = 0; count < nodeList.getLength(); count++) {
                Node tmpNode = nodeList.item(count);
                if (Node.ELEMENT_NODE == tmpNode.getNodeType()) {
                    if (tmpNode.hasChildNodes()) {
                        NodeList innerNodeList = tmpNode.getChildNodes();
                        for (int item = 0; item < innerNodeList.getLength(); ++item) {
                            Node tempNode = innerNodeList.item(item);
                            if (Node.ELEMENT_NODE == tempNode.getNodeType()) {
                                fillClassInfo(tempNode);
                                fillMemberInfo(tempNode);
                            }
                        }
                    }
                }
            }
        }
    }

    private void fillClassInfo(Node tempNode) throws DOMException, NumberFormatException {
        if (tempNode.getNodeName().equals("Class")) {
            ClassInfo classInfo = new ClassInfo();
            NamedNodeMap map = tempNode.getAttributes();
            String className = fillClassNameInfo(map, classInfo);
            fillLineInfo(map, classInfo);
            fillMemberInfo(map, classInfo);
            m_classInfo.put(className, classInfo);
        }
    }

    public ClassInfo getClassInfo(String className) {
        return m_classInfo.get(className);
    }

    private String getName(String str) {
        MemberInfo memInfo = m_memberInfo.get(str);
        if (memInfo != null) {
            return memInfo.getM_name();
        }
        return "";
    }

    public MemberInfo getMemberInfoFromId(String memberId) {
        MemberInfo memInfo = m_memberInfo.get(memberId);
        return memInfo;
    }

    public MethodInfo getMethodInfo(String methodName)
    {
       return m_methodInfo.get(methodName);
    }
    private void fillMemberInfo(Node tempNode) {
        if (tempNode.getNodeName().equals("Field")) {
            MemberInfo memInfo = new MemberInfo();
            NamedNodeMap map = tempNode.getAttributes();
            String memberName = map.getNamedItem("name").getTextContent();
            memInfo.setM_name(memberName);
            String line = map.getNamedItem("line").getTextContent();
            memInfo.setLineNumber(Integer.parseInt(line));
            String id = map.getNamedItem("id").getTextContent();
            m_memberInfo.put(id, memInfo);
        }
    }

    private void fillMemberInfo(NamedNodeMap map, ClassInfo classInfo) throws DOMException {
        Node memberNode = map.getNamedItem("members");
        String memberIds[] = null;
        if (memberNode != null) {
            memberIds = memberNode.getTextContent().split(" ");
            classInfo.setM_members(memberIds);
        }
    }

    private void fillLineInfo(NamedNodeMap map, ClassInfo classInfo) throws NumberFormatException, DOMException {
        String line = map.getNamedItem("line").getTextContent();
        classInfo.setM_lineNumber(Integer.parseInt(line));
    }

    private String fillClassNameInfo(NamedNodeMap map, ClassInfo classInfo) throws DOMException {
        String className = map.getNamedItem("name").getTextContent();
        classInfo.setM_className(className);
        return className;
    }

    public HashSet<String> getAllMethodsInClass(String classNameToFind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public MemberInfo getMemberInfo(String methodName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}