# Webshop MVC Application
 
## Short review:
The application is a fictitious webshop (Nefertiti Online Store) , where exotic cosmetic products are sold in various categories. It is also important to note, that this is a demo application about a simulation of typical webshop activities (product selecting, adding to cart, rating the product, customer tracking, management of the stock, etc).

* users
* products
* categories
* products-categories joinTable

 
## The attributes of the product
 
* **name**
* **actual price**
* **ingredients** (short description about the products)
* **quantity** (how many pieces of the product are actually in the storage, in case of buy, the quantity is decreased).
 
The product can be classified into a category. The category is a separate class. The category classifies the actual products. One category can store many products (OneToMany connection). 
 
**Actually quantity of the product:** this attribute isn’t in the database, this is temporary data, it shows, how many products are actually in the customer’s basket. It will be cleared after every buying.  
 
## The participants of the application
 
In this application there are two participants with different possibilities and permissions:
* the customer
* the administrator
 
## The customer’s permissions:
 
* **Registration:** if the customer wants to buy something, it is necessary to register. The registration is initiated by the customer. The customer must give a username and a password to complete the registration.

    **The strength of the password:** The application always checks the strength of the password. The password is appropriate if it contains a number and upper case. If the registration was successful, the customer can log in, otherwise, the customer must restart the process of the registration.

    **Check the registered users:** if the user by the registration gives a username that can be found in the database, the application warns the user to choose another name.

* **Login:** if the registration was successful, the customer can enter.
* **Choosing a category:** After the entry, the customer can see the list of the categories. The customer can continue the buy by choosing a category.
* **Add to the cart:** After choosing the category, on the next page, the customer can see all products that belong to the selected category. Here can be read all necessary information about the products (name, price, actual quantity, descriptions).
* **Select a product:** the customer can choose a product (using the drop-down menu), and she/he can set the required quantity from the product. If the customer gives a minus number or sets a larger number than the number of the actual quantity, the application warns the customer about the unsuccessful choice.
 
* **Purchase:** if the customer has chosen the product(s) and defined the quantity, the application leads the customer to the next page. The customer here can check the order and the total price. The customer can every time step back before the finalization, he can choose another category, or he can add to cart other products.
The application continuously follows up these movements, and it updates the condition of the cart (actual products, quantities, total price).
If the customer finalizes the buy by pushing the purchase button, the cart becomes empty again and decreases the quantity of the bought products in the storage. 
 
* **Delete product from the cart, or decrease the product’s quantity in the cart** : The customer can delete the product from the cart before the purchase. If he bought for example two or more different products, it is possible to delete something from these products. The customer can decrease the quantity of a selected product before the buy.

## The administrator's permissions
 
* The administrator is registered from the beginning, but he has to log in as admin.
* The administrator can insert new products into the database.
* The administrator can change the price of the product.
* The administrator can modify the quantity of the product in the storage.

**The next update is coming soon: The customer will be able to rate the products.**

