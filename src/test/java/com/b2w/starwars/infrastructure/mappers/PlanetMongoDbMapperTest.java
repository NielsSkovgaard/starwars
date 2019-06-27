package com.b2w.starwars.infrastructure.mappers;

import com.b2w.starwars.application.services.SwapiService;
import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.infrastructure.models.PlanetMongoDb;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetMongoDbMapperTest {
    @MockBean
    private SwapiService mockSwapiService;

    @Autowired
    private PlanetMongoDbMapper planetMongoDbMapper;

    @Test
    public void testMapDomainToMongoDb() {
        // Arrange
        Planet planet = new Planet(null, "name", "climate", "terrain", 2);
        PlanetMongoDb expected = new PlanetMongoDb("name", "climate", "terrain");

        // Act
        PlanetMongoDb result = planetMongoDbMapper.map(planet);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testMapDomainToMongoDb_nullParameter() {
        // Arrange
        Planet input = null;

        // Act
        PlanetMongoDb result = planetMongoDbMapper.map(input);

        // Assert
        assertNull(result);
    }

    @Test
    public void testMapMongoDbToDomain() {
        // Arrange
        PlanetMongoDb input = new PlanetMongoDb(new ObjectId("507f191e810c19729de860ea"), "name", "climate", "terrain");
        Planet expected = new Planet("507f191e810c19729de860ea", "name", "climate", "terrain", 2);
        Mockito.when(mockSwapiService.getMovies("name")).thenReturn(2);

        // Act
        Planet result = planetMongoDbMapper.map(input);

        // Assert
        assertEquals(expected, result);
        Mockito.verify(mockSwapiService).getMovies("name");
    }

    @Test
    public void testMapMongoDbToDomain_nullParameter() {
        // Arrange
        PlanetMongoDb input = null;

        // Act
        Planet result = planetMongoDbMapper.map(input);

        // Assert
        assertNull(result);
    }
}
