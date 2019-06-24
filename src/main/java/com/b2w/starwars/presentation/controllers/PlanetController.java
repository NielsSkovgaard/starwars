package com.b2w.starwars.presentation.controllers;

import com.b2w.starwars.application.services.PlanetService;
import com.b2w.starwars.presentation.mappers.PlanetDtoMapper;
import com.b2w.starwars.presentation.models.PlanetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/planets")
public class PlanetController {
    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlanetDtoMapper planetDtoMapper;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlanetDto> findAll() {
        return planetService
                .findAll()
                .stream()
                .map(planetDtoMapper::map)
                .collect(Collectors.toList());
    }
}
