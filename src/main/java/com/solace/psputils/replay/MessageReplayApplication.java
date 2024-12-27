package com.solace.psputils.replay;

import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.SpringJCSMPFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class MessageReplayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageReplayApplication.class, args);
    }

    private static MessageConsumer consumer = new MessageConsumer();
    // private static PublishEventHandler pubEventHandler = new
    // PublishEventHandler();
    private static QueueFlowEventHandler flowEventHandler = new QueueFlowEventHandler();

    @Component
    static class Runner implements CommandLineRunner {
        private static final Logger logger = LoggerFactory.getLogger(Runner.class);
        private final Queue queue = JCSMPFactory.onlyInstance().createQueue("q.sfdc.svc.response");

        @Autowired
        private SpringJCSMPFactory solaceFactory;

        // @Autowired(required = false)
        // private JCSMPProperties brokerProperties;

        @Override
        public void run(String... args) throws Exception {
            final JCSMPSession session = solaceFactory.createSession();
            final ConsumerFlowProperties flowProp = new ConsumerFlowProperties();

            flowProp.setEndpoint(queue);
            flowProp.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);
            flowProp.setActiveFlowIndication(true);

            FlowReceiver flowReceiver = session.createFlow(consumer, flowProp, null, flowEventHandler);
            flowReceiver.start();
            logger.info("Flow receiver started and listening on queue: {}", queue);

            try {
                while (!consumer.getShutdown()) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                logger.error("I was awoken while waiting.");
            }
        }

    }

}
