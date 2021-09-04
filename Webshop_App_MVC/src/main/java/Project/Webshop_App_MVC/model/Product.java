package Project.Webshop_App_MVC.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="products")
public class Product {

	@Id
	@Column(name="id")
	@GeneratedValue (strategy = GenerationType.IDENTITY )
	private int id; 
	
	@Column (name="name")
	private String name; 
	
	@Column (name="price")
	private int price; 
	
	@Column (name="ingredients")
	private String ingredients; 
	
	@Column (name="quantity")
	private int quantity; 
	
	@Enumerated(EnumType.STRING)
	@Column (name="rating")
	private Product_Rating rating;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product_Rating getRating() {
		return rating;
	}

	public void setRating(Product_Rating rating) {
		this.rating = rating;
	} 
	
	
}
