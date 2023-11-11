package org.application;

import config.DataPreparation;
import service.ApiDadata;
import service.ExcelDataOutput;

import java.io.FileNotFoundException;

public class App{
    public static void main( String[] args ) throws FileNotFoundException {
        DataPreparation.setParameters();
       ApiDadata.getAddress(DataPreparation.lat, DataPreparation.lon, DataPreparation.radius_meters);

    }
}
