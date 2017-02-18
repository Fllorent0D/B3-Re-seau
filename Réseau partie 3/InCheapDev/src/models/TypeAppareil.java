/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;

/**
 *
 * @author bastin
 */
public class TypeAppareil implements Serializable {
    protected int idTypeAppareil;
    protected String libelle;
    protected float prixVenteBase;
    protected String marque;
    protected int available;

    public int getIdTypeAppareil() {
        return idTypeAppareil;
    }

    public void setIdTypeAppareil(int idTypeAppareil) {
        this.idTypeAppareil = idTypeAppareil;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public float getPrixVenteBase() {
        return prixVenteBase;
    }

    public void setPrixVenteBase(float prixVenteBase) {
        this.prixVenteBase = prixVenteBase;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    } 

    @Override
    public String toString() {
        return getLibelle() + " (" + getPrixVenteBase() + " €)";
    }
}
               