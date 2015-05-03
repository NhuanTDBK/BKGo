package view;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import model.FileStorage;
import model.FileStorageDAO;
import model.FileVersion;
import model.FileVersionDAO;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import utils.ServerUtil;

public class TestDB {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		List<FileVersion> storage = FileVersionDAO.getAll();
		FileVersion fv = FileVersionDAO.getByFileName("a");
		if(fv==null) System.out.println("Khong thay file");
//		FileStorage f = null;//FileStorageDAO.getById(61);
//		f.setFileName("A Little Too Not Over you.mp3");
//		f.setFileRealPath(ServerUtil.encryptMessage(f.getFileName()));
//		f.setIsFile(1);
//		f.setFileSize(123);
//		f.setCreateDate(new Date());
//		f.setFileHash(f.getFileRealPath());
//		f.setUserId(1);
//		f.setFileType("mp3");
//		for(FileStorage s: storage)
//		{
//			System.out.println(s.getFileName());
//		}
		//FileStorageDAO.insertFile(f);
		//FileStorageDAO.deleteFile(2);
	}
	public static SessionFactory getSession()
	{
		Configuration cfg = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
		SessionFactory factory = cfg.buildSessionFactory(builder.build());
		return factory;
	}

	
}
