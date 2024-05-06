/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.databaseexample;

/**
 *
 * @author ak7272
 */
public class Trainer {

    private int id;
    private String name;
    private String username;
    private String rank;
    private String ownedPokemon;
    private byte[] profPic;
    private int FavoriteGameID;

    public int getFavoriteGameID() {
        return FavoriteGameID;
    }

    public void setFavoriteGameID(int FavoriteGameID) {
        this.FavoriteGameID = FavoriteGameID;
    }

    public byte[] getProfPic() {
        return profPic;
    }

    public void setProfPic(byte[] profPic) {
        this.profPic = profPic;
    }

   
    
    
    public Trainer(int id, String name, String username, String rank, String ownedPokemon, byte[] profPic, int FavoriteGameID) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.rank = rank;
        this.ownedPokemon = ownedPokemon;
        this.profPic = profPic;
        this.FavoriteGameID = FavoriteGameID;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

   
    public String getOwnedPokemon() {
        return ownedPokemon;
    }

    public void setOwnedPokemon(String ownedPokemon) {
        this.ownedPokemon = ownedPokemon;
    }
    
    
    
    
}
