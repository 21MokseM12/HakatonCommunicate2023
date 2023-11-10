package org.spring.mvcapp;

import config.DataPreparation;
import service.DaDataAPIService;

public class App{
    public static void main( String[] args ) {
        DataPreparation.setParameters();
        DaDataAPIService daDataAPIService = new DaDataAPIService();
        daDataAPIService.getAdress();
    }

}
