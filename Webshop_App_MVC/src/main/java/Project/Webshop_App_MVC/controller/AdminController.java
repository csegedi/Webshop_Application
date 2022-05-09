package Project.Webshop_App_MVC.controller;

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
import Project.Webshop_App_MVC.model.Role;
import Project.Webshop_App_MVC.model.User;

@Controller
public class AdminController {

	@GetMapping("/")
	public String index(HttpServletRequest request, HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(0);
				response.addCookie(cookies[i]);
			}
		}

		return "index.html";
	}
	
	@PostMapping("/admin")
	public String admin(Model model,
			@RequestParam(required = false, name = "username") String username,
			@RequestParam(required = false, name = "password") String password,
			@CookieValue(required = false, name = "role") String role, HttpServletResponse response) {

		Database db = new Database();
		List<Product_Category> categorieList = db.getAllCategory();
		List<Product> product_list = db.getAllProducts();
		User admin = null;

		String returnPage = null;
		
		/** CHECK THE USER IN CASE OF ENTRY*/
		
		if ( ( username != null) && ( password != null) ) { 

			List<User> admins = db.getAllUser();

			for (int adminsIndex = 0; adminsIndex < admins.size(); adminsIndex++) {
				User current = admins.get(adminsIndex);
				if ((current.getUsername().equals(username)) && (current.getPassword().equals(password))
						&& (current.getRole().equals(Role.ADMIN))) {

					admin = admins.get(adminsIndex);
					role = admin.getRole().toString();
					Cookie cookie = new Cookie("role", role);
					response.addCookie(cookie);

					model.addAttribute("categories", categorieList);
					model.addAttribute("list", product_list);
					returnPage = "admin.html";

					break;
				}

				else {

					returnPage = "failedLogin.html";
				}
			}
		}
		
		/** CHECK THE USER'S PRESENCE */
		
		else if (role!=null) {
		
			model.addAttribute("categories", categorieList);
			model.addAttribute("list", product_list);
			returnPage = "admin.html";
		}
		

		db.close();

		return returnPage;
	}

	@PostMapping("/admin/newItem")
	public String newItems(Model model, @RequestParam(required = false, name = "category") String category,
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "price") Double price,
			@RequestParam(required = false, name = "ingredients") String ingredients,
			@RequestParam(required = false, name = "quantity") Integer quantity,
			@RequestParam(required = false, name = "categorie_var") Integer category_id) {

		Database db = new Database();
		String newItemText = null;

		/** INSERT NEW CATEGORY */

		if (category != null && category.isBlank() == false) {

			db.insertCategory(category);
			newItemText = "New Category: " + category;

		}

		/** INSERT NEW PRODUCT */

		else if (((name != null) && (name.isBlank() == false)) && (price != null)
				&& ((ingredients != null) && (name.isBlank() == false)) && (quantity != null)) {

			Product product = new Product(name, price, ingredients, quantity, 0);

			db.insertNewProduct(product.getName(), product.getPrice(), product.getIngredients(), product.getQuantity(), product.getAverageRating());
			Product_Category pc = db.getCategoryById(category_id);
			List<Product> product_list = pc.getProducts();
			List<User> allUsers=db.getAllUser(); 
			product_list.add(product);

			List<Product> products = db.getProductByName(name);
			int productId = products.get(0).getId();

			db.refreshMapping(pc.getId(), productId);
			
			for (int i=0; i<allUsers.size(); i++) {
				int currentUserId=allUsers.get(i).getId(); 
				db.refreshUserRatingMapping(currentUserId, productId); 
			}
			

			newItemText = "New Product: " + product.getName() + " " + product.getPrice() + " "
					+ product.getIngredients() + " " + product.getQuantity() + " " + "category: " + pc.getName();

		}

		/** IF SOME INFORMATION IS NOT AVAILABLE */

		else {

			newItemText = "Something is missing...Check your inputs one more time!";

		}

		model.addAttribute("message", newItemText);

		return "newItemsOrChanges.html";

	}

	@PostMapping("/admin/change")
	public String changeItems(Model model, 
			@RequestParam(required = false, name = "selected_pprice") Integer id1,
			@RequestParam(required = false, name = "selected_pquantity") Integer id2,
			@RequestParam(required = false, name = "price") Double price,
			@RequestParam(required = false, name = "quantity") Integer quantity) {

		Database db = new Database();
		String modMessage = null;
		
		/** UPDATE OF THE PRICE OR THE QUANTITY */

		if (id1 != null && price != null) {

			db.UpdatePrice(id1, price);
			modMessage = "The price has been changed. New price: " + price;

		}

		else if (id2 != null && quantity != null) {

			db.UpdateQuantity(id2, quantity);
			modMessage = "The quantity has been changed. New quantity: " + quantity;
			
		} else {

			modMessage = "Something is missing...Check your inputs one more time!";
		}

		model.addAttribute("message", modMessage);

		return "newItemsOrChanges.html";

	}

}
