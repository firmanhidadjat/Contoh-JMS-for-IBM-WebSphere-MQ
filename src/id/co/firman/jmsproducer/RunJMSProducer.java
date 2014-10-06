/*
 *  @Author Firman Hidayat
 */
package id.co.firman.jmsproducer;

public class RunJMSProducer {
	public static void main(String[] args) {
		String host = "192.168.0.55"; // Nama host
		host = "localhost";
		int port = 1618; // Port Listener
		String channel = "SYSTEM.ADMIN.SVRCONN";
		
		String queueManagerName = "QMWSA"; // Nama Queue Manager
		String destinationName = "RSCF.INPUT"; // Nama queue
		boolean isTopic = false;

		System.out.println("Running...");
		
		
		JMSProducer.kirimMessage(host, port, channel, queueManagerName,
				destinationName, isTopic);
		
	}
}
