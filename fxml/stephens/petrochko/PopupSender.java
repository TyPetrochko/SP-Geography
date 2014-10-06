/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Tyler
 */
public class PopupSender {
    
    static String titleHolder;
    static String messageHolder;
    
    public void SendPopup(String title, String message){
        Base.Maximize(false);
        try{
            if(messageHolder==null){
                messageHolder = message;
            }
            if(titleHolder==null){
                titleHolder = title;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("PopupWindow.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle(title);
            stage.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ShowHowTo(){
        Base.Maximize(false);
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("HowTo.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("How To");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PopupSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ShowAbout(){
        Base.Maximize(false);
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("About.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("About");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PopupSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void AreYouSureYouWantToExit(){
        Base.Maximize(false);
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("SureYouWantToExit.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("Sure you want to exit?");
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(PopupSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ShowBadQuizPopup(){
        Base.Maximize(false);
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("BadQuizPopup.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("Bad Quiz");
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(PopupSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void ShowRestoreDefaultSettings(){
        Base.Maximize(false);
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("RestoreDefaultSettings.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("Restore Default Settings");
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(PopupSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void ShowMessage (String message, String title){
        Base.Maximize(false);
        //Create generic UI elements
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        Label messageLabel = new Label();
        
        
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        messageLabel.setText(message);
        grid.add(messageLabel, 0, 0);
        
        
        
        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setWidth(600);
        stage.setResizable(false);
        stage.setTitle(title);
        stage.showAndWait();
        
    }
}
