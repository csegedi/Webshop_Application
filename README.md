# Webshop MVC Application
 
## Short review:
The application is a fictitious webshop (Nefertiti Online Store) , where exotic cosmetic products are sold in various categories. It is also important to note, that this is a demo application about a simulation of typical webshop activities (product selecting, adding to cart, rating the product, customer tracking, management of the stock, etc).

In this application can be two participants with different possibilities and permissions:
* the customer
* the administrator

After a registration the customer can select from the categories of the product, he or she can select a current product within the selected category, the selected product can be added to the cart, it can be removed from the cart, or its quantity in the cart can be modified, and of course, the customer can buy the selected product(s). The customer has furthermore the opportunity, to rate the selected product(s).
The administrator is responsible for the management of the stock, he can add new categories, and products to the webshop, and he can modify the properties of the extant products. 

This application has been built on the grounds of the MVC pattern, where the control logic consist of two classes. The AdminController class contains the methods which are able to operate the activities of the administrator. The UserController class is responsible for the functioning of every activity, which can be done by the customer.

This application uses cookies to track the customers' activity (for example keeping the content of the cart, while the customer steps back to another page).

## The database tables 

The application has a database connection, the database tables are the following:
*	**users**: store the user’s name password, and it contains the role of the customers (role can be customer or administrator). 
*	**products**: this table contains the most important data of the products such as name, price, and ingredients. Moreover, it stores the quantity of the current product in the storage, and it stores the average value of the customers' ratings. 
*	**categories**: it contains the name of the categories.
*	**categories_product_mapping**: it helps to build the connection between the categories and products tables. 
*	**ratings**: it stores the ratings given by the customers
*	**user_products_ratingtable**: This is a connection between the customer and the products. After the first login, the customer gets every product to rate. This table contains the identifier number of the user and the identifier number of each product. If the customer has given a rate, the rated product will be removed from the customer: so the current customer can rate the current product just once. For example, the registered customer „John” gave a rate for the product „Levendula soap”. He is a very satisfied customer, he gave the highest value. John has no more opportunity to rate the product again, but another customer, who never rated this product, can give a rate of course.

## The model elements

**The attributes of the product**
* name (String): the name of the current product
* actual price (double): the price of the product
*	ingredients (String): a short description of the products:
*	quantity (int): it shows many pieces of the product are actually in the storage: in case of buy, the quantity is decreased).
*	ratings: This is a list, which contains Ratings classes. The ratings can be given by the customers. One product can store many products (OneToMany connection). 
*	averageRating: This stores the average value of the ratings given by several customers. To this attribute is attached a method within this class, which calculate its value.	actualQuartQuantity: this attribute isn’t in the database, this is temporary data, it shows, how many products are actually in the customer’s basket. It will be cleared after every buying.

**The Category Class**

The product can be classified into a category. The category is a separate class. The category classifies the actual products. One category can store many products (OneToMany connection).

**Rating Class**

The Rating class can be stored by a list in the Products Class. The Ratings Class has the grade (int) attribute, which represents the numeric value of the current rating. 

**Role Enum Class**

This enum class contains two constant values: CUSTOMER, and ADMIN. 

**User Class**

Every user has a username and password. The User class contains an attribute in type Role. After the registration, every user becomes a customer, and the value of the role attribute will get the CUSTOMER constant. 
Furthermore, the User class contains a list, which stores every Product without the rating of the current User (OneToMany connection). If the current user has given a rate, the rated Product will be removed from this list. The connection between the user and the products is based on the user_products_ratingtable.

## The UserController Class

**Registration**: if the customer wants to buy something, it is necessary to register. The registration is initiated by the customer. The customer must give a username and a password to complete the registration.

**The strength of the password**: The application always checks the strength of the password. The password is appropriate if it contains a number and upper case. If the registration was successful, the customer can log in, otherwise, the customer must restart the process of the registration.

**Check the registered users**: if the user by the registration gives a username that can be already found in the database, the application warns the user to choose another name, otherwise the registration will be denied. 

**Login**: if the registration was successful, the customer can enter. In the case of login, the application always checks the right username and password. If the customer login is successful, cookies will be created, which contain the customer’s name and password. By the first entry, the new customer’s list will be filled with all the products. This list will contain the product, which will be able to rate by the customer. 

**Choosing a category**: After the entry, the customer can see the list of the categories. The customer can continue the buy by choosing a category. The id of the chosen category will be stored in a cookie. 

**Rating**: the first page after the entry even contains the rating button. If the customer pushes this button she or he will be redirected to the rating page. Here can be found every product, which hasn’t been rated by the current customer before. Here the customer can select the product to rate, and then he or she can choose the rating value (perfect-5, good-4, average-3, bad-2, poor-1). Here I wanted to accomplish that the given customer won’t be able to give a rate for the current product whenever he or she wants.  The rated product will be removed from the customer's list, the current customer can rate the current product just once. The rating will be added to the current product.

**Catalog of the selected product category**: After choosing the category, on the next page, the customer can see all products that belong to the selected category. Here can be read all necessary information about the products (name, price, actual quantity in the storage, descriptions about the ingredients of the products, average value of the product’s ratings).

**Select a product**: the customer can choose a product (using the drop-down menu), and she/he can set the required quantity from the product. If the customer gives a larger number than the number of the actual quantity of the product in the storage, the application warns the customer about the unsuccessful choice.
The selected product will be put in the cart. The cart is a temporary store for the product, which can be found in the UserController class. This list can store the various kind of products, and it can store of course how many pieces of products put the customer to the cart.  The customer can step back and can choose a product from another category. The cart can be filled with every kind of product belonging to various categories. 

**Purchase, delete products from the cart or decrease the product’s quantity in the cart**: if the customer has chosen the product(s) and defined the quantity, the application leads the customer to the next page. The customer here can check the order and the total price of the order. The customer can every time step back before the finalization, he or she can choose another category, or can add to the cart other products. 
The customer can delete the product from the cart before the purchase. If he or she bought for example two or more different products, it is possible to delete something from these products in the cart. The customer can decrease the quantity of a selected product before the buy.
The application continuously follows up on these movements, and it updates the condition of the cart (actual products, quantities, total price). If the customer finalizes the buy by pushing the purchase button, the cart becomes empty again and decreases the quantity of the bought products in the storage.

## The AdminController class

This class contains the operation of the administrator:
* The administrator is registered from the beginning, but he has to log in as admin.
* The administrator can insert new products into the database.
* The administrator can change the price of the product.
* The administrator can modify the quantity of the product in the storage.

## The Style of the Nefertiti webshop

This imagined webshop sells exotic cosmetic products, the appearance of the webshop reflects the style and art of ancient Egypt.





