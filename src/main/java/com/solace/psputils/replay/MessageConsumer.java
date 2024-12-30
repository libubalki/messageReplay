package com.solace.psputils.replay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.XMLMessageListener;

public class MessageConsumer implements XMLMessageListener {
    // private CountDownLatch latch = new CountDownLatch(1);
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    boolean isShutdown = false;
    SvcResponseProcessorr msgProc = new SvcResponseProcessorr();

    public void onReceive(BytesXMLMessage msg) {
        try {
            msgProc.processMessage(msg);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onException(JCSMPException e) {
        logger.error("Exception in Queue Receiver: {}", e);
        logger.error("Shutting down receiver......");
        isShutdown = true;
    }

    public boolean getShutdown() {
        return isShutdown;
    }
    // public CountDownLatch getLatch() {
    // return latch;
    // }
}
