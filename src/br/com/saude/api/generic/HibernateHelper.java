package br.com.saude.api.generic;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Persistence;

public class HibernateHelper {
	
	private static SessionFactory sessionFactory;
	private static boolean busy;
	
	private static SessionFactory getSessionFactory() {
		if(sessionFactory == null)
			try {
				sessionFactory = (SessionFactory)Persistence.createEntityManagerFactory("hibernate");
			}catch (Throwable ex) {
				throw new ExceptionInInitializerError(ex);
			}
		return sessionFactory;
	}
	
	public static Session getSession() {
		while( busy ) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		busy = true;
		return getSessionFactory().openSession();
	}
	
	public static void close(Session session) {
		session.close();
		busy = false;
	}
	
}
