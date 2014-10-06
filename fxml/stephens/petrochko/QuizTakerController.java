/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Tyler
 */
public class QuizTakerController implements Initializable{
    
    //========FXML elements===========
    @FXML public StackPane targetStackPane;
    @FXML public TabPane tabPane;
    @FXML public StackPane leftSide;
    @FXML public AnchorPane quizAnchor;
    @FXML public StackPane globalAnchor;
    @FXML public ScrollPane viewingPane;
    @FXML public Tab mainTab;
    @FXML public Tab quizTab;
    @FXML public Tab roundTab;
    @FXML public Tab aboutTab;
    
    @FXML public Label QuizName;
    @FXML public Label QuizType;
    @FXML public Label PercentCorrect;
    @FXML public Label PercentComplete;
    
    
    @FXML public ImageView background;
    @FXML public ImageView currentElementImageView;
    @FXML public Label currentElementName;
    @FXML public ScrollPane scrollPane;
    @FXML public VBox NotificationArea;
    
    @FXML public ListView <geoObj> CorrectElementsListView;
    @FXML public ListView <geoObj> IncorrectElementsListView;
    @FXML public Label Timer;
    
    @FXML public Button BeginQuizButton;
    @FXML public Button StartOverButton;
    @FXML public Button SaveProgressButton;
    @FXML public Button RetakeQuizButton;
    @FXML public Button PracticeMissedButton;
    
    //========Quiz elements===========
    
    final double notificationFadeSpeed = 3.0; //how long it takes the notification to dissapear
    
    private QuizCreation quiz;
    private boolean quizzing = false; //is a quiz in session?
    private int currentGeoObjIndex = 0;
    private ImageView imgHolder;
    private boolean canDrag=false;
    
    private double numberOfGuessesSoFar = 0;
    private double numberOfCorrectGuessesSoFar = 0;
    
    
    
    
    @Override
    public void initialize (URL url, ResourceBundle rb) {
        /* This function is called when the controller is initialized. It is responsible
         * for loading the quiz from the quizbuffer and calling ALL functions that initialize
         * the UI based on the quiz type. This method itself does not distinguish between 
         * quiz types as of yet.
         */
        
        
        //takes the contents of the quiz buffer; once this class is initialized it's made null
        quiz = Auxiliary.QUIZ_BUFFER;
        rearrange(quiz);
        
        
        try{
            /*Everything is initialized based on the type of quiz. For example, a Drag-Drop quiz
             * is initialized differently from a Point-Click practice. This leads to huge functions. 
             * This is necessary, however, to remain somewhat object oriented.
             */
            initializeBackground();
            initializeOtherUIElements();
            
            
            
        }catch(BadQuizException bqe){
            bqe.printStackTrace();
        }
        
        
    }
    
    private void initializeBackground () throws BadQuizException{
        int quizType = quiz.getType();
        
        if(quizType==QuizCreation.DRAG_DROP){
            
            //=========Drag-Drop Quiz Background===========
            
            /*If the quiz-type is Drag-Drop, the background in is initialized as a blank bamp.
             * All the program must do is load the root element and set viewer to hold the 
             * image.
             */
            
            if(quiz.getRoot().mapElement!=null){
                
                Image toBeSet = quiz.getRoot().mapElement;

                background.setFitWidth(toBeSet.getWidth());
                background.setPreserveRatio(true);
                background.setFitHeight(toBeSet.getHeight());
                background.setImage(toBeSet);



            }else{
                throw new BadQuizException("The map element corresponding to the root of the quiz is null");
            }
        }
        else if(quizType==QuizCreation.POINT_CLICK){
            
            //=========Point-Click QuizBackground=========
            
            /*If the quiz-type is Point-Click, the program has to not only load the blank background map
             * from the root element, but create all the ImageViews that go on top of the background element
             * to detect user input when they are being clicked on. Each handler must check if the current 
             * element is a feature or a city:
             *      -If a feature, is THIS the right feature to be clicked on? Has the user clicked on
             *      a part of the ImageView that is not Transparent (so they clicked within the 
             *      boundaries of the feature)
             *      -If a city, is it within a certain distance (giveOrTake) from the city's coordinates, 
             *      making sure to take into account the layout offset from the ImageView's Translate X and
             *      Translate Y.
             */
            if(quiz.getRoot().mapElement!=null){
                //set up the background first
                Image toBeSet = quiz.getRoot().mapElement;

                background.setFitWidth(toBeSet.getWidth());
                background.setPreserveRatio(true);
                background.setFitHeight(toBeSet.getHeight());
                background.setImage(toBeSet);
                
                //now go through all the elements and add them on top of the background
                
                for(geoObj currentGeoObj: quiz.getGeoObjs().toArray(new geoObj[quiz.getGeoObjs().size()])){
                    if(currentGeoObj.type==geoObj.CATEGORY_AND_FEATURE||currentGeoObj.type==geoObj.FEATURE){
                        //for a feature
                        if(currentGeoObj.mapElement!=null){
                            
                            final String toPrintWhenClicked = currentGeoObj.name+" was clicked";
                            final GeoObjView newLayer = new GeoObjView(currentGeoObj);
                            newLayer.setOnMouseClicked(new EventHandler<MouseEvent>(){

                                @Override
                                public void handle(MouseEvent t) {
                                    if(t.isStillSincePress()){
                                        System.out.println(toPrintWhenClicked);
                                        //moveToTopOfLayer(newLayer);
                                        if(quizzing){
                                            //quiz in-session
                                            if(quiz.getGeoObjs().get(currentGeoObjIndex)==newLayer.getGeoObj()){
                                                correct();
                                            }else{
                                                incorrect(true);
                                            }
                                        }
                                    }
                                    
                                    
                                }
                            
                            
                            
                            });
                            addToStackPane(newLayer);                            
                        }else{
                            throw new BadQuizException("Geo Object "+currentGeoObj.name+" is missing a map element");
                        }
                        
                    }else if (currentGeoObj.type==geoObj.CITY){
                        //city
                        
                        final String toPrintWhenClicked = currentGeoObj.name+" was clicked";
                        final GeoObjView newLayer = new GeoObjView(currentGeoObj);
                        newLayer.setOnMouseClicked(new EventHandler<MouseEvent>(){

                            @Override
                            public void handle(MouseEvent t) {
                                System.out.println(toPrintWhenClicked);
                                //moveToTopOfLayer(newLayer);
                                
                                if(quizzing){
                                        //quiz in-session
                                        if(quiz.getGeoObjs().get(currentGeoObjIndex)==newLayer.getGeoObj()){
                                            correct();
                                        }else{
                                            incorrect(true);
                                        }
                                    }
                            }



                        });

                        newLayer.setTranslateX(currentGeoObj.xCoord);
                        newLayer.setTranslateY(currentGeoObj.yCoord);
                        targetStackPane.getChildren().add(newLayer);
                    }
                }
                
                
                
                
            }else{
                throw new BadQuizException("The map element corresponding to the root of the quiz is null");
            }
            
            
            
        }else{
            throw new BadQuizException("Invalid quiz type");
        }
    }
    private void initializeOtherUIElements() throws BadQuizException{
        int typeOfQuiz = quiz.getType();
        if(typeOfQuiz==QuizCreation.DRAG_DROP){
            setUpDragDropEvents();
            QuizType.setText("Drag-Drop");
        }else if(typeOfQuiz==QuizCreation.POINT_CLICK){
            QuizType.setText("Point-Click");
            currentElementImageView.setImage(new Image(getClass().getResourceAsStream(Base.logoLocation)));
            currentElementImageView.setOpacity(.2);
            currentElementImageView.setDisable(true);
        }
        quizTab.setDisable(true);
        roundTab.setDisable(true);
        PercentComplete.setText("0% Complete");
        PercentCorrect.setText("0% Correct");
        QuizName.setText(quiz.getName());
        
    }
    
    private void setUpDragDropEvents(){
        
        currentElementImageView.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(canDrag){
                        Dragboard db = currentElementImageView.startDragAndDrop(TransferMode.COPY);
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(currentElementImageView.getImage());
                        db.setContent(content);

                        event.consume();
                    }
                }
            });

            targetStackPane.setOnDragEntered(new EventHandler<DragEvent>(){

                @Override
                public void handle(DragEvent event) {
                    if (event.getGestureSource() == currentElementImageView && event.getDragboard().hasImage()){
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        //initialize imgHolder
                        Dragboard db = event.getDragboard();
                        imgHolder = new ImageView(db.getImage());
                        imgHolder.setTranslateX(event.getX());
                        imgHolder.setTranslateY(event.getY());


                        targetStackPane.getChildren().add(imgHolder);
                    }
                }


            });


            targetStackPane.setOnDragExited (new EventHandler<DragEvent>(){

                @Override
                public void handle(DragEvent event) {
                    if (event.getGestureSource() == currentElementImageView && event.getDragboard().hasImage()){
                        //remove imgHolder
                        targetStackPane.getChildren().remove(imgHolder);
                    }
                }


            });

            targetStackPane.setOnDragOver(new EventHandler<DragEvent>() {

                @Override
                public void handle(DragEvent event) {
                    if (event.getGestureSource() == currentElementImageView && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        imgHolder.setTranslateX(event.getX()-(.5*imgHolder.getImage().getWidth()));
                        imgHolder.setTranslateY(event.getY()-(.5*imgHolder.getImage().getHeight()));
                        System.out.println("Coords "+event.getX()+"x"+event.getY());
                    }

                    event.consume();
                }
            });

            targetStackPane.setOnDragDropped(new EventHandler<DragEvent>(){

                @Override
                public void handle(DragEvent event) {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasImage()) {
                        /* HERE: code here needs to check whether the user has put down the image close enough to 
                         * the required coordinates, then assess whether the user guessed correctly or incorrectly.
                         * If correct, needs to leave the ImageView there (set imgHolder to null)
                         * If incorrect, needs to remove imgHolder from the targetStackPane
                         */
                        geoObj obj = quiz.getGeoObjs().get(currentGeoObjIndex);
                        if (Math.abs(obj.xCoord-imgHolder.getTranslateX())<Base.pixelGiveOrTake&&Math.abs(obj.yCoord-imgHolder.getTranslateY())<Base.pixelGiveOrTake){
                            //then the user guessed within the Base.pixelGiveOrTake distance
                            //remove imgHolder, set imgHolder to null, then add the imageview correctly
                            targetStackPane.getChildren().remove(imgHolder);
                            GeoObjView toAdd = new GeoObjView(obj);
                            addToStackPane(toAdd);
                            correct();
                            System.out.println("Correct!");
                        }else{
                            //user did NOT guess within Base.pixelGiveOrTake on either X or Y
                            targetStackPane.getChildren().remove(imgHolder);
                            incorrect(true);
                            System.out.println("INCORRECT:");
                            System.out.println("Correct Coords are: "+obj.xCoord+"x"+obj.yCoord);
                            System.out.println("User guessed: "+imgHolder.getTranslateX()+"x"+imgHolder.getTranslateY());
                            imgHolder = null;
                        }



                       //set success to true to register the event
                       success = true;
                    }
                    /* let the source know whether the string was successfully 
                     * transferred and used */
                    event.setDropCompleted(success);

                    event.consume();
                }

            });
    }
    
    
    private void startQuiz () throws BadQuizException{
        quizTab.setDisable(false);
        roundTab.setDisable(false);
        StartOverButton.setDisable(false);
        SaveProgressButton.setDisable(false);
        quizzing = true;
        currentGeoObjIndex = 0;
        resetUI();
        quizElement(quiz.getGeoObjs().get(currentGeoObjIndex));
        setupTimer();
    }
    
    private void quizElement (geoObj obj) throws BadQuizException{
        
        if(quiz.getType()==QuizCreation.DRAG_DROP){
            if(obj.type==geoObj.CATEGORY_AND_FEATURE||obj.type==geoObj.FEATURE){
                //it has a map element (supposedly - we'll check)
                if(obj.mapElement!=null){
                    //we're good to go! The obj is not corrupted

                    canDrag = true;
                    currentElementImageView.setImage(obj.mapElement);
                    currentElementImageView.setSmooth(true);
                    currentElementImageView.setFitWidth(obj.mapElement.getWidth());
                    currentElementImageView.setFitHeight(obj.mapElement.getHeight());
                    currentElementImageView.setPreserveRatio(true);
                    System.out.println("OBJ: "+obj.mapElement.getWidth()+"x"+obj.mapElement.getHeight());
                    System.out.println("PANE: "+scrollPane.getWidth()+"x"+scrollPane.getHeight());
                    if(obj.mapElement.getWidth()>scrollPane.getPrefWidth()){
                        //too wide, make it thinner
                        currentElementImageView.setFitWidth(scrollPane.getPrefWidth());
                    }
                    if(obj.mapElement.getHeight()>scrollPane.getPrefHeight()){
                        //too tall, make it shorter
                        currentElementImageView.setFitHeight(scrollPane.getPrefHeight());
                    }
                    
                    currentElementName.setText(obj.name);         


                }else{
                    throw new BadQuizException("Object "+obj.name+" does not have a mapElement");
                }
            }else if(obj.type==geoObj.CITY){
                //it's just a pinpoint location (no map element)

                canDrag = true;
                Image pinpointGraphic = Auxiliary.getCityGraphic();
                currentElementImageView.setFitWidth(pinpointGraphic.getWidth());
                currentElementImageView.setFitHeight(pinpointGraphic.getHeight());
                currentElementImageView.setPreserveRatio(true);
                currentElementImageView.setImage(pinpointGraphic);
                currentElementName.setText(obj.name);







            }else{
                throw new BadQuizException("Neither a city nor a feature");
            }
        }else if(quiz.getType()==QuizCreation.POINT_CLICK){
            //FINISH THIS
            currentElementName.setText(obj.name);
            //it's a feature, so move the element to the top of its layer, so it is easily clicked
            //this might change...
            moveToTopOfLayer(findMatchingGeoObjView(obj));
            
        }
    }

    
     private void correct(){
        /*This function is intended to be relatively quiz-type independent; it is responsible
         * for tracking/updating progress and moving to the next element
         */
        
        //notify that the guess was correct
        NotificationArea.getChildren().clear();
        Label notification = new Label("Correct!");
        notification.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        FadeTransition ft = new FadeTransition(Duration.millis(1000*notificationFadeSpeed), notification);
        ft.setFromValue(1);
        ft.setToValue(0);
        NotificationArea.getChildren().add(notification);
        ft.play();
        
        //add the geoObj to the list of correct geoObjs
        if(!CorrectElementsListView.getItems().contains(quiz.getGeoObjs().get(currentGeoObjIndex))&&!IncorrectElementsListView.getItems().contains(quiz.getGeoObjs().get(currentGeoObjIndex))){
            CorrectElementsListView.getItems().add(quiz.getGeoObjs().get(currentGeoObjIndex));
            //set the Retake-Quiz button to enabled
            if(RetakeQuizButton.isDisable()){
                RetakeQuizButton.setDisable(false);
            }
        }
        
        numberOfGuessesSoFar++;
        numberOfCorrectGuessesSoFar++;
        long percentCorrect = Math.round(((double)CorrectElementsListView.getItems().size()/(double)(currentGeoObjIndex+1))*100);
        PercentCorrect.setText(percentCorrect+"% Correct");
        
        long progress = Math.round(((double)currentGeoObjIndex/(double)quiz.getGeoObjs().size())*100);     
        PercentComplete.setText(progress+"% Complete");
        
        //is quiz done yet?
        if(currentGeoObjIndex<(quiz.getGeoObjs().size()-1)){
            //quiz isn't done yet
            currentGeoObjIndex++;
            resetUI();
            try {
                quizElement(quiz.getGeoObjs().get(currentGeoObjIndex));
            } catch (BadQuizException ex) {
                Logger.getLogger(QuizTakerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            //quiz is finished
            resetUI();
            quizzing = false;
            quizEnded();
        }
    }
    private void incorrect(boolean showMessage){
        if(showMessage){
            //notify Incorrect!
            NotificationArea.getChildren().clear();
            Label notification = new Label("Incorrect!");
            notification.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            FadeTransition ft = new FadeTransition(Duration.millis(1000*notificationFadeSpeed), notification);
            ft.setFromValue(1);
            ft.setToValue(0);
            NotificationArea.getChildren().add(notification);
            ft.play();
        }
        
        //add the current geoObj to the incorrect list
        if(!IncorrectElementsListView.getItems().contains(quiz.getGeoObjs().get(currentGeoObjIndex))){
            IncorrectElementsListView.getItems().add(quiz.getGeoObjs().get(currentGeoObjIndex));
            //now set the Retake-Quiz Button enabled
            if(RetakeQuizButton.isDisable()){
                RetakeQuizButton.setDisable(false);
            }
            //set the Retake-Quiz button to enabled
            if(PracticeMissedButton.isDisable()){
                PracticeMissedButton.setDisable(false);
            }
        }
        
        numberOfGuessesSoFar++;
        System.out.println(numberOfCorrectGuessesSoFar+" and "+numberOfGuessesSoFar);
        long progress = Math.round(((double)currentGeoObjIndex/(double)quiz.getGeoObjs().size())*100);        
        PercentComplete.setText(progress+"% Complete");
        long percentCorrect = Math.round(((double)CorrectElementsListView.getItems().size()/(double)(currentGeoObjIndex+1))*100);
        PercentCorrect.setText(percentCorrect+"% Correct");
    }
    
    private void resetUI(){
        //clears the element and element name
        if(quiz.getType()==QuizCreation.DRAG_DROP){
            if(currentElementImageView!=null){
                currentElementImageView.setImage(null);
            }
            currentElementName.setText("");
        }
    }
    
    private void rearrange (QuizCreation toArrange){
        /*This function ensures that all the features come FIRST,
         * BEFORE the cities. It also ensures a random order for both the
         * features AND the cities. It is done now rather than at quiz
         * creation so that each time, the order is new (so you
         * can't memorize the order to make it easier).
         */
        
        ArrayList<geoObj> features = new ArrayList<geoObj>();
        ArrayList<geoObj> cities = new ArrayList<geoObj>();
        
        for(geoObj current : toArrange.getGeoObjs()){
            if(current.type==geoObj.CATEGORY_AND_FEATURE||current.type==geoObj.FEATURE){
                features.add(current);
            }else if(current.type==geoObj.CITY){
                cities.add(current);
            }
        }
        Collections.shuffle(features, new Random(System.nanoTime()));
        Collections.shuffle(cities, new Random(System.nanoTime()));
        
        //now to combine the two lists
        
        ArrayList<geoObj> newList = new ArrayList<geoObj>();
        
        //add all the features
        if(!features.isEmpty()){
            for (geoObj f : features){
                newList.add(f);
            }
        }
        //add all the cities
        if(!cities.isEmpty()){
            for (geoObj c : cities){
                newList.add(c);
            }
        }
        
        toArrange.SetGeoObjs(newList);
        
    }
    
    private void addToStackPane(GeoObjView toAdd){
        
        
        geoObj geoObjToAdd = toAdd.getGeoObj();
        
        if(geoObjToAdd.type == geoObj.FEATURE || geoObjToAdd.type == geoObj.CATEGORY_AND_FEATURE){
            int layer = toAdd.getGeoObj().layer;
            System.out.println("Adding "+geoObjToAdd+" to layer "+layer+"...");
            for(int index =0; index<targetStackPane.getChildren().size(); index++){
                if(targetStackPane.getChildren().get(index) instanceof GeoObjView){
                    GeoObjView currentGeoView = (GeoObjView) targetStackPane.getChildren().get(index);
                    geoObj comparativeGeoObj = currentGeoView.getGeoObj();
                    
                    if(comparativeGeoObj.type==geoObj.CITY){
                        //it's a city, so it must go here
                        System.out.println("    Found a city - "+comparativeGeoObj+" - so add here!");
                        targetStackPane.getChildren().add(index, toAdd);
                        System.out.println("    Adding "+geoObjToAdd.name+" to index "+targetStackPane.getChildren().indexOf(toAdd));
                        break;
                    }else if(targetStackPane.getChildren().size()-index==1){
                        //we're checking the last item
                        System.out.println("    We hit the last index...");
                        if(comparativeGeoObj!=null){
                            //check to see if the layer is larger or smaller
                            if(comparativeGeoObj.layer>layer){
                                //the last index is larger! Add right before the end - right here
                                targetStackPane.getChildren().add(index, toAdd);
                                System.out.println("    The last index is larger! Add right before the end - right here.");
                                System.out.println("    Adding "+geoObjToAdd.name+" to index "+targetStackPane.getChildren().indexOf(toAdd));
                                break;
                            }else{
                                //the last index is the same or smaller! So add to the very end - one higher
                                targetStackPane.getChildren().add(index+1, toAdd);
                                System.out.println("    The last index is the same or smaller! So add to the very end - one higher.");
                                System.out.println("    Adding "+geoObjToAdd.name+" to index "+targetStackPane.getChildren().indexOf(toAdd));
                                break;
                            }
                        }else{
                            //the current geoObj is not included in the quiz, so it's likely the background or a bug.
                            targetStackPane.getChildren().add(index+1, toAdd);
                            System.out.println("    Adding "+geoObjToAdd.name+" to index "+targetStackPane.getChildren().indexOf(toAdd));
                            break;
                        }
                    }else if(comparativeGeoObj!=null){
                        //OK, it's not the last index, but it's a real object so let's see if the layer is larger or smaller
                        System.out.println("    Should it be added here? Checking "+comparativeGeoObj);
                        
                        //it's a feature
                        int currentLayer = comparativeGeoObj.layer;//the layer to compare against
                        if(currentLayer>layer){
                            //current layer is bigger than the one we want, so add it here
                            targetStackPane.getChildren().add(index, toAdd);
                            System.out.println("    Adding "+geoObjToAdd.name+" to index "+targetStackPane.getChildren().indexOf(toAdd));
                            break;
                        }else{
                            System.out.println("    No, it's not the last item and the layer is not larger.");
                        }
                    }else{
                        //there's no geoObj to match the imageView, so it's likely the background
                    }
                }else if(targetStackPane.getChildren().size()-index==1){
                    //not an instance of GeoObjView, but it is the last element
                    targetStackPane.getChildren().add(index+1, toAdd);
                    System.out.println("    Adding "+geoObjToAdd.name+" to index "+targetStackPane.getChildren().indexOf(toAdd));
                    break;
                }
            }
        }else if(geoObjToAdd.type==geoObj.CITY){
            System.out.println("Adding "+geoObjToAdd+" (city) ...");
            for(int index =0; index<targetStackPane.getChildren().size(); index++){
                if(targetStackPane.getChildren().get(index) instanceof GeoObjView){
                    GeoObjView currentGeoView = (GeoObjView) targetStackPane.getChildren().get(index);
                    geoObj comparativeGeoObj = currentGeoView.getGeoObj();
                    
                    if(comparativeGeoObj.type==geoObj.CITY){
                        //it's a city, so it must go here
                        System.out.println("    Found a city - "+comparativeGeoObj+" - so add here!");
                        targetStackPane.getChildren().add(index, toAdd);
                        System.out.println("    Adding "+geoObjToAdd.name+" to index "+targetStackPane.getChildren().indexOf(toAdd));
                        break;
                    }else if(targetStackPane.getChildren().size()-index==1){
                        //we're checking the last item
                        System.out.println("    We hit the last index...");
                        System.out.println("    Adding "+geoObjToAdd.name+" to index "+targetStackPane.getChildren().indexOf(toAdd));
                        targetStackPane.getChildren().add(index+1, toAdd);
                        break;
                    }else{
                        //there's no geoObj to match the imageView, so it's likely the background
                        
                    }
                }else if(targetStackPane.getChildren().size()-index==1){
                    //not an instance of GeoObjView, but it is the last element
                    targetStackPane.getChildren().add(index+1, toAdd);
                    System.out.println("    Adding "+geoObjToAdd.name+" to index "+targetStackPane.getChildren().indexOf(toAdd));
                    break;
                }
            }
        }
    }
    
    private void moveToTopOfLayer(GeoObjView theView){
        try{
            int oldIndex = targetStackPane.getChildren().indexOf(theView);
            targetStackPane.getChildren().remove(theView);
            addToStackPane(theView);
            System.out.println("Moved "+theView.getGeoObj()+" from index "+oldIndex+" to "+targetStackPane.getChildren().indexOf(theView));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void clearMap(){
        ImageView backgroundHolder = background;
        targetStackPane.getChildren().clear();
        background= backgroundHolder;
        targetStackPane.getChildren().add(background);
        try {
            initializeBackground();
        } catch (BadQuizException ex) {
            Logger.getLogger(QuizTakerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private geoObj findMatchingGeoObj(Image image){
        for (geoObj current: quiz.getGeoObjs().toArray(new geoObj[quiz.getGeoObjs().size()])){
            if(current.mapElement == image){
                return current;
            }
        }
        return null;
    }
    
    private GeoObjView findMatchingGeoObjView(geoObj obj){
        for(ImageView imgView: targetStackPane.getChildren().toArray(new ImageView[targetStackPane.getChildren().size()])){
            if(imgView instanceof GeoObjView){
                GeoObjView geoView = (GeoObjView) imgView;
                if(geoView.getGeoObj()==obj){
                    return (GeoObjView) geoView;
                }
            }
        }
        return null;
    }
    
    private void setupTimer(){
        final TimeKeeper tk = new TimeKeeper();
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(quizzing){
                    tk.tickMillisecond();
                    Timer.setText(tk.getTime());
                }
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }
    
    private void quizEnded(){
        //first, switch to the "round" tab
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(roundTab);
        //now disable the quiz tab and buttons that aren't needed anymore
        quizTab.setDisable(true);
        SaveProgressButton.setDisable(true);
    }
    
    
    //=============================ActionEvents==============================================
    
        
    
    public void Cheat(){
        correct();
    }
    public void BeginQuiz(){
        try {
            startQuiz();
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(quizTab);
        } catch (BadQuizException ex) {
            Logger.getLogger(QuizTakerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void Debug(){
        System.out.println("Debug");
        
    }
    public void GiveUp(){
        /* This function essentially does the same thing as "Correct()"
         * in that it continues quizzing the next element. The key difference
         * is that instead of logging an element as "correct," it logs it as
         * "incorrect" (so, at the end, users can see which they got correct
         * and incorrect.
         */
        double greyedOutOpacityLevel = .5;
        
        //for Point-Click, show the correct feature (greyed out)
        if(quiz.getType()==QuizCreation.DRAG_DROP){
            System.out.println("Adding incorrect feature");
            GeoObjView toAdd = new GeoObjView(quiz.getGeoObjs().get(currentGeoObjIndex));
            toAdd.setOpacity(greyedOutOpacityLevel);
            addToStackPane(toAdd);
        }
        //if Drag-Drop, grey out the correct feature
        if(quiz.getType()==QuizCreation.POINT_CLICK){
            
            try{
                //go through all layers of targetStackPane, looking for the current geoObj
                for(int current = 0; current<targetStackPane.getChildren().size(); current++){
                    
                    ImageView imgView = (ImageView) targetStackPane.getChildren().get(current);
                    //is it a GeoObjView or juts a ImageView?
                    if(imgView instanceof GeoObjView){
                        GeoObjView geoObjView = (GeoObjView) imgView;
                        //is it the current geoObj?
                        if(geoObjView.getGeoObj()==quiz.getGeoObjs().get(currentGeoObjIndex)){                            
                            //Here, do something to show the correct element
                            geoObjView.setOpacity(greyedOutOpacityLevel);
                            
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        incorrect(false);
        if(currentGeoObjIndex<(quiz.getGeoObjs().size()-1)){
            //quiz isn't done yet
            currentGeoObjIndex++;
            resetUI();
            try {
                quizElement(quiz.getGeoObjs().get(currentGeoObjIndex));
            } catch (BadQuizException ex) {
                Logger.getLogger(QuizTakerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            //quiz is finished
            resetUI();
            quizzing = false;
            quizEnded();
        }
        
    }
    
    public void StartOver(){
        try{
            //reset all elements
            CorrectElementsListView.getItems().clear();
            IncorrectElementsListView.getItems().clear();
            quizzing = true;
            currentGeoObjIndex = 0;
            resetUI();
            quizElement(quiz.getGeoObjs().get(currentGeoObjIndex));
            setupTimer();
            numberOfCorrectGuessesSoFar = 0;
            numberOfGuessesSoFar = 0;
            PercentCorrect.setText("0% Correct");
            PercentComplete.setText("0% Complete");
            
            
            //set correct disabled state for all buttons/tabs
            quizTab.setDisable(false);
            SaveProgressButton.setDisable(false);
            StartOverButton.setDisable(false);
            RetakeQuizButton.setDisable(true);
            PracticeMissedButton.setDisable(true);
            
            //clear map and reset
            clearMap();
            
            //change to the right tab
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(quizTab);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void PracticeMissed(){
        ArrayList<geoObj> allGeoObjs = new ArrayList<geoObj>();
        for(int c = 0; c<IncorrectElementsListView.getItems().size();c++){
            allGeoObjs.add(IncorrectElementsListView.getItems().get(c));
        }
       QuizCreation missedElements = new QuizCreation(allGeoObjs, quiz.getRoot(), "(Practice) "+quiz.getName(), quiz.getType());
       Base.TakeQuiz(missedElements);
    }
    
    public void ShowHelp(){
        new PopupSender().ShowHowTo();
    }
    
    public void ShowAbout(){
        new PopupSender().ShowAbout();
    }
    
     public void Back (ActionEvent e){
        try {
            FXMLStephensPetrochko.getInstance().replaceSceneContent("MainScreen.fxml", Base.PROJECT_TITLE);
        } catch (Exception ex) {
            Logger.getLogger(PresetQuizController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     public void Exit (){
         new PopupSender().AreYouSureYouWantToExit();
     }
     
     public void ToggleFullScreen(){
         Base.ToggleFullScreen();
     }
   
}//end of class
