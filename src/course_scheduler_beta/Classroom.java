/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

/**
 *
 * @author AJ
 */
public class Classroom {
    private String roomNum;
    private int mEnroll;
    private String buildingName;
    private String[] timeSlots;
    private boolean[] emptySlots;
    public String name;
    public int numberOfSeats;
    
    public Classroom(){
        this.roomNum  = "";
        this.mEnroll = -1;
        this.buildingName = "";
    }
    
    public Classroom(String roomNum){
        this.roomNum  = roomNum;
    }
    
    @Override
    public String toString () {
        return "Room = " + roomNum + "; Max Enrollment = " + mEnroll + "; Building = " + buildingName;
    }
    
    public boolean isSlotEmpty(String slot){
        if(timeSlots != null){
            for(int i = 0; i < timeSlots.length; i++){
                if(slot.equalsIgnoreCase(timeSlots[i])){
                    //System.out.println("empty slot " + slot);
                    //System.out.println(emptySlots[i]);
                    return emptySlots[i];
                }
            }
        }
        
        //System.out.println("nop");
        return false;
    }
    
    public void useSlot(String slot){
        for(int i=0;i<timeSlots.length; i++){
            if(timeSlots[i].equalsIgnoreCase(slot))
                emptySlots[i] = false;
        }
        
    }
    
    public void setTimeSlots(String[] timeSlots) {
        this.timeSlots = timeSlots;
        emptySlots = new boolean[timeSlots.length];
        for(int i = 0; i<timeSlots.length; i++){
            emptySlots[i] = true;
            //System.out.println(b);
        }
        //System.out.println(emptySlots[1]);
    }  
    
    public boolean[] getEmptySlots(){
        return emptySlots;
    }
    
    /**
     * @return the roomNum
     */
    public String getRoomNum() {
        return roomNum;
    }

    /**
     * @param roomNum the roomNum to set
     */
    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    /**
     * @return the mEnroll
     */
    public int getmEnroll() {
        return mEnroll;
    }

    /**
     * @param mEnroll the mEnroll to set
     */
    public void setmEnroll(int mEnroll) {
        this.mEnroll = mEnroll;
    }

    /**
     * @return the buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * @param buildingName the buildingName to set
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
    
}
