package org.application;

import config.DataPreparation;
import service.ApiDadata;

public class App{
    public static void main( String[] args ){
        DataPreparation.setParameters();
        ApiDadata.getAddress(DataPreparation.lat, DataPreparation.lon, DataPreparation.radius_meters);
    }
}
