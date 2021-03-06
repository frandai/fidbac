/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events.feelings;

import events.answer.*;
import events.question.*;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONDecoder;
import org.primefaces.push.impl.JSONEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PushEndpoint("/feeling/{presentation}")
@Singleton
public class FeelingManager {

    private final Logger logger = LoggerFactory.getLogger(FeelingManager.class);

    @PathParam("presentation")
    private String presentation;

    @OnMessage(decoders = {JSONDecoder.class}, encoders = {JSONEncoder.class})
        public FeelingEvent onMessage(FeelingEvent feeling) {
        return feeling;
    }
   
    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
    }
 
    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
    }
}
