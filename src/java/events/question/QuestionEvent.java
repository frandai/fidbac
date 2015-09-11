/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events.question;

/**
 *
 * @author fran
 */
public class QuestionEvent {
    private int id;
    private boolean open;

    public QuestionEvent() {
    }

    public QuestionEvent(int id, boolean open) {
        this.id = id;
        this.open = open;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
