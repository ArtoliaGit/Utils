package com.artolia.utils.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	
	/**
	 * 读取excel
	 * @param fileName 文件名
	 * @param sheetNum 表格序号
	 * @return
	 */
	public List<List<Object>> read(String fileName, int sheetNum, int beginRow) {
		XSSFWorkbook wb = null;
		List<List<Object>> list = new ArrayList<>();
		try {
			InputStream ips = new FileInputStream(fileName);
			wb = new XSSFWorkbook(ips);
			XSSFSheet sheet = wb.getSheetAt(sheetNum);
			int rowIndex = 0;
			for (Iterator<Row> itr = sheet.rowIterator(); itr.hasNext();) {
				if (rowIndex < beginRow) continue;
				XSSFRow row = (XSSFRow) itr.next();
				List<Object> rowList = new ArrayList<>();
				for (Iterator<Cell> itc = row.cellIterator(); itc.hasNext();) {
					XSSFCell cell = (XSSFCell) itc.next();
					System.out.println(cell.getRawValue());
					switch (cell.getCellTypeEnum()) {
					case BOOLEAN:
						rowList.add(cell.getBooleanCellValue());
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							rowList.add(cell.getDateCellValue());
						} else {
							rowList.add(cell.getNumericCellValue());
						}
						break;
					case FORMULA:
						rowList.add(cell.getCellFormula());
						break;
					case STRING:
						rowList.add(cell.getStringCellValue());
						break;
					default:
						rowList.add(cell.getRawValue());
						break;
					}
				}
				list.add(rowList);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
}
