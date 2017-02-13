package com.jeffhanson.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class provides methods for interfacing with MS Excel files
 *
 * @author J. Jeffrey Hanson
 */
public class MsExcelServices
{
    private static MsExcelServices instance = new MsExcelServices();

    /**
     * Singleton instantiator
     * 
     * @return
     */
    public static MsExcelServices getInstance()
    {
        return instance;
    }

    /**
     * Constructor
     */
    private MsExcelServices()
    {
        // hide from direct instantiation
    }

    /**
     * Read specific columns in all rows in a file
     * 
     * @param fileName
     * @param includeColNames
     * @return
     * @throws Exception
     */
    public Object[][] readAll(String fileName, boolean includeColNames) throws Exception
    {
        File file = new File(fileName);

        if (file.exists() == false)
        {
            throw new Exception("Could not find file: " + fileName);
        }

        ArrayList<Object[]> inventoryMovementRowsList = new ArrayList<Object[]>();

        FileInputStream inputStream = new FileInputStream(file);

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();

        int i = 0;
        int cellCount = 0;

        if (includeColNames == false && iterator.hasNext())
        {
            Row nextRow = iterator.next();
            cellCount = nextRow.getLastCellNum();
            i++;
        }

        while (iterator.hasNext())
        {
            Row nextRow = iterator.next();

            if (i == 0)
            {
                cellCount = nextRow.getLastCellNum();
            }

            i++;

            Object[] inventoryMovementRow = new Object[cellCount];

            for (int j = 0; j < cellCount; j++)
            {
                Cell cell = nextRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                inventoryMovementRow[j] = cell.toString();
            }

            /*
             * Iterator<Cell> cellIterator = nextRow.cellIterator();
             * 
             * int colIdx = 0;
             * 
             * while (cellIterator.hasNext()) { Cell cell = cellIterator.next(); inventoryMovementRow[colIdx++] = cell.toString(); }
             */
            inventoryMovementRowsList.add(inventoryMovementRow);
        }

        workbook.close();
        inputStream.close();

        Object[][] inventoryMovementRows = new Object[inventoryMovementRowsList.size()][];
        inventoryMovementRows = inventoryMovementRowsList.toArray(inventoryMovementRows);

        return inventoryMovementRows;
    }
}
