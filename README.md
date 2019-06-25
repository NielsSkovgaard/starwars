Star Wars REST API
=

About
-

This solution builds a REST API to maintain a collection of planets in the Star Wars universe.

After running the solution, go to: http://localhost:8080/planets.

It has the following operations:
- Add new planet
- List all planets
- Get single planet by ID
- Get single planet by name
- Delete planet by ID

The planet domain model is:
- ID (String/ObjectID)
- Name (String)
- Climate (String)
- Terrain (String)
- Movies (int)
    - This is the number of Star Wars movies the planet is in, which is looked up through an external integration with the Star Wars API (SWAPI) (https://swapi.co/api/planets/).

Technologies
-
- Java JDK 8
- Spring
- Maven
- JUnit
- Mockito
- MongoDB
  - Should be running on http://localhost:27017.

Operations
-

<b>Add Planets</b>

When starting the application for the first time, the collection is empty, so let's add two planets, by posting to the operation two times.

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

<b>Get List</b>

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

<b>Get by ID</b>

Retrieving a single planet by ID.

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

<b>Get by Name</b>

Retrieving a single planet by Name.

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

<b>Delete</b>

Deleting a single planet by ID.

<pre>
<b><u>Request</u></b>
DELETE http://localhost:8080/5d1188ed43d0433b7cef006e

<b><u>Response (204 No Content)</u></b>
(empty body)
</pre>

Software Architecture
-
The solution is implemented having a modern, loosely coupled, and highly maintainable architecture. It applies Domain-Driven Design (DDD) principles and the Onion Architecture. 

The following areas correspond to the folder structure in the solution.

- <b>Domain</b>
    - The domain has the Planet model class and PlanetRepository interface.

- <b>Infrastructure</b>
    - This has the MongoDB implementation of the PlanetRepository interface, the PlanetMongoDb model class that is saved in and retrieved from the database, plus mappers between the two different Planet model classes in the Domain and Infrastructure, respectively.

- <b>Services</b>
    - The PlanetService resides in the layer on top of the PlanetRepository interface and invokes its methods (that are implemented further down in the PlanetMongoDbRepository).
    - The SwapiService handles the external integration with SWAPI (https://swapi.co/api/planets/) plus caching of the results after they are retrieved for all planets in SWAPI on application startup. (See below).

- <b>Presentation (API)</b>
    - This has the Controllers that define the REST operations and routing, as well as the PlanetDto model class (as a Data Transfer Object), and mappers for mapping between the PlanetDto and the Planet model class in the Domain. 

External Integration: SWAPI (Star Wars API)
-
For every planet that a user adds via the REST API, it may also exist in SWAPI with the same name. When retrieving planets that also exist in SWAPI, the number of movies the planet is featured in should also be returned from the REST API.

This can be solved in different ways:

- <b>On save Planet:</b> When a new planet is added, the number of movies is looked up in SWAPI and saved in the database along with the rest of the model.
- <b>On retrieve Planet:</b> Lookup in the SWAPI as part of the mapping from PlanetMongoDb to the Planet Domain model class to give a value to number of movies the planet is in. 
- <b>On application start: </b> Fetch and cache a (quick-lookup) HashMap of all Planet names and the number of movies they are in.

I decided to go with the third option, because it results in a faster average service response time than the first two, and thus a better user experience.

So, when the application starts, the SwapiService fetches all planets from SWAPI and stores the name and number of all movies in a quick-lookup HashMap. This adds 3-5 seconds of startup time to the application, which is bearable for now.

Data Storage: MongoDB
-
MongoDB is used for data storage, which is configured through Spring Data annotations to define the collection name, set up the ID column and make a unique index on the Name column.

This enables quick lookup by ID or Name as required by two of the operations. 

Testing
-
- <b>Manual testing:</b> This has been done for each operation using a REST client (e.g. Postman). Some of the results are displayed in the Operations section.  
- <b>Automated testing:</b> Unit and integration tests: TODO.