/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Tyler
 */
public class MainScreenController implements Initializable {



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Base();
    }
    public void pressedPreset (ActionEvent e){
        try{
            FXMLStephensPetrochko.getInstance().replaceSceneContent("ChoosePresetQuiz.fxml", Base.CHOOSE_PRESET_QUIZ_TITLE);
        }catch (Exception exc) {
            exc.printStackTrace();
        }
       
    }
    public void pressedMakeQuiz (ActionEvent e){
        try{
            FXMLStephensPetrochko.getInstance().replaceSceneContent("MakeQuiz.fxml", Base.MAKE_QUIZ_TITLE);
        }catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public void pressedSettings (ActionEvent e){
        try {
            FXMLStephensPetrochko.getInstance().replaceSceneContent("SettingsScreen.fxml", Base.SETTINGS_TITLE);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
}
