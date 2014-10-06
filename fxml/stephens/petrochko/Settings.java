/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Tyler
 */
public class Settings implements Serializable {
    
    private String quizLocation;
    private String mapPackLocation;
    private String userMapPackLocation;
    private String cityGraphicLocation;
    private String progressLocation;
    
    public Settings (){
        //default settings
        quizLocation = "C:/Program Files/Stephens-Petrochko/Quizzes";
        userMapPackLocation = System.getProperty("user.home")+"/Documents/Stephens-Petrochko/Map Packs";
        mapPackLocation = "C:/Program Files/Stephens-Petrochko/Map Packs";
        cityGraphicLocation = "C:/Program Files/Stephens-Petrochko/CityGraphic.png";
        progressLocation = System.getProperty("user.home")+"/Documents/Stephens-Petrochko/Quizzes";
        
        //make user folders if they don't exist (try, atleast)
        try{
            File progressFolder = new File(progressLocation);
            File userMapPackFolder = new File(userMapPackLocation);
            String toPrint = "";

            if(!progressFolder.exists() || !progressFolder.isDirectory()){
                progressFolder.mkdirs();
                toPrint += "Created User Quiz folder: "+progressFolder.getAbsolutePath()+"\n";
            }
            if(!userMapPackFolder.exists() || !userMapPackFolder.isDirectory()){
                userMapPackFolder.mkdirs();
                toPrint+= "Created User Map Pack folder: "+userMapPackFolder.getAbsolutePath()+"\n";
            }
            
            if(!toPrint.equals("")){
                PopupSender.ShowMessage(toPrint, "Created user folder(s)");
            }
        
        }catch(Exception e){
            e.printStackTrace();
            PopupSender.ShowMessage("Failed to create missing user folder(s)", "Error creating user folders");

        }
        
        
        
    }
    public String getQuizLocation(){
        return quizLocation;
    }
    public void setQuizLocation(String s){
        quizLocation = s;
    }
    public String getMapPackLocation(){
        return mapPackLocation;
    }
    public void setMapPackLocation(String s){
        mapPackLocation = s;
    }
    public String getCityGraphicLocation (){
        return cityGraphicLocation;
    }
    public void setCityGraphicLocation (String s){
        cityGraphicLocation = s;
    }
    public String getProgressLocation (){
        return progressLocation;
    }
    public void setProgressLocation (String s){
        progressLocation = s;
    }
    public void setUserMapPackLocation (String s){
        userMapPackLocation = s;
    }
    public String getUserMapPackLocation (){
        return userMapPackLocation;
    }
    // that ^ actually took forever to make -.-
}
