package actionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.FileChange;
import mydropbox.MyDropboxSwing;
import controller.Property;
import controller.TransactionHTTP;

public class UploadAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//Lay tid gan nhat
		int tId = TransactionHTTP.getTransaction()+1;
		String tid = Integer.toString(tId);
		//Luu tid vao file property
//		WriteXml write = new WriteXml(MyDropboxSwing.urls,tid);
//		MyDropboxSwing.prop.setProperty("tid",tid);
//		try {
//			write.writexml();
//		} catch (DOMException | NoSuchAlgorithmException | IOException ex) {
//			// TODO Auto-generated catch block
//			ex.printStackTrace();
//		}
	//	int tid = TransactionHTTP.getTransaction()+1;
		System.out.println("Transaction: "+tid);
		for(FileChange fileChange: MyDropboxSwing.lstCommit)
		{
			fileChange.setTid(tid);
			System.out.println(fileChange.toString());
			fileChange.doAction();
		}
		MyDropboxSwing.lstCommit.clear();
		Property property = new Property();
		property.write("tid", tid);
	}

}
