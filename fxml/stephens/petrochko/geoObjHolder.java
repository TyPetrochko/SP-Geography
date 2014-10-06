/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 *
 * @author Tyler
 */
public class geoObjHolder implements Serializable{
    /* Wrapper object to hold a geoObject and the state 
     * of its TreeItem in the TreeView. GeoObjs are not
     * serializable, so this is a serializable version 
     * of one. Only external methods are toGeoObj(),
     * getChildren(), add(), checked(). DOES NOT HANDLE
     * CHILDREN!!!!!
     */
    
    
    private String name;
    private int type;
    private int xCoord;
    private int yCoord;
    private boolean checked;
    private ArrayList<geoObjHolder> children;
    private byte[] imageData;
    private int layer;
    
    public geoObjHolder(geoObj go, boolean chk){
        name = go.name;
        type = go.type;
        xCoord = go.xCoord;
        yCoord = go.yCoord;
        children = new ArrayList<geoObjHolder> ();
        if(go.mapElement!=null){
            loadImageData(go.mapElement);
        }
        checked = chk;
        layer = go.layer;
    }
    public geoObj toGeoObj(){
        geoObj toReturn = new geoObj();
        if(imageData!=null){
            toReturn.mapElement = restoreMapData(imageData);
            
        }
        toReturn.name = name;
        toReturn.type = type;
        toReturn.xCoord = xCoord;
        toReturn.yCoord = yCoord;
        toReturn.layer= layer;
        return toReturn;
    }
    public boolean checked (){
        return checked;
    }
    public void add(geoObjHolder toAdd){
        if(children==null){
            children = new ArrayList<geoObjHolder>();
        }
        children.add(toAdd);
    }
    public ArrayList<geoObjHolder> getChildren(){
        return children;
    }
    
    private void loadImageData (Image image){
        try{           
            /* Here, to address a bug that arises when converting a 
            * BufferedImage to a ByteArray, it is first converted to 
            * a BufferedImage with alpha specifically included
            */
            BufferedImage bi = SwingFXUtils.fromFXImage(image, null);
            BufferedImage convertedImg = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
            convertedImg.getGraphics().drawImage(bi, 0, 0, null);
            
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
            ImageIO.write(convertedImg, "png", baos );
            baos.flush();
            imageData = baos.toByteArray();
            baos.close();

            
           
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Image restoreMapData (byte[] data){
        try{
                InputStream in = new ByteArrayInputStream(data);
                BufferedImage bi = ImageIO.read(in);
                return SwingFXUtils.toFXImage(bi, null);
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
    }
    public boolean hasChildren(){
        if(!children.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

}

