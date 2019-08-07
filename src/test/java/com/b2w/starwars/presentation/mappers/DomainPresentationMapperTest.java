package com.b2w.starwars.presentation.mappers;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.presentation.models.PlanetDto;
import com.b2w.starwars.presentation.models.PlanetDtoCreate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DomainPresentationMapperTest {
    @Autowired
    private DomainPresentationMapper<Planet, PlanetDto, PlanetDtoCreate> domainPresentationMapper;

    @Test
    public void testMapDomainToPresentation() {
        // Arrange
        Planet input = new Planet("id", "name", "climate", "terrain", 2);
        PlanetDto expected = new PlanetDto("id", "name", "climate", "terrain", 2);

        // Act
        PlanetDto result = domainPresentationMapper.map(input);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testMapDomainToPresentation_NullParameter() {
        // Arrange
        Planet input = null;

        // Act
        PlanetDto result = domainPresentationMapper.map(input);

        // Assert
        assertNull(result);
    }

    @Test
    public void testMapPresentationCreateToDomain() {
        // Arrange
        PlanetDtoCreate input = new PlanetDtoCreate("name", "climate", "terrain");
        Planet expected = new Planet("name", "climate", "terrain");

        // Act
        Planet result = domainPresentationMapper.map(input);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testMapPresentationCreateToDomain_NullParameter() {
        // Arrange
        PlanetDtoCreate input = null;

        // Act
        Planet result = domainPresentationMapper.map(input);

        // Assert
        assertNull(result);
    }
}
