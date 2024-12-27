package com.solace.psputils.replay;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPChannelProperties;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.JCSMPStreamingPublishCorrelatingEventHandler;
import com.solacesystems.jcsmp.SessionEventHandler;
import com.solacesystems.jcsmp.XMLMessageConsumer;
import com.solacesystems.jcsmp.XMLMessageListener;
import com.solacesystems.jcsmp.XMLMessageProducer;

public class SolaceClient {
    protected static Logger logger = LogManager.getLogger(SolaceClient.class);
    private static XMLMessageProducer producer;
    private static XMLMessageConsumer consumer;
    private static JCSMPSession session;
    private static JCSMPProperties properties = new JCSMPProperties();

    public XMLMessageProducer createPublisher(JCSMPSession session,
            JCSMPStreamingPublishCorrelatingEventHandler PublishEventHandler) {

        logger.info("Creating Solace Publisher.");
        try {
            producer = session.getMessageProducer(PublishEventHandler);
        } catch (JCSMPException e) {
            logger.info("Exception thrown when trying to create a Message Producer.");
            e.printStackTrace();
        }
        return producer;

    }

    public XMLMessageConsumer createConsumer(JCSMPSession session, XMLMessageListener ListenerHandler) {
        logger.info("Creating Solace Consumer.");
        try {
            consumer = session.getMessageConsumer(ListenerHandler);
        } catch (JCSMPException e) {
            logger.error("Exception thrown when trying to create a Message Consumer.");
            e.printStackTrace();
        }
        return consumer;
    }

    public JCSMPSession createSession(SessionEventHandler AppSessionEventHandler) {
        try {
            session = JCSMPFactory.onlyInstance().createSession(properties, null, AppSessionEventHandler);
            logger.info("Solace session created.");
        } catch (InvalidPropertiesException e) {
            logger.error("Exception while creating solace session.Recheck session properties.");
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception while creating session.");
            e.printStackTrace();
        }
        return session;
    }

    public void closeSession(JCSMPSession session) {
        session.closeSession();
        logger.info("Session closed.");
    }

    public void setProperty(String propName, String propValue) {
        logger.info("Setting Property {}: {}", propName, propValue);
        properties.setProperty(propName, propValue);
    }

    public JCSMPProperties getProperties() {
        return properties;
    }

    public void setChannelProperty(JCSMPChannelProperties channelProperties) {
        properties.setProperty(JCSMPProperties.CLIENT_CHANNEL_PROPERTIES, channelProperties);
        logger.info("Channel properties set.");
    }

    public JCSMPSession getSession() {
        return session;
    }
}
