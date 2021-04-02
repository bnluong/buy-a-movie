# Buy-a-Movie

Buy-a-Movie is a mock e-commerce website that sells movies. The website has
standard e-commerce features such as searching, browsing, add to cart, checkout,
login, create account, and many more.

## Description

This is a full-stack web development project that uses the following technology:

    Front End: HTML5, CSS, JavaScript, Bootstrap, JQuery, JSON, AJAX
    Backend: Java EE, MySQL, REST
    Web server: Apache Tomcat
    Tools: Linux, MySQL Server, Eclipse, VSCode, Maven

The project is a demonstration of my understanding and studying of full-stack
web development. It utilizes the microservices architecture to achieve
front-end/back-end separation.

The website is designed with the responsive approach. It will display and fit
various screens from desktop to small smart phones.

The project has been developed for about 2 months and finally achieved a basic
working version with standard e-commerce features.

My plan is to continue learning and developing this project with the uses of
frameworks including React for front-end and Spring for back-end.

## Design Interfaces

![Interfaces](/design_interfaces.png?raw=true 'Interfaces')

## Demo

###### Video Demo

[![Buy-a-Movie Demo](http://img.youtube.com/vi/idoEeiNtcgw/0.jpg)](http://www.youtube.com/watch?v=idoEeiNtcgw "Buy-a-Movie Demo")

###### Landing Page

![Homepage](screenshots/homepage.png?raw=true 'Homepage')

<details>
<summary>More Screenshots</summary>

###### Browsing Page

![Browsing](screenshots/browse.png?raw=true 'Browsing')

###### Searching Page

![Searching](screenshots/search.png?raw=true 'Searching')

###### Movie Page

![Movie Info](screenshots/movie-info.png?raw=true 'Movie Info')

###### Star Page

![Star Info](screenshots/star-info.png?raw=true 'Star Info')

###### Login Page

![Login](screenshots/login.png?raw=true 'Login')

###### Create Account Page

![Register](screenshots/register.png?raw=true 'Register')

###### Cart Page

![Cart](screenshots/cart.png?raw=true 'Cart')

###### Checkout Page

![Checkout](screenshots/checkout.png?raw=true 'Checkout')

</details>

## Getting Started

### Prerequisites & Dependencies

```
1. Java EE (Java 8)
2. Eclipse IDE (for Java EE)
3. Apache Tomcat 8
4. Maven
5. MySQL Server
```

### Getting the Source Code

This project is [hosted on GitHub](https://github.com/bnluong/buy-a-movie). You
can clone this project directly using this command:

```
git clone git@github.com:bnluong/buy-a-movie.git
```

### Building the Project

```
1. Clone the project
2. Run the SQL scripts in the database-scripts directory
3. Create a MySQL user with and grant access access privileges to the databased created from the script
4. Create a Tomcat web server in Eclipse
4. Open Eclipse IDE -> File -> Import -> Existing Maven Project -> Choose the directory where the project was cloned
5. Open WebContent/META-INF/context.xml to configure the data source for MySQL
    a. Change the username & password to the user created in step 2
    b. Change the port in the url to the port in which the MySQL server operates (if the port is not 3306)
6. Run the project from eclipse: Right click on the project -> Run As -> Run on server
7. The website can now be accessed via localhost: http://localhost:8080/buy-a-movie/
```

## Tests

Add tests here.

## Authors

-   **Bao Luong** - _Owner_ - [bnluong](https://github.com/bnluong)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file
for details.

## Acknowledgments

[Kaggle](https://www.kaggle.com/rounakbanik/the-movies-dataset?select=credits.csv) -
For providing the The Movies Dataset needed for the database.
