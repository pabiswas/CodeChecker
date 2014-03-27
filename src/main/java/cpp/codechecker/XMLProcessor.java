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

    public XMLProcessor() 
    {
        m_classNames = new HashSet<String>();
    }
   
   public void process(String[] args) throws SAXException, IOException
   {
       DOMParser parser = new DOMParser();   
       parser.parse(new File(args[0]).getName());
       Document doc = parser.getDocument();
       Element root = doc.getDocumentElement();
       NodeList classes = root.getElementsByTagName("Class");
       
       for (int numOfClasses = 0; numOfClasses < classes.getLength(); ++numOfClasses)
       {
           NamedNodeMap map = classes.item(numOfClasses).getAttributes();
           String className = map.getNamedItem("name").getTextContent();
           System.out.println("ClassName : " + className);
           m_classNames.add(className);
       }
   }
   
   public HashSet<String> getAllClassNames()
   {
       return m_classNames;
   }
}