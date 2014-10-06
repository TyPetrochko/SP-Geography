/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

/**
 *
 * @author Tyler
 */
public class BadQuizException extends Exception{
    
    private String error;
    
    public BadQuizException(String errorDescriptor){
        error = errorDescriptor;
    }
    @Override
    public void printStackTrace(){
        System.out.println("Quiz is corrupted; error: "+error);
    }
}
