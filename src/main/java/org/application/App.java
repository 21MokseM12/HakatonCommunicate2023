package org.application;

import config.DataPreparation;

public class App{
    public static void main( String[] args ){
        System.out.println(DataPreparation.validateLat("44.4dfgrfgrgrfgrdgsrgsrg"));
        System.out.println(DataPreparation.validateLon("47.4"));
        System.out.println(DataPreparation.validateRadiusMeters("1000"));
    }
}
