/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.nio.file.StandardCopyOption;


/**
 * FXML Controller class
 *
 * @author Tyler
 */
public class SettingsScreenController implements Initializable {
    

    @FXML public Button ChooseQuizzesFolder;
    @FXML public Button ChooseUserMapPackFolder;
    @FXML public Button ChooseProgressFolder;
    @FXML public Button ChooseCityGraphic;
    
    @FXML public Button SettingsHelpButton;
    @FXML public Button SaveSettings;
    @FXML public Button ImportQuiz;
    @FXML public Button ImportMapPack;
    @FXML public Button Cancel;
    
    @FXML public TextField QuizzesFolderLocation;
    @FXML public TextField UserMapPackLocation;
    @FXML public TextField ProgressFolderLocation;
    @FXML public TextField CityGraphicLocation;
    
    public void initialize(URL url, ResourceBundle rb) {
        //First - load the settings
        QuizzesFolderLocation.setText(Base.quizLocation);
        UserMapPackLocation.setText(Base.userMapPackLocation);
        ProgressFolderLocation.setText(Base.progressLocation);
        CityGraphicLocation.setText(Base.cityGraphicLocation);
    }    
    
    public void ChooseQuizzesFolder(){
        File newDirectory = Auxiliary.getDirectoryViaDialogBox(QuizzesFolderLocation.getText());
        if(newDirectory!=null){
            QuizzesFolderLocation.setText(newDirectory.getAbsolutePath());
        }
    }
    
    public void ChooseUserMapPackFolder(){
        File newDirectory = Auxiliary.getDirectoryViaDialogBox(UserMapPackLocation.getText());
        if(newDirectory!=null){
            UserMapPackLocation.setText(newDirectory.getAbsolutePath());
        }
    }
    
    public void ChooseProgressFolder(){
        File newDirectory = Auxiliary.getDirectoryViaDialogBox(ProgressFolderLocation.getText());
        if(newDirectory!=null){
            ProgressFolderLocation.setText(newDirectory.getAbsolutePath());
        }
    }
    
    public void ChooseCityGraphic(){
        File newDirectory = Auxiliary.getDirectoryViaDialogBox(CityGraphicLocation.getText());
        if(newDirectory!=null){
            CityGraphicLocation.setText(newDirectory.getAbsolutePath());
        }
    }
    
    public void SaveSettings(){
        Settings toSave = new Settings();
        toSave.setCityGraphicLocation(CityGraphicLocation.getText());
        toSave.setUserMapPackLocation(UserMapPackLocation.getText());
        toSave.setProgressLocation(ProgressFolderLocation.getText());
        toSave.setQuizLocation(QuizzesFolderLocation.getText());
        Auxiliary.saveSettings(toSave);
        Auxiliary.setSettings(toSave);
        try {
            FXMLStephensPetrochko.getInstance().replaceSceneContent("MainScreen.fxml", Base.PROJECT_TITLE);
        } catch (Exception ex) {
            Logger.getLogger(SettingsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Cancel (){
        try {
            FXMLStephensPetrochko.getInstance().replaceSceneContent("MainScreen.fxml", Base.PROJECT_TITLE);
        } catch (Exception ex) {
            Logger.getLogger(SettingsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void RestoreDefaultSettings(){
        try {
            FXMLStephensPetrochko.getInstance().replaceSceneContent("MainScreen.fxml", Base.PROJECT_TITLE);
        } catch (Exception ex) {
            Logger.getLogger(SettingsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        File settingsFolder = new File(Base.settingsLocation);
        File settingsFile = new File(settingsFolder.getAbsolutePath()+"/Stephens-Petrochko/settings.ser");
        settingsFile.delete();
        new PopupSender().ShowRestoreDefaultSettings();
    }
    
    public void ShowHelp(){
        new PopupSender().ShowHowTo();
    }
    public void ShowAbout(){
        new PopupSender().ShowAbout();
    }
    
    public void ImportQuiz () {
        FileChooser fc = new FileChooser();
        fc.setTitle("Import Quiz");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Quiz(*.quz)", "*.quz"));
        fc.setInitialDirectory(new File(Base.quizLocation));
        File f = fc.showOpenDialog(null);
        File src = f;
        File destination = new File(Base.progressLocation+"/"+f.getName());
        
        Auxiliary.copyFile(src, destination);
        /*
        if(f!=null){
            try {
                Files.copy(src.toPath(), destination.toPath());
            } catch (IOException ex) {
                Logger.getLogger(SettingsScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
    }
    public void ImportMapPack(){
        File toImport = Auxiliary.getDirectoryViaDialogBox(ProgressFolderLocation.getText());
        if(toImport!=null){
            try {
                File destination = new File(Base.userMapPackLocation+"/"+toImport.getName());
                Auxiliary.copyFolder(toImport, destination);
            } catch (Exception ex) {
                Logger.getLogger(SettingsScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /*
     * If you put a million monkeys at a million keyboards, one of them will eventually write a Java program.
     * The rest of them will write Perl programs.
    */
}
