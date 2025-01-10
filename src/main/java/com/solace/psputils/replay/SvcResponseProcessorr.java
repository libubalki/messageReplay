package com.solace.psputils.replay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.TextMessage;

public class SvcResponseProcessorr {
    private static final Logger logger = LoggerFactory.getLogger(SvcResponseProcessorr.class);
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
        SvcResponseData svcRespData = new Gson().fromJson(data, SvcResponseData.class);

        GetSFDCStatus getSFDCStatus = new GetSFDCStatus();
        int respCode = getSFDCStatus.getSfdcStatus(data);
        logger.info("Response Code from SFDC: {}", respCode);
        // if (msgCount == 1) {
        Queue queue = JCSMPFactory.onlyInstance().createQueue("q.sfdc.store");
        QueueBrowser qBrow = new QueueBrowser();
        qBrow.browseQueue(queue, "a0UNy000005lTmwMAE-268680", respCode);
        // }
        msg.ackMessage();
        logger.info("Message Acknowledged.");
    }

    static class SvcResponseData {
        String refId;
        String resp;
        String svcName;
    }
}
