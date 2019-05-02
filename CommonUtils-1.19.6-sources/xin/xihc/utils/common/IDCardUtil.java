package xin.xihc.utils.common;

/**
 * 18位身份证号校验
 * 
 * @author 席恒昌
 * @Date 2018年3月8日
 * @version 1.0
 * @since 1.9
 * 
 */
public final class IDCardUtil {

	private static String[] lastChar = new String[] { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
	private static int[] mode = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

	public static boolean check(String certNo) {
		if (CommonUtil.isNullEmpty(certNo) || certNo.length() != 18) {
			return false;
		}
		certNo = certNo.toUpperCase();
		int sum = 0;
		for (int i = 0; i < mode.length; i++) {
			sum += Integer.parseInt(certNo.substring(i, i + 1)) * mode[i];
		}
		int temp = sum % 11;
		if (lastChar[temp].equals(certNo.substring(17, 18))) {
			return true;
		}
		return false;
	}

}
