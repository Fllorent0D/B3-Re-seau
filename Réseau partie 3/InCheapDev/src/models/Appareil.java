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
public class Appareil implements Serializable{
    protected String numeroSerie;
    protected String typeGeneral;
    protected TypeAppareil typeAppareil;
    protected int prixVenteEffectif;
    protected int posX;
    protected int posY;
    protected int etatSituation;
    protected String etatPaiement;
    protected String typeAchat;

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getTypeGeneral() {
        return typeGeneral;
    }

    public void setTypeGeneral(String typeGeneral) {
        this.typeGeneral = typeGeneral;
    }

    public TypeAppareil getTypeAppareil() {
        return typeAppareil;
    }

    public void setTypeAppareil(TypeAppareil typeAppareil) {
        this.typeAppareil = typeAppareil;
    }

    public int getPrixVenteEffectif() {
        return prixVenteEffectif;
    }

    public void setPrixVenteEffectif(int prixVenteEffectif) {
        this.prixVenteEffectif = prixVenteEffectif;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getEtatSituation() {
        return etatSituation;
    }

    public void setEtatSituation(int etatSituation) {
        this.etatSituation = etatSituation;
    }

    public String getEtatPaiement() {
        return etatPaiement;
    }

    public void setEtatPaiement(String etatPaiement) {
        this.etatPaiement = etatPaiement;
    }

    public String getTypeAchat() {
        return typeAchat;
    }

    public void setTypeAchat(String typeAchat) {
        this.typeAchat = typeAchat;
    }
 
}
