package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.type.StandardBasicTypes;

import controller.NotificationController;

public class FileChangeDAO extends Observable {
	public FileChangeDAO() {
		addObserver(new NotificationController());
	}

	public static SessionFactory getSession() {
		return DBUtils.getSessionFactory();
	}

	public static List<FileChange> getUpperByTransactionId(String transactionId) {
		List<FileChange> lstFileChange = new ArrayList<FileChange>();
		Session session = getSession().openSession();
		Query query = session
				.createQuery("FROM FileChange f WHERE f.tid > :tid");
		query.setParameter("tid", transactionId);
		try {
			lstFileChange = query.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Retrieve not successful");
		}
		System.out.println("Retrieve successful");
		session.close();
		return lstFileChange;
	}

	public static int maxTransactionId() {
		int maxId = 0;
		Session session = getSession().openSession();
		Transaction tx = null;
		Integer maxTID = new Integer(0);
		try {
			Criteria criteria = session.createCriteria(FileChange.class)
					.setProjection(Projections.max("tid"));
			try {
				maxTID = Integer.parseInt((String) criteria.uniqueResult());
				maxId = maxTID.intValue();
			} catch (NumberFormatException ex) {
				maxId = 1;
			}
		} catch (HibernateException ex) {
			ex.printStackTrace();

		} finally {
			session.close();
		}
		return maxId;
	}

	public static List getCursor() {
		List<Object[]> list = null;
		Session session = getSession().openSession();
		try {
			Query query = session
					.createSQLQuery(
							"select TID as tid, MAX(FILE_CHANGE_ID) as fileChangeId from FILE_CHANGE group by TID having TID >= (select MAX(TID) from FILE_CHANGE)")
							.addScalar("tid").addScalar("fileChangeId");

			list = query.list();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		List lst = new ArrayList();
		if (list.size() != 0)
			for (Object[] rows : list) {
				lst.add(rows[0]);
				lst.add(rows[1]);
			}
		return lst;
	}

	public static ConcurrentHashMap<String, FileCursor> getAllCursor() {
		List<Object[]> list = null;
		ConcurrentHashMap<String, FileCursor> result = new ConcurrentHashMap<String, FileCursor>();
		Session session = getSession().openSession();
		try {
			String queryStr = "select USER_ID as userId, TID as tid, MAX(FILE_CHANGE_ID) as fileChangeId, timestamp from FILE_CHANGE group by USER_ID, TID having fileChangeId >= (select MAX(FILE_CHANGE_ID) from FILE_CHANGE group by USER_ID)";
			Query query = session
					.createSQLQuery(queryStr)
					.addScalar("userId").addScalar("tid").addScalar("fileChangeId");

			list = query.list();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		List lst = new ArrayList();
		if (list.size() != 0)
			for (Object[] rows : list) {
				String userId = rows[0].toString();
				String tidStr = rows[1].toString();
				String indexStr = rows[2].toString();
				result.put(userId, new FileCursor(tidStr, indexStr));
			}
		else

		{
			result.put("1", new FileCursor("0","0"));
		}
		return result;
	}

	public static boolean insertFile(FileChange file) {
		boolean result = true;
		Session session = getSession().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(file);
			tx.commit();
			System.out.println("Insert file change successful");


		} catch (HibernateException ex) {

			tx.rollback();
			result = false;
			ex.printStackTrace();
			System.out.println("Insert failed");
		} finally {
			session.close();
		}
		return result;
	}

	public boolean insertFile(FileChange file, boolean broadcast) {
		boolean result = true;
		Session session = getSession().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(file);
			tx.commit();
			System.out.println("Insert successful");
			System.out.println("Put this update to queue");
			setChanged();
			notifyObservers(file);
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

	/*
	 * Tra ve vector timestamp thay doi tren he thong List(0) : Transaction Id
	 * List(1) : Tong so cap nhat
	 */
	public static List getVectorUpdate() {
		List list = null;
		Session session = getSession().openSession();
		try {
			Query query = session
					.createQuery("SELECT f.tid, COUNT(f.fileChangeId) FROM FileChange f GROUP BY TID");
			list = query.list();
			if (list.size() == 0)
				return null;
		} catch (HibernateException ex) {

		} finally {
			session.close();
		}
		return list;
	}

	public static List getVectorUpdate(int tid) {
		List list = null;
		Session session = getSession().openSession();
		Query query = session
				.createQuery("SELECT f.tid, COUNT(f.fileChangeId) FROM FileChange f GROUP BY f.tid");
		// query.setParameter("tid",tid);
		list = query.list();
		if (list.size() == 0)
			return null;
		session.close();
		return list;
	}

	public static List<FileChange> getUpperByTransactionId(
			int transactionId, int index) {
		List<FileChange> lstFileChange = new ArrayList<FileChange>();
		Session session = getSession().openSession();
		session.beginTransaction();
		Query query = session
				.createQuery("FROM FileChange f WHERE f.tid >= :tid and f.fileChangeId > :index");
		query.setParameter("tid", transactionId);
		query.setParameter("index", index);
		try {
			lstFileChange = query.list();
			if(!lstFileChange.isEmpty())
				return lstFileChange;
			else return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Retrieve not successful");
		}
		System.out.println("Retrieve successful");
		session.close();
		return lstFileChange;
	}

	//	public static void main(String[] args) {
	//		@SuppressWarnings("rawtypes")
	//		List<FileChange> lst = getUpperByTransactionId(5, 24);
	//		System.out.println(lst==null?"khong co du lieu":"co du lieu");
	//	}

	public static void insertList(List<FileChange> fileChangeLst) {
		// TODO Auto-generated method stub
		Session session = getSession().openSession();
		Transaction tx = session.beginTransaction();
		try {
			for (FileChange fileChange : fileChangeLst) {
				session.save(fileChange);
			}
		} catch (HibernateException ex) {
			tx.rollback();
		} finally {
			tx.commit();
			session.close();
		}
	}
	@SuppressWarnings("unchecked")
	public static List<FileChange> getUpdate()
	{
		List<Object[]> list = null;
		List<FileChange> lstFileChange = new ArrayList<FileChange>();
		Session session = getSession().openSession();
		try {
			String queryStr = "select FILE_CHANGE_ID as fci, TID as tid, max(TYPE) as type, FILE_ID as fileId, TIMESTAMP as timestamp, IS_FILE as isFile, FILE_NAME as fileName "
					+ "from FILE_CHANGE WHERE FILE_ID not in (select FILE_ID from FILE_CHANGE WHERE TYPE = 0) group by FILE_NAME";
			Query query = session.createSQLQuery(queryStr)
					.addScalar("fci", StandardBasicTypes.INTEGER)
					.addScalar("tid", StandardBasicTypes.INTEGER)
					.addScalar("type", StandardBasicTypes.INTEGER)
					.addScalar("timestamp", StandardBasicTypes.STRING)
					.addScalar("isFile", StandardBasicTypes.INTEGER)
					.addScalar("fileName", StandardBasicTypes.STRING)
					;
			list = query.list();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		List lst = new ArrayList();
		FileChange file = null;
		if (list.size() != 0)
			for (Object[] rows : list) {
				file = new FileChange();
				file.setFileChangeId((int)rows[0]);
				file.setTid((int)rows[1]);
				file.setType((int)rows[2]);
				Date date = new Date((String)rows[3]);
				file.setTimestamp(date);
				file.setIsFile((int)rows[4]);
				file.setFileName((String)rows[5]);
			}
		return lstFileChange;
	}
	public static List<FileChange> getUpdate(int tid, int index)
	{
		List<Object[]> list = null;
		List<FileChange> lstFileChange = new ArrayList<FileChange>();
		Session session = getSession().openSession();
		try {
			String queryStr = "select FILE_CHANGE_ID as fci, TID as tid, max(TYPE) as type, FILE_ID as fileId, TIMESTAMP as timestamp, IS_FILE as isFile, FILE_NAME as fileName "
					+ "from FILE_CHANGE WHERE TID > :tid and FILE_CHANGE_ID > :index and FILE_ID not in (select FILE_ID from FILE_CHANGE WHERE TYPE = 0 AND TID > :tid and FILE_CHANGE_ID > :index) group by FILE_NAME";
			Query query = session.createSQLQuery(queryStr)
					.addScalar("fci", StandardBasicTypes.INTEGER)
					.addScalar("tid", StandardBasicTypes.INTEGER)
					.addScalar("type", StandardBasicTypes.INTEGER)
					.addScalar("fileId",StandardBasicTypes.INTEGER)
					.addScalar("timestamp", StandardBasicTypes.DATE)
					.addScalar("isFile", StandardBasicTypes.INTEGER)
					.addScalar("fileName", StandardBasicTypes.STRING)
					.setParameter("tid", tid)
					.setParameter("index", index);
			list = query.list();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		FileChange file = null;
		if (list.size() != 0)
			for (Object[] rows : list) {
				file = new FileChange();
				file.setFileChangeId(Integer.parseInt(rows[0].toString()));
				file.setTid(Integer.parseInt(rows[1].toString()));
				file.setType(Integer.parseInt(rows[2].toString()));
				file.setFileId(Integer.parseInt(rows[3].toString()));
				java.sql.Date date = (java.sql.Date)rows[4];
				Date d = new Date(date.getTime());
				file.setTimestamp(date);
				file.setIsFile((int)rows[5]);
				file.setFileName((String)rows[6]);
				lstFileChange.add(file);
			}
		return lstFileChange;
	}
	public static void main(String [] args)
	{
		getUpdate(1,1);
	}
}
