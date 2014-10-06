/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

/**
 *
 * @author Tyler
 */
public class SerializableQuizCreation implements Serializable {
    /*This class is used to hold a quiz, which will be serialized and then
     * deserialized when taken. Very similar to InProgressQuizCreation.
     * 
     * 
     * NOTE: This object is only used to SAVE quizzes, not take them. To 
     * take a quiz, it must be converted from a SerializableQuizCreation 
     * to a QuizCreation.
     */
    
    private ArrayList <geoObjHolder> listOfGeoObjHolders;
    private geoObjHolder root;
    private int type;
    
    public SerializableQuizCreation(ListView lv, geoObj theGeoObj, int quizType){
        type = quizType;
        setUpList(lv);
        root = new geoObjHolder(theGeoObj, true);
    }
    
    private void setUpList(ListView lv){
        //sets up the arraylist (to reconvert it into a listview)
        ObservableList<geoObj> listItems = lv.getItems();
        listOfGeoObjHolders = new ArrayList<geoObjHolder>();
        for(int currentIndex = 0; currentIndex<listItems.size();currentIndex++){
            listOfGeoObjHolders.add(new geoObjHolder(listItems.get(currentIndex), true));
        }
    }
    
    private ArrayList<geoObj> getGeoObjs(){
        //does not return geoObjs with children, just data
        ArrayList<geoObj> toReturn = new ArrayList<geoObj>();
        
        if(listOfGeoObjHolders!=null){
            for(int current = 0;current<listOfGeoObjHolders.size();current++){
                toReturn.add(listOfGeoObjHolders.get(current).toGeoObj());
            }
        }
        
        return toReturn;
    }
    public QuizCreation toQuizCreation(){
        ArrayList<geoObj> listToConvert = new ArrayList<geoObj>();
        for(int x=0;x<listOfGeoObjHolders.size();x++){
            listToConvert.add(listOfGeoObjHolders.get(x).toGeoObj());
        }
        QuizCreation toReturn = new QuizCreation(listToConvert, root.toGeoObj(), Base.defaultQuizName, type);
        return toReturn;
    }
    
}
