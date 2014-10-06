/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author Tyler
 */
public class InProgressQuizCreation implements Serializable {
    /* A workaround due to the fact that 
     * literally NOTHING in Java FX is
     * serializable - so basically I have
     * infinitely more work to do. Thanks,
     * Oracle.
     * 
     * The constructor is passed a TreeView and a ListView, which
     * it uses to create a root geoObjHolder (to mimic TreeView) 
     * and an ArrayList of geoObjHolders (to mimic ListView). In
     * this format, this object is SERIALIZABLE so it can be saved!
     */
    private geoObjHolder root;
    private ArrayList<geoObjHolder> list;
    private int quizType;
    
    public InProgressQuizCreation(TreeView tv, ListView lv){
        list = new ArrayList<geoObjHolder>();
        setUpList(lv);
        setUpRoot(tv);
    }
    
    public geoObjHolder getRoot(){
        return root;
    }
    public ArrayList<geoObjHolder> getList(){
        return list;
    }

    
    private void setUpList(ListView lv){
        //sets up the arraylist (to reconvert it into a listview)
        ObservableList<geoObj> listItems = lv.getItems();
        for(int currentIndex = 0; currentIndex<listItems.size();currentIndex++){
            list.add(new geoObjHolder(listItems.get(currentIndex), true));
        }
    }

    private void setUpRoot(TreeView tv){
        //sets up the root geoObjHolder, to reconvert to a TreeView
        root = recursivelyAddGeoObjHolders(tv.getRoot());
    }
    private geoObjHolder recursivelyAddGeoObjHolders(TreeItem<geoObj> current){
        geoObjHolder toReturn = new geoObjHolder(current.getValue(), ((CheckBox) current.getGraphic()).isSelected());
        if(!current.isLeaf()){
            for(int x =0;x<current.getChildren().size();x++)
            toReturn.add(recursivelyAddGeoObjHolders(current.getChildren().get(x)));
        }
        return toReturn;
    }
}
