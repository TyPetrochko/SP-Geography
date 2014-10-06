/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Tyler
 */
public class SureYouWantToExitController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void Back (ActionEvent e){
       Node  source = (Node)  e.getSource(); 
       Stage stage  = (Stage) source.getScene().getWindow();
       stage.close();
     }
     public void Exit (ActionEvent event){
        System.exit(0);
     }
}
