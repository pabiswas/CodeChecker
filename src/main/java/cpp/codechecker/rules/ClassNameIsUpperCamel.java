/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpp.codechecker.rules;

import cpp.codechecker.CodeChecker;
import cpp.codechecker.IPrinter;
import cpp.codechecker.IXMLProcessor;

/**
 *
 * @author basme
 */
public class ClassNameIsUpperCamel implements IRule{

    @Override
    public void runRule(IXMLProcessor processor, IPrinter printer) {
        for(String className : processor.getAllClassNames())
        {
            if(!className.matches("^[A-Z].*"))
            {
                printer.printError("Classname \'"+className+"\' should start with a upper case.");
            }
        }
    }
}
