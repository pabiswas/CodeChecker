/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpp.codechecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author isaac
 */
public class CleanFileProcessor {
    
    private ArrayList<String> classNames;
    private final String tempFileName = "tempFile.txt";
    private boolean isMultiLineCommentFlag = false;
    private boolean isStringDataFlag = false;
    private final String allowedCharsInClassName;

    public CleanFileProcessor() {
        this.classNames = new ArrayList();
        allowedCharsInClassName = "abcdefghijklmnopqrstuvwxyz0123456789_";
    }
    
    public ArrayList<String> getClassNames(String fileName) throws FileNotFoundException, IOException {
        File inputFile = new File(fileName);
        File tempFile = new File(tempFileName);
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        String currentLine;
        while((currentLine = reader.readLine()) != null)
        {
            String processed = currentLine;
            if(processed.length() == 0)
                continue;
            if(isMultiLineCommentFlag) {
                boolean skipLine = isEntireLineCommented(processed);
                if(skipLine) {
                    continue;
                } else {
                    int commentEndIndex = processed.indexOf("*/");
                    processed = processed.substring(commentEndIndex + 2);
                    isMultiLineCommentFlag = false;
                }
            }
            if(isStringDataFlag) {
                boolean skipLine = isEntireLineAString(processed);
                if(skipLine) {
                    continue;
                } else {
                    int stringEndIndex = processed.indexOf('"');
                    processed = processed.substring(stringEndIndex);
                    isStringDataFlag = false;
                }
            }
            processed = removeComments(processed);
            processed = removeStringData(processed, 0);
            writer.write(processed + "\n");
            String className = findClassName(processed);
            if(className != null) {
            classNames.add(className);
            }
        }
        writer.close();
        reader.close();
        return classNames;
    }
    
    private boolean isEntireLineCommented(String line)
    {
        return !(line.contains("*/"));
    }
    
    private boolean isEntireLineAString(String line)
    {
        return !(line.contains("\""));
    }
    
    private String removeComments(String line) {
        String newLine = line;
        int multiLineStartIndex = line.indexOf("/*");
        int multiLineEndIndex = line.indexOf("*/");
        int singleLineCommentStart = line.indexOf("//");
        if((singleLineCommentStart != -1)) 
        {
            if(multiLineStartIndex > -1 && singleLineCommentStart < multiLineStartIndex) 
            {
                newLine = line.substring(0, singleLineCommentStart);
                return newLine;
            }
            if(multiLineStartIndex == -1) 
            {
                newLine = line.substring(0, singleLineCommentStart);
                return newLine;
            }
        }
        if((multiLineStartIndex > -1) && (multiLineEndIndex > -1)) 
        {
            newLine = line.substring(0, multiLineStartIndex) + line.substring(multiLineEndIndex+2); // +2 because of */ has 2 chars
            newLine = removeComments(newLine);
            return newLine;
        }
        if((multiLineStartIndex > -1) && (multiLineEndIndex == -1)) 
        {
            isMultiLineCommentFlag = true;
            newLine = line.substring(0, multiLineStartIndex);
            return newLine;
        }
        return newLine;
    }
    
    
    private String removeStringData(String line, int startPosition) 
    {
        String newLine = line;
        int stringStartIndex = newLine.indexOf('"', startPosition);
        int stringEndIndex = newLine.indexOf('"', stringStartIndex+1);
        if(stringStartIndex > -1 && Math.abs(stringStartIndex - stringEndIndex) > 1) 
        {
            if(stringEndIndex == -1) 
            {
                isStringDataFlag = true;
                newLine = newLine.substring(0, stringStartIndex+1);
            } 
            else 
            {
                newLine = newLine.substring(0, stringStartIndex+1) + newLine.substring(stringEndIndex);
                newLine = removeStringData(newLine, stringStartIndex+2);
                return newLine;
            }
        }
        return newLine;
    }
    
    private String findClassName(String line)
    {
        String realName = null;
        line = line.trim();
        int classKeywordBegin = -1;
        if((classKeywordBegin = line.indexOf(" class ")) > -1) 
        {
            String lineAfterKeyword = line.substring(classKeywordBegin + 7).trim();
            realName = extractClassName(lineAfterKeyword);
        }
        else if((classKeywordBegin = line.indexOf("class ")) > -1
                && (classKeywordBegin == 0 || line.charAt(classKeywordBegin-1) == ';' || line.charAt(classKeywordBegin-1) == '}') ) 
        {
            String lineAfterKeyword = line.substring(classKeywordBegin + 6).trim();
            realName = extractClassName(lineAfterKeyword);
        }
        return realName;
    }
    
    private String extractClassName(String lineAfterKeywordClass) 
    {
        int i;
        for(i=0; i<lineAfterKeywordClass.length(); i++) 
        {
            if(!allowedCharsInClassName.contains(String.valueOf(lineAfterKeywordClass.charAt(i)).toLowerCase())) 
            {
                break;
            }
        }
        return lineAfterKeywordClass.substring(0, i);
    }
}
