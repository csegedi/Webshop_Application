package Project.Webshop_App_MVC.model;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	private double price; 
	
	@Column (name="ingredients")
	private String ingredients; 
	
	@Column (name="quantity")
	private int quantity; 
	
	@Transient
	private int actualCartQuantity; 
	
	@OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable (
			name="products_ratings_mapping",
			joinColumns= @JoinColumn (name="product_id"),
			inverseJoinColumns=@JoinColumn (name="rating")
			)
	private List<Rating>ratings; 
	
	@Column (name="averageRating")
	private double averageRating; 
	
	
	public Product() {
		super();
	}

	public Product(String name, Double price, String ingredients, int quantity, int initAverageRating) {
		super();
		this.name = name;
		this.price = price;
		this.ingredients = ingredients;
		this.quantity = quantity;
		this.averageRating=initAverageRating; 
		
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

	public double getPrice() {
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
	

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
	
	
	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating() {
		
			int assistant=0;
			
			for (int i=0; i<ratings.size(); i++) {
				assistant+=ratings.get(i).getGrade(); 
			}
			averageRating=assistant/ratings.size(); 
			
		}

	public void setPrice(double price) {
		this.price = price;
	}


	public int getActualCartQuantity() {
		return actualCartQuantity;
	}

	public void setActualCartQuantity(int actualCartQuantity) {
		this.actualCartQuantity = actualCartQuantity;
	}
	
	
	public void cartIncrease (int quantity) {
		
		 int increase=actualCartQuantity+quantity;
		
		this.actualCartQuantity=increase; 

	}
	
	public void cartDecrease (int quantity) {
		
		 int decrease=actualCartQuantity-quantity;
		
		this.actualCartQuantity=decrease; 

	}

	public void quantityDecrease() {
		
		int decrease=quantity-actualCartQuantity;  
		
		this.quantity=decrease; 
	}
	
	
	

}
