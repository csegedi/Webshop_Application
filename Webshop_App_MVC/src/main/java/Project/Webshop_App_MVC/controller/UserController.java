package Project.Webshop_App_MVC.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Project.Webshop_App_MVC.database.Database;
import Project.Webshop_App_MVC.model.Role;
import Project.Webshop_App_MVC.model.User;

@Controller
public class UserController {

	@PostMapping("/login")
	public String login() {

		return "login.html";

	}

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
		
			/**CHECK THE USED USERNAMES*/
		
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

		return returnPage;

	}

	@PostMapping("/shop")
	public String shop() {

		return "shop.html";
	}

}
