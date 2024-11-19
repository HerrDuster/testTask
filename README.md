
# Currency Rate App

A brief description of what this project does and who it's for

This is an application for retrieving currency rates. It utilizes Docker Compose to set up a PostgreSQL database with pgAdmin4 for managing the database. Follow the steps below to get started:
# Prerequisites
Docker installed on your machine

#Installation Steps

1) Execute the docker-compose.yml file to set up the PostgreSQL database along with pgAdmin4.
This command will start the database service in the background.  
docker-compose up -d  
2)Access pgAdmin4 in your browser and create a new database:
Open your web browser and navigate to http://localhost:5050.
Log in to pgAdmin4 using the default credentials 
(username: pgadmin4@pgadmin.org, password: root).
Once logged in, navigate to Servers > PostgreSQL 13 > Databases.
Right-click on Databases and select Create > Database >   
Enter name: postgres  
Username: postgres  
Password: 123  
Enter a name for the database and click Save.  
3)Set the APP_ID environment variable with your API key:  
APP_ID=your_api_key_here  
4)Run the application:  
./gradlew bootRun  
5)Access the Swagger documentation for detailed information about the available API endpoints:
Open your web browser and navigate to http://localhost:8080/swagger-ui/index.html#.