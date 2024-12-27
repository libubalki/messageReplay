package com.solace.psputils.replay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.FlowEventArgs;
import com.solacesystems.jcsmp.FlowEventHandler;

public class QueueFlowEventHandler implements FlowEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueueFlowEventHandler.class);

    @Override
    public void handleEvent(Object source, FlowEventArgs event) {
        logger.info("Recevied event: {} {}", source, event);
    }
}
