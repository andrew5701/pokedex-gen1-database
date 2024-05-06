package com.mycompany.databaseexample;

/**
 * Represents a Region in the Pokémon universe.
 */
public class Regions {
    private int regionID;
    private String regionName;
    private byte[] regionImage; // This will store the image binary data

    // Constructor
    public Regions(int regionID, String regionName, byte[] regionImage) {
        this.regionID = regionID;
        this.regionName = regionName;
        this.regionImage = regionImage;
    }

    // Getters and Setters
    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public byte[] getRegionImage() {
        return regionImage;
    }

    public void setRegionImage(byte[] regionImage) {
        this.regionImage = regionImage;
    }

    // Additional methods like toString(), equals(), hashCode(), etc. could be implemented as needed.
}