package com.solace.psputils.replay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.solacesystems.jcsmp.Browser;
import com.solacesystems.jcsmp.BrowserProperties;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.SessionEventArgs;
import com.solacesystems.jcsmp.SessionEventHandler;
import com.solacesystems.jcsmp.TextMessage;

public class QueueBrowser {

    private static final Logger logger = LoggerFactory.getLogger(QueueBrowser.class);
    private SolaceClient client = new SolaceClient();

    public void browseQueue(Queue queue, String transId) throws Exception {
        logger.info("Starting Queue Browser on {} with transaction id: {}", queue, transId);

        client.setProperty(JCSMPProperties.HOST, "tcp://localhost:55554");
        client.setProperty(JCSMPProperties.VPN_NAME, "lblocaldev");
        client.setProperty(JCSMPProperties.USERNAME, "lbdev");
        client.setProperty(JCSMPProperties.PASSWORD, "lbdev");

        final JCSMPSession session = client.createSession(new AppSessionEventHandler());

        BrowserProperties browProp = new BrowserProperties();
        browProp.setEndpoint(queue);
        browProp.setTransportWindowSize(1);
        browProp.setWaitTimeout(1000);
        // browProp.setSelector(transId);
        Browser qBrowser = session.createBrowser(browProp);

        BytesXMLMessage msg = null;
        int msgCount = 0;
        do {
            msg = qBrowser.getNext();
            if (msg != null) {
                String data;
                if (msg instanceof TextMessage) {
                    logger.info("Its a text message.");
                    data = ((TextMessage) msg).getText();
                } else {
                    logger.info("Its a byte message.");
                    byte[] payload = new byte[msg.getAttachmentContentLength()];
                    msg.readAttachmentBytes(payload);
                    data = new String(payload);
                }
                logger.info("msg.getDestination:{}, msg.rmid:{}",
                        msg.getDestination(), msg.getReplicationGroupMessageId());
                logger.info("Message: {}", data);
                EventData eventData = new Gson().fromJson(data, EventData.class);
                String refId = eventData.payload.Reference_Id__c;
                logger.info("RefId: {}", refId);
                if (refId.equals(transId)) {
                    logger.info("Transaction id found: {}", refId);
                    logger.info("Removing message from queue.");
                    qBrowser.remove(msg);
                }
                logger.info("\n===================================================\n");
            }
        } while (msg != null);

        logger.info("Finished Browsing.");
        logger.info("Total messages: {}", msgCount);
        qBrowser.close();
        session.closeSession();
    }

    private static class AppSessionEventHandler implements SessionEventHandler {
        @Override
        public void handleEvent(SessionEventArgs event) {
            logger.info("Received  session event {}", event);
        }
    }

    static class EventData {
        String schema;
        Payload payload;
        Event event;
    }

    static class Payload {
        public String Reference_Id__c;
        public String CreatedById;
        public String CreatedDate;
        public String Request_Ext_c;
        public String SVC_Name__c;
        public String Request__c;
    }

    class Event {
        public String EventUuid;
        public String replayId;
        public String EventApiName;
    }

}