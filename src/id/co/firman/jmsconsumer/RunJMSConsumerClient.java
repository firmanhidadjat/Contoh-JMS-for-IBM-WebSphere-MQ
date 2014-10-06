/*
 *  @Author Firman Hidayat
 */
package id.co.firman.jmsconsumer;
 
import java.io.IOException;
 
import javax.xml.parsers.ParserConfigurationException;
 
import org.xml.sax.SAXException;
 
public class RunJMSConsumerClient {
    public static void main(String[] a) throws ParserConfigurationException, IOException, SAXException {
 
        String host = "192.168.0.55"; // host
        host = "localhost";
        int port = 1616; // Port Listener
        String channel = "SYSTEM.ADMIN.SVRCONN";
        String queueManagerName = "QMB"; // Nama Queue Manager
        String destinationName = "RSCF.IN"; // Nama queue
        boolean isTopic = false;
 
        System.out.println("Running...");
        JMSConsumerClient.jojon(host, port, channel, queueManagerName,
                destinationName, isTopic);
    }
}