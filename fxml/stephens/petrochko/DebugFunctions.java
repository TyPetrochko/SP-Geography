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
public class DebugFunctions {
    
    public void DebugTreeFromRoot(geoObj root){
        analyze(root, 0);
    }
    private void analyze (geoObj current, int level){
        ArrayList <geoObj> children = current.children;
        String bump = "";
        for(int bumper = 0; bumper<level;bumper++){
            bump = bump+"    ";
        }
        
        System.out.println(bump+current.name+"; coords: "+current.xCoord+"x"+current.yCoord+"; Type: "+current.type);
        if(current.hasChildren()){
            geoObj [] childrenArray = new geoObj [children.size()];
            childrenArray = children.toArray(childrenArray);
            for(geoObj child: childrenArray){
                analyze(child, level+1);
            }
        }
    }
}
