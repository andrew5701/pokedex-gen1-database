/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.databaseexample;

/**
 *
 * @author ak7272
 */
public class Pokemon {
    private int id;
    private String name;
    private String type;
    private int pokedexNo;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    private Trainer trainer;

    public Pokemon(int id, String name, String type, int level, String imageUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.pokedexNo = level;
        this.imageUrl = imageUrl;
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

    public String getType() {
        return type;
    }

    public void setType(String Type) {
        this.type = Type;
    }

    public int getPokedexNo() {
        return pokedexNo;
    }

    public void setPokedexNo(int pokedexNo) {
        this.pokedexNo = pokedexNo;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
    


    
}

