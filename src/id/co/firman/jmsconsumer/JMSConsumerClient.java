/*
 *  @Author Firman Hidayat
 */
package id.co.firman.jmsconsumer;
 
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
 
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.jms.TextMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
 
public class JMSConsumerClient {
 
    static void jojon(String host, int port, String channel,
            String queueManagerName, String destinationName, boolean isTopic)
            throws ParserConfigurationException, IOException, SAXException {
 
        int timeout = 15000; // in ms or 15 seconds
 
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                "hasil.txt", true)));
 
        // Variables
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageConsumer consumer = null;
 
        try {
            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory
                    .getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();
 
            // Set the properties
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
            cf.setIntProperty(WMQConstants.WMQ_PORT, port);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE,
                    WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER,
                    queueManagerName);
 
            // Create JMS objects
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            if (isTopic) {
                destination = session.createTopic(destinationName);
            } else {
                destination = session.createQueue(destinationName);
            }
            consumer = session.createConsumer(destination);
 
            // Start the connection
            connection.start();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            while (true) {
 
                // And, receive the message
                Message message = consumer.receive(timeout);
                TextMessage dd = (TextMessage) message;
                Date waktuAmbil = new Date();
                if (message != null) {
                    BacaXML.bacaXML(dd.getText(), out, waktuAmbil, dBuilder);
                } else {
                    System.out.println("No message received!\n");
                    recordFailure(null);
                }
            }
        } catch (JMSException jmsex) {
            recordFailure(jmsex);
        } finally {
            if (consumer != null) {
                try {
                    consumer.close();
                } catch (JMSException jmsex) {
                    System.out.println("Consumer could not be closed.");
                    recordFailure(jmsex);
                }
            }
 
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException jmsex) {
                    System.out.println("Session could not be closed.");
                    recordFailure(jmsex);
                }
            }
 
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException jmsex) {
                    System.out.println("Connection could not be closed.");
                    recordFailure(jmsex);
                }
            }
        }
    }
 
    private static void recordSuccess() {
        return;
    }
 
    private static void recordFailure(Exception ex) {
        if (ex != null) {
            if (ex instanceof JMSException) {
                processJMSException((JMSException) ex);
            } else {
                System.out.println(ex);
            }
        }
        return;
    }
 
    private static void processJMSException(JMSException jmsex) {
        System.out.println(jmsex);
        Throwable innerException = jmsex.getLinkedException();
        if (innerException != null) {
            System.out.println("Inner exception(s):");
        }
        while (innerException != null) {
            System.out.println(innerException);
            innerException = innerException.getCause();
        }
        return;
    }
 
}