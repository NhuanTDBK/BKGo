package model;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class VersionDAO {

    public static SessionFactory getSession() {
    	return DBUtils.getSessionFactory();
    }

    public static List<Version> getAll() {
        Session session = getSession().openSession();
        List<Version> version = session.createQuery("FROM Version").list();
        session.close();
        System.out.println("Retrieve successful");
        return version;
    }

    public static Version getById(int fileId) {
        Session session = getSession().openSession();
        Version version = null;
        try {
            version = (Version) session.get(Version.class, fileId);
        } catch (HibernateException ex) {
            return version;
        } finally {
            session.close();
        }
        return null;
    }

    public static boolean addVersion(Version version) {
        boolean result = true;
        Session session = getSession().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(version);
            tx.commit();
            System.out.println("Insert new version successful");
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            result = false;
            ex.printStackTrace();
            System.out.println("Insert failed");
        } finally {
            session.close();
        }
        return result;
    }

    public static List<Version> getVersionByName(String name) {
        List<Version> lstVersion = null;
        Session session = getSession().openSession();
        try {
            Query query = session.createQuery("FROM Version v WHERE v.fileName =:fileName");
            query.setParameter("fileName", name);
            lstVersion = query.list();
            return lstVersion;
        } catch (HibernateException ex) {
            return null;
        }
    }

    public static boolean deleteVersion(int fileId) {
        boolean result = true;
        Session session = getSession().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Version version = (Version) session.get(Version.class, fileId);
            session.delete(version);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            result = false;
            ex.printStackTrace();
            System.out.println("Update failed");
        } finally {
            session.close();
        }
        return result;
    }
}
