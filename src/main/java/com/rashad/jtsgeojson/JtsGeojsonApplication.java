package com.rashad.jtsgeojson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rashad.jtsgeojson.model.bina.FeatureBina;
import com.rashad.jtsgeojson.model.bina.GeoJsonBina;
import com.rashad.jtsgeojson.model.yollar.FeatureYollar;
import com.rashad.jtsgeojson.model.yollar.GeoJsonYollar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringBootApplication
public class JtsGeojsonApplication {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(JtsGeojsonApplication.class, args);
    }

//    @PostConstruct
    public void geoJsonBuilding() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        GeoJsonBina geoJson1 = mapper.readValue(
                new File("/Users/rashad/Desktop/Pilot Proje PDF & Data/data/bina1.geojson"), GeoJsonBina.class);
        GeoJsonBina geoJson2 = mapper.readValue(
                new File("/Users/rashad/Desktop/Pilot Proje PDF & Data/data/bina2.geojson"), GeoJsonBina.class);
        FeatureBina[] buildings1 = geoJson1.getFeatures();
        FeatureBina[] buildings2 = geoJson2.getFeatures();

        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO bina (geom) VALUES (ST_SetSRID(ST_GeomFromGeoJSON(?), 4326));";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (FeatureBina featureBina : buildings1) {
                    pstmt.setString(1, featureBina.getGeometry().toString());
                    pstmt.executeUpdate();
                }
                for (FeatureBina featureBina : buildings2) {
                    pstmt.setString(1, featureBina.getGeometry().toString());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("***** BITTI *****");
    }

//    @PostConstruct
    public void geoJsonRoads() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        GeoJsonYollar geoJsonYollar = mapper.readValue(
                new File("/Users/rashad/Desktop/Pilot Proje PDF & Data/data/yollar.geojson"), GeoJsonYollar.class);
        FeatureYollar[] features = geoJsonYollar.getFeatures();

        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO yollar (geom) VALUES (ST_SetSRID(ST_GeomFromGeoJSON(?), 4326));";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (FeatureYollar featureYollar : features) {
                    pstmt.setString(1, featureYollar.getGeometry().toString());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("***** BITTI *****");
    }
}