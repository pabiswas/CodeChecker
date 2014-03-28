package cpp.codechecker;

/**
 *
 * @author pabiswas
 */
public class MemberInfo {
    private boolean isConst;
    private Integer lineNumber;
    private String m_name;

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public boolean isIsConst() {
        return isConst;
    }

    public void setIsConst(boolean isConst) {
        this.isConst = isConst;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
    
}
