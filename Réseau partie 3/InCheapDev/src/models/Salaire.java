/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author bastin
 */
public class Salaire implements Serializable {
    protected int idSalaire;
    protected float montant;
    protected int payed;
    protected Personnel personnel;
    protected Date dateSalaire;

    public int getIdSalaire() {
        return idSalaire;
    }

    public void setIdSalaire(int idSalaire) {
        this.idSalaire = idSalaire;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public int getPayed() {
        return payed;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public Date getDateSalaire() {
        return dateSalaire;
    }

    public void setDateSalaire(Date dateSalaire) {
        this.dateSalaire = dateSalaire;
    }
}
