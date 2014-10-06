
package fxml.stephens.petrochko;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.image.Image;


/**
 *
 * @author Tyler
 */
public class geoObj implements Serializable{
    static final int CATEGORY_AND_FEATURE=0;
    static final int CATEGORY = 1;
    static final int CITY = 2;
    static final int FEATURE = 3;
    static final int BACKGROUND = 4;
    
    boolean selectedInTree;
    String name;
    int type;
    ArrayList <geoObj> children;
    int xCoord;
    int yCoord;
    Image mapElement; // if this doesn't work, consider non-javafx import
    int layer;
    
    public geoObj(){
        name = null;
        children = new ArrayList<geoObj>();
        mapElement = null;
    }
    public boolean hasChildren (){
        if(children!= null){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public String toString(){
        return name;
    }
    
}
