package com.artolia.utils.idcard;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.artolia.utils.excel.ExcelReaderUtil;

public class IDCardGenerator {

	/**
	 * 身份证号加权因子
	 */
	private static final int[] W = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	
	public static String generate() {
		return computeIdCard();
	}
	
	private static String computeIdCard() {
		try {
			String path = "D:\\project\\eclipse-oxygen\\Utils\\src\\main\\resources\\行政区划代码200712.xlsx";
			List<Object> list = ExcelReaderUtil.readExcel(path);
			long seed = System.nanoTime();
			Random rand = new Random(seed);
			int randIndex = rand.nextInt(list.size());
			Object[] codeObj = (Object[]) list.get(randIndex);
			String code = codeObj[0] == null ? "110105" : codeObj[0].toString();
			int curYear = LocalDate.now().getYear();
			int year = rand.nextInt(100) + curYear - 100;
			int month = rand.nextInt(12) + 1;
			int day = rand.nextInt(LocalDate.of(year, month, 1).lengthOfMonth()) + 1;
			int sequenceCode = rand.nextInt(999) + 1;
			String yearStr = String.valueOf(year);
			String monthStr = StringUtils.leftPad(String.valueOf(month), 2, '0');
			String dayStr = StringUtils.leftPad(String.valueOf(day), 2, '0');
			String sequenceCodeStr = StringUtils.leftPad(String.valueOf(sequenceCode), 3, '0');
			String masterNumber = code + yearStr + monthStr + dayStr + sequenceCodeStr;
			String checkNumber = computeCheckNumber(masterNumber);
			String idCard = masterNumber + checkNumber;
			return idCard;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String computeCheckNumber(String masterNumber) {
		char[] masterNumberArray = masterNumber.toCharArray();
		int sum = 0;
		for (int i = 0; i < W.length; i ++) {
			sum += Integer.parseInt(String.valueOf(masterNumberArray[i])) * W[i];
		}
		String[] checkNumberArray = { "1", "0", "X", "9", "8", "7", "6", "5", "4","3", "2" };
		String checkNumber = checkNumberArray[sum % 11];
		return checkNumber;
	}
}
