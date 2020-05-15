package com.mycompany.myapp.entities;

import java.util.Objects;

public class Store {
    int id;
    int owner_id;
    String nom,description;

    public Store() {
    }
    public Store(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public Store(int id, int owner_id, String nom, String description) {
        this.id = id;
        this.owner_id = owner_id;
        this.nom = nom;
        this.description = description;
    }

    public Store(int owner_id, String nom, String description) {
        this.owner_id = owner_id;
        this.nom = nom;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id == store.id &&
                owner_id == store.owner_id &&
                Objects.equals(nom, store.nom) &&
                Objects.equals(description, store.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner_id, nom, description);
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", owner_id=" + owner_id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
