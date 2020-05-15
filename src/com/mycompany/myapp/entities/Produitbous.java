package com.mycompany.myapp.entities;

import java.util.Objects;

public class Produitbous {

    int id,store_id,prix,qte;
    String nom,description,image,categorie;

    public Produitbous() {
    }

    public Produitbous(int id, int store_id, int prix, int qte, String nom, String description, String image, String categorie) {
        this.id = id;
        this.store_id = store_id;
        this.prix = prix;
        this.qte = qte;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.categorie = categorie;
    }

    public Produitbous(int store_id, int prix, int qte, String nom, String description, String image, String categorie) {
        this.store_id = store_id;
        this.prix = prix;
        this.qte = qte;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.categorie = categorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produitbous that = (Produitbous) o;
        return id == that.id &&
                store_id == that.store_id &&
                prix == that.prix &&
                qte == that.qte &&
                Objects.equals(nom, that.nom) &&
                Objects.equals(description, that.description) &&
                Objects.equals(image, that.image) &&
                Objects.equals(categorie, that.categorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, store_id, prix, qte, nom, description, image, categorie);
    }

    @Override
    public String toString() {
        return "Produitbous{" +
                "id=" + id +
                ", store_id=" + store_id +
                ", prix=" + prix +
                ", qte=" + qte +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
