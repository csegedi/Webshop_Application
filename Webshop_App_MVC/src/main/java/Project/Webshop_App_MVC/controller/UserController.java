package Project.Webshop_App_MVC.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Project.Webshop_App_MVC.database.Database;
import Project.Webshop_App_MVC.model.Product;
import Project.Webshop_App_MVC.model.Product_Category;
import Project.Webshop_App_MVC.model.Rating;
import Project.Webshop_App_MVC.model.Role;
import Project.Webshop_App_MVC.model.User;

@Controller
public class UserController {

	private ArrayList<Product> cart = new ArrayList<Product>();

	@PostMapping("/signIn")
	public String createAccount() {

		return "signIn.html";
	}

	@PostMapping("/signIn/result")
	public String registration(Model model,
			@RequestParam(name = "username") String username,
			@RequestParam(name = "password") String password) {

		Database db = new Database();
		User user = new User();
		String returnPage = null;

		/** CHECK THE USED USERNAMES */

		List<User> users = db.getAllUsersByUsername(username);

		if (users.isEmpty() == false) {
			returnPage = "usedUsername.html";
		}

		else {

			/** CHECK THE PASSWORD'S STRENG (UPPERCASE AND NUMBER CONTAINING) */

			int upperCaseCounter = 0;
			int numberCharacterCounter = 0;

			for (int passwordIndex = 0; passwordIndex < password.length(); passwordIndex++) {
				char a = password.charAt(passwordIndex);

				if (Character.isUpperCase(a)) {
					upperCaseCounter++;
				}
			}

			if (password.matches(".*\\d.*")) {
				numberCharacterCounter++;
			}

			if ((upperCaseCounter > 0) && numberCharacterCounter > 0) {

				user.setUsername(username);
				user.setPassword(password);
				user.setRole(Role.CUSTOMER);
				String roleToString = Role.CUSTOMER.toString();
				
				db.insertUser(username, password, roleToString);
				
				
				returnPage = "completedRegistration.html";
			}

			else {

				returnPage = "failedRegistration.html";
			}
		}

		db.close();

		return returnPage;

	}

	@PostMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {

		return "login.html";

	}

	@PostMapping("/shop/categories")
	public String categories(Model model, 
			@RequestParam(required = false, name = "username") String username,
			@RequestParam(required = false, name = "password") String password,
			@CookieValue(required = false, name = "cookie_Username") String cookieName,
			@CookieValue(required = false, name = "cookie_Password") String cookiePassword,
			HttpServletResponse response, HttpServletRequest request) {

		Database db = new Database();

		String returnPage = null;

		List<User> customers = db.getAllUser();

		List<Product_Category> categorie_list = db.getAllCategory();

		/** CHECK THE USER AFTER THE LOGIN */                    

		if ((username != null) && (password != null)) {           

			for (int customersIndex = 0; customersIndex < customers.size(); customersIndex++) {
				User current = customers.get(customersIndex);
				if ((current.getUsername().equals(username)) && (current.getPassword().equals(password))
						&& (current.getRole().equals(Role.CUSTOMER))) {

					cookieName = current.getUsername();                  //got the RequestParams by the first entry
					cookiePassword = current.getPassword();
					Cookie cookie = new Cookie("cookie_Username", cookieName);
					Cookie cookie2 = new Cookie("cookie_Password", cookiePassword);

					response.addCookie(cookie);
					response.addCookie(cookie2);
					
					if (current.getProductRatings().isEmpty()) {
						List<Product> products=db.getAllProducts(); 
						
						for (int i=0; i<products.size(); i++) {
							Product product=products.get(i);
							current.getProductRatings().add(product);
							db.updateUser(current); 
						}
						
					}

					model.addAttribute("categories", categorie_list);
					returnPage = "categories.html";

					break;

				} else {

					returnPage = "failedLogin.html";
				}
			}

		}

		/** CHECK THE USER AFTER RETURN */

		else if (cookieName != null && cookiePassword != null) {           //RequestParam don't exist, CookieValues are used
			
			for (int customersIndex = 0; customersIndex < customers.size(); customersIndex++) {
				User current = customers.get(customersIndex);
				if (current.getRole().equals(Role.ADMIN)) {
					
					returnPage = "failedLogin.html";
				}
					
				else {
					model.addAttribute("categories", categorie_list);
					returnPage = "categories.html"; 
				}

			}
		}

		db.close();

		return returnPage;
	}

	@PostMapping("/shop/categories/products")
	public String shop(Model model, 
			@CookieValue(name = "cookie_Username") String username,
			@CookieValue(name = "cookie_Password") String password,
			@RequestParam(required = false, name = "categorie_var") Integer category_id,
			@CookieValue(required = false, name = "categorie_var_cookie") String category_id_cookie,
			HttpServletResponse response) {

		Database db = new Database();
		Product_Category category = null;
		ArrayList<Product>products=new ArrayList<Product>(); 
		
		if (category_id != null) {            //got the RequestParam by the first entry

			category = db.getCategoryById(category_id);
			 
			
			HashSet<Product> deleteDuplicate=new HashSet<Product>(category.getProducts()); 
			products.addAll(deleteDuplicate); 
		
			String categoryString = String.valueOf(category_id);     
			Cookie cookie = new Cookie("categorie_var_cookie", categoryString);   
			response.addCookie(cookie);
		
		}

		else {                                            //RequestParam don't exist, CookieValue is used
			
			int formatted_id = Integer.valueOf(category_id_cookie);
			category = db.getCategoryById(formatted_id);
			HashSet<Product> deleteDuplicate=new HashSet<Product>(category.getProducts()); 
			products.addAll(deleteDuplicate); 
			
		}

		model.addAttribute("productsList", products);
		
		
		db.close(); 

		return "shop.html";
	}

	@PostMapping("/shop/categories/products/cart")
	public String cart(Model model, 
			@RequestParam(required = false, name = "selected_quantity") Integer quantity,
			@RequestParam(required = false, name = "selected_product")  Integer selectedProductId,
			HttpServletResponse response, HttpServletRequest request) {

		Database db = new Database();
		String message = null;
		Product product=null; 
		double pay=0;
		
		if (selectedProductId!=null) {
			
			product = db.getProductById(selectedProductId);
			
																	//out of the limit of the storage
		if ((quantity!=null) && (quantity > product.getQuantity())) {               
			message = "You have added more products to your cart, what we own in our storage at the moment!"
					+ "\nPlease check the quantity of the selected product!";
		} 
		
		/** ADD PRODUCT TO THE CART*/
		
		else {

			int assistant = 0;

			for (int cartIndex = 0; cartIndex < cart.size(); cartIndex++) {    //checking: is the product in the cart?
				if (cart.get(cartIndex).getName().equals(product.getName())) {
					assistant++;
				}
			}
			
			if (assistant == 0) {                              //if the selected product isn't in the cart (new product)/add the product to cart with the incoming quantity
				product.setActualCartQuantity(quantity);
				if (product.getActualCartQuantity()!=0) {
					cart.add(product);  
					
				}
				

			} 
			
			else {                                            //if the selected product is already in the cart 

				for (int cartIndex = 0; cartIndex < cart.size(); cartIndex++) {
					
					if (cart.get(cartIndex).getName().equals(product.getName())) {
						cart.get(cartIndex).cartIncrease(quantity);
					}
				}

			}
			
			message = "Your cart: ";
		}
		
		}
		
		/**CALCULATION OF TOTAL PRICE  */
		
		for (int cartIndex=0; cartIndex<cart.size(); cartIndex++) {
				
			Product current=cart.get(cartIndex);
			double assistant=current.getPrice()*current.getActualCartQuantity(); 
				pay+=assistant;  
			}
		
		model.addAttribute("cart", cart);
		model.addAttribute("message", message);
		model.addAttribute("pay", pay); 
		
		db.close(); 

		return "cart.html";

	}
	
	@PostMapping ("/shop/categories/products/cart/purchase")
	public String purchasing (Model model) {
		
		Database db=new Database(); 
		String returnPage=null; 
		boolean buyOverFlow=false; 
		ArrayList<Product>toMuchProducts=new ArrayList<Product>(); 
		

		for (int i=0; i<cart.size(); i++) {
			Product current=cart.get(i); 
			if (current.getActualCartQuantity()>current.getQuantity()) {
				buyOverFlow=true;
				toMuchProducts.add(current);
			}
		}
		
		if(buyOverFlow==true) {
			model.addAttribute("overflow", toMuchProducts); 
			for (int i=0; i<toMuchProducts.size(); i++) {
				Product current=toMuchProducts.get(i); 
				for (int y=0; y<cart.size(); y++) {
					if(current.getName().equals(cart.get(y).getName())) {
						cart.remove(cart.get(y));
					}
				}
			}
			
			returnPage="toMuchProduct.html"; 
		}
		
		else if (cart.isEmpty()) {
			
			returnPage="emptyCart.html"; 
		}
		
		else {
			
		model.addAttribute("cart", cart);
		
		for (int cartIndex=0; cartIndex<cart.size(); cartIndex++) {
			
			Product current=cart.get(cartIndex); 
			int newQuantity=current.getQuantity()-current.getActualCartQuantity();
			//current.quantityDecrease();                                 
			
			db.UpdateQuantity(current.getId(), newQuantity); 	//decrease from the storage
		}
		
		for (int cartIndex=0; cartIndex<cart.size(); cartIndex++) {   //delete from the cart
			Product current=cart.get(cartIndex);
			current.setActualCartQuantity(0); 
		
		}
		
		cart.clear(); 
		
		returnPage="buy.html"; 
		
		}
		
		db.close(); 
		
		return returnPage;
		
	}
	
	@PostMapping ("/shop/categories/products/cart/delete")
	public String deleteFromTheCart(Model model,
			@RequestParam(required = false, name = "selected_quantity") Integer quantity,
			@RequestParam(required = false, name = "selected_product")  Integer selectedProductId) {
		
		String returnPage=null; 
		
		Database db=new Database(); 
		
		/** REMOVE PRODUTS FROM THE CART*/
		
		if (selectedProductId!=null && quantity==null) {
			
			Product product=db.getProductById(selectedProductId);
			
			for (int cartIndex=0; cartIndex<cart.size(); cartIndex++) {
			
				if (cart.get(cartIndex).getName().equals(product.getName())){
				cart.remove(cartIndex); 
				}
			}
			
			returnPage="deleteFromTheCart.html"; 
			
		}
		
		/** DECREASE THE QUANTITY OF THE SELECTED PRODUCT IN THE CART*/
		
		else if (quantity!=null && selectedProductId!=null) {
			
			Product product=db.getProductById(selectedProductId);
			
			for (int cartIndex=0; cartIndex<cart.size(); cartIndex++) {
				
				if (cart.get(cartIndex).getName().equals(product.getName())){
				cart.get(cartIndex).cartDecrease(quantity); 
				if (cart.get(cartIndex).getActualCartQuantity()<0) {    //in case the customer delete more products 
					cart.remove(cartIndex); 
				}
			}
		}
			
			returnPage="deleteFromTheCart.html"; 
			
	}
		
		else  {
			
			returnPage="emptyCart.html"; 
		}
		
		return returnPage;
	}
	
	@PostMapping("/shop/categories/ratings")
	public String selectProductToRating(Model model, 	
			@CookieValue(name = "cookie_Username") String username,
			@CookieValue(name = "cookie_Password") String password) {
		
		Database db=new Database(); 
		List<User> users=db.getUsersByUsernamePassword(username, password);
		User currentUser=users.get(0); 
		List<Product> listOfTheProductWithoutRate=currentUser.getProductRatings(); 
	 
		
		model.addAttribute("products", listOfTheProductWithoutRate);

		return "ratingPage.html";
	}
	
	@PostMapping("shop/categories/ratings/add")
	public String addRating (Model model,
			@RequestParam(required = false, name="selectedProduct") Integer productId,
			@RequestParam (required = false, name="givenRating") Integer givenRating,
			@CookieValue(name = "cookie_Username") String username,
			@CookieValue(name = "cookie_Password") String password) {
		
		Database db=new Database();
		Product currentProduct=db.getProductById(productId); 
		List<Rating>listOfTheRatingsOfTheCurrentProduct=currentProduct.getRatings(); 

		List<User> users=db.getUsersByUsernamePassword(username, password); 
		User user=users.get(0); 
		List<Product>userProductsToRate=user.getProductRatings();   
		String returnPage=null; 
		

		if (givenRating!=null) {
			Rating rating=new Rating();
			rating.setGrade(givenRating); 
			for (int i=0; i<userProductsToRate.size(); i++) {
				Product currentProductByTheUser=userProductsToRate.get(i); 
				if (currentProductByTheUser.getName().equals(currentProduct.getName())) {
					userProductsToRate.remove(currentProductByTheUser); 
					listOfTheRatingsOfTheCurrentProduct.add(rating); 
					break; 
				}
				
			}
			currentProduct.setAverageRating();	
			db.updateProduct(currentProduct); 
			db.updateUser(user); 
			
			for (int i=0; i<currentProduct.getRatings().size(); i++) {
				System.out.println (currentProduct.getRatings().get(i).getGrade()); 
			}
			
			returnPage="ratingAdded.html"; 
			
		}
		else {
			
			returnPage="emptyRatingValue.html"; 
			
		}

		return returnPage; 
	}
	


	@GetMapping("/shop/categories/products/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
	
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(0);
				response.addCookie(cookies[i]);
			}
		}
		
		cart.clear(); 

		String page = "redirect:/";

		return page;
	}
}
