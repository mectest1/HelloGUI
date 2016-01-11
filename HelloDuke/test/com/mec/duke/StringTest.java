package com.mec.duke;

import java.io.PrintStream;

import org.junit.Test;

public class StringTest {

	@Test
	public void testTrimStr() {
//		fail("Not yet implemented");
		String str = "010898";
		str = fillOrSubPreString(0, "0", str);
		out.printf(str);
	}

	
	
	private static String fillOrSubPreString(int length, String preChar, String value) {
		if (value == null) {
			value = "";
		}
		int vLength = value.length();
		if (vLength > length) {
			int bIndex = -1;
			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) != '0') {
					bIndex = i;
					break;
				}
			}

			value = value.substring(bIndex);
		} else if (vLength < length) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < length - vLength; i++) {
				buffer.append(preChar);
			}
			value = buffer.append(value).toString();
		}
		return value;
	}
	
	private static final PrintStream out = System.out;
}
