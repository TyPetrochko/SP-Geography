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
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Tyler
 */
public class PresetQuizController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void backPressed (ActionEvent e){
        try {
            FXMLStephensPetrochko.getInstance().replaceSceneContent("MainScreen.fxml", Base.PROJECT_TITLE);
        } catch (Exception ex) {
            Logger.getLogger(PresetQuizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
