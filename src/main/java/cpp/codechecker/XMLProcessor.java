package cpp.codechecker;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author pabiswas
 */
public class XMLProcessor {
    private HashSet<String> m_classNames;
    private HashSet<String> m_members;
    private DOMParser parser;
    private Document doc;
    private Element root;

    public XMLProcessor() 
    {
        m_classNames = new HashSet<String>();
        m_members = new HashSet<String>();
    }
   
   public void process(String[] args) throws SAXException, IOException
   {
       setUpXmlParser(args[0]);
       NodeList classes = root.getElementsByTagName("Class");
       
       for (int numOfClasses = 0; numOfClasses < classes.getLength(); ++numOfClasses)
       {
           NamedNodeMap map = classes.item(numOfClasses).getAttributes();
           String className = map.getNamedItem("name").getTextContent();
           System.out.println("ClassName : " + className);
           m_classNames.add(className);
       }
   }
   
   private void setUpXmlParser(String xmlToParse) throws SAXException, IOException
   {
       parser = new DOMParser();   
       parser.parse(xmlToParse);
       doc = parser.getDocument();
       root = doc.getDocumentElement();
   }
   
   public HashSet<String> getAllClassNames()
   {
       return m_classNames;
   }
   
   public HashSet<String> getAllMembers(String classNameToFind)
   {
       String dirtyMembers[] = getMemberIdsOfClass(classNameToFind);
       NodeList fields = root.getElementsByTagName("Field");
       for(int i = 0; i < dirtyMembers.length; ++i)
       {
           dirtyMembers[i] = dirtyMembers[i].trim();
           for(int j = 0; j < fields.getLength(); ++j)
           {
               NamedNodeMap map = fields.item(j).getAttributes();
               String fieldId = map.getNamedItem("id").getTextContent();
               if(fieldId.equals("_" + dirtyMembers[i]))
               {
                   m_members.add(map.getNamedItem("name").getTextContent());
               }
           }
       }
       return m_members;
   }
   
   public String[] getMemberIdsOfClass(String classNameToFind)
   {
       NodeList classes = root.getElementsByTagName("Class");
       String memberIds[] = null;
       for(int numOfClasses = 0; numOfClasses < classes.getLength(); ++numOfClasses)
       {
           NamedNodeMap map = classes.item(numOfClasses).getAttributes();
           String className = map.getNamedItem("name").getTextContent();
           if(className.equals(classNameToFind))
           {
               memberIds = map.getNamedItem("members").getTextContent().split("_");
               break;
           }
       }
       return memberIds;
   }
}