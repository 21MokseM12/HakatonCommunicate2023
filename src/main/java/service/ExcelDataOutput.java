package service;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import java.io.*;

public class ExcelDataOutput {
    private static ExcelDataOutput instance;
    private boolean init;
     int numberOfRecords = 1;
    static HSSFWorkbook workbook = new HSSFWorkbook();
    static HSSFSheet sheet = workbook.createSheet("addresses");
    static HSSFCellStyle style = createStyleForTitle(workbook);

    static Row row;
    static Cell cell;

        private ExcelDataOutput(){}
        public static ExcelDataOutput getInstance(){
            if (instance == null){
                instance = new ExcelDataOutput();
            }
            return instance;
        }
        private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook){
            HSSFFont font = workbook.createFont();
            font.setBold(true);
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);
            return style;
        }
        private void initSelf(){
            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Почтовый индекс");
            cell.setCellStyle(style);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Город");
            cell.setCellStyle(style);
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("Улица");
            cell.setCellStyle(style);
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Номер дома");
            cell.setCellStyle(style);
            init = true;
        }
        public void write(String postal_code, String city, String street, String house){
            if (!init) initSelf();
            row = sheet.createRow(numberOfRecords);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(postal_code);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(city);
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(street);
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(house);
            numberOfRecords++;
        }
        public void save() throws FileNotFoundException {
            File file = new File(System.getProperty("user.dir"));
            try{
                FileOutputStream outFile = new FileOutputStream(file+"\\Addresses.xlx");
                workbook.write(outFile);
                System.out.println("Saved " + file+"\\Addresses.xlx");
            } catch (FileNotFoundException e){
                System.out.println("Something went wrong");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

}
