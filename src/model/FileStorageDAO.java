package model;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import utils.Constants;

public class FileStorageDAO {
	public static SessionFactory getSession() {
		return DBUtils.getSessionFactory();
	}
	@SuppressWarnings("unchecked")
	public static List<FileStorage> getAll() {
		Session session = getSession().getCurrentSession();
		List<FileStorage> files = session.createQuery(
				"FROM FileStorage f WHERE f.isActive = 1").list();
		System.out.println("Retrieve successful");
		session.close();
		return files;
	}

	public static FileStorage getById(int fileId) {
		Session session = getSession().openSession();
		FileStorage file=null;
		try{
			file = (FileStorage) session.get(FileStorage.class, fileId);
			System.out.println("Retrieve by Id successfull");
		}catch(HibernateException ex)
		{
			System.out.println("Retrieve by Id successfull");
		}
		finally {
			session.close();
		}
		return file;
	}

	public static boolean insertFile(FileStorage file) {
		boolean result = true;
		Session session = getSession().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(file);
			tx.commit();
			System.out.println("Insert successful");
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			result = false;
			ex.printStackTrace();
			System.out.println("Insert failed");
		} finally {
			session.close();
		}
		return result;
	}

	public static boolean updateFile(int fileId, FileStorage file) {
		boolean result = true;
		Session session = getSession().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(file);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			result = false;
			ex.printStackTrace();
			System.out.println("Update failed");
		}
		finally {
			session.close();
		}
		return result;
	}

	public static boolean deleteFile(int fileId) {
		boolean result = true;
		Session session = getSession().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			FileStorage file = (FileStorage) session.get(FileStorage.class,
					fileId);
//			session.delete(file);
                        file.setIsActive(0);
                        session.saveOrUpdate(file);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			result = false;
			ex.printStackTrace();
			System.out.println("Update failed");
		}
		finally {
			session.close();
		}
		return result;
	}

	public static boolean isUploaded(String fileName, String fileHash) {
		boolean found = false;
		Session session = getSession().openSession();
		Query query = null;
		try {
			String SQL = "FROM FileStorage f WHERE f.fileName = :fileName AND f.fileHash = :fileHash AND f.isActive = 1";
			query = session.createQuery(SQL);
			query.setParameter("fileName", fileName);
			query.setParameter("fileHash", fileHash);
			@SuppressWarnings("unchecked")
			List<FileStorage> l = query.list();
			if (!l.isEmpty())
				found = true;
		} catch (HibernateException ex) {
			found = false;
			System.out.println("Error ");
			ex.printStackTrace();
		}
		finally {
			session.close();
		}
		return found;
	}

	public static void deleteFileByFileName(String fileName) {
            
		Session session = getSession().openSession();
		Query query = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String SQL = "UPDATE FileStorage f SET f.isActive = 0 WHERE f.fileName = :fileName AND f.isActive = 1 ";
			query = session.createQuery(SQL);
			query.setParameter("fileName", fileName);
			query.executeUpdate();
                        System.out.println("Done");
			tx.commit();
		} catch (HibernateException ex) {

			System.out.println("Error ");
			ex.printStackTrace();
		}
		finally {
			session.close();
		}

	}

	public static List<FileStorage> getFileByFileName(String fileName) {
		boolean found = false;
		Session session = getSession().openSession();
		Query query = null;
		List<FileStorage>lst = null;
		try {
			String SQL = "FROM FileStorage f WHERE f.fileName = :fileName AND f.isActive = 1";
			query = session.createQuery(SQL);
			query.setParameter("fileName", fileName);

			lst = query.list();
			System.out.println("Khong tim thay ten file");
			System.out.println("Retrieve file version by name");
		} catch (HibernateException ex) {

			System.out.println("Error ");
			ex.printStackTrace();
		}
		finally {
			session.close();
		}
		return lst;
	}
	public static List<FileStorage> getFileDelete(int userId) {
		boolean found = false;
		Session session = getSession().openSession();
		Query query = null;
		List<FileStorage>lst = null;
		try {
			String SQL = "FROM FileStorage f WHERE f.isActive = 0 and f.userId = :userId";
			query = session.createQuery(SQL);
			query.setParameter("userId", userId);
			lst = query.list();
			System.out.println("Retrieve file delete ");
		} catch (HibernateException ex) {

			System.out.println("Error ");
			ex.printStackTrace();
		}
		finally {
			session.close();
		}
		return lst;
	}
        public static void main(String [] args)
        {
            deleteFileByFileName("Image");
        }
}
