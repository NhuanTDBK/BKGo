package model;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DBUtils {
	private static SessionFactory sessionFactory;
	static{
		try{
			System.out.println("Init Connection");
			Configuration cfg = new Configuration().configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
			.applySettings(cfg.getProperties());
			sessionFactory = cfg.buildSessionFactory(builder.build());
		}catch(Exception e){
			System.out.println("Error Connection" + e.getMessage());
		}

	}

	public static SessionFactory getSessionFactory(){

		return sessionFactory;

	}
}
