/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Tyler
 */
public class FXMLStephensPetrochko extends Application {
    private Stage stage;
    private static FXMLStephensPetrochko instance;
    
    public FXMLStephensPetrochko(){
        instance = this;
    }
    public static FXMLStephensPetrochko getInstance(){
        return instance;
    }
    
    @Override
    public void start(Stage newStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        stage = newStage;
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle(Base.PROJECT_TITLE);
        stage.show();
        Base.initializeSettings();
        
    }

    public void replaceSceneContent(String fxml, String name) throws Exception{
        //replacing scene content
        Parent toReplace = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(toReplace);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setTitle(name);
        stage.setResizable(false);
        stage.setFullScreen(false);
    }
    public void replaceSceneContent(String fxml, String name, boolean maximize, boolean resizable) throws Exception{
        //replacing scene content
        Parent toReplace = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(toReplace);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setTitle(name);
        stage.setResizable(resizable);
        stage.setFullScreen(maximize);
    }
    
    public void ToggleFullScreen (){
        stage.setFullScreen(!stage.isFullScreen());
    }
    
    public void maximize(boolean toSet){
        stage.setFullScreen(toSet);
    }
    

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
