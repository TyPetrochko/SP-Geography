/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Tyler
 */
public class MakeQuizController implements Initializable {
    ObservableList<MapPackFileWrapper> mapPacks;
    
    @FXML
    private TreeView tv;
    @FXML
    private ListView lv;
    @FXML
    ChoiceBox mapPackChoiceBox;
    @FXML
    AnchorPane mainPane;
    @FXML
    ChoiceBox quizTypeChoiceBox;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeChoiceBoxes();
        initializeObservableListAndListView();
    } 
    private TreeItem<geoObj> recursivelySetUpTree(geoObj parent){
        /*This function takes the root of the geoObj tree and translates it 
         * into a CheckBoxTreeItem of type geoObj, with included children.
         * It is recursively operated. 
         */
        final TreeItem<geoObj> itemToReturn = new TreeItem<>(parent);
        CheckBox checkBox = new CheckBox();
        
        
        //Variables referenced from an inner class that are outside must be made final first
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            /*To make a listener, you first have to get the selectedProperty of the
             * checkBoxTreeItem. Then, add a listener to the selected property of type
             * "ChangeListener<Boolean> then extend it as an inner class and include
             * overridden changed() function with necessary arguments.
             */
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                geoObj theGeoObj = itemToReturn.getValue();
                if (t1){
                    //itemToReturn was just checked
                    checkAllChildren(itemToReturn);
                    if(theGeoObj.type==geoObj.CATEGORY_AND_FEATURE||theGeoObj.type==geoObj.FEATURE||theGeoObj.type==geoObj.CITY){
                        //it's type A C or D
                        addToList(theGeoObj);
                    }
                }else{
                    //itemToReturn was just unchecked
                    uncheckAllChildren(itemToReturn);
                    //System.out.println(theGeoObj.name+" was just unchecked");
                    if(theGeoObj.type==geoObj.CATEGORY_AND_FEATURE||theGeoObj.type==geoObj.FEATURE||theGeoObj.type==geoObj.CITY){
                        //it's type A C or D
                        deleteFromList(theGeoObj);
                    }
                }
            }
            });
        itemToReturn.setExpanded(false);//set it unexpanded to start
        if(parent.hasChildren()){
            //recursively sets up children
            geoObj [] parentsChildren = new geoObj [parent.children.size()];
            parentsChildren = parent.children.toArray(parentsChildren);
            for(geoObj currentGeoObj: parentsChildren){
                itemToReturn.getChildren().add(recursivelySetUpTree(currentGeoObj));
            }
        }
        
        itemToReturn.setGraphic(checkBox);
        return itemToReturn;
    }
    private void setUpTreeViewFromRootGeoObj(geoObj root){
        /* For information on how to use TreeView elements,
         * visit http://docs.oracle.com/javafx/2/ui_controls/tree-view.htm
         */
        
        
        /*CellFactory dictates how a "row" is rendered.
         * Very interesting concept, research it later if time
         * permits. Different CellFactories can be used to 
         * include checkboxes, graphics, etc. VERY interesting.
         */
        tv.setRoot(recursivelySetUpTree(root));
        tv.getRoot().setExpanded(true);
        tv.setShowRoot(false);
    }
    
    public void manuallyChangeMapPack(){
        /*Launches a small menu to allow users to look for a new map pack.
         * This is solely for debugging purposes, as it won't be used in 
         * the real program.
         */
        changeMapPack(Auxiliary.getDirectoryViaDialogBox());
    }
    
    public boolean changeMapPack (File newDirectory){
        /* This function returns true if completed successfully,
         * false if completed unsuccessfully. This is implemented so that 
         * if it does happen to fail, the selection box can go
         * back to its previous state.
         */
        try{
            if(newDirectory!=null){
                geoObj theRoot = Base.getRootGeoObjFromDirectory(newDirectory);
                setUpTreeViewFromRootGeoObj(theRoot);
                lv.getItems().clear();
            }
        }catch (Exception e ){
            PopupSender.ShowMessage(newDirectory.getName()+" is a corrupt and/or unusable Map Pack Folder", "Unusable Map Pack");
            return false;
        }
        return true;
    }
    
    private void addToList(geoObj toBeAdded){
        //includes toBeAdded in the list of geoObjects to appear on the quiz
        //uses redundancy to check the object everywhere
        if(!listContainsName(lv, toBeAdded.name)){
            lv.getItems().add(toBeAdded);
            checkEverywhere((TreeItem<geoObj>) tv.getRoot(), toBeAdded.name);

        }else{
            //includedGeoObjs was not correctly declared
        }
    }
    private void deleteFromList(geoObj toBeDeleted){
        //deletes toBeDeleted from the list of geoObjects to appear on the quiz
        //recursively unchecks it everywhere
        if(lv.getItems().contains(toBeDeleted)){
            lv.getItems().remove(toBeDeleted);
        }
        uncheckEverywhere((TreeItem<geoObj>) tv.getRoot(), toBeDeleted.name);
    }
    
    private void initializeChoiceBoxes(){
        /*Called when MakeQuiz window is launched; goes through the 
         * map pack directory, looking for all available map packs and
         * listing them in the choice box. It relies on the fact that 
         * all folders in the map pack directory are actually map packs,
         * otherwise it will cause a bug. Instead of using a 
         * ChoiceBox<File>, it uses ChoiceBox<MapPackFileWrapper>
         * because its toString() must present the file's name, not path.
         * 
         * Also makes the four options for the Quiz Type dropdown chocice
         * box.
         * 
         * At some point, there needs to be a feature that lets the user
         * change the default location of the map packs.
         */
        
        
        //map packs first
        File f = new File(Base.mapPackLocation);
        List<MapPackFileWrapper> mapPackList = new ArrayList<MapPackFileWrapper>();
        
        if(f.isDirectory()){
            for (File currentFile: f.listFiles()){
                if(currentFile.isDirectory()){
                    mapPackList.add(new MapPackFileWrapper(currentFile));
                }
            }
        }else{
            //directory doesn't exist
        }
        //now import all user Map Packs!
        File uf = new File(Base.userMapPackLocation);
        if(uf.isDirectory()){
            for (File currentFile: uf.listFiles()){
                if(currentFile.isDirectory()){
                    mapPackList.add(new MapPackFileWrapper(currentFile));
                }
            }
        }else{
            //directory doesn't exist
        }
        mapPacks = FXCollections.observableList(mapPackList);
        mapPackChoiceBox.setItems(mapPacks);
        mapPackChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MapPackFileWrapper>(){

            @Override
            public void changed(ObservableValue<? extends MapPackFileWrapper> ov, MapPackFileWrapper oldfile, MapPackFileWrapper newfile) {
                /* Here, the function attempts to change the map pack 
                 * to the new value. The variable "changed" assesses 
                 * whether it was successfully changed. If an error was
                 * encountered, it resets it its previous map pack selection.
                 */
                boolean changed = changeMapPack(newfile.theFile);
                if(changed ==false){
                    mapPackChoiceBox.setValue(oldfile);
                }
            }
        });
        try{
            mapPackChoiceBox.setValue(mapPacks.get(0));
        }catch(Exception e){
            e.printStackTrace();
            PopupSender.ShowMessage("Could not find any Map Packs.", "No Map Packs");
        }
        
        //now for the quiz-type choicebox
        
        quizTypeChoiceBox.getItems().clear();
        /*Originally there was a "practice" and a "test" mode 
         * for each, but since has been consolidated to just
         * the two types.
         */
        quizTypeChoiceBox.getItems().add("Drag-Drop");
        quizTypeChoiceBox.getItems().add("Point-Click");
        quizTypeChoiceBox.getSelectionModel().selectFirst();
    }
    
    private void uncheckEverywhere(TreeItem<geoObj> currentTreeItem, String theNameToLookFor){
        /*This function is designed to implement redundancy; it assumes that any two 
         * geo objects that are supposed to be the same have the same name. An example
         * of redundancy would be having Harrisburg under Pennsylvania but then also
         * having it under capitals.
         * 
         * The first argument is the current Tree Item BEING CHECKED, the second is
         * the name to look for. So, if you want to delete a geoObj named "Vermont,"
         * you would just call it on the root - i.e. uncheckEverywhere(tv.getRoot(), "Vermont")
         */
        
        //First, see if this is the element, and if so, uncheck it
        if(currentTreeItem.getValue().name.equals(theNameToLookFor)){
            ((CheckBox)currentTreeItem.getGraphic()).setSelected(false);
        }
        
        //Now, go through all of the element's kids recursively and look for more repeats
        ObservableList<TreeItem<geoObj>> kids = currentTreeItem.getChildren();
        if(!kids.isEmpty()){
            for(int current = 0; current<kids.size(); current++){
                uncheckEverywhere((TreeItem<geoObj>) kids.get(current), theNameToLookFor);
            }
        }
    }
    
    private void checkEverywhere(TreeItem<geoObj> currentTreeItem, String theName){
        /*This function is designed to implement redundancy; it assumes that any two 
         * geo objects that are supposed to be the same have the same name. An example
         * of redundancy would be having Harrisburg under Pennsylvania but then also
         * having it under capitals.
         */
        
        //First, see if this is the element, and if so, check it
        if(currentTreeItem.getValue().name.equals(theName)){
            ((CheckBox)currentTreeItem.getGraphic()).setSelected(true);
        }
        ObservableList<TreeItem<geoObj>> kids = currentTreeItem.getChildren();
        if(!kids.isEmpty()){
            //now go through and do the same for all the kids (if they exist)
            for(int current = 0; current<kids.size(); current++){
                checkEverywhere((TreeItem<geoObj>) kids.get(current), theName);
            }
        }
    }
    private boolean listContainsName (ListView list, String nameToCheck){
        //used to see if the list has a certain element in it (to delete it)
        ObservableList<geoObj> listToCheck = list.getItems();
        if(!listToCheck.isEmpty()){
            for(int currentIndex = 0; currentIndex<listToCheck.size();currentIndex++){
                if(listToCheck.get(currentIndex).name.equals(nameToCheck)){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }
    //=========================INITIALIZE LISTVIEW and OBSERVABLE LIST
    private void initializeObservableListAndListView(){
        //initiates the observablelist and the listview
        //also attaches a listener
        
        //this is not used yet, but it shows the geoObj currently selected
         lv.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<geoObj>() {
            @Override
            public void changed(ObservableValue<? extends geoObj> ov, geoObj oldValue, geoObj newValue) {
                //newValue is now selected
            }
        });
        /*When setCellFactory is called, a callback item is passed into it.
         * CallBack items have two paramaters, P and R. The P value is the type
         * passed into the call function, and R is the value returned. P is
         * ListView<geoObj>, and R is a ListCell. The call function is 
         * overridden for the callback, and it returns the type of cell that 
         * will be used in the listView. It returns a type of custom ListCell
         * which is in this case "geoObjCell". The custom cell must be made
         * as an inner class, with updateItem() overridden to setText and
         * setGraphic. Cool stuff!
         */
        lv.setCellFactory(new Callback<ListView<geoObj>, 
            ListCell<geoObj>>() {

            @Override
            public ListCell<geoObj> call(ListView<geoObj> p) {
                final geoObjCell cellToReturn = new geoObjCell();
                cellToReturn.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent me) {
                            if(me.getClickCount()==2){
                                geoObjCell theCell = (geoObjCell) me.getSource();
                                if(theCell.getItem()!=null){
                                    deleteFromList(theCell.getItem());
                                }
                            }
                        };
                    });
                return cellToReturn;
            }
                
            });
    }
    //=========================CUSTOM CELLS FOR CELL FACTORIES
     private class geoObjCell extends ListCell<geoObj> {
         @Override
         protected void updateItem(geoObj item, boolean empty){
             super.updateItem(item, empty);
             if(!empty){
                 setText(item.name);
             }
         } 
     }
     /*
      * This was ORIGINALLY supposed to dictate how a cell is rendered,
      * but it was discovered that it is a MUCH better idea to customize 
      * the TreeItems, not the cells, seeing as cells are constantly re-
      * updated.
     private class customCheckBoxCell extends TreeCell<geoObj>{       
         CheckBox checkBox;
         boolean currentState= false;
         
         public customCheckBoxCell(){
             System.out.println("Just made a new ");
         }
         
         @Override
         public void updateItem(geoObj item, boolean empty){
             super.updateItem(item, empty);
             
             if(!empty){
                checkBox = new CheckBox();
                checkBox.setAllowIndeterminate(false);
                checkBox.setOnAction(new EventHandler(){

                    public void handle(Event t) {
                        System.out.println("Toggling checkbox");
                    }

                });
                    setText(item.name);
                    setGraphic(checkBox);
                    checkBox.setSelected(currentState);//sets it equal to the new state
                 
            }
             
         } 
     }
     */
     //===============================================================
     
     public void checkAllChildren(TreeItem<geoObj> theTreeItem){
         /* This feature is used to select all the children when 
          * the Check Box in a Tree Item is selected. By default, 
          * CheckBoxTreeItem does this - but this TreeView actually
          * uses ordinary TreeItems with Checkboxes.
          */
         CheckBox graphic = (CheckBox) theTreeItem.getGraphic();
         graphic.setSelected(true);
         if(!theTreeItem.isLeaf()){
             ObservableList<TreeItem<geoObj>> allChildren = theTreeItem.getChildren();
             for(int current = 0; current<allChildren.size();current++){
                 checkAllChildren(allChildren.get(current));
             }
         }
     }
     public void uncheckAllChildren(TreeItem<geoObj> theTreeItem){
         /* Essentially the same as the checkAllChildren() method, 
          * but in reverse. For documentation, see checkAllChildren()
          */
         CheckBox graphic = (CheckBox) theTreeItem.getGraphic();
         graphic.setSelected(false);
         if(!theTreeItem.isLeaf()){
             ObservableList<TreeItem<geoObj>> allChildren = theTreeItem.getChildren();
             for(int current = 0; current<allChildren.size();current++){
                 uncheckAllChildren(allChildren.get(current));
             }
         }
     }
     
     public void saveUnfinishedQuiz(){
         /* This function is called when user hits Save in the File
          * menu. The function brings up a dialog box, and if a file is
          * chosen, it attempts to convert the TreeView and ListView into
          * an InProgressQuizCreation and serializing it as a .uquz in the
          * specified location.
          */
         if(tv!=null&lv!=null){
             InProgressQuizCreation currentQuiz = new InProgressQuizCreation(tv, lv);
             FileChooser fc = new FileChooser();
             fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Unfinished Quiz(*.uquz)", "*.uquz"));
             File initialDirectory = new File(Base.progressLocation);
             if(initialDirectory.exists()){
                fc.setInitialDirectory(initialDirectory);
             }
             File f = fc.showSaveDialog(null);
             
             if(f!=null){
                 //User picked a file!
                 if(!f.getName().contains(".")) {
                     f = new File(f.getAbsolutePath() + ".uquz");
                 }
                 try {
                     FileOutputStream fs = new FileOutputStream(f);
                     ObjectOutputStream os = new ObjectOutputStream(fs);
                     os.writeObject(currentQuiz);
                     os.close();
                     
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
                 
                 //saved! (hopefully)

             }else{
                 //decided to not save it
             }
         }
     }
     
     private geoObj recursivelyConvertGeoObjHolderToGeoObj(geoObjHolder currentHolder){
         /* This function is called on the root geoObjHolder, and it returns a
          * geoObj tree. It is recursive.
          */
         
         //convert it to a geoObj
         geoObj toReturn = currentHolder.toGeoObj();
         //now handle children!
         if(currentHolder.hasChildren()){
             //need to continue recursion - it has children
             System.out.println("Analyzing "+toReturn.name+" and does it have children? "+currentHolder.hasChildren());
             toReturn.children = new ArrayList<geoObj>();
             for(int currentIndex = 0; currentIndex<currentHolder.getChildren().size();currentIndex++){
                toReturn.children.add(recursivelyConvertGeoObjHolderToGeoObj(currentHolder.getChildren().get(currentIndex)));
             }
         }
         return toReturn;
     }
     
     public void loadUnfinishedQuiz(){
         /* This function is called when a user hits the Open option in the File menu.
          * It launches an Open dialog, and if a user chooses a file, it attempts to
          * call setUpUnfinishedQuiz with the deserialized InProgressQuizCreation.
          */
         FileChooser fc = new FileChooser();
         fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Unfinished Quiz(*.uquz)", "*.uquz"));
         File initialDirectory = new File(Base.progressLocation);
         if(initialDirectory.exists()){
             fc.setInitialDirectory(initialDirectory);
         }
         
         //choose the unfinished quiz
         File f = fc.showOpenDialog(null);
         if (f!=null){
             try{
                //load the unfinished quiz
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(f));
                InProgressQuizCreation ipqz = (InProgressQuizCreation) is.readObject();
                is.close();
                setUpUnfinishedQuiz(ipqz);
             }catch(Exception e){
                 e.printStackTrace();
             }
         }else{
             //user didn't select a file
         }
     }
     
     private void setUpUnfinishedQuiz(InProgressQuizCreation theQuiz){
         /* This function takes an InProgressQuizCreation and loads it 
          * into the TreeView, then matches the ListView accordingly, 
          * linking the two.
          */
         tv.setRoot(recursivelySetUpTreeFromUnfinishedQuiz(theQuiz.getRoot()));
         tv.getRoot().setExpanded(true);
         tv.setShowRoot(false);
         matchListViewToTreeView(tv.getRoot());
     }
     private TreeItem<geoObj> recursivelySetUpTreeFromUnfinishedQuiz(geoObjHolder parent){
        /*This function is the same as recursivelySetUpTree; the only difference is that
         * before the changeListener is implemented, it sets the TreeItem checked if it
         * should be, so that the program remembers whether or not an item is selected.
         */
        geoObj theGeoObj=parent.toGeoObj();
        final TreeItem<geoObj> itemToReturn = new TreeItem<>(theGeoObj);
        CheckBox checkBox = new CheckBox();
        
        //IMPORTANT: set the checkbox selected if need be
        checkBox.setSelected(parent.checked());
        
        
        //Variables referenced from an inner class that are outside must be made final first
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            /*To make a listener, you first have to get the selectedProperty of the
             * checkBoxTreeItem. Then, add a listener to the selected property of type
             * "ChangeListener<Boolean> then extend it as an inner class and include
             * overridden changed() function with necessary arguments.
             */
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                geoObj theGeoObj = itemToReturn.getValue();
                if (t1){
                    //itemToReturn was just checked
                    checkAllChildren(itemToReturn);
                    if(theGeoObj.type==geoObj.CATEGORY_AND_FEATURE||theGeoObj.type==geoObj.FEATURE||theGeoObj.type==geoObj.CITY){
                        //it's type A C or D
                        addToList(theGeoObj);
                    }
                }else{
                    //itemToReturn was just unchecked
                    uncheckAllChildren(itemToReturn);
                    //System.out.println(theGeoObj.name+" was just unchecked");
                    if(theGeoObj.type==geoObj.CATEGORY_AND_FEATURE||theGeoObj.type==geoObj.FEATURE||theGeoObj.type==geoObj.CITY){
                        //it's type A C or D
                        //System.out.println(theGeoObj.name+" was just removed");
                        deleteFromList(theGeoObj);
                    }
                }
            }
            });
        itemToReturn.setExpanded(false);//set it unexpanded to start
        if(parent.hasChildren()){
            //recursively sets up children
            geoObjHolder [] parentsChildren = new geoObjHolder [parent.getChildren().size()];
            parentsChildren = parent.getChildren().toArray(parentsChildren);
            for(geoObjHolder currentGeoObj: parentsChildren){
                itemToReturn.getChildren().add(recursivelySetUpTreeFromUnfinishedQuiz(currentGeoObj));
            }
        }
        
        itemToReturn.setGraphic(checkBox);
        return itemToReturn;
    }
    
    private void matchListViewToTreeView(TreeItem<geoObj> current){
        //goes through and links the ListView to the TreeView
        //if a geoObj is checked in the outline, it must be added
        CheckBox checkBox = (CheckBox) current.getGraphic();
        if(checkBox.isSelected()){
            if(current.getValue().type==geoObj.CATEGORY_AND_FEATURE||current.getValue().type==geoObj.CITY||current.getValue().type==geoObj.FEATURE){
                addToList(current.getValue());
            }
        }else{
            deleteFromList(current.getValue());
        }
        if(!current.isLeaf()){
            for(int currentChild = 0;currentChild<current.getChildren().size();currentChild++){
                matchListViewToTreeView(current.getChildren().get(currentChild));
            }
        }
    }
    private int getQuizType(){
        if(((String) quizTypeChoiceBox.getSelectionModel().getSelectedItem()).equals("Drag-Drop")){
            return QuizCreation.DRAG_DROP;
        }else if(((String) quizTypeChoiceBox.getSelectionModel().getSelectedItem()).equals("Point-Click")){
            return QuizCreation.POINT_CLICK;
        }else{
            return -1;
        }
    }
    public void backPressed (ActionEvent e){
        try {
            FXMLStephensPetrochko.getInstance().replaceSceneContent("MainScreen.fxml", Base.PROJECT_TITLE);
        } catch (Exception ex) {
            Logger.getLogger(PresetQuizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void export (){
        File storageFolder = new File(Base.progressLocation);
        SerializableQuizCreation quiz = new SerializableQuizCreation(lv, (geoObj) tv.getRoot().getValue(), getQuizType());
        if(storageFolder.exists()){
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(storageFolder);
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Quiz(*.quz)", "*.quz"));
            File f = fc.showSaveDialog(null);

            if(f!=null){
                //User picked a file!
                if(!f.getName().contains(".")) {
                    f = new File(f.getAbsolutePath() + ".quz");
                }
                try {
                    /*This method works, but try another
                     * 
                     * FileOutputStream fs = new FileOutputStream(f);
                    ObjectOutputStream os = new ObjectOutputStream(fs);
                    os.writeObject(quiz);
                    os.close();
                    */
                    
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream (f));
                    oos.writeObject(quiz);
                    oos.close();
                    
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream (bos);
                    oos.writeObject(quiz);
                    oos.close();
                    
                    byte[] rawData = bos.toByteArray(); //not sure of the purpose of this
                    

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //saved! (hopefully)

            }else{
                //decided to not save it
            }
        }else{
            //the directory doesn't exist
            System.out.println(storageFolder.getAbsolutePath()+" doesn't exist");
            PopupSender.ShowMessage(storageFolder.getAbsolutePath()+" doesn't exist", "Error");
        }

    }
    public void TakeIt(){
        ArrayList<geoObj> geoObjs = new ArrayList<geoObj> ();
        for(int x = 0;x<lv.getItems().size();x++){
            geoObjs.add((geoObj) lv.getItems().get(x));
        }
        QuizCreation toTake = new QuizCreation(geoObjs, (geoObj) tv.getRoot().getValue(), Base.defaultQuizName, getQuizType());
        //for debugging, let's try a serializable one
        //SerializableQuizCreation sQuiz = new SerializableQuizCreation(lv, (geoObj) tv.getRoot().getValue(), getQuizType());
        //TestCorruption(toTake);
        
        Base.TakeQuiz(toTake);
    }
    public void ShowHelp(){
        new PopupSender().ShowHowTo();
    }
    public void ShowAbout(){
        new PopupSender().ShowAbout();
    }
    public void Clear(){
        try {
            FXMLStephensPetrochko.getInstance().replaceSceneContent("MakeQuiz.fxml", Base.MAKE_QUIZ_TITLE);
        } catch (Exception ex) {
            Logger.getLogger(MakeQuizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void Website(){
        PopupSender.ShowMessage("Visit us at SPgeography.weebly.com", "Visit us");
    }
    
    public void AddAll(){
        /*Ease of use feature, to ADD all items from the tree view
         * at a time.
         */
        checkAllChildren(tv.getRoot());
    }
    
    public void RemoveAll(){
        /*Ease of use feature, to REMOVE all items from the list
         * at a time.
         */
        uncheckAllChildren(tv.getRoot());
        if(!lv.getItems().isEmpty()){
            
            
            for(int c = 0; c<lv.getItems().size();c++){
                
                deleteFromList((geoObj) lv.getItems().get(c));
                
                
            }
            
            
            
        }
    }
    
    public void TestCorruption (QuizCreation quiz){
        /* This is a debugging function, which recursively does some
         * "operational" code to each geoObj (to look for bugs).
         */
        
        //OPERATIONAL CODE HERE
        
        for(geoObj obj : quiz.getGeoObjs()){
            System.out.println("Testing: "+obj.name);

            if(obj.mapElement!=null){
                try{           
                    
                    /* Here, to address a bug that arises when converting a 
                     * BufferedImage to a ByteArray, it is first converted to 
                     * a BufferedImage with alpha specifically included
                     */
                    BufferedImage bi = SwingFXUtils.fromFXImage(obj.mapElement, null);
                    BufferedImage convertedImg = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                    convertedImg.getGraphics().drawImage(bi, 0, 0, null);
                    
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
                    ImageIO.write(convertedImg, "png", baos );
                    baos.flush();

                    byte [] imageData = baos.toByteArray();        
                    baos.close();


                    InputStream in = new ByteArrayInputStream(imageData);
                    
                    BufferedImage bio = ImageIO.read(in);
                    Image recoveredImage = SwingFXUtils.toFXImage(bio, null);
                    obj.mapElement=recoveredImage;


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    
    /*
     * To understand what recursion is, you must first understand recursion.
     */
    
}
