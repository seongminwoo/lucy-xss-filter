/*
 * @(#) XssFilterTestCase.java 2010. 8. 11 
 *
 * Copyright 2010 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.nhncorp.lucy.security.xss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * {@link XssFilterTest} 공통 코드 모음.
 * 
 * 테스트용 파일을 읽어 오는 메소드와 성능 측정용 메소드를 포함한다.
 * 
 * @author Web Platform Development Team
 */
public class XssFilterTestCase {
	private static final String CHARSET_NAME = "utf-8";

	// 클래스 경로의 파일을 읽고 라인 단위로 읽어서() List로 반환한다.
	protected List<String> readLines(String filePath) throws IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (null == classLoader) {
			classLoader = this.getClass().getClassLoader();
		}

		if (null == classLoader) {
			classLoader = ClassLoader.getSystemClassLoader();
		}

		List<String> lines = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				classLoader.getResourceAsStream(filePath), CHARSET_NAME));
		String line;
		while (null != (line = in.readLine())) {
			if (line.startsWith("#") || 0 == line.length()) {
				continue;
			}

			lines.add(line.trim());
		}
		in.close();

		return lines;
	}

	protected List<String> readString(String... filePaths) throws Exception {
		List<String> result = new ArrayList<String>();
		for (String filePath : filePaths) {
			result.add(readString(filePath));
		}
		return result;
	}

	protected String readString(String filePath) throws IOException {
		List<String> lines = readLines(filePath);
		StringBuilder buffer = new StringBuilder();
		for (String line : lines) {
			buffer.append(line);
		}

		return buffer.toString();
	}

	// loopCount가 0이면 무한 반복
	protected void printPerformance(XssFilter filter, String text, int loopCount)
			throws Exception {
		// warming
		filter.doFilter(text);

		long start = System.currentTimeMillis();
		if (0 == loopCount) {
			while (true) {
				filter.doFilter(text);
			}
		} else {
			for (int i = 0; i < loopCount; i++) {
				filter.doFilter(text);
			}
			long end = System.currentTimeMillis();
			System.out.println("size = "
					+ byteCountToDisplaySize(text.getBytes().length)
					+ ", count = " + loopCount + ", time = " + (end - start)
					+ "msec");
		}
	}

	private String byteCountToDisplaySize(long size) {
		long KB = 1024;
		long MB = KB * KB;
		long GB = KB * MB;

		if (size / GB > 0) {
			return String.valueOf(size / GB) + " GB";
		} else if (size / MB > 0) {
			return String.valueOf(size / MB) + " MB";
		} else if (size / KB > 0) {
			return String.valueOf(size / KB) + " KB";
		}
		return String.valueOf(size) + " bytes";
	}
	
	@Test
	public void dummy() {
		System.out.println("this is dummy");
	}
}