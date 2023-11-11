package org.application;

import config.DataPreparation;
import service.ApiDadata;
import service.ExcelDataOutput;

import java.io.FileNotFoundException;

public class App{
    public static void main( String[] args ) throws FileNotFoundException {
        ExcelDataOutput excel = ExcelDataOutput.getInstance();
        excel.write("127567", "Иваново", "Lybimova", "14");
        excel.write("777777", "Armavir", "Moskovskay", "17");
        excel.save();
    }
}
