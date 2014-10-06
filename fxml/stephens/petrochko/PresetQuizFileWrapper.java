/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.io.File;

/**
 *
 * @author Tyler
 */
public class PresetQuizFileWrapper {
    /*Essentially just a wrapper class for a file, the same as a file
     * but its toString method returns the actual name of the file, not
     * its path. This is used in MakeQuizController because ChoiceBox 
     * displays file with their PATHS, not their NAMES. The solution is
     * to make this wrapper class.
     */
    private File theFile;
    public String toString(){
        String name = theFile.getName();
        String toReturn = name.split("\\.")[0];
        return toReturn;
    }
    public PresetQuizFileWrapper(File f){
            theFile= f;
    }
    public File getFile(){
        return theFile;
    }
}
