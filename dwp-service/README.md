# DWP Service

### Task
Build an API which calls this API, and returns people who are listed as either living in London, or whose current coordinates are within 50 miles of London. Push the answer to Github, and send us a link.

This is a Spring Boot application for the DWP interview. This demo app was developed using the following tools

  - Maven
  - Java 8
  - Spring Boot
  
#### Building and running the Application
Maven:
```sh
$ mvn clean package or ./mvnw clean package
```

```sh
$ mvn spring-boot:run
```
### Hint
There is a <code>CommandLineRunner</code> class that logs people from London and surrounding area. The latitude and longitude seems not very accurate for london and other cities but i understand that this is only for demostration purpose only.

### Result
Please see the swagger endpoint or application url to view a list of people who are listed as either living in London, or whose current coordinates are within 50 miles of London.
- base url http://localhost:8080/dwp/api/
- http://localhost:8080/dwp/api/search - this accepts city name, latitude, longitude & radius
- http://localhost:8080/dwp/api/ui - swagger 

### Sanple result
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
```

I'm very much open to feedback and how we can improve this solution. 

Thanks for your time.

License
----

MIT
