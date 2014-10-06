/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author Tyler
 */
public class Auxiliary {
    static final boolean ELEMENTS_INITIALLY_CHECKED = false;
    static QuizCreation QUIZ_BUFFER;
    
    
    /*This is an auxiliary class, so it has mostly all static functions.
     * It does included some debug functions that require an instance to be 
     * created, but otherwise not. Other static functions that pertain to the
     * program (stages etc.) are handled in Base.
     */
    
    static String findTrueName(String toParse){
        //takes first part of filename, i.e. "hi.html" becomes "hi"
        return toParse.split("\\.")[0];
    }
    static int findNumOfFolders(File f){
        //finds the number of folders in a directory
        int counter = 0;
        File [] a = f.listFiles();
        for (File c: a){
            if(c.isDirectory()){
                counter++;
            }
        }
        return counter;
    }
    static boolean hasMatchingPNG (File directory, File toMatch){
        //looks to see if a corresponding PNG file exists
        boolean toReturn = false;
        File [] a = directory.listFiles();
        for (File c: a){
            boolean matchesFirst = Auxiliary.findTrueName(c.getName()).equals(Auxiliary.findTrueName(toMatch.getName()));
            if(matchesFirst&&c.isFile()){
                boolean PNG = c.getName().split("\\.")[1].equals("png");
                if(PNG){
                    //System.out.println("Directory "+directory.getName()+" does have a .png matching "+toMatch.getName());
                    return true;
                }
            }
        }//System.out.println("Directory "+directory.getName()+" does not have a .png matching "+toMatch.getName());
         return false;
    }
    
    static File findMatchingPNG (File directory, File toMatch) throws DNEException{
         //returns matching PNG
        File toReturn = null;
        File [] a = directory.listFiles();
        for (File c: a){
            boolean matchesFirst = Auxiliary.findTrueName(c.getName()).equals(Auxiliary.findTrueName(toMatch.getName()));
            if(matchesFirst&&c.isFile()){
                boolean PNG = c.getName().split("\\.")[1].equals("png");
                if(PNG){
                    return c;
                }
            }
        }
        if(toReturn==null){
            throw new DNEException();
        }
        return toReturn;
    }
    
    static File findFirstFolder (File directory) throws DNEException{
        File toReturn = null;
        File [] toTraverse = directory.listFiles();
        for (File c: toTraverse){
            if(c.isDirectory()){
                return c;
            }
        }
        throw new DNEException();
    }
    
    static boolean hasMatchingTXT (File directory, File toMatch){
        //looks to see if a corresponding TXT file exists
        //notice* this was copied from the corresponding PNG function, so errors may exist
        boolean toReturn = false;
        File [] a = directory.listFiles();
        for (File c: a){
            boolean matchesFirst = Auxiliary.findTrueName(c.getName()).equals(Auxiliary.findTrueName(toMatch.getName()));
            if(matchesFirst&&c.isFile()){
                boolean TXT = c.getName().split("\\.")[1].equals("txt");
                if(TXT){
                    return true;
                }
            }
        }
         return false;
    }
    
    static File findMatchingTXT (File directory, File toMatch) throws DNEException{
        //returns matching TXT
        //notice* this was copied from the corresponding PNG function, so errors may exist
        File toReturn = null;
        File [] a = directory.listFiles();
        for (File c: a){
            boolean matchesFirst = Auxiliary.findTrueName(c.getName()).equals(Auxiliary.findTrueName(toMatch.getName()));
            if(matchesFirst&&c.isFile()){
                boolean TXT = c.getName().split("\\.")[1].equals("txt");
                if(TXT){
                    return c;
                }
            }
        }
        if(toReturn==null){
            throw new DNEException();
        }
        return toReturn;
    }
    static boolean hasMatchingFolder (File directory, File toMatch){
        //looks to see if a corresponding folder exists
        boolean toReturn = false;
        File [] a = directory.listFiles();
        for (File c: a){
          if (c.isDirectory()&&c.getName().equals(Auxiliary.findTrueName(toMatch.getName()))){
              return true;
          }
        }
         return toReturn;
    }
    
    static File findMatchingFolder (File directory, File toMatch) throws DNEException{
        //looks to see if a corresponding folder exists
        File toReturn = null;
        File [] a = directory.listFiles();
        for (File c: a){
          if (c.isDirectory()&&c.getName().equals(Auxiliary.findTrueName(toMatch.getName()))){
              return c;
          }
        }
         if(toReturn==null){
            throw new DNEException();
        }
         return toReturn;
    }
    
    static coords findCoords (File f){
        //not yet finished
        coords c = new coords(0, 0);
        try{
            String input = findTextFromFile(f);
            String firstLine = input.split("\\r?\\n")[0];
            String [] splitCoords = firstLine.split("x");
            c.x = Integer.parseInt(splitCoords[0]);
            c.y = Integer.parseInt(splitCoords[1]);
        }catch(Exception e){}
        
        return c;
    }
    static boolean hasLayer(File f){
        try{
            String input = findTextFromFile(f);
            String secondLine = input.split("\\r?\\n")[1];
            int layer = Integer.parseInt(secondLine.trim());
            if(layer>0){
                return true;
            }
        }catch(Exception e){}
        
        return false;
    }
    static int getLayer(File f){
        //gets the layer from a text file, returns -1 if it can't find it
        try{
            String input = findTextFromFile(f);
            String secondLine = input.split("\\r?\\n")[1];
            int layer = Integer.parseInt(secondLine.trim());
            if(layer>0){
                return layer;
            }
        }catch(Exception e){}
        return -1;
    }
    
    static String findTextFromFile(File f){
        //Takes a file and returns the text from it
        //If no text exists, it returns null
        String input = "";
        try{
            FileReader fileReader = new FileReader(f);
            BufferedReader reader = new BufferedReader(fileReader);
            String currentLine = null;
            while((currentLine=reader.readLine())!=null){
                input=input+currentLine+"\n";
            }
            
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return input;
    }
    
    static File getDirectoryViaDialogBox (){
        /*This function returns a file to be used for the root directory.
         * It can only return a directory.
         */
        DirectoryChooser dc = new DirectoryChooser();
        return dc.showDialog(null);
    }
    
    static File getDirectoryViaDialogBox (String startingLocation){
        /*This function returns a file to be used for the root directory.
         * It can only return a directory.
         */
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(startingLocation));
        return dc.showDialog(null);
    }
    
    static Image getCityGraphic(){
        try{
            File f = new File(Base.cityGraphicLocation);
            Image toReturn = new Image(f.toURI().toString());
            return toReturn;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    static void setSettings (Settings toSet){
        Base.userMapPackLocation = toSet.getUserMapPackLocation();
        Base.cityGraphicLocation = toSet.getCityGraphicLocation();
        Base.mapPackLocation = toSet.getMapPackLocation();
        Base.quizLocation = toSet.getQuizLocation();
        Base.progressLocation = toSet.getProgressLocation();
        
        //do any user folders need to be set up?
        try{
            File progressFolder = new File(Base.progressLocation);
            File userMapPackFolder = new File(Base.userMapPackLocation);
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
        //check if any Program Files are missing 
        try{
            File CompQuizFolder = new File(Base.quizLocation);
            File CompMapPackFolder = new File(Base.mapPackLocation);
            File CityGraphic = new File(Base.cityGraphicLocation);
            String toPrint = "";
            
            if(!CompQuizFolder.exists() || !CompQuizFolder.isDirectory()){
                toPrint += "Quiz Folder not set up: "+CompQuizFolder.getAbsolutePath()+"\n";
            }
            if(!CompMapPackFolder.exists() || !CompMapPackFolder.isDirectory()){
                toPrint += "Map Pack Folder not set up: "+CompMapPackFolder.getAbsolutePath()+"\n";
            }
            if(!CityGraphic.exists()){
                toPrint += "Missing City Graphic: "+CityGraphic+"\n";
            }
        
            //now send error message if needed
            if(!toPrint.equals("")){
                PopupSender.ShowMessage(toPrint, "Missing Folder(s)/File(s)");
            }
        }catch(Exception e){
            e.printStackTrace();
            PopupSender.ShowMessage("Error reaching Program Files folder(s)", "Error");
        }
    }
    static void saveSettings (Settings toSave){
        try{
            File locationToSave = new File(System.getenv("APPDATA")+"/Stephens-Petrochko");
            if(!locationToSave.isDirectory()){
                locationToSave.mkdir();
            }
            System.out.println("Attempting to make "+locationToSave.getAbsolutePath()+"/settings.ser");
            FileOutputStream fs = new FileOutputStream(locationToSave.getAbsolutePath()+"/settings.ser");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(toSave);
            os.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void copyFolder(File src, File dest)throws IOException{
        /*
         * Found a guy who perfected this class
         * check it out at: 
         * http://www.mkyong.com/java/how-to-copy-directory-in-java/
        */ 
    	if(src.isDirectory()){
 
    		//if directory not exists, create it
    		if(!dest.exists()){
    		   dest.mkdir();
    		   System.out.println("Directory copied from " 
                              + src + "  to " + dest);
    		}
 
    		//list all the directory contents
    		String files[] = src.list();
 
    		for (String file : files) {
    		   //construct the src and dest file structure
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   //recursive copy
    		   copyFolder(srcFile,destFile);
    		}
 
    	}else{
    		//if file, then copy it
    		//Use bytes stream to support all file types
    		InputStream in = new FileInputStream(src);
    	        OutputStream out = new FileOutputStream(dest); 
 
    	        byte[] buffer = new byte[1024];
 
    	        int length;
    	        //copy the file content in bytes 
    	        while ((length = in.read(buffer)) > 0){
    	    	   out.write(buffer, 0, length);
    	        }
 
    	        in.close();
    	        out.close();
    	        System.out.println("File copied from " + src + " to " + dest);
    	}
    }
    static void copyFile (File src, File dest){
        InputStream in = null;
        try {
            in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            //copy the file content in bytes
            while ((length = in.read(buffer)) > 0){
                   out.write(buffer, 0, length);
            }
            in.close();
            out.close();
            System.out.println("File copied from " + src + " to " + dest);
        } catch (Exception ex) {
            Logger.getLogger(Auxiliary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Auxiliary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
