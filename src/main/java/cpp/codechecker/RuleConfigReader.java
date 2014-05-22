/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpp.codechecker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author basme
 */
public class RuleConfigReader {
    
    private final static String fileName = "rules.properties";
    private String[] ruleList;
    private int currentRule = 0;

    public RuleConfigReader() {
        try {
            InputStream inStream = this.getClass().getResourceAsStream("/"+fileName);
            Properties configs = new Properties();
            configs.load(inStream);
            ruleList = configs.getProperty("rules").split(",");
        } catch (IOException ex) {
            Logger.getLogger(RuleConfigReader.class.getName()).log(Level.SEVERE, "Cannot open \'"+fileName+"\'.", ex);
        }
    }
    
    public boolean isNextRuleAvailable()
    {
        return ruleList.length > currentRule;
    }
    
    public String getNextRule()
    {
        return (ruleList.length > currentRule)? ruleList[currentRule++] : null;
    }
}
