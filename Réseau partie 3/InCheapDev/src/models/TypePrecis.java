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
public class TypePrecis implements Serializable {
    protected int idTypePrecis;
    protected String type;

    public int getIdTypePrecis() {
        return idTypePrecis;
    }

    public void setIdTypePrecis(int idTypePrecis) {
        this.idTypePrecis = idTypePrecis;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
    
      
}
