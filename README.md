#Star Wars REST API
##About
The application allows its users to maintain a collection of planets in the Star Wars universe through a REST API.

It will run on: <http://localhost:8080/planets>.

**Available operations** (also see [Operations](#operations]))
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

##Technologies
- Java JDK 8
- Spring
- Maven
- JUnit
- Mockito
- MongoDB
  - Should be running on: <http://localhost:27017>.

##Operations
#####Add planet

When starting the application the first time, the collection is empty. We can make the following two POSTs to add the two planets.

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

#####List all planets

The two planets have now been added and can be listed.

Notice that the first planet exists in the Star Wars API and therefore has two movies.
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

#####Get single planet by ID
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

#####Get single planet by name
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

#####Delete planet by ID
<pre>
<b><u>Request</u></b>
DELETE http://localhost:8080/5d1188ed43d0433b7cef006e

<b><u>Response (204 No Content)</u></b>
(empty body)
</pre>

##Software Architecture
The solution is implemented having a modern, loosely coupled, and highly maintainable architecture. It applies Domain-Driven Design (DDD) principles and the Onion Architecture. 

The following areas correspond to the folder structure in the solution.

- #####Domain
  - The domain has the Planet model class and PlanetRepository interface.

- #####Infrastructure
  - The MongoDB implementation of the PlanetRepository interface, the PlanetMongoDb model class that is saved in and retrieved from the database, plus mappers between the Planet (in Domain) and PlanetMongoDb model classes.

- #####Services
  - The PlanetService resides in the layer on top of the PlanetRepository interface and invokes its methods (that are implemented further down in PlanetMongoDbRepository.
  
  - The SwapiService handles the external integration with SWAPI (<https://swapi.co/api/planets/>) plus caching of all relevant SWAPI data. Also see: [External Integration: SWAPI (Star Wars API)](#external-integration-swapi-star-wars-api).

- #####Presentation (API)
  - This has the Controllers that define the REST operations and routing, as well as the PlanetDto model class (as a Data Transfer Object), and mappers for mapping between PlanetDto and Planet (in Domain). 

##External Integration: SWAPI (Star Wars API)
Planets are added via the REST API. For every planet that is retrieved, the number of movies from SWAPI is part of the response object.

- The REST API neither makes a lookup in the external SWAPI when a planet is saved nor retrieved. That would give higher response times for the users.

- Instead, the REST API fetches all "Planet Name" → "Number of Movies" relations from SWAPI on the first time they are needed. The data will stay cached in memory until the application is stopped.
  - Thus, the first REST API call after starting the application takes around +3 seconds to "warm up" the application i.e. call SWAPI to build the cache.
  - Afterwards, all relevant SWAPI data will be cached, and lookups will be fast. 

##Data Storage: MongoDB
MongoDB is used for data storage, which is configured through Spring Data annotations to define the collection name, set up the ID column and make a unique index on the Name column.

This enables quick lookup by ID or Name as required by two of the operations. 

##Testing
- **Manual testing**
  - This has been done for each operation with a REST client (e.g., Postman). The HTTP requests and responses are displayed in the [Operations](#operations) section.
  
- **Automated testing**
  - Yes, unit testing using JUnit and Mockito.