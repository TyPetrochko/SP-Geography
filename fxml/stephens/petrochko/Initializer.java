/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Tyler
 */
public class Initializer {
    /*This assumes that a valid folder format has ONE folder in its main 
     * directory with a corresponding .png file to serve as the background 
     * for the map. Otherwise, it will throw an error.
     */
    
    
    Path directory;

    geoObj initialize(File directory) throws InvalidFolderFormatException, DNEException, ImproperFileTypeException {
        /*takes a directory, and returns the root 
         * geoObj with all necessary children.
         */
        geoObj root = setUpRoot(directory);
        return root;
    }
   private geoObj setUpRoot(File directory) throws InvalidFolderFormatException{
       //sets up root file, WITH children
       //directory is the map-pack folder, contained in which is the first geoObj
       geoObj toReturn = null;
       if (Auxiliary.findNumOfFolders(directory) != 1){
           //doesn't meet the right format; not exactly one matching folder
           throw new InvalidFolderFormatException();
       }else{
           //this is where rootGeoObj is actually set up
           try{
                File firstGeoObj = Auxiliary.findFirstFolder(directory);
                toReturn = new geoObj();
                toReturn.name = firstGeoObj.getName();
                File matchingPNG = Auxiliary.findMatchingPNG(directory, firstGeoObj);
                toReturn.mapElement = new Image(matchingPNG.toURI().toString());
                toReturn.children = addRecursivelyChildren(firstGeoObj);
                toReturn.type = geoObj.BACKGROUND;
                toReturn.layer= -1;
                return toReturn;
           }catch (Exception e){
               throw new InvalidFolderFormatException();
           }
       }
   }
   private ArrayList <geoObj> addRecursivelyChildren(File father) throws InvalidFolderFormatException, DNEException, ImproperFileTypeException{
       ArrayList <geoObj> allChildren = new ArrayList <geoObj>();
       //System.out.println("Recursively adding children of "+father.getName());
       /*this function receives a parent (father) folder, and its duty
        * is to traverse all the included files and return an ArrayList
        * that has all geoObjs underneath it. NOTICE that some of those
        * geoObjs will likely have children of their own, therefore it
        * is a recursive function.
        */
       File [] children = father.listFiles();
       /*There are 5 types of geoObjs:
        *   A)folder + .txt + .png (category and feature: state, country, etc.)
        *   B)folder (category: NE America, SE America)
        *   C).txt (a city or pinpoint location)
        *   D).txt +.png (feature: Mississippi River, Bucks County)
        *   E)folder + .png (background) - notice this is not included in the following functions
        * 
        * So in traversing the files, this system is used to avoid repeats:
        *   1) if it's a .png, move on to next file
        *   2) if it's a .txt, make a new geoObj and decide whether it's type
        *       A, C, or D
        *   3) if it's a folder, look for a corresponding .txt, and if it exists,
        *       do nothing. If one does not, then make a new geoObj of type B
        */
       if(!father.isDirectory()){
           throw new ImproperFileTypeException();
       }
       for(File currentFile: children){
           //first, find if it's a folder or a file
           System.out.println("Analyzing "+currentFile.getName());
           String[] splitFile = currentFile.getName().split("\\.");
           if (splitFile.length>1){
               //it's a file, or something.something
               if(splitFile[1].equals("png")){
                   //it's a .png file
                   //do nothing
               }else if (splitFile[1].equals("txt")){
                   //it's a .txt file
                   geoObj newGeoObj = new geoObj();
                   if(Auxiliary.hasMatchingPNG(father, currentFile)){
                       //has a matching PNG, so either type A or type
                       //System.out.println(currentFile.getName()+" is either A or D");
                       if(Auxiliary.hasMatchingFolder(father, currentFile)){
                           //it's type A, so a category and feature
                           System.out.println("Found a type A");
                           newGeoObj.type = geoObj.CATEGORY_AND_FEATURE;
                           File matchingPNG = Auxiliary.findMatchingPNG(father, currentFile);
                           newGeoObj.mapElement = new Image(matchingPNG.toURI().toString());
                           coords geoObjCoords = Auxiliary.findCoords(currentFile);
                           newGeoObj.xCoord = geoObjCoords.x;
                           newGeoObj.yCoord = geoObjCoords.y;
                           newGeoObj.name = Auxiliary.findTrueName(currentFile.getName());
                           if(Auxiliary.hasLayer(currentFile)){
                               //find if a layer is specified
                               int layer = Auxiliary.getLayer(currentFile);
                               if(layer>0){
                                   newGeoObj.layer = layer;
                                   System.out.println(newGeoObj+" is in layer "+layer);
                               }else{
                                   newGeoObj.layer = 1;
                               }
                           }else{
                               newGeoObj.layer=1;
                           }
                           
                           File matchingFolder = Auxiliary.findMatchingFolder(father, currentFile);
                           newGeoObj.children = addRecursivelyChildren(matchingFolder);
                       }else{
                           //it's type D, so just a feature
                           System.out.println("Found a type D");
                           newGeoObj.type = geoObj.FEATURE;
                           File matchingPNG = Auxiliary.findMatchingPNG(father, currentFile);
                           newGeoObj.mapElement = new Image(matchingPNG.toURI().toString());
                           coords geoObjCoords = Auxiliary.findCoords(currentFile);
                           newGeoObj.xCoord = geoObjCoords.x;
                           newGeoObj.yCoord = geoObjCoords.y;
                           newGeoObj.name = Auxiliary.findTrueName(currentFile.getName());
                           if(Auxiliary.hasLayer(currentFile)){
                               //find if a layer is specified
                               int layer = Auxiliary.getLayer(currentFile);
                               if(layer>0){
                                   newGeoObj.layer = layer;
                                   System.out.println(newGeoObj+" is in layer "+layer);
                               }else{
                                   newGeoObj.layer = 1;
                               }
                           }else{
                               newGeoObj.layer=1;
                           }
                           
                       }
                   }else{
                       //it's type C, so a city
                       System.out.println("Found a type C");
                       newGeoObj.type = geoObj.CITY;
                       coords geoObjCoords = Auxiliary.findCoords(currentFile);
                       newGeoObj.xCoord = geoObjCoords.x;
                       newGeoObj.yCoord = geoObjCoords.y;
                       newGeoObj.layer=0;
                       newGeoObj.name = Auxiliary.findTrueName(currentFile.getName());
                   }
                   allChildren.add(newGeoObj);
               }
           }else if(currentFile.isDirectory()){
               //it's a folder, so no 'dot' indicator
               if(Auxiliary.hasMatchingPNG(father, currentFile)){
                   //it's type A, so do nothing
               }else{
                   //it's type B, so just a category
                   System.out.println("Found a type B");
                   geoObj newGeoObj = new geoObj();
                   newGeoObj.type = geoObj.CATEGORY;
                   newGeoObj.name = Auxiliary.findTrueName(currentFile.getName());
                   newGeoObj.children = addRecursivelyChildren(currentFile);
                   allChildren.add(newGeoObj);
               }
           }else{
               throw new InvalidFolderFormatException();
           }
       }
       
       return allChildren;
   }
    
   
}
