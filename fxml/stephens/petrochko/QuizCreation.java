/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.util.ArrayList;

/**
 *
 * @author Tyler
 */
public class QuizCreation {
    /*This class is a "carrier" class to bring a list of geo objects
     * into the Quiz-Taking Functionality.
     */
    private ArrayList<geoObj> geoObjs;
    private geoObj root;
    private String name;
    private int type;
    
    static final int DRAG_DROP = 0;
    static final int POINT_CLICK = 1;
    
    public QuizCreation(ArrayList<geoObj> allGeoObjs, geoObj theRoot, String itsName, int quizType){
        type = quizType;
        root = theRoot;
        root.children = null; //saves space
        geoObjs=allGeoObjs;
        name = itsName;
    }
    
    public void SetGeoObjs(ArrayList<geoObj> toSet){
        geoObjs=toSet;        
    }
    
    public ArrayList<geoObj> getGeoObjs(){
        return geoObjs;
    }
    public String getName(){
        return name;
    }
    public void setName(String toName){
        name = toName;
    }
    public String toString(){
        return name;
    }
    public geoObj getRoot(){
        return root;
    }
    public int getType(){
        return type;
    }
}
