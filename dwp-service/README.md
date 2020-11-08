# DWP Service

## Task
Build an API which calls this API, and returns people who are listed as either living in London, or whose current coordinates are within 50 miles of London. Push the answer to Github, and send us a link.

## Tools & Frameworks
This is a Spring Boot application for the Department for Work and Pensions (DWP) interview. This demo app was developed using the following;

  - Maven
  - Java 8
  - Spring Boot
  
## Building and Running the Application
### Maven :
```sh
$ mvn clean package or ./mvnw clean package
```
```sh
$ mvn spring-boot:run
```
### Docker :
```sh
cd dwp-service
docker build -t <user.name>/dwp:${artifact.version} .
```
Once done, run the Docker image and map the port to any port of your choice.

```sh
docker run -d -p 8000:8080 --restart="always" <user.name>/dwp:${artifact.version}
```

### Note
There is a <code>CommandLineRunner</code> class to list people from London and surrounding area in a log file (dwp.log). The demo data contain latitude and longitude that seems not very accurate for london and other cities. 

### Result
Please see the swagger endpoint or application url to view a list of people who are listed as either living in London, or whose current coordinates are within 50 miles of London.
- base url http://localhost:8080/dwp/api/
  - <code> curl -X GET "http://localhost:8080/dwp/api/" -H  "accept: application/json" </code>
- http://localhost:8080/dwp/api/search - this accepts the city name, latitude, longitude & radius
  - example http://localhost:8080/dwp/api/search?city=London&lat=51.509865&lon=-0.118092&radius=10
- http://localhost:8080/dwp/api/ui - swagger UI

### Sample result
```sh
  {
    "id": 520,
    "first_name": "Andrew",
    "last_name": "Seabrocke",
    "email": "aseabrockeef@indiegogo.com",
    "ip_address": "28.146.197.176",
    "latitude": 27.69417,
    "longitude": 109.73583,
    "city": "London"
  },
  {
    "id": 396,
    "first_name": "Terry",
    "last_name": "Stowgill",
    "email": "tstowgillaz@webeden.co.uk",
    "ip_address": "143.190.50.240",
    "latitude": -6.7098551,
    "longitude": 111.3479498,
    "city": "London"
  },
  .....
```

License
----

`This application is distributed under the` [Apache 2.0 license.](https://www.apache.org/licenses/LICENSE-2.0.html)
