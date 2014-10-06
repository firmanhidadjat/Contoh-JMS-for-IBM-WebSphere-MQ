/*
 *  @Author Firman Hidayat
 */
package id.co.firman.jmsproducer;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JMSProducer {
	static void kirimMessage(String host, int port, String channel,
			String queueManagerName, String destinationName, boolean isTopic) {

		// Variables
		Connection connection = null;
		Session session = null;
		Destination destination = null;
		MessageProducer producer = null;
		Long i = 1L;
		// WHILE (bikin koneksi setiap kirim message) =======================
		 while (true) {
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
			producer = session.createProducer(destination);

			// Start the connection
			connection.start();

			// WHILE (Satu koneksi banyak kirim data) =====================
//			while (true) {
				try {

					SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
							"HH:mm:ss:SS");
					Date waktu = new Date();

					// Data yang dikirimkan
					String stringDenganSOAPEnvelope = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:han=\"http://firman.co.id/\">"
							+ " <soapenv:Header/>"
							+ "<soapenv:Body>"
							+ "<han:tambahList>"

							// ===== ISI DATA - awal
							+ "<TestMsg><ID>"
							+ i
							+ "</ID>"
							+ "<JamKirim>"
							+ DATE_FORMAT.format(waktu)
							+ "</JamKirim>"
							+ "<Message>"
							+ String.valueOf(waktu.getTime())
							+ "</Message></TestMsg>"
							// ===== ISI DATA - akhir

							+ "</han:tambahList>"
							+ "</soapenv:Body>"
							+ "</soapenv:Envelope>";

//					String stringPesan = "<DataJojon>" + "<ID>" + i + "</ID>"
//							+ "<Pesan>" + "Bakso gak pakai sambal" + "</Pesan>"
//							+ "</DataJojon>";

					// Ubah menjadi TextMessage
					TextMessage message = session
							.createTextMessage(stringDenganSOAPEnvelope);

					// And, send the message
					producer.send(message);
					System.out.print(i + "\t" + DATE_FORMAT.format(waktu)
							+ "\t" + String.valueOf(waktu.getTime()) + "\t");
					i++;
					recordSuccess();
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//			}
			// EndWhile (Satu koneksi banyak kirim data) =====================

		} catch (JMSException jmsex) {
			recordFailure(jmsex);
		} finally {
			if (producer != null) {
				try {
					producer.close();
				} catch (JMSException jmsex) {
					System.out.println("Producer could not be closed.");
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
		// ENDWHILE (bikin koneksi setiap kirim message) =======================
	}

	private static void recordSuccess() {
		System.out.println("SUKSES");
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
		System.out.println("FAILURE");
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
