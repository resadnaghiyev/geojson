package com.rashad.jtsgeojson.model.bina;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonBina {

    private String type;
    private FeatureBina[] features;

}
