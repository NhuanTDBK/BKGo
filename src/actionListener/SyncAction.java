package actionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.w3c.dom.DOMException;

import controller.DownloadHTTP;
import controller.WriteXml;
import mydropbox.MyDropboxSwing;

public class SyncAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		String tid = MyDropboxSwing.prop.getProperty("tid");
		
	}
	public static void main(String [] args)
	{
		System.out.println(System.getProperty("user.home"));
	}
}
