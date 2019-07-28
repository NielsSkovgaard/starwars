package com.b2w.starwars.presentation.controllers;

import com.b2w.starwars.application.services.SwapiService;
import com.b2w.starwars.infrastructure.models.PlanetMongoDb;
import com.b2w.starwars.infrastructure.repositories.PlanetMongoDbRepository;
import com.b2w.starwars.presentation.models.PlanetDto;
import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetControllerTest {
    @MockBean
    private SwapiService mockSwapiService;

    @Autowired
    private PlanetController planetController;

    @Autowired
    private ApplicationContext context;

    @Test
    public void testGetAll() {
        // Arrange
        List<PlanetMongoDb> mockList = Arrays.asList(
                new PlanetMongoDb(new ObjectId("5d12dc6f2163cce70691e0e6"), "name1", "climate1", "terrain1"),
                new PlanetMongoDb(new ObjectId("5d12dc7549cd73f9ac95664d"), "name2", "climate2", "terrain2"),
                new PlanetMongoDb(new ObjectId("5d12dc5f40c36370f4c3d3a7"), "name3", "climate3", "terrain3"));

        List<PlanetDto> expected = Arrays.asList(
                new PlanetDto("5d12dc6f2163cce70691e0e6", "name1", "climate1", "terrain1", 1),
                new PlanetDto("5d12dc7549cd73f9ac95664d", "name2", "climate2", "terrain2", 2),
                new PlanetDto("5d12dc5f40c36370f4c3d3a7", "name3", "climate3", "terrain3", 3));

        // Mock MongoTemplate
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        Mockito.when(mockMongoTemplate.findAll(PlanetMongoDb.class, "planets")).thenReturn(mockList);
        ReflectionTestUtils.setField(context.getBean(PlanetMongoDbRepository.class), "mongoTemplate", mockMongoTemplate);

        // Mock SwapiService
        Mockito.when(mockSwapiService.getMovies("name1")).thenReturn(1);
        Mockito.when(mockSwapiService.getMovies("name2")).thenReturn(2);
        Mockito.when(mockSwapiService.getMovies("name3")).thenReturn(3);

        // Act
        ResponseEntity<List<PlanetDto>> result = planetController.getAll();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expected, result.getBody());
        Mockito.verify(mockMongoTemplate).findAll(PlanetMongoDb.class, "planets");
        Mockito.verify(mockSwapiService).getMovies("name1");
        Mockito.verify(mockSwapiService).getMovies("name2");
        Mockito.verify(mockSwapiService).getMovies("name3");
    }

    @Test
    public void testGetById() {
        // Arrange
        PlanetMongoDb planetMongoDb = new PlanetMongoDb(new ObjectId("5d12dc6f2163cce70691e0e6"), "name1", "climate1", "terrain1");
        PlanetDto expected = new PlanetDto("5d12dc6f2163cce70691e0e6", "name1", "climate1", "terrain1", 1);

        // Mock MongoTemplate
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        Mockito.when(mockMongoTemplate.findOne(Query.query(Criteria.where("_id").is("5d12dc6f2163cce70691e0e6")), PlanetMongoDb.class, "planets")).thenReturn(planetMongoDb);
        ReflectionTestUtils.setField(context.getBean(PlanetMongoDbRepository.class), "mongoTemplate", mockMongoTemplate);

        // Mock SwapiService
        Mockito.when(mockSwapiService.getMovies("name1")).thenReturn(1);

        // Act
        ResponseEntity<PlanetDto> result = planetController.getById("5d12dc6f2163cce70691e0e6");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expected, result.getBody());
        Mockito.verify(mockMongoTemplate).findOne(Query.query(Criteria.where("_id").is("5d12dc6f2163cce70691e0e6")), PlanetMongoDb.class, "planets");
        Mockito.verify(mockSwapiService).getMovies("name1");
    }

    @Test
    public void testGetById_NotFound() {
        // Arrange
        // Mock MongoTemplate
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        Mockito.when(mockMongoTemplate.findOne(Query.query(Criteria.where("_id").is("5d12dc6f2163cce70691e0e6")), PlanetMongoDb.class, "planets")).thenReturn(null);
        ReflectionTestUtils.setField(context.getBean(PlanetMongoDbRepository.class), "mongoTemplate", mockMongoTemplate);

        // Act
        ResponseEntity<PlanetDto> result = planetController.getById("5d12dc6f2163cce70691e0e6");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
        Mockito.verify(mockMongoTemplate).findOne(Query.query(Criteria.where("_id").is("5d12dc6f2163cce70691e0e6")), PlanetMongoDb.class, "planets");
    }

    @Test
    public void testGetByName() {
        // Arrange
        PlanetMongoDb planetMongoDb = new PlanetMongoDb(new ObjectId("5d12dc6f2163cce70691e0e6"), "name1", "climate1", "terrain1");
        PlanetDto expected = new PlanetDto("5d12dc6f2163cce70691e0e6", "name1", "climate1", "terrain1", 1);

        // Mock MongoTemplate
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        Pattern pattern = Pattern.compile(MessageFormat.format("^{0}$", Pattern.quote("name1")), Pattern.CASE_INSENSITIVE);
        Mockito.when(mockMongoTemplate.findOne(Query.query(Criteria.where("name").regex(pattern)), PlanetMongoDb.class, "planets")).thenReturn(planetMongoDb);
        ReflectionTestUtils.setField(context.getBean(PlanetMongoDbRepository.class), "mongoTemplate", mockMongoTemplate);

        // Mock SwapiService
        Mockito.when(mockSwapiService.getMovies("name1")).thenReturn(1);

        // Act
        ResponseEntity<PlanetDto> result = planetController.getByName("name1");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expected, result.getBody());
        Mockito.verify(mockMongoTemplate).findOne(Query.query(Criteria.where("name").regex(pattern)), PlanetMongoDb.class, "planets");
        Mockito.verify(mockSwapiService).getMovies("name1");
    }

    @Test
    public void testGetByName_NotFound() {
        // Arrange
        // Mock MongoTemplate
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        Pattern pattern = Pattern.compile(MessageFormat.format("^{0}$", Pattern.quote("name1")), Pattern.CASE_INSENSITIVE);
        Mockito.when(mockMongoTemplate.findOne(Query.query(Criteria.where("name").regex(pattern)), PlanetMongoDb.class, "planets")).thenReturn(null);
        ReflectionTestUtils.setField(context.getBean(PlanetMongoDbRepository.class), "mongoTemplate", mockMongoTemplate);

        // Act
        ResponseEntity<PlanetDto> result = planetController.getByName("name1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
        Mockito.verify(mockMongoTemplate).findOne(Query.query(Criteria.where("name").regex(pattern)), PlanetMongoDb.class, "planets");
    }

    @Test
    public void testSave() {
        // Arrange
        PlanetDto planetDto = new PlanetDto(null, "name1", "climate1", "terrain1", 0);
        PlanetMongoDb planetMongoDbResult = new PlanetMongoDb(new ObjectId("5d13d35f993279be2b2e04f0"), "name1", "climate1", "terrain1");

        // Mock MongoTemplate
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        Mockito.when(mockMongoTemplate.save(Mockito.any(), Mockito.anyString())).thenReturn(planetMongoDbResult);
        ReflectionTestUtils.setField(context.getBean(PlanetMongoDbRepository.class), "mongoTemplate", mockMongoTemplate);

        // Mock SwapiService
        Mockito.when(mockSwapiService.getMovies("name1")).thenReturn(1);

        // Act
        ResponseEntity<PlanetDto> result = planetController.save(planetDto);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNull(result.getBody());

        String expectedLocationEnd = "/id/5d13d35f993279be2b2e04f0";
        String actualLocation = result.getHeaders().getLocation().toString();
        String actualLocationEnd = actualLocation.substring(actualLocation.length() - expectedLocationEnd.length());
        assertEquals(expectedLocationEnd, actualLocationEnd);

        Mockito.verify(mockMongoTemplate).save(Mockito.any(), Mockito.anyString());
        Mockito.verify(mockSwapiService).getMovies("name1");
    }

    @Test
    public void testSave_BadRequest() {
        // Arrange
        PlanetDto planetDto = new PlanetDto(null, null, "climate1", "terrain1", 0);
        DataIntegrityViolationException expectedException = Mockito.mock(DataIntegrityViolationException.class);

        // Mock MongoTemplate
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        Mockito.when(mockMongoTemplate.save(Mockito.any(), Mockito.anyString())).thenThrow(expectedException);
        ReflectionTestUtils.setField(context.getBean(PlanetMongoDbRepository.class), "mongoTemplate", mockMongoTemplate);

        // Act
        ResponseEntity<PlanetDto> result = planetController.save(planetDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
        Mockito.verify(mockMongoTemplate).save(Mockito.any(), Mockito.anyString());
    }

    @Test
    public void testDelete() {
        // Arrange
        // Mock MongoTemplate
        DeleteResult mockDeleteResult = Mockito.mock(DeleteResult.class);
        Mockito.when(mockDeleteResult.wasAcknowledged()).thenReturn(true);
        Mockito.when(mockDeleteResult.getDeletedCount()).thenReturn(1L);
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        Mockito.when(mockMongoTemplate.remove(Query.query(Criteria.where("_id").is("5d13c0096c447e9610850ff0")), "planets")).thenReturn(mockDeleteResult);
        ReflectionTestUtils.setField(context.getBean(PlanetMongoDbRepository.class), "mongoTemplate", mockMongoTemplate);

        // Act
        ResponseEntity result = planetController.delete("5d13c0096c447e9610850ff0");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        Mockito.verify(mockDeleteResult).wasAcknowledged();
        Mockito.verify(mockDeleteResult).getDeletedCount();
        Mockito.verify(mockMongoTemplate).remove(Query.query(Criteria.where("_id").is("5d13c0096c447e9610850ff0")), "planets");
    }

    @Test
    public void testDelete_NotFound() {
        // Arrange
        // Mock MongoTemplate
        DeleteResult mockDeleteResult = Mockito.mock(DeleteResult.class);
        Mockito.when(mockDeleteResult.wasAcknowledged()).thenReturn(true);
        Mockito.when(mockDeleteResult.getDeletedCount()).thenReturn(0L);
        MongoTemplate mockMongoTemplate = Mockito.mock(MongoTemplate.class);
        Mockito.when(mockMongoTemplate.remove(Query.query(Criteria.where("_id").is("5d13cd41a1294f701239fed7")), "planets")).thenReturn(mockDeleteResult);
        ReflectionTestUtils.setField(context.getBean(PlanetMongoDbRepository.class), "mongoTemplate", mockMongoTemplate);

        // Act
        ResponseEntity result = planetController.delete("5d13cd41a1294f701239fed7");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
        Mockito.verify(mockDeleteResult).wasAcknowledged();
        Mockito.verify(mockDeleteResult).getDeletedCount();
        Mockito.verify(mockMongoTemplate).remove(Query.query(Criteria.where("_id").is("5d13cd41a1294f701239fed7")), "planets");
    }
}
