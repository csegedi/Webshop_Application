package Project.Webshop_App_MVC.controller;

import java.util.ArrayList;
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
	public String registration(Model model, @RequestParam(name = "username") String username,
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
	public String categories(Model model, @RequestParam(required = false, name = "username") String username,
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

					cookieName = current.getUsername();
					cookiePassword = current.getPassword();
					Cookie cookie = new Cookie("cookie_Username", cookieName);
					Cookie cookie2 = new Cookie("cookie_Password", cookiePassword);

					response.addCookie(cookie);
					response.addCookie(cookie2);

					model.addAttribute("categories", categorie_list);
					returnPage = "categories.html";

					break;

				} else {

					returnPage = "failedLogin.html";
				}
			}

		}

		/** CHECK THE USER AFTER RETURN */

		else if (cookieName != null && cookiePassword != null) {
			
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
	public String shop(Model model, @CookieValue(name = "cookie_Username") String username,
			@CookieValue(name = "cookie_Password") String password,
			@RequestParam(required = false, name = "categorie_var") Integer category_id,
			@CookieValue(required = false, name = "categorie_var_cookie") String category_id_cookie,
			HttpServletResponse response) {

		Database db = new Database();
		Product_Category category = null;
		List<Product> products = null;
		if (category_id != null) {

			category = db.getCategoryById(category_id);
			products = category.getProducts();
			String categoryString = String.valueOf(category_id);
			Cookie cookie = new Cookie("categorie_var_cookie", categoryString);
			response.addCookie(cookie);
		}

		else {
			int formatted_id = Integer.valueOf(category_id_cookie);
			category = db.getCategoryById(formatted_id);
			products = category.getProducts();
		}

		model.addAttribute("productsList", products);

		return "shop.html";
	}

	@PostMapping("/shop/categories/products/cart")
	public String cart(Model model, 
			@RequestParam(required = false, name = "selected_quantity") int quantity,
			@RequestParam(required = false, name = "selected_product")  int selectedProductId,
			HttpServletResponse response, HttpServletRequest request) {

		Database db = new Database();
		String message = null;
		Product product = db.getProductById(selectedProductId);
		double pay=0;

		if (quantity > product.getQuantity()) {
			message = "You have added more products to your cart, what we own in our storage at the moment!"
					+ "\nPlease check the quantity of the selected product!";
		} else if (quantity < 0) {
			message = "Wrong input! Please check your entered data!";
		}

		else {

			String formatted_quantity = String.valueOf(quantity);
			Cookie cookie2 = new Cookie("cookie_quantity", formatted_quantity);
			response.addCookie(cookie2);

			int assistant = 0;

			for (int cartIndex = 0; cartIndex < cart.size(); cartIndex++) {
				if (cart.get(cartIndex).getName().equals(product.getName())) {
					assistant++;

				}
			}

			if (assistant == 0) {
				product.setActualCartQuantity(quantity); 
				cart.add(product);

			} else {

				for (int cartIndex = 0; cartIndex < cart.size(); cartIndex++) {
					if (cart.get(cartIndex).getName().equals(product.getName())) {
						cart.get(cartIndex).cartIncrease(quantity);
					}
				}

			}
			
			message = "Your cart: ";
		}
		
		
		for (int cartIndex=0; cartIndex<cart.size(); cartIndex++) {
				
				Product current=cart.get(cartIndex);
			double assistant=current.getPrice()*current.getActualCartQuantity(); 
				pay+=assistant;  
			}
		
	
		
		model.addAttribute("cart", cart);
		model.addAttribute("message", message);
		model.addAttribute("pay", pay); 

		return "cart.html";

	}

	@GetMapping("/shop/categories/products/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			System.out.println(cookies.length);

			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(0);
				response.addCookie(cookies[i]);
			}
		}

		String page = "redirect:/";

		return page;
	}
}
