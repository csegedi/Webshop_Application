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
import Project.Webshop_App_MVC.model.Rating;
import Project.Webshop_App_MVC.model.User;

public class Database {
	
	private SessionFactory sessionFactory;

	public Database() {

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		
	}
	
	// 1. USER OPERATIONS
	
	//INSERT USER INTO THE DATABASE AFTER SUCCESSFULLY REGISTRATION
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
	
	//QUERY USERS BY USERNAME
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
	
	//QUERY USERS BY USERNAME AND PASSWORD
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
	
	//QUERY ALL USERS 
	public List<User> getAllUser() {
		
		List<User>userList=null; 
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("SELECT * FROM users", User.class);
		
		userList=query.getResultList(); 
		
		
		session.getTransaction().commit();; 
		session.close(); 
		
		return userList;
		
	}
	
	//2. ADMINS OPERATIONS: INSERT NEW ELEMENTS, MODIFICATIONS

	// INSERT NEW CATEGORY TO THE DATABASE
	public void insertCategory(String category) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO categories (name) VALUES (:newCategoryName)"); 
		query.setParameter("newCategoryName", category); 
		
		query.executeUpdate(); 
		
		session.getTransaction().commit();; 
		session.close(); 

	}
	
	//INSERT NEW PRODUCT TO THE DATABASE
	public void insertNewProduct(String name, double d, String ingredients, int quantity, double init) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO products (name, price, ingredients, quantity, averageRating) VALUES (:newName, :newPrice, :newIngredients, :newQuantity, :init )"); 
		query.setParameter("newName", name); 
		query.setParameter("newPrice", d); 
		query.setParameter("newIngredients", ingredients); 
		query.setParameter("newQuantity", quantity); 
		query.setParameter("init", init); 
		
		query.executeUpdate(); 
		
		session.getTransaction().commit();; 
		session.close(); 
		
	}
	
	//UPDATE THE PRICE OF A CURRENT PRODUCT BY ADMIN
	public void UpdatePrice(int id, Double price ) {
		 
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("UPDATE products SET price=:newPrice " + "WHERE id=:incomingId");
		
		query.setParameter("incomingId", id); 
		query.setParameter("newPrice", price);
	
		query.executeUpdate();

		session.getTransaction().commit();
		session.close();
		
	}
	
	//UPDATE THE QUANTITY OF THE CURRENT PRODUCT IN THE STORAGE BY ADMIN
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
	
	//REFRESH CATEGORY-PRODUCT JOINTABLE
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
	
	//REFRESH THE USER PRODUCT RATING MAPPING IF THE NEW PRODUCT INSERTED AFTER THE REGISTRATION OF THE USER
	public void refreshUserRatingMapping(int currentUserId, int productId) {
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO user_products_ratingtable (user_id, product_id) VALUES (:newUser_Id, :newProduct_Id)"); 
		query.setParameter("newUser_Id", currentUserId);
		query.setParameter("newProduct_Id", productId); 
		
		query.executeUpdate(); 
		
		session.getTransaction().commit();; 
		session.close();
		
	}
	
	
	//3. PRODUCTS QUERY OPERATIONS
	
	//QUERY ALL PRODUCTS
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
	
	//QUERY PRODUCTS BY NAME
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
	 
	 //QUERY PRODUCTS BY ID
	 public Product getProductById(int id) {

		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
			
		Product product=session.get(Product.class, id); 
			 
		session.getTransaction().commit();; 
		session.close(); 
		
		return product;
		
	 }
		

	//QUERY PRODUCTS BY ITS CATEGORY
	public List<Product> getProductsFromCategory(Integer category_id) {
		
		
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		List<Product>products=null; 
		
		Query query=session.createNativeQuery("SELECT * FROM categories_products_mapping WHERE categorie_id=:incoming ", Product.class);
		query.setParameter("incoming", category_id); 
		 
		products=query.getResultList(); 
		
		session.getTransaction().commit();; 
		session.close(); 
	
		return products;
		
	}

	//4. CATEGORY QUERY OPERATIONS
	 
	//QUERY ALL CATEGORIES
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
	
	//QUERY CATEGORY BY ID
	public Product_Category getCategoryById(int id) {

		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Product_Category pc=session.get(Product_Category.class, id); 
		 
		session.getTransaction().commit();; 
		session.close(); 
	
		return pc;
	
	}

	//5. UPDATE AFTER BUYING
	
	//UPDATE THE USER
	public void updateUser(User user) {
			
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
			
		session.update(user);
			
		session.getTransaction().commit();
		session.close();
		
	}
	
	//UPDATE THE PRODUCT
	public void updateProduct(Product currentProduct) {
			
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
			
		session.update(currentProduct);
			
		session.getTransaction().commit();
		session.close();
		
	}	

	//6. RATING OPERATIONS
	
	//INSERT NEW RATING
	public void saveRating(int id, Integer givenRating) {
	
		Session session=sessionFactory.openSession(); 
		session.beginTransaction(); 
		
		Query query=session.createNativeQuery("INSERT INTO products_ratings_mapping (product_id, rating_id) VALUES (:newProduct_Id, :newRating)"); 
		
		query.setParameter("newProduct_Id", id); 
		query.setParameter("newRating", givenRating);
		
		query.executeUpdate(); 
		
		session.getTransaction().commit();; 
		session.close();
		
		
	}

	public void close() {
		
		sessionFactory.close();
	}

}
