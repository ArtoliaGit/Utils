package com.artolia.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.artolia.utils.excel.ExcelReaderUtil;
import com.artolia.utils.excel.ExportExcel;
import com.artolia.utils.excel.ReadExcel;
import com.artolia.utils.idcard.IDCardGenerator;
import com.artolia.utils.idcard.IDCardValidate;

public class Test {
	
	private final Class<?> resourceLoaderClass = this.getClass();
//    private final ClassLoader classLoader = this.;

	@org.junit.Test
	public void test() {
		URL url = resourceLoaderClass.getResource("/poi");
		URL url2 = resourceLoaderClass.getResource("/");
		File file = new File(url.getPath() + "/freemarker模板.ftl");
		File file2 = new File(url2.getPath() + "/template2");
		if (!file2.exists()) file2.mkdir();
		file.renameTo(new File(url2.getPath() + "/template2/freemarker模板.ftl"));
//		Stream.of(file.list()).forEach(System.out::println);
		System.out.println(file.isFile());
		System.out.println(file.getName());
		System.out.println(url.getPath());
	}
	
	@org.junit.Test
	public void testExcel() {
		LinkedHashMap<String, String> headers = new LinkedHashMap<>();
		headers.put("a", "a");
		headers.put("b", "b");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", "1");
		map.put("b", "2");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		ExportExcel<Map<String, Object>> export = new ExportExcel<>();
		export.exportExcel07("", headers, list, "d:/123.xlsx");
	}
	
	@org.junit.Test
	public void testid() {
		for (int i = 0; i < 10; i ++) {
			String idCard = IDCardGenerator.generate();
			System.out.println(idCard + " " + IDCardValidate.checkIDCard(idCard));
		}
	}
	
	@org.junit.Test
	public void readExcel() {
		ReadExcel read = new ReadExcel();
		List<List<Object>> list = read.read("D:\\project\\eclipse-oxygen\\Utils\\src\\main\\java\\com\\artolia\\utils\\idcard\\行政区划代码200712.xlsx", 0, 1);
		System.out.println(list);
	}
	
	@org.junit.Test
	public void readBigExcel() throws Exception {
		String path = "D:\\project\\eclipse-oxygen\\Utils\\src\\main\\java\\com\\artolia\\utils\\idcard\\行政区划代码200712.xlsx";
		List<Object> list = ExcelReaderUtil.readExcel(path);
		list.stream().forEach(fe -> {
			System.out.println(((Object[])fe)[0] + ":" + ((Object[])fe)[1]);
		});
		System.out.println(list);
	}
}
