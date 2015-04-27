package actionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import service.TransactionService;
import model.FileChange;
import mydropbox.MyDropboxSwing;
import controller.AppConfig;

public class UploadAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//Lay tid gan nhat
		int tId = TransactionService.getTransaction()+1;
		String tid = Integer.toString(tId);
		//Luu tid vao file property
		System.out.println("Transaction: "+tid);
		for(FileChange fileChange: MyDropboxSwing.lstCommit)
		{
			fileChange.setTid(tid);
			System.out.println(fileChange.toString());
			fileChange.doAction();
		}
		MyDropboxSwing.lstCommit.clear();
		AppConfig property = new AppConfig();
		property.write("tid", tid);
	}

}
