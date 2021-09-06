package Project.Webshop_App_MVC.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
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
	
	@OneToMany (cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable (
			name="products_ratings_mapping",
			joinColumns=@JoinColumn (name="product_id"),
			inverseJoinColumns= @JoinColumn (name="rating_id")
			)		
	private List<Product> ratings;
	
	
	public Product() {
		super();
	}

	public Product(String name, int price, String ingredients, int quantity) {
		super();
		this.name = name;
		this.price = price;
		this.ingredients = ingredients;
		this.quantity = quantity;
	}

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

	public List<Product> getRatings() {
		return ratings;
	}

	public void setRatings(List<Product> ratings) {
		this.ratings = ratings;
	}


	
}
