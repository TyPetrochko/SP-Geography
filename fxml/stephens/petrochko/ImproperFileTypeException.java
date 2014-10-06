/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

/**
 *
 * @author Tyler
 */
public class ImproperFileTypeException extends Exception{
    @Override
    public void printStackTrace(){
        System.out.println("A file or the wrong type was used. For example, if addRecursivelyChildren was called on a .txt.");
    }
}
