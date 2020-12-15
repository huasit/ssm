package com.huasit.ssm.system.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
public class ExcelUtil {

    /**
     *
     */
    public static Workbook getWorkbook(File file) throws IOException {
        Workbook workbook;
        String fileName = file.getName();
        InputStream inputStream = new FileInputStream(file);
        if (fileName.toLowerCase().endsWith(".xls")) {
            workbook = new HSSFWorkbook(new POIFSFileSystem(inputStream));
        } else if (fileName.toLowerCase().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            throw new IOException();
        }
        return workbook;
    }

    /**
     *
     */
    public static Float getFloatValue(Sheet sheet, int rowNum, int cellNum) {
        Float value;
        try {
            value = Float.parseFloat(getStringValue(sheet, rowNum, cellNum));
        } catch (Exception e) {
            return null;
        }
        return value;
    }

    /**
     *
     */
    public static Integer getIntegerValue(Sheet sheet, int rowNum, int cellNum) {
        String str = getStringValue(sheet, rowNum, cellNum);
        if (str == null) {
            return null;
        }
        Float value;
        try {
            value = Float.parseFloat(str);
        } catch (Exception e) {
            return null;
        }
        return value.intValue();
    }

    /**
     *
     */
    public static BigDecimal getBigDecimalValue(Sheet sheet, int rowNum, int cellNum) {
        String str = getStringValue(sheet, rowNum, cellNum);
        if (str == null) {
            return new BigDecimal(0);
        }
        str = str.trim().replace(",", "");
        boolean fs = false;
        if (str.startsWith("-")) {
            fs = true;
            str = str.substring(1, str.length());
        }
        str = str.replace("-", "");
        if ("".equals(str)) {
            return new BigDecimal(0);
        }
        BigDecimal value;
        try {
            value = new BigDecimal(str);
        } catch (Exception e) {
            return null;
        }
        return fs ? (value.multiply(new BigDecimal(-1))) : value;
    }

    /**
     *
     */
    public static String getStringValue(Sheet sheet, int rowNum, int cellNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            return null;
        }
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            return null;
        }
        return cell.toString().replace("\r", "").replace("\n", "");
    }

    /**
     *
     */
    public static String getStringValueWithTrim(Sheet sheet, int rowNum, int cellNum) {
        String s = getStringValue(sheet, rowNum, cellNum);
        if (s != null) {
            s = s.trim();
            if (s.endsWith(".0")) {
                s = s.substring(0, s.length() - 2);
            }
        }
        return s;
    }

    /**
     *
     */
    public static HSSFWorkbook export(String[] titles, List<Object[]> datas) {
        // 创建Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        int sheetIndex = 0;
        int sheetMaxRows = 65534;
        while (true) {
            sheetIndex++;
            HSSFSheet sheet = workbook.createSheet("sheet" + sheetIndex);
            // 表头标题样式
            HSSFFont headfont = workbook.createFont();
            headfont.setFontName("宋体");
            headfont.setFontHeightInPoints((short) 12);
            HSSFCellStyle headStyle = workbook.createCellStyle();
            headStyle.setFont(headfont);
            headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            headStyle.setLocked(true);
            // 文本列名样式
            HSSFFont font = workbook.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);// 字体大小
            HSSFCellStyle textStyle = workbook.createCellStyle();
            textStyle.setFont(font);
            textStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
            textStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            textStyle.setLocked(true);
            // 数据列名样式
            HSSFCellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setFont(font);
            numberStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 左右居中
            numberStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            numberStyle.setLocked(true);
            Row titleRow = sheet.createRow(0);
            for (int n = 0; n < titles.length; n++) {
                Cell cell = titleRow.createCell(n);
                cell.setCellValue(titles[n]);
                cell.setCellStyle(headStyle);
            }
            if (datas != null) {
                int rowNum = 1;
                int trueSize = datas.size() - (sheetMaxRows * (sheetIndex - 1));
                for (int m = 0; m < (trueSize > sheetMaxRows ? sheetMaxRows : trueSize); m++) {
                    Object[] data = datas.get(m - trueSize + datas.size());
                    Row dataRow = sheet.createRow(rowNum++);
                    for (int n = 0; n < data.length; n++) {
                        Cell cell = dataRow.createCell(n);
                        String text = data[n] == null ? "" : data[n].toString();
                        cell.setCellValue(text);
                        cell.setCellStyle(numberStyle);
                    }
                }
                if (trueSize <= sheetMaxRows) {
                    break;
                }
            }
        }
        return workbook;
    }
}