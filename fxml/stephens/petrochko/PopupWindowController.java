/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Tyler
 */
public class PopupWindowController implements Initializable {

    @FXML public Label label;
    @FXML public Button ok;
    @FXML public Label text;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(PopupSender.titleHolder!=null){
            setLabel(PopupSender.titleHolder);
            PopupSender.titleHolder = null;
        }
        if(PopupSender.messageHolder!=null){
            setText(PopupSender.messageHolder);
            PopupSender.messageHolder=null;
        }
    }   
    
    private void setLabel(String toSet){
        label.setText(toSet);
    }
    private void setText(String toSet){
        text.setText(toSet);
    }
    public void Okay(ActionEvent e){
        Node  source = (Node)  e.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
