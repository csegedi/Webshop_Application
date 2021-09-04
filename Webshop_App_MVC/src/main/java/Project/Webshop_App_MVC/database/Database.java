package Project.Webshop_App_MVC.database;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import Project.Webshop_App_MVC.model.User;

public class Database {
	
	private SessionFactory sessionFactory;

	public Database() {

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		
	}
	
	
	public void insertUser(String username, String password, String role) {
	
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO users (username, password, role) VALUES (:newUsername, :newPassword, :newRole)"); 
		query.setParameter("newUsername", username); 
		query.setParameter("newPassword", password); 
		query.setParameter("newRole", role);
		
		query.executeUpdate(); 
		
		session.getTransaction().commit(); 
		session.close(); 
	}
	
	public List<User> getAllUsersByUsername(String username) {
		
		List<User>userList=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("SELECT * FROM users WHERE username=:newUsername");
		query.setParameter("newUsername", username); 
		userList=query.getResultList(); 
		
		session.getTransaction().commit();; 
		session.close(); 
		
		return userList;
		
	}
	
	public void close() {
		
		sessionFactory.close();
	}

	
	

}
