package cpp.codechecker;

/**
 *
 * @author pabiswas
 */
public class ClassInfo {
    private String m_className;
    private String[] m_members;
    private Integer m_lineNumber;

    public String getM_className() {
        return m_className;
    }

    public void setM_className(String m_className) {
        this.m_className = m_className;
    }

    public String[] getM_members() {
        return m_members;
    }

    public void setM_members(String[] m_members) {
        this.m_members = m_members;
    }

    public Integer getM_lineNumber() {
        return m_lineNumber;
    }

    public void setM_lineNumber(Integer m_lineNumber) {
        this.m_lineNumber = m_lineNumber;
    }
}
