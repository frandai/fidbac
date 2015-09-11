/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdb;

/**
 *
 * @author fran
 */
public class SimpleProperty {
    private int propertireId;
    private String propertireValue;

    public SimpleProperty() {
    }

    public SimpleProperty(int propertireId, String propertireValue) {
        this.propertireId = propertireId;
        this.propertireValue = propertireValue;
    }

    public int getPropertireId() {
        return propertireId;
    }

    public void setPropertireId(int propertireId) {
        this.propertireId = propertireId;
    }

    public String getPropertireValue() {
        return propertireValue;
    }

    public void setPropertireValue(String propertireValue) {
        this.propertireValue = propertireValue;
    }
}
