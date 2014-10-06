/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

/**
 *
 * @author Tyler
 */
public class InvalidFolderFormatException extends Exception{
    @Override
    public void printStackTrace(){
        //Make this nicer later
        System.out.println("The Map Pack folder in use does not match the Map Pack folder format.");
    }
}
