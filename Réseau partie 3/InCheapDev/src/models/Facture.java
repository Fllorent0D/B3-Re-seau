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
public class Facture implements Serializable {
    private int idFacture;
    private Client client;
    private Date dateFacturation;
    private String typeAchat;
    private String modePaiement;
    private String adresseLivraison;
    private Ville ville;
    private int validee;

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDateFacturation() {
        return dateFacturation;
    }

    public void setDateFacturation(Date dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public String getTypeAchat() {
        return typeAchat;
    }

    public void setTypeAchat(String typeAchat) {
        this.typeAchat = typeAchat;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public int getValidee() {
        return validee;
    }

    public void setValidee(int validee) {
        this.validee = validee;
    }
    
    
}
