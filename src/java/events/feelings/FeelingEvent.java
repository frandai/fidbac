/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events.feelings;

import events.answer.*;

/**
 *
 * @author fran
 */
public class FeelingEvent {
    private int currentFeeling;
    private int previousFeeling;
    private String filter;
    private int userId;

    public FeelingEvent() {
    }

    public FeelingEvent(int currentFeeling, int previousFeeling, String filter, int userId) {
        this.currentFeeling = currentFeeling;
        this.previousFeeling = previousFeeling;
        this.filter = filter;
        this.userId = userId;
    }
    
    public int getCurrentFeeling() {
        return currentFeeling;
    }

    public void setCurrentFeeling(int currentFeeling) {
        this.currentFeeling = currentFeeling;
    }

    public int getPreviousFeeling() {
        return previousFeeling;
    }

    public void setPreviousFeeling(int previousFeeling) {
        this.previousFeeling = previousFeeling;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
