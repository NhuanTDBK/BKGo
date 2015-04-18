package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.w3c.dom.DOMException;

import mydropbox.MyDropboxSwing;

public class SyncController implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		WriteXml writeXml = new WriteXml(MyDropboxSwing.urls);
		
//	try {
//		writeXml.writexml();
//	} catch (DOMException | NoSuchAlgorithmException | IOException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
		DownloadHTTP download = new DownloadHTTP();
		try {
			download.getAll();
		} catch (IllegalStateException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
}
}
