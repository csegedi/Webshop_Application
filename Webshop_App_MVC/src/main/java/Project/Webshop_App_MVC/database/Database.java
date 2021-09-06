package Project.Webshop_App_MVC.database;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import Project.Webshop_App_MVC.model.Product;
import Project.Webshop_App_MVC.model.Product_Category;
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
	
	public List<User> getUsersByUsernamePassword(String username, String password) {
		
		List<User>userList=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("SELECT * FROM users WHERE username=:newUsername AND password=:newPassword", User.class);
		query.setParameter("newUsername", username); 
		query.setParameter("newPassword", password); 
		userList=query.getResultList(); 
		
		
		session.getTransaction().commit();; 
		session.close(); 
		
		return userList;
		
	}
	
	public void insertCategory(String category) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO categories (name) VALUES (:newCategoryName)"); 
		query.setParameter("newCategoryName", category); 
		
		query.executeUpdate(); 
		
		session.getTransaction().commit();; 
		session.close(); 

	}
	
	public void insertNewProduct(String name, int price, String ingredients, int quantity) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO products (name, price, ingredients, quantity) VALUES (:newName, :newPrice, :newIngredients, :newQuantity)"); 
		query.setParameter("newName", name); 
		query.setParameter("newPrice", price); 
		query.setParameter("newIngredients", ingredients); 
		query.setParameter("newQuantity", quantity); 
		
		query.executeUpdate(); 
		
		session.getTransaction().commit();; 
		session.close(); 
		
	}
	
	public List<Product> getAllProducts () {
			
		List<Product> list=null; 
	
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
			
		Query query=session.createNativeQuery("SELECT * FROM products", Product.class); 
		list=query.getResultList(); 
		
		session.getTransaction().commit();; 
		session.close();
		
		return list; 
				
	}
	
	
	 public List <Product> getProductByName(String name) {
		
		 List<Product> list=null; 
		 
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
			
		Query query=session.createNativeQuery("SELECT * FROM products WHERE name=:newName",Product.class); 
		query.setParameter("newName", name); 
		list=query.getResultList(); 
		
		session.getTransaction().commit();; 
		session.close();
		
		return list; 
		
	}
	 
	 public Product getProductById(int id) {

			Session session=sessionFactory.openSession(); 
			session.beginTransaction(); 
			
			Product product=session.get(Product.class, id); 
			 
			session.getTransaction().commit();; 
			session.close(); 
		
			return product;
		
		}
	
	 public void UpdatePrice(int id, Integer price ) {
		 
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("UPDATE products SET price=:newPrice " + "WHERE id=:incomingId");
		
		query.setParameter("incomingId", id); 
		query.setParameter("newPrice", price);
	
		query.executeUpdate();

		session.getTransaction().commit();
		session.close();
		
	}
	 
	 public void UpdateQuantity(int id, Integer quantity) {
		 
			Session session=sessionFactory.openSession(); 
			session.beginTransaction(); 
			
			Query query=session.createNativeQuery("UPDATE products SET quantity=:newQuantity " + "WHERE id=:incomingId");
			
			query.setParameter("incomingId", id); 
			query.setParameter("newQuantity", quantity);
		
			query.executeUpdate();

			session.getTransaction().commit();
			session.close();
			
		}
	 
	 
	 

	public List<Product_Category> getAllCategory() {

		List<Product_Category>categorieList=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("SELECT * FROM categories ", Product_Category.class);
		 
		categorieList=query.getResultList(); 
		
		session.getTransaction().commit();; 
		session.close(); 
	
		return categorieList;
	
	}
	
	
	public Product_Category getCategoryById(int id) {

		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Product_Category pc=session.get(Product_Category.class, id); 
		 
		session.getTransaction().commit();; 
		session.close(); 
	
		return pc;
	
	}
	
	public void refreshMapping(int category_id, int product_id) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO categories_products_mapping (categorie_id, product_id) VALUES (:newCatId, :newPId)"); 
		query.setParameter("newCatId", category_id); 
		query.setParameter("newPId", product_id); 
		
		query.executeUpdate(); 
		
		session.getTransaction().commit();; 
		session.close(); 
		
	}
	
	
	public void close() {
		
		sessionFactory.close();
	}

	


}
