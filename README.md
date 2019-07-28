Star Wars REST API
=

About
-
The application allows its users to maintain a collection of planets in the Star Wars universe through a REST API.

It will run on <http://localhost:8080/planets>.

**Available operations** (see also [Operations](#operations))
- Add new planet
- List all planets
- Get single planet by ID
- Get single planet by name
- Delete planet by ID

**Planet data model**
- ID (String/ObjectID)
- Name (String)
- Climate (String)
- Terrain (String)
- Movies (int)
  - The number of Star Wars movies the planet is in.
  - This external information comes from the Star Wars API (SWAPI) (<https://swapi.co/api/planets/>).

Technologies
-
- Java JDK 8
- Spring
- Maven
- JUnit
- Mockito
- MongoDB
  - Should be running on <http://localhost:27017>.

Operations
- 
**Add planet**

When starting the application the first time, the collection is empty. We can make the following two POSTs to add two planets.

<pre>
<b><u>Request</u></b>
POST http://localhost:8080/planets
    
<b><u><i>Request Body</i></u></b>
<i>[1]</i>
    {
        "name": "Alderaan",
        "climate": "temperate",
        "terrain": "grasslands, mountains"
    }
    
<i>[2]</i>
    {
        "name": "Some unknown planet", 
        "climate": "bla bla", 
        "terrain": "bla bla"
    }

<b><u>Response (201 Created)</u></b>
(empty body) (for each request)
</pre>

**List all planets**

The two planets have now been added and can be listed.

Notice that the first planet exists in the Star Wars API where it has two movies.
<pre>
<b><u>Request</u></b>
GET http://localhost:8080/planets

<b><u>Response (200 OK)</u></b>
[
    {
        "id": "5d1188e543d0433b7cef006d",
        "name": "Alderaan",
        "climate": "temperate",
        "terrain": "grasslands, mountains",
        <b><i>"movies": 2</i></b>
    },
    {
        "id": "5d1188ed43d0433b7cef006e",
        "name": "Some unknown planet",
        "climate": "bla bla",
        "terrain": "bla bla",
        "movies": 0
    }
]
</pre>

**Get single planet by ID**
<pre>
<b><u>Request</u></b>
GET http://localhost:8080/planets/id/5d1188e543d0433b7cef006d

<b><u>Response (200 OK)</u></b>
{
    "id": "5d1188e543d0433b7cef006d",
    "name": "Alderaan",
    "climate": "temperate",
    "terrain": "grasslands, mountains",
    "movies": 2
}
</pre>

**Get single planet by name**

The search is case-insensitive and needs to match exactly.
<pre>
<b><u>Request</u></b>
GET http://localhost:8080/planets/name/Some unknown planet

<b><u>Response (200 OK)</u></b>
{
    "id": "5d1188ed43d0433b7cef006e",
    "name": "Some unknown planet",
    "climate": "bla bla",
    "terrain": "bla bla",
    "movies": 0
}
</pre>

**Delete planet by ID**
<pre>
<b><u>Request</u></b>
DELETE http://localhost:8080/planets/5d1188ed43d0433b7cef006e

<b><u>Response (204 No Content)</u></b>
(empty body)
</pre>

Software Architecture
-
The application has a modern, loosely coupled, and highly testable and maintainable architecture. It applies Domain-Driven Design (DDD) and Inversion of Control (IoC) principles in an Onion Architecture. 

The following areas correspond to the folder structure in the project.

- **Presentation (API)**
  - PlanetController: Defines REST operations and routing.
  - PlanetDto (Data Transfer Object) model sent via PlanetController.
  - Mapper for Planet (Domain) ↔ PlanetDto (Presentation).

- **Application**
  - PlanetService: Invokes methods in PlanetRepository.
  - SwapiService: Handles external integration with SWAPI (<https://swapi.co/api/planets/>) plus caching of relevant SWAPI data. See also [External Integration: SWAPI (Star Wars API)](#external-integration-swapi-star-wars-api).
  - RestService: Used to call REST endpoints. This is used by SwapiService.

- **Domain**
  - Planet model.
  - Repository interfaces: PlanetRepository and the generic Repository interface.

- **Infrastructure**
  - PlanetMongoDb model.
  - MongoDbRepository: Generic MongoDB repository.
  - PlanetMongoDbRepository that extends MongoDbRepository and implements PlanetRepository (in Domain).
  - Mapper for Planet (Domain) ↔ PlanetMongoDb (Infrastructure).

- **Core**
  - Entity&lt;TId&gt; interface plus DomainObject, InfrastructureObject, and PresentationObject interfaces.

- **Config**
  - Configuration interface/class with data from the src/main/resources/application.properties file.

External Integration: SWAPI (Star Wars API)
-
Planets are added via the REST API. For every planet that is retrieved, the number of movies from SWAPI is part of the response object.

- The application neither makes a lookup in the external SWAPI when a planet is saved nor retrieved. That would give higher response times for the users.

- Instead, the application fetches all "Planet Name" → "Number of Movies" relations from SWAPI at the first time they are needed. The data will stay cached in memory until the application is stopped.
  - Thus, the first REST API call after starting the application will take around +3 seconds extra to "warm up" the application i.e. call SWAPI to build the cache.
  - Afterward, all relevant SWAPI data will be cached in memory, and lookups will be fast. (The memory footprint is very small). 

Data Storage: MongoDB
-
MongoDB is used for data storage, which is configured through Spring Data annotations to define the collection name, set up the ID column, and make a unique index on the Name column.

This enables quick lookup by ID or Name as required by two of the operations. 

Testing
-
- **Manual testing**
  - This has been done for each operation with a REST client (e.g., Postman). The HTTP requests and responses are displayed in the [Operations](#operations) section.
  
- **Automated testing**
  - Yes, unit testing using JUnit and Mockito.
