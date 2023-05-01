package com.rashad.jtsgeojson.model.bina;

import lombok.Data;

import java.util.Map;

@Data
public class FeatureBina {

    private String type;
    private GeometryBina geometry;
    private Map<String, Object> properties;
    private String id;
}
