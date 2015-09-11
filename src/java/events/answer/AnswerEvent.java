/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events.answer;

/**
 *
 * @author fran
 */
public class AnswerEvent {
    private String[] option;
    private boolean correct;
    private String text;

    public AnswerEvent() {
    }

    public AnswerEvent(String[] option, boolean correct) {
        this.option = option;
        this.correct = correct;
    }

    public String[] getOption() {
        return option;
    }

    public void setOption(String[] option) {
        this.option = option;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
