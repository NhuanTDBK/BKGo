package actionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.FileChange;
import mydropbox.MyDropboxSwing;
import controller.TransactionHTTP;

public class UploadAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//Lay tid gan nhat
		String tid = Integer.toString(TransactionHTTP.getTransaction()+1);
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
		MyDropboxSwing.prop.setProperty("TID", tid);
	}

}
