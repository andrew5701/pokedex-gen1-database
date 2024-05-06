/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.databaseexample;

/**
 *
 * @author ak7272
 */
public class GymLeaders {
    private int leaderID;
    private String leaderName;
    private int regionID; // This should reference a valid Region
    private String badgeName;
    private byte[] leaderImage; // This will store the image binary data

    public int getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(int leaderID) {
        this.leaderID = leaderID;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public byte[] getLeaderImage() {
        return leaderImage;
    }

    public void setLeaderImage(byte[] leaderImage) {
        this.leaderImage = leaderImage;
    }

    // Constructor
    public GymLeaders(int leaderID, String leaderName, int regionID, String badgeName, byte[] leaderImage) {
        this.leaderID = leaderID;
        this.leaderName = leaderName;
        this.regionID = regionID;
        this.badgeName = badgeName;
        this.leaderImage = leaderImage;
    }
}
