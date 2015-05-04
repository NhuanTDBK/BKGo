package model;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class UserDAO {
	public static SessionFactory getSession()
	{
		return DBUtils.getSessionFactory();
	}
	public static List<User>getAll()
	{
		Session session = getSession().openSession();
		List<User> files = session.createQuery("FROM User").list();
		session.close();
		System.out.println("Retrieve successful");
		return files;
	}
	public static boolean addUser(User user)
	{
		boolean result = true;
		Session session = getSession().openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
			System.out.println("Insert successful");
		}
		catch(HibernateException ex)
		{
			if(tx!=null)
			tx.rollback();
			result = false;
			ex.printStackTrace();
			System.out.println("Insert failed");
		}
		finally{
			session.close();
		}
		return result;
	}
	public static boolean updateUser(int fileId,User user)
	{
		boolean result=true;
		Session session = getSession().openSession();
		Transaction tx = null;
		try
		{
			tx=session.beginTransaction();
			session.update(user);
			tx.commit();
		}
		catch(HibernateException ex)
		{
			if(tx!=null)
			tx.rollback();
			result = false;
			ex.printStackTrace();
			System.out.println("Update failed");
		}
		return result;
	}
	public static boolean deleteUser(int fileId)
	{
		boolean result=true;
		Session session = getSession().openSession();
		Transaction tx = null;
		try
		{
			tx=session.beginTransaction();
			User user = (User)session.get(User.class, fileId);
			session.delete(user);
			tx.commit();
		}
		catch(HibernateException ex)
		{
			if(tx!=null)
			tx.rollback();
			result = false;
			ex.printStackTrace();
			System.out.println("Update failed");
		}
		return result;
	}
	public static boolean checkUserId(int userId) {
		// TODO Auto-generated method stub
		Session session = getSession().openSession();
		User user = (User)session.get(User.class, userId);
		boolean result = false;
		if(user!=null)
		{
			result = true;
		}
		session.close();
		return true;
	}
	public static User getUserByName(String userName)
	{
		Session session = getSession().openSession();
		User user = null;
		org.hibernate.Query query = session.createQuery("FROM User f WHERE f.userName=:userName");
		query.setParameter("userName",userName);
		try{
			List lst = query.list();
			if(!lst.isEmpty())
			{
				user = (User)lst.get(0);
			}
			else 
			{
				System.out.println("Khong tim thay ten user");
			}
		}catch(HibernateException e)
		{
			System.out.println("Khong tim thay ten user");
			e.printStackTrace();
		}
		return user;
	}
	public static User getUserByName(String userName,String password)
	{
		Session session = getSession().openSession();
		User user = null;
		org.hibernate.Query query = session.createQuery("FROM User f WHERE f.userName=:userName and f.password=:password");
		query.setParameter("userName",userName);
		query.setParameter("password", password);
		try{
			List lst = query.list();
			if(!lst.isEmpty())
			{
				user = (User)lst.get(0);
			}
			else 
			{
				System.out.println("Khong tim thay ten user");
			}
		System.out.println("Retrieve user by name");
		}catch(HibernateException e)
		{
			System.out.println("Khong tim thay ten user");
			e.printStackTrace();
		}
		return user;
	}
}
