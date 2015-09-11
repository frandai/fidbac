/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

import events.answer.AnswerEvent;
import events.feelings.FeelingEvent;
import events.question.QuestionEvent;
import events.section.SectionEvent;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

public class EventsUtilities {
    private static EventBus eventBus = EventBusFactory.getDefault().eventBus();

    private static EventBus getEventBus() {
        if(eventBus == null) {
            eventBus = EventBusFactory.getDefault().eventBus();
        }
        return eventBus;
    }
    
    public static void publishSection(String presentationId, SectionEvent secion) {
        getEventBus().publish("/section/"+presentationId, secion);
    }
    
    public static void publishQuestion(String presentationId, QuestionEvent question) {
        getEventBus().publish("/question/"+presentationId, question);
    }
    
    public static void publishAnswer(String presentationId, AnswerEvent answer) {
        getEventBus().publish("/answer/"+presentationId, answer);
    }
    
    public static void publishFeeling(String presentationId, FeelingEvent feeling) {
        getEventBus().publish("/feeling/"+presentationId, feeling);
    }
}
