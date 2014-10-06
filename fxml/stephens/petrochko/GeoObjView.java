/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Tyler
 */
public class GeoObjView extends ImageView {
    
    private geoObj link;
    
    public GeoObjView (geoObj link){
        super(returnAppropriateGraphic(link));
        this.link = link;
        try{
            this.setTranslateX(link.xCoord);
            this.setTranslateY(link.yCoord);
        }catch(Exception e){}
    }
    
    public geoObj getGeoObj(){
        return link;
    }
    static Image returnAppropriateGraphic(geoObj toCheck){
        if (toCheck.type==geoObj.CATEGORY_AND_FEATURE||toCheck.type==geoObj.FEATURE){
            return(toCheck.mapElement);
        }else if (toCheck.type==geoObj.CITY){
            return(Auxiliary.getCityGraphic());
        }
        return null;
    }
}
