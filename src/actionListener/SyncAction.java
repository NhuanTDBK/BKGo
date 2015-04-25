package actionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import controller.DownloadHTTP;
import controller.TransactionHTTP;
import controller.WriteXml;
import model.FileChange;
import model.XmlFactory;
import mydropbox.MyDropboxSwing;

public class SyncAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		String tid = MyDropboxSwing.prop.getProperty("tid");
		try {
			
			MyDropboxSwing.watcher.stop();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//parse thanh list cac doi tuong file change
		//Kiem tra file do co thuoc danh muc commit hay khong?
		//Neu co, bao conflict
		//thuc hien doUpdate
		//Update xong, khoi dong lai watcher
		TransactionHTTP transactionHTTP = new TransactionHTTP();
		DomRepresentation dom = transactionHTTP.getSync(tid);
		XmlFactory factory = new XmlFactory(dom);
		Document doc=null;
		List<FileChange> lst=null;
		try {
			doc = dom.getDocument();
			lst = factory.parseXML(doc);
			for(FileChange fileChange:lst)
			{
				fileChange.doUpdate();
				tid = fileChange.getTid();
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.out.println("Last version!!!!");
		}
		MyDropboxSwing.property.write("tid",tid);
		try {
			MyDropboxSwing.watcher.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
//	public static void main(String [] args)
//	{
//		System.out.println(System.getProperty("user.home"));
//	}
}
