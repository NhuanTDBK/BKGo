package service;

import java.io.IOException;
import model.FileChange;

public class NoticationServer implements Runnable {
	public int port = 15672;
	public NoticationServer() {
	}

	@Override
	public void run() {
		System.out.println("Try to connect");
		RabbitService service = new RabbitService(null, "demo");
		while(true)
		{
			try {
				FileChange f = new FileChange();
				System.out.println("Connect successfull!");
				f.setIpAddress("192");
				service.sendMessage("Hello World");
				Thread.sleep(10000);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String args[]) {
		Thread t = new Thread(new NoticationServer());
		t.start();
	}
}
