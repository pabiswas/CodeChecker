/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpp.codechecker;

/**
 *
 * @author suhsubra
 */
public class MethodInfo {
    private boolean isConst;
    private String m_name;
    private String m_returnType;

    public MethodInfo() {
        this.isConst = false;
    }
   
    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public boolean getIsConst() {
        return isConst;
    }

    public void setIsConst(boolean isConst) {
        this.isConst = isConst;
    }
    
    public String getReturnType()
    {
        return m_returnType;
    }
    
    public void setReturnType(String returnType)
    {
        this.m_returnType = returnType;
    }
}
