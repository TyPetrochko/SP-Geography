/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.stephens.petrochko;

/**
 *
 * @author Tyler
 */
public class TimeKeeper {
    private String time="00:00:000";
    private int millis=0;
    private int sec=0;
    private int min=0;
    
    
    public void tickMillisecond(){
        if(millis<999){
            millis++;
        }else{
            if(sec<60){
                millis=0;
                tickSecond();
            }else{
                millis=999;
            }
        }
    }
    public void tickSecond(){
        if(sec<59){
            sec++;
        }else{
            if(min<99){
                sec=0;
                tickMinute();
            }else{
                sec=59;
            }
        }
    }
    public void tickMinute(){
        if(min<99){
            min++;
        }else{
            min=99;
        }
    }
    public String getTime(){
        String minToPut=min+"";
        if(min<10){
            minToPut="0"+min;
        }
        String secToPut=sec+"";
        if(sec<10){
            secToPut="0"+sec;
        }
        String millisToPut=millis+"";
        if(millis<100){
            millisToPut="0"+millis;
        }
        if(millis<10){
            millisToPut="00"+millis;
        }
        return(minToPut+":"+secToPut+":"+millisToPut);
    }
}
