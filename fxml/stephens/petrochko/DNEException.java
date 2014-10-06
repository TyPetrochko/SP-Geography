/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

/**
 *
 * @author Tyler
 */
public class DNEException extends Exception{
    @Override
    public void printStackTrace(){
        System.out.println("A file/folder was queried that does not exist.");
    }
}
