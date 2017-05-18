package com.dmall.blas.collect.util;

import org.apache.commons.lang.StringUtils;

public enum DataTypeEnum {

	LONG("LONG", new DataChecker() {
		@Override
		public boolean check(String data) {
			if (data == null) {
				return true;
			}
			return StringUtils.isNumeric(data);
		}

	}), STRING("STRING", new DataChecker() {

		@Override
		public boolean check(String data) {
			return true;
		}

	});

	private String str;
	private DataChecker checker;

	DataTypeEnum(String str, DataChecker checker) {
		setStr(str);
		setChecker(checker);
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public DataChecker getChecker() {
		return checker;
	}

	public static DataChecker getChecker(String dataType) {
		for (DataTypeEnum d : DataTypeEnum.values()) {
			if (d.getStr().equals(dataType)) {
				return d.getChecker();
			}
		}

		return null;
	}

	public void setChecker(DataChecker checker) {
		this.checker = checker;
	}

	public static boolean checkData(String dataType, String data) {
		DataChecker checker = getChecker(dataType);
		if (checker == null) {
			return true;
		}
		return checker.check(data);
	}

	static interface DataChecker {
		public boolean check(String data);
	}

}
