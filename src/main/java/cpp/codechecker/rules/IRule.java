/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpp.codechecker.rules;

import cpp.codechecker.IPrinter;
import cpp.codechecker.IXMLProcessor;

/**
 *
 * @author basme
 */
public interface IRule {
    void runRule(IXMLProcessor processor, IPrinter printer);
}
