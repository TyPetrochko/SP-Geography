/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Tyler
 */
public class ChoosePresetQuizController implements Initializable {
    
    //Variables: Stay Organized!
    @FXML
    ListView theListView;
    @FXML
    Label label;
    @FXML
    AnchorPane mainPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Starts here!
        initializeListView(Base.quizLocation, Base.progressLocation);
    }    
    public void backPressed (ActionEvent e){
        //user pressed "Back" button
        try {
            FXMLStephensPetrochko.getInstance().replaceSceneContent("MainScreen.fxml", Base.PROJECT_TITLE);
        } catch (Exception ex) {
            Logger.getLogger(PresetQuizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initializeListView(String defaultLocation, String userDefaultLocation){
        /*This function is responsible for going to the Quizzes default location
         * and processing every quiz and making a corresponding ListView element.
         */
        File theDefaultDirectory = new File(defaultLocation);
        File userDefaultDirectory = new File(userDefaultLocation);
        
        
        
        //load in files from default directory
        if(theDefaultDirectory.exists()&&theDefaultDirectory.isDirectory()){
            for (File currentFile : theDefaultDirectory.listFiles()){
                if(currentFile.getName().split("\\.").length>1){
                    //make sure it's not a directory
                    if(currentFile.getName().split("\\.")[1].equals("quz")){
                        PresetQuizFileWrapper fileItem = new PresetQuizFileWrapper(currentFile);
                        theListView.getItems().add(fileItem);
                    }
                }else{
                    //not a quiz
                }
            }
        }else{
            //either the directory doesn't exist, or it's not a directory
        }
        
        
        
        //now load in files from user-made quizzes directory
        if(userDefaultDirectory.exists()&&userDefaultDirectory.isDirectory()){
            for (File currentFile : userDefaultDirectory.listFiles()){
                if(currentFile.getName().split("\\.").length>1){
                    //make sure it's not a directory
                    if(currentFile.getName().split("\\.")[1].equals("quz")){
                        PresetQuizFileWrapper fileItem = new PresetQuizFileWrapper(currentFile);
                        theListView.getItems().add(fileItem);
                    }
                }else{
                    //not a quiz
                }
            }
        }else{
            //either the directory doesn't exist, or it's not a directory
        }
        
        
        
        theListView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<PresetQuizFileWrapper>() {
            @Override
            public void changed(ObservableValue<? extends PresetQuizFileWrapper> ov, PresetQuizFileWrapper oldValue, PresetQuizFileWrapper newValue) {
                label.setText(newValue.toString());
            }
        });
    }
    
    public void TakeIt(){
        try{
        if(theListView.getSelectionModel().getSelectedItem()!=null){
            PresetQuizFileWrapper fileWrapper = (PresetQuizFileWrapper) theListView.getSelectionModel().getSelectedItem();
            File quizFile = fileWrapper.getFile();
            try{
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(quizFile));
                SerializableQuizCreation serialized = (SerializableQuizCreation) in.readObject();
                in.close();
                QuizCreation toTake = serialized.toQuizCreation();
                toTake.setName(quizFile.getName().split("\\.")[0]);
                Base.TakeQuiz(toTake);
            }catch(Exception e){
                e.printStackTrace();
                new PopupSender().ShowBadQuizPopup();
            }
        }
        }catch (Exception e){
            e.printStackTrace();
            new PopupSender().ShowBadQuizPopup();
        }
    }
}
