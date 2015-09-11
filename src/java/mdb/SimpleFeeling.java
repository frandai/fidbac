/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdb;

import java.util.List;

/**
 *
 * @author fran
 */
public class SimpleFeeling {

    private int presentationId;
    private int userId;
    private int feelingId;
    private int previousFeelingId;
    private long time;
    private List<SimpleProperty> propertires;

    public SimpleFeeling(int presentationId, int userId, int feelingId, int previousFeelingId, long time) {
        this.presentationId = presentationId;
        this.userId = userId;
        this.feelingId = feelingId;
        this.time = time;
        this.previousFeelingId = previousFeelingId;
    }

    public int getPreviousFeelingId() {
        return previousFeelingId;
    }

    public void setPreviousFeelingId(int previousFeelingId) {
        this.previousFeelingId = previousFeelingId;
    }

    public int getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(int presentationId) {
        this.presentationId = presentationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFeelingId() {
        return feelingId;
    }

    public void setFeelingId(int feelingId) {
        this.feelingId = feelingId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<SimpleProperty> getPropertires() {
        return propertires;
    }

    public void setPropertires(List<SimpleProperty> propertires) {
        this.propertires = propertires;
    }

}
