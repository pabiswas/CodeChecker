/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpp.codechecker;

/**
 *
 * @author basme
 */
public class ConsolePrinter implements IPrinter{
    
    @Override
    public void printMessage(String msg) {
        System.out.println("Message: "+msg);
    }

    @Override
    public void printError(String msg) {
        System.out.println("Error: "+msg);
    }
}
