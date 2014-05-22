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
public class MemberNameIsLowerCamel implements IRule{

    @Override
    public void runRule(IXMLProcessor processor, IPrinter printer) {
        for(String className: processor.getAllClassNames())
        {
            for(String memberName: processor.getAllMemberInClass(className))
            {
                if(!memberName.matches("^[a-z].*"))
                {
                    printer.printError("Member name \'"+className+"."+memberName+"\' should start with a lower case.");
                }
            }
        }
    }
    
}
