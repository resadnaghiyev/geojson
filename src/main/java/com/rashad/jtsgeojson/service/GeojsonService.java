package com.rashad.jtsgeojson.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rashad.jtsgeojson.model.bina.FeatureBina;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
@RequiredArgsConstructor
public class GeojsonService {

    private final DataSource dataSource;

    public String getAllBuildings() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            String query = "SELECT get_buildings_geojson()";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBuildingById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT get_one_building_geojson(?)")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String updateBuildingById(int id, MultipartFile multipartFile) {
        ObjectMapper mapper = new ObjectMapper();
        FeatureBina geoJson;
        try {
            geoJson = mapper.readValue(multipartFile.getBytes(), FeatureBina.class);
            System.out.println(geoJson.getGeometry().getType());
            System.out.println(geoJson.getGeometry().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE bina SET geom = ST_SetSRID(ST_GeomFromGeoJSON(?), 4326) WHERE id = ?")) {
            stmt.setString(1, geoJson.getGeometry().toString());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getBuildingById(id);
    }

    public String deleteBuildingById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM bina WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "id: " + id + " deleted!";
    }

    public int addBuilding(MultipartFile multipartFile) {
        ObjectMapper mapper = new ObjectMapper();
        FeatureBina geoJson;
        try {
            geoJson = mapper.readValue(multipartFile.getBytes(), FeatureBina.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO bina (geom) VALUES (ST_SetSRID(ST_GeomFromGeoJSON(?), 4326)) RETURNING id;")) {
            stmt.setString(1, geoJson.getGeometry().toString());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getPoiByBuildingId(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT get_points_in_building(?)")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}


