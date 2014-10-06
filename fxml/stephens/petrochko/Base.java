/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Tyler
 */
public class Base {
    /* This function is called as soon as the program is run,
     * and is called in the MainScreenController.java class.
     * It is responsible (now) for initializing the Map Pack 
     * folder 
     */
    
    
    static geoObj rootGeoObj;
    //progressLocation and userMapPackLocation is added on to System.getProperty("user.home")
    static String quizLocation = "C:/Program Files/Stephens-Petrochko/Quizzes";
    static String mapPackLocation = "C:/Program Files/Stephens-Petrochko/Map Packs";
    static String userMapPackLocation ="/Documents/Stephens-Petrochko/Map Packs";
    static String cityGraphicLocation = "C:/Program Files/Stephens-Petrochko/CityGraphic.png";
    static String logoLocation = "Logo.png";
    static String progressLocation = "/Documents/Stephens-Petrochko/Quizzes";//for USER-made quizzes
    static final String settingsLocation = System.getenv("APPDATA");
    static final String defaultQuizName = "Quiz";
    static final String MAKE_QUIZ_TITLE = "Make a Quiz";
    static final String PROJECT_TITLE = "Stephens-Petrochko";
    static final String CHOOSE_PRESET_QUIZ_TITLE = "Choose a Quiz";
    static final String SETTINGS_TITLE = "Settings";
    static final int pixelGiveOrTake = 50;
    
    public Base(){
        run();
    }
    private void run(){
        /* immediately called when program starts, responsible
         * for calling initialize() and various things.
         */
    }
    static geoObj getRootGeoObjFromDirectory(File theDirectory){
        //Takes a directory and sets up root
        geoObj toReturn=null;
        try{
            toReturn = new Initializer().initialize(theDirectory);
            
        } catch(Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }
    
    static void TakeQuiz(QuizCreation toTake){
        /*This is the method used to take a quiz, whether from 
         * the Choose a Preset Quiz or Make A Quiz menu. It checks
         * whether the quiz is null or not, and if not, then it
         * loads a static buffer in the Base class to allow the 
         * controller to access it. Clever, right? Thanks. I wrote it.
         */
        if(toTake!=null){
            Auxiliary.QUIZ_BUFFER = toTake;
            try{
                FXMLStephensPetrochko.getInstance().replaceSceneContent("QuizTaker.fxml", toTake.getName(), false, true);
            }catch(Exception e){
                e.printStackTrace();
            }
            Auxiliary.QUIZ_BUFFER = null;
        }
    }
    public static void initializeSettings(){
        try{
            File settingsFolder = new File(Base.settingsLocation);
            File settingsFile = new File(settingsFolder.getAbsolutePath()+"/Stephens-Petrochko/settings.ser");
            Settings settingsToSet;
            if(settingsFile.exists()){
                //a settings file exists
                System.out.println("A settings file exists!");
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(settingsFile));
                settingsToSet = (Settings) in.readObject();
                in.close();
            }else{
                //no settings file - make a new one!
                System.out.println("a settings file does NOT exist!");
                settingsToSet = new Settings();
                Auxiliary.saveSettings(settingsToSet);
            }
            Auxiliary.setSettings(settingsToSet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static void ToggleFullScreen(){
        try{
                FXMLStephensPetrochko.getInstance().ToggleFullScreen();
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    static void Maximize(boolean toSet){
        try{
                FXMLStephensPetrochko.getInstance().maximize(toSet);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    
}
