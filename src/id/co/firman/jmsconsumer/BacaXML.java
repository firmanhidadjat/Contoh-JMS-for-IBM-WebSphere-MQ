/*
 *  @Author Firman Hidayat
 */
package id.co.firman.jmsconsumer;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
 
public class BacaXML {
    static void bacaXML(String xml, PrintWriter out, Date waktuAmbil,
            DocumentBuilder dBuilder) throws ParserConfigurationException,
            SAXException, IOException {
        String kalimat = "";
        Long waktuPut = null;
 
        Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
 
        NodeList nList = doc.getElementsByTagName("ID");
        for (int t = 0; t < nList.getLength(); t++) {
            Node nNode = nList.item(t);
            kalimat = kalimat + nNode.getTextContent() + "\t";
            System.out.print(nNode.getTextContent() + "\t");
        }
        nList = doc.getElementsByTagName("JamKirim");
        for (int t = 0; t < nList.getLength(); t++) {
            Node nNode = nList.item(t);
            kalimat = kalimat + nNode.getTextContent() + "\t";
            System.out.print(nNode.getTextContent() + "\t");
        }
 
        nList = doc.getElementsByTagName("Message");
        for (int t = 0; t < nList.getLength(); t++) {
            Node nNode = nList.item(t);
            kalimat = kalimat + nNode.getTextContent() + "\t";
            waktuPut = Long.parseLong(nNode.getTextContent());
            System.out.print(nNode.getTextContent() + "\t");
        }
 
        kalimat = kalimat + waktuAmbil.getTime() + "\t";
        System.out.print(String.valueOf(waktuAmbil.getTime()) + "\t");
 
        Long selisihWaktu = waktuAmbil.getTime() - waktuPut;
        kalimat = kalimat + selisihWaktu + "\t";
        System.out.print(selisihWaktu + "\t");
 
        nList = doc.getElementsByTagName("BrokerName");
        for (int t = 0; t < nList.getLength(); t++) {
            Node nNode = nList.item(t);
            System.out.print(nNode.getTextContent() + "\t");
            kalimat = kalimat + nNode.getTextContent() + "\t";
        }
        out.println(kalimat);
        out.flush();
        System.out.println();
 
    }
}