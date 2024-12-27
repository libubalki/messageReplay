package com.solace.psputils.replay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishCorrelatingEventHandler;

public class PublishEventHandler implements JCSMPStreamingPublishCorrelatingEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(PublishEventHandler.class);

    @Override
    public void handleErrorEx(Object messageID, JCSMPException e, long timestamp) {
        logger.info("Producer received error for msg: {}@{} - {}%n", messageID, timestamp, e);
    }

    @Override
    public void responseReceivedEx(Object messageID) {
        logger.info("Producer received response for msg: {}", messageID);
    }

}
