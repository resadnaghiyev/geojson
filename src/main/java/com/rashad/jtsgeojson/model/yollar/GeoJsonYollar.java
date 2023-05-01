package com.rashad.jtsgeojson.model.yollar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonYollar {

    private String type;
    private FeatureYollar[] features;

}
