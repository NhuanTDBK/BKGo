package model;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class FileVersionDAO {
	public static SessionFactory getSession()
	{
		return DBUtils.getSessionFactory();
	}
	public static List<FileVersion>getAll()
	{
		Session session = getSession().openSession();
		List<FileVersion> files = session.createQuery("FROM FileVersion").list();
		session.close();
		System.out.println("Retrieve successful");
		return files;
	}
	public static FileVersion getById(int fileId)
	{
		Session session = getSession().openSession();
		FileVersion file = (FileVersion)session.get(FileVersion.class, fileId);
		session.close();
		return file;
	}
	public static FileVersion getById(int fileId,int userId)
	{
		Session session = getSession().openSession();
		FileVersion file = (FileVersion)session.get(FileVersion.class, fileId);
		session.close();
		return file;
	}
	public static FileVersion getByFileName(String fileName)
	{
		Session session = getSession().openSession();
		FileVersion file = null;
		org.hibernate.Query query = session.createQuery("FROM FileVersion f WHERE f.fileName=:fileName");
		query.setParameter("fileName",fileName);
		try{
			List lst = query.list();
			if(!lst.isEmpty())
			{
				file = (FileVersion)lst.get(0);
			}
			else 
			{
				System.out.println("Khong tim thay ten file");
			}
		System.out.println("Retrieve file version by name");
		}catch(HibernateException e)
		{
			System.out.println("Khong tim thay ten file");
			e.printStackTrace();
		}
		finally
		{
			session.close();
		}
		return file;
	}
}
