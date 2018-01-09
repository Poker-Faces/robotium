package com.sunnymum.utils;

import com.robotium.solo.Solo;

/**
 * 封装方法
 * 
 * @author lucky
 * @version 1.0
 */
public class Measure {

	/**
	 * 等待某个Text文本出现并做点击; waitText；等待的文本; id:点击的Resource-id; waitTime:等待时间秒;
	 */
	public static void waitText_and_clickOnId(Solo solo, String waitText, String Resource_id, int waitTime) {
		for (int i = 0; i < waitTime; i++) {
			if (solo.waitForText(waitText, 1, 1000, false)) {
				solo.clickOnView(solo.getView(Resource_id));
				break;
			}
		}
	}

	/**
	 * 等待某个Text文本出现并做点击; waitText：等待的文本; id:点击的Resource-id; waitTime:等待时间秒;
	 */
	public static void waitText_and_clickOnText(Solo solo, String waitText, String clickText, int waitTime) {
		for (int i = 0; i < waitTime; i++) {
			if (solo.waitForText(waitText, 1, 1000, false)) {
				solo.clickOnText(clickText);
				break;
			}
		}
	}

	/**
	 * 节省时间的等待Text Text:等待文本 waitTime:等待时间，秒
	 * 
	 * @return true
	 */
	public static boolean waitText(Solo solo, String Text, int waitTime) {
		try {
			for (int i = 0; i < waitTime; i++) {
				if (solo.waitForText(Text, 1, 1000, true, false)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 等待页面view
	 */
	public static boolean waitView(Solo solo, String Rid, int waitTime) {
		try {
			for (int i = 0; i < waitTime; i++) {
				if (solo.waitForView(solo.getView(Rid), 1000, true)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 需要返回多少次，1代表一次
	 * 
	 * @param solo
	 * @param match
	 */
	public static void goBack(Solo solo, int match) {
		for (int i = 0; i < match; i++) {
			solo.goBack();
		}
	}
}