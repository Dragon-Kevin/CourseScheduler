/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler;

/**
 *
 * @author AJ
 */
public class Classroom {
    private String roomNum;
    private int mEnroll;
    private String buildingName;
    
    public Classroom(){
        this.roomNum  = "";
        this.mEnroll = -1;
        this.buildingName = "";
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
