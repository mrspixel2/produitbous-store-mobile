package com.mycompany.myapp.entities;

public class Rating {
    int id,id_user,id_produitbou,rate;

    public Rating() {
    }

    public Rating(int id_user, int id_produitbou, int rate) {
        this.id_user = id_user;
        this.id_produitbou = id_produitbou;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_produitbou() {
        return id_produitbou;
    }

    public void setId_produitbou(int id_produitbou) {
        this.id_produitbou = id_produitbou;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
