# Bee Tee Ohh README
---
# Members
 - Timmanee Yannaputt
 - Tan Wei Xin
 - Ma Shiqi
 - Koh Kai Jie
 - Ong Jee Shen


# Prerequisites
    -Java JDK 17 or later
    -Apache Maven 3.6.0 or later

# Setup Instructions



## 1.  Clone the Repository:
```
git clone https://github.com/PuttTim/sc2002-bee-tee-ohh.git
cd sc2002-bee-tee-ohh
```
## 2.  Build the project:

```
mvn clean package
```

## 3. Running the application
```
mvn exec:java "-Dexec.mainClass=App"
```

## 4. Running the tests
```
mvn test
```

## Login Credentials
All users by default have a password of "password".

## Directories to know
 - /reports: Contains the generated reports for the project.
 - /report: Contains two files, our test case report and our assignment report writeup.
 - /uml class diagram: Contains the UML class diagram for the project.
 - /uml sequence diagram: Contains the UML sequence diagram for the project.
 - /data: Contains the data files used in the project that is loaded into the application on startup
 - /docs: Contains the generated javadoc HTML for the project.
 - /src/main/java: Contains the source code for the project.
 - /src/test/java: Contains the test code for the project.