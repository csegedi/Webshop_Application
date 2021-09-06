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
	
	@GetMapping ("/")
	public String index (HttpServletRequest request,
			HttpServletResponse response) {
		
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
	public String admin (Model model,
			@RequestParam (required=false, name="username") String username,
			@RequestParam (required=false, name="password") String password,
			@CookieValue (required =false,  name="role") String role,
			HttpServletResponse response) {
		
		Database db=new Database();
		List<Product_Category> cats=null; 
		List<Product> product_list=null; 
		
		String returnPage=null; 
	
		/** CHECK THE USER BY INCOMING USERNAME AND PASSWORD IN CASE OF ENTER*/
	
		if ( (username!=null) && (password!=null) || role!=null ) {           //FIRST ENTRY
			
			if (username!=null && password!=null) {
				 List<User> admins=db.getUsersByUsernamePassword(username, password); 
				 
				if (admins.size()==1) {
				 role=Role.ADMIN.toString(); 
				 Cookie cookie=new Cookie("role", role); 
				 response.addCookie(cookie);
				}
			}
			
				 cats=db.getAllCategory(); 									//AFTER THE FIRST ENTER CHECK THE COOKIE
				 product_list=db.getAllProducts(); 
				 
				 model.addAttribute("categories", cats); 
				 model.addAttribute("list", product_list); 
				 
				 returnPage="admin.html";

			}
			 
			else {
				
				returnPage="failedLogin.html";
			}
		
		
			db.close(); 
		
		
		return returnPage;
	}
	
	@PostMapping("/admin/newItem")
	public String newItems(Model model,
			@RequestParam (required=false, name="category") String category, 
			@RequestParam (required=false, name="name") String name,
			@RequestParam (required=false, name="price") Integer price,
			@RequestParam (required=false, name="ingredients") String ingredients,
			@RequestParam (required=false, name="quantity") Integer quantity,
			@RequestParam (required=false, name="categorie_var") Integer category_id) {

		Database db=new Database(); 
		String newItemText=null; 
		
		/**INSERT NEW CATEGORY */
		
		if (category!=null && category.isBlank()==false) {
		
			db.insertCategory(category); 
			newItemText="New Category: "+category; 
		 
		}
		
		
		/**INSERT NEW PRODUCT*/
	
		else if ( ( (name!=null) && (name.isBlank()==false) ) && (price!=null) && ( (ingredients!=null) && (name.isBlank()==false) ) && (quantity!=null) ) {
			Product product=new Product(name, price, ingredients, quantity); 
			
			db.insertNewProduct(product.getName(), product.getPrice(), product.getIngredients(), product.getQuantity()); 
			Product_Category pc=db.getCategoryById(category_id); 
			List<Product>product_list=pc.getProducts(); 
			product_list.add(product); 
			
			List<Product> products= db.getProductByName(name);
			int productId=products.get(0).getId();

			db.refreshMapping(pc.getId(), productId); 
			
			newItemText="New Product: "+product.getName()+" "+product.getPrice()+" "+product.getIngredients()+" "+product.getQuantity()+" "+"category: "+pc.getName(); 
		
		}
		
		/** IF SOME INFORMATION IS NOT AVAILABLE */
		
		else {
			
			newItemText="Something is missing...Check your inputs one more time!";
			
		}
		
		model.addAttribute("message", newItemText); 
			
		return "newItemsOrChanges.html";
		
	} 
	
	@PostMapping("/admin/change")
	public String changeItems(Model model,
			@RequestParam (required=false, name="selected_pprice") Integer id1,
			@RequestParam (required=false, name="selected_pquantity") Integer id2,
			@RequestParam (required=false, name="price") Integer price,
			@RequestParam (required=false, name="quantity") Integer quantity) {
		
		Database db=new Database();
		String modMessage=null; 
		
		
		if (id1!=null && price!=null) {
			
			db.UpdatePrice(id1, price); 
			modMessage="The price has been changed. New price: "+price; 
	 
		}
		
		else if (id2!=null && quantity!=null) {
			
			db.UpdateQuantity(id2, quantity);
			modMessage="The quantity has been changed. New quantity: "+quantity; 
		}
		else {
			
			modMessage="Something is missing...Check your inputs one more time!";
		}
		
		model.addAttribute("message", modMessage);
		
		return "newItemsOrChanges.html";
		
	}

}
