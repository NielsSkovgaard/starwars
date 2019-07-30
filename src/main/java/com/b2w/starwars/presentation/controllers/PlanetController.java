package com.b2w.starwars.presentation.controllers;

import com.b2w.starwars.application.services.PlanetService;
import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.presentation.mappers.PlanetDtoMapper;
import com.b2w.starwars.presentation.models.PlanetDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ValidationException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/planets")
public class PlanetController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetController.class);

    private final PlanetService planetService;
    private final PlanetDtoMapper planetDtoMapper;

    public PlanetController(PlanetService planetService, PlanetDtoMapper planetDtoMapper) {
        this.planetService = planetService;
        this.planetDtoMapper = planetDtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<PlanetDto>> getAll() {
        List<PlanetDto> result = planetService
                .getAll()
                .stream()
                .map(planetDtoMapper::map)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<PlanetDto> getById(@PathVariable("id") String id) {
        Planet planet = planetService.getById(id);
        if (planet != null) {
            return ResponseEntity.ok(planetDtoMapper.map(planet));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/name/{name}")
    public ResponseEntity<PlanetDto> getByName(@PathVariable("name") String name) {
        Planet planet = planetService.getByName(name);
        if (planet != null) {
            return ResponseEntity.ok(planetDtoMapper.map(planet));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PlanetDto> save(@RequestBody PlanetDto planetDto) {
        try {
            PlanetDto planetDtoResult = planetDtoMapper.map(planetService.save(planetDtoMapper.map(planetDto)));
            URI planetUrl = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/id/{id}")
                    .buildAndExpand(planetDtoResult.getId())
                    .toUri();
            return ResponseEntity.created(planetUrl).build();
        } catch (ValidationException e) {
            LOGGER.error("ValidationException on save", e);
            return ResponseEntity.badRequest().build();
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("DataIntegrityViolationException on save", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        try {
            planetService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("ID not found", e);
            return ResponseEntity.notFound().build();
        }
    }
}
