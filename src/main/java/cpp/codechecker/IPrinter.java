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
public interface IPrinter {
    void printMessage(String msg);
    void printError(String msg);
}
