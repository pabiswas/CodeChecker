package cpp.codechecker;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.File;
import java.io.IOException;
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
   private String m_className;
   
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
           System.out.println("ClassName : " + map.getNamedItem("name").getTextContent());
           m_className = map.getNamedItem("name").getTextContent();
       }
   }
  
   public String getClassName()
   {
       return m_className;
   }
}
