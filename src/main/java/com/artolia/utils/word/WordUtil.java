package com.artolia.utils.word;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import sun.misc.BASE64Encoder;

public class WordUtil {

	public void createWord(Map<String, Object> dataMap, String templateName, String filePath, String fileName) {
		try {
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
			configuration.setDefaultEncoding("UTF-8");
			configuration.setClassForTemplateLoading(WordUtil.class, "/template");
			Template template = configuration.getTemplate(templateName);
			File outFile = new File(filePath + File.separator + fileName);
			System.out.println(outFile.getPath());
			if (!outFile.getParentFile().exists()) {
				outFile.getParentFile().mkdirs();
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
			template.process(dataMap, out);
			out.flush();
			out.close();
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("username", "张三");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		dataMap.put("currDate", sdf.format(new Date()));
		
		dataMap.put("content", "这是正文");
		
		try {
			FileInputStream fis = new FileInputStream(new File("D:\\下载\\图片\\55474959_p0.jpg"));
			byte[] data = new byte[fis.available()];
			fis.read(data);
			fis.close();
			
			BASE64Encoder encoder = new BASE64Encoder();
			dataMap.put("image", encoder.encode(data));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Map<String, Object>> newList = new ArrayList<>();
		for (int i = 1; i <= 10; i ++) {
			Map<String, Object> map = new HashMap<>();
			map.put("title", "标题" + i);
			map.put("content", "内容" + i);
			map.put("author", "作者" + i);
			newList.add(map);
		}
		dataMap.put("newList", newList);
		
		Random r = new Random();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
		StringBuffer sb = new StringBuffer();
		sb.append(sdf1.format(new Date()));
		sb.append("_");
		sb.append(r.nextInt(100));
		
		String filePath = "/upload";
		
		String fileOnlyName = "导出word" + sb + ".doc";
		String fileName = "导出word.doc";
		
		createWord(dataMap, "freemarker模板.ftl", filePath, fileOnlyName);
	}
}
