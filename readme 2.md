# CAT; Recommendation API
 This is Recommendation API to provides functionality to manage Condition Monitoring Recommendation.

## Getting Started

### Requirements

 * IntelliJ Community ide
 * Java 11 installed
 * Maven 3.1.0 or newer


### Running Flyway scripts locally
1.- Create a new data base called: "reco"
Run the below command by giving your local values.

* mvn flyway:migrate -Dflyway.url=jdbc:postgresql://localhost:5432/reco -Dflyway.user=postgres -Dflyway.password=admin

Clean up:

* mvn flyway:clean -Dflyway.url=jdbc:postgresql://localhost:5432/reco -Dflyway.user=postgres -Dflyway.password=admin

### Running the Spring Java Application from command line

1. Start a terminal window and clone the P-Reco repository
    ```bash
    git clone ...
    ```
2.  Execute maven commands to resolve and build the JAR file
     ```bash
     mvn clean install
     ```

### Required Headers for call the application

1. Authorization

     ```bash
     see https://catdigital.atlassian.net/wiki/spaces/CD/pages/66387969/How+to+get+OAuth+tokens+using+Postman+CWS+login
   to know how to get the token using postman
   

     ```
2. ENTITLEMENTS ('x-cat-entitlements')
   Ensure that x-cat-entitlements header is present in the request header

### Running functional test locally

1. On application-local.yml file add the below variables with valid cwsID and password:
    - functionalTest.cwsId
    - functionalTest.cwsPassword
    
2. On SpringContextLoader.class comment out the annotations @MockBean and @EnableAutoConfiguration 

3. On BeanConfiguration.java and DatabaseConfiguration.java files comment out @TestConfiguration annotation