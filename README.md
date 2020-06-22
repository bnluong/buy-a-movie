# Buy-a-Movie

Buy-a-Movie is a mock e-commerce website that sells movies. The website has standard e-commerce features such as searching, browsing, add to cart, checkout, login, create account, and many more.

## Description

This is a full-stack web development project that uses the following technology:

    Front End: HTML5, CSS, JavaScript, Bootstrap, JQuery, JSON, AJAX
    Backend: Java EE, MySQL, REST
    Web server: Apache Tomcat
    Tools: Linux, MySQL Server, Eclipse, VSCode, Maven

The project is a demonstration of my understanding and studying of full-stack web development. It utilizes the micro-services architecture to achieve front-end/back-end separation.

The project has been developed for about 2 months and finally achieved a basic working version with standard e-commerce features.

My plan is to continue learning and developing this project with the uses of frameworks including React for front-end and Spring for back-end.

## Demo

Coming Soon.

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

This project is [hosted on GitHub](https://github.com/bnluong/buy-a-movie). You can clone this project directly using this command:

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

There are some utilites in the com.buyamovie.utilites package of the project which utilizes JUnit testing.

To run the JUnit tests:

```
Right click on the project -> Run As -> JUnit Tests
```

## Usage

Coming Soon

## Authors

* **Bao Luong** - *Owner* - [bnluong](https://github.com/bnluong)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

[Kaggle](https://www.kaggle.com/rounakbanik/the-movies-dataset?select=credits.csv) - For providing the The Movies Dataset needed for the database.