package mx.com.core.utilidades;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelManager {

    public static List read(File xlsxFile) throws IOException {
        FileInputStream fis = new FileInputStream(xlsxFile);
        List data = new ArrayList<>();
        List dataRow = null;

        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);

        // Return first sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);

        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = mySheet.iterator();

        // Traversing over each row of XLSX file
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            dataRow = new ArrayList();

            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        dataRow.add(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        dataRow.add(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        dataRow.add(cell.getBooleanCellValue());
                        break;
                }
            }

            data.add(dataRow);
        }

        return data;
    }

    public static void write(File archivo, List<List> data) throws IOException {
        String nombreHoja1 = "Hoja1";//nombre de la hoja1

        XSSFWorkbook libroTrabajo = new XSSFWorkbook();
        XSSFSheet hoja1 = libroTrabajo.createSheet(nombreHoja1) ;


        //iterando numero de filas (r)
        for (int r = 0; r < data.size(); r++ ) {
            XSSFRow row = hoja1.createRow(r);

            List dataRow = data.get(r);
            //iterando numero de columnas (c)
            for (int c=0; c < dataRow.size(); c++ ){
                XSSFCell cell = row.createCell(c);
                cell.setCellValue(dataRow.get(c).toString());
            }
        }

        FileOutputStream fileOut = new FileOutputStream(archivo);

        //escribir este libro en un OutputStream.
        libroTrabajo.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    public static void main(String[] args) {
        try {
            File file = new File("/home/elver/PruebaExcel.xlsx");
            File file2 = new File("/home/elver/PruebaExcel2.xlsx");
            List data = ExcelManager.read(file);
            System.out.println(data);
            ExcelManager.write(file2, data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
