package com.rashad.jtsgeojson.controller;

import com.rashad.jtsgeojson.service.GeojsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
public class GeojsonController {

    private final GeojsonService geojsonService;

    @GetMapping
    public String getAllBuildings() {
        return geojsonService.getAllBuildings();
    }

    @GetMapping("/{id}")
    public String getBuildingById(@PathVariable int id) {
        return geojsonService.getBuildingById(id);
    }

    @PutMapping("/{id}")
    public String updateBuildingById(@PathVariable int id,
                                     @RequestParam("file")MultipartFile multipartFile) {
        return geojsonService.updateBuildingById(id, multipartFile);
    }

    @DeleteMapping("/{id}")
    public String deleteBuildingById(@PathVariable int id) {
        return geojsonService.deleteBuildingById(id);
    }

    @PostMapping
    public int addBuilding(@RequestParam("file")MultipartFile multipartFile) {
        return geojsonService.addBuilding(multipartFile);
    }

    @GetMapping("/getPoi/{id}")
    public String getPoiByBuildingId(@PathVariable int id) {
        return geojsonService.getPoiByBuildingId(id);
    }

}
