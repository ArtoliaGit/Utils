/**
 * 
 */
package com.artolia.utils.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author artolia
 *
 */
public class ExportExcel<T> {

	public void exportExcel(LinkedHashMap<String, String> headers, Collection<T> dataset, String filepath) {
		exportExcel(null, headers, dataset, filepath, "yyyy-MM-dd");
	}
	
	public void exportExcel07(LinkedHashMap<String, String> headers, Collection<T> dataset, String filepath) {
		exportExcel07(null, headers, dataset, filepath, "yyyy-MM-dd");
	}
	
	public void exportExcel(String title, LinkedHashMap<String, String>  headers, Collection<T> dataset, String filepath) {
		exportExcel(title, headers, dataset, filepath, "yyyy-MM-dd");
	}
	
	public void exportExcel07(String title, LinkedHashMap<String, String>  headers, Collection<T> dataset, String filepath) {
		exportExcel07(title, headers, dataset, filepath, "yyyy-MM-dd");
	}
	
	@SuppressWarnings("unchecked")
	private void exportExcel(String title, LinkedHashMap<String, String> headers, Collection<T> dataset, String filepath, String pattern) {
		//声明一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		//生成一个表格
		HSSFSheet sheet = workbook.createSheet();
		//设置表格默认列宽
		sheet.setDefaultColumnWidth((short) 15);
		//生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		//设置样式
		style.setFillForegroundColor(HSSFColorPredefined.SKY_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		//生成字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColorPredefined.VIOLET.getIndex());
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		//把字体运用到当前的样式
		style.setFont(font);
		//生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
//		style2.setFillBackgroundColor(HSSFColorPredefined.LIGHT_YELLOW.getIndex());
//		style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		//生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font.setBold(true);
		//把字体应用到当前样式
		style2.setFont(font2);
		
		//声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		//定义注释的大小和位置
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		//设置注释内容
		comment.setAuthor("artolia");
		
		int index = 0;
		HSSFRow row = sheet.createRow(index);
		if (title != null && title != "") {
			index ++;
			HSSFCell cell = row.createCell(0);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(title);
			cell.setCellValue(text);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));
		}
		
		//产生表格标题行
		row = sheet.createRow(index);
		Set<String> headers_keys = headers.keySet();
		Object[] headers_values = headers.values().toArray();
		for (short i = 0; i < headers_values.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers_values[i].toString());
			cell.setCellValue(text);
		}
		
		//遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		while (it.hasNext()) {
			index ++;
			row = sheet.createRow(index);
			Map<String, Object> map = new HashMap<>();
			T t = (T) it.next();
			if (t instanceof Map) {
				map = (Map<String, Object>) t;
			} else {
				map = objectToMap(t);
			}
			int col = 0;
			for (Iterator<String> i = headers_keys.iterator(); i.hasNext();) {
				HSSFCell cell = row.createCell(col);
				col ++;
				HSSFRichTextString text = new HSSFRichTextString(map.get(i.next()).toString());
				cell.setCellValue(text);
				cell.setCellStyle(style2);
			}
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(filepath);
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void exportExcel07(String title, LinkedHashMap<String, String> headers, Collection<T> dataset, String filepath, String pattern) {
		//声明一个工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		//生成一个表格
		XSSFSheet sheet = workbook.createSheet();
		//设置表格默认列宽
		sheet.setDefaultColumnWidth((short) 15);
		//生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		//设置样式
		style.setFillForegroundColor(HSSFColorPredefined.SKY_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		//生成字体
		XSSFFont font = workbook.createFont();
		font.setColor(HSSFColorPredefined.VIOLET.getIndex());
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		//把字体运用到当前的样式
		style.setFont(font);
		//生成并设置另一个样式
		XSSFCellStyle style2 = workbook.createCellStyle();
//		style2.setFillBackgroundColor(HSSFColorPredefined.LIGHT_YELLOW.getIndex());
//		style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		//生成另一个字体
		XSSFFont font2 = workbook.createFont();
		font.setBold(true);
		//把字体应用到当前样式
		style2.setFont(font2);
		
		int index = 0;
		XSSFRow row = sheet.createRow(index);
		if (title != null && title != "") {
			index ++;
			XSSFCell cell = row.createCell(0);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(title);
			cell.setCellValue(text);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));
		}
		
		//产生表格标题行
		row = sheet.createRow(index);
		Set<String> headers_keys = headers.keySet();
		Object[] headers_values = headers.values().toArray();
		for (short i = 0; i < headers_values.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers_values[i].toString());
			cell.setCellValue(text);
		}
		
		//遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		while (it.hasNext()) {
			index ++;
			row = sheet.createRow(index);
			Map<String, Object> map = new HashMap<>();
			T t = (T) it.next();
			if (t instanceof Map) {
				map = (Map<String, Object>) t;
			} else {
				map = objectToMap(t);
			}
			int col = 0;
			for (Iterator<String> i = headers_keys.iterator(); i.hasNext();) {
				XSSFCell cell = row.createCell(col);
				col ++;
				XSSFRichTextString text = new XSSFRichTextString(map.get(i.next()).toString());
				cell.setCellValue(text);
				cell.setCellStyle(style2);
			}
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(filepath);
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Map<String, Object> objectToMap(T t) {
		if (t == null) return null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Field[] fields = t.getClass().getDeclaredFields();
		Stream.of(fields).forEach(field -> {
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get(t));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		
		return map;
	}
}
