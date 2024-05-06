/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.databaseexample;

import java.sql.Date;

/**
 *
 * @author ak7272
 */
public class Games {
    private int gameID;
    private String gameName;
    private int regionID; // This should reference a valid Region
    private String releaseDate;
    private byte[] gameImage; // This will store the image binary data

    public byte[] getGameImage() {
        return gameImage;
    }

    public void setGameImage(byte[] gameImage) {
        this.gameImage = gameImage;
    }

    // Constructor
    public Games(int gameID, String gameName, int regionID, String releaseDate, byte[] gameImage) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.regionID = regionID;
        this.releaseDate = releaseDate;
        this.gameImage = gameImage;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

   

    // Getters and Setters
    // ... similar to the Region class

    // Additional methods like toString(), equals(), hashCode(), etc. could be implemented as needed.
}
