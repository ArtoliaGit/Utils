package com.artolia.utils.idcard;

public class IDCardValidate {

	/**
	 * 正则表达式匹配规则
	 */
	private static final String REGEX_ID_NO_18 = "^"
			+ "\\d{6}"
			+ "(18|19|([23]\\d))\\d{2}"
			+ "((0[1-9])|(1[0-2]))"
			+ "(([0-2][1-9])|10|20|30|31)"
			+ "\\d{3}"
			+ "[0-9Xx]"
			+ "$";
	
	/**
	 * 身份证号加权因子
	 */
	private static final int[] W = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	
	public static boolean checkIDCard(String idCard) {
		if (!checkStrLength(idCard, 18)) {
			System.out.println("位数不对");
			return false;
		}
		if (!regexMatch(idCard, REGEX_ID_NO_18)) {
			System.out.println("身份证号格式错误");
			return false;
		}
		return validateCheckNumber(idCard);
	}

	/**
	 * 身份证号码规则校验
	 * @param idCard
	 * @return
	 */
	private static boolean validateCheckNumber(String idCard) {
		char[] idCardArray = idCard.toCharArray();
		int sum = 0;
		for (int i = 0; i < W.length; i ++) {
			sum += Integer.parseInt(String.valueOf(idCardArray[i])) * W[i];
		}
		if (idCardArray[17] == 'x' || idCardArray[17] == 'X') {
			sum += 10;
		} else {
			sum += Integer.parseInt(String.valueOf(idCardArray[17]));
		}
		return sum % 11 == 1;
	}

	/**
	 * 对身份证进行正则表达式校验
	 * @param idCard
	 * @param regexIdNo18
	 * @return
	 */
	private static boolean regexMatch(String idCard, String regexIdNo18) {
		return idCard.matches(regexIdNo18);
	}

	/**
	 * 验证身份证的长度
	 * @param idCard
	 * @param len
	 * @return
	 */
	private static boolean checkStrLength(String idCard, int len) {
		if (idCard == null || idCard.length() != len) return false;
		return true;
	}
	
}
