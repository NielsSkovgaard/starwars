package com.b2w.starwars.presentation.mappers;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.presentation.models.PlanetDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetDtoMapperTest {
    @Autowired
    private PlanetDtoMapper planetDtoMapper;

    @Test
    public void testMapDomainToDto() {
        // Arrange
        Planet input = new Planet("id", "name", "climate", "terrain", 2);
        PlanetDto expected = new PlanetDto("id", "name", "climate", "terrain", 2);

        // Act
        PlanetDto result = planetDtoMapper.map(input);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testMapDomainToDto_nullParameter() {
        // Arrange
        Planet input = null;

        // Act
        PlanetDto result = planetDtoMapper.map(input);

        // Assert
        assertNull(result);
    }

    @Test
    public void testMapDtoToDomain() {
        // Arrange
        PlanetDto input = new PlanetDto("id", "name", "climate", "terrain", 2);
        Planet expected = new Planet("id", "name", "climate", "terrain", 2);

        // Act
        Planet result = planetDtoMapper.map(input);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testMapDtoToDomain_nullParameter() {
        // Arrange
        PlanetDto input = null;

        // Act
        Planet result = planetDtoMapper.map(input);

        // Assert
        assertNull(result);
    }
}
