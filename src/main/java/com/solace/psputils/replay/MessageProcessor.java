package com.solace.psputils.replay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.TextMessage;

public class MessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
    public static Object processMessage;
    private int msgCount = 0;

    public void processMessage(BytesXMLMessage msg) throws Exception {
        msgCount++;
        String data;
        if (msg instanceof TextMessage) {
            data = ((TextMessage) msg).getText();
        } else {
            byte[] payload = new byte[msg.getAttachmentContentLength()];
            msg.readAttachmentBytes(payload);
            data = new String(payload);
        }
        logger.info("Received message: {}", data);

        if (msgCount == 1) {
            Queue queue = JCSMPFactory.onlyInstance().createQueue("q.sfdc.store");
            QueueBrowser qBrow = new QueueBrowser();
            qBrow.browseQueue(queue, "a0UNy000005lTmwMAE-268680");
        }
        msg.ackMessage();
        logger.info("Message Acknowledged.");
    }

}
