package com.sunnymum.login;

import com.robotium.solo.Solo;
import com.sunnymum.utils.Base;
import com.sunnymum.utils.Rid;
import com.sunnymum.utils.Send;
import com.sunnymum.utils.Set;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 用户版登录
 * 
 * @author gds_lucky
 * 
 */
@SuppressWarnings("rawtypes")
public class MonitorLogin extends ActivityInstrumentationTestCase2 {

	String content = "";
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = Rid.mainActivity;
	private static Class<?> LaunchActivity;
	static {
		try {
			LaunchActivity = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	private Solo solo;

	@SuppressWarnings("unchecked")
	public MonitorLogin() throws ClassNotFoundException {
		super(LaunchActivity);
	}

	@Override
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		solo.takeScreenshot("tearDown");
		solo.finishOpenedActivities();
	}

	/**
	 * 能否正常登录
	 * 
	 * @throws Exception
	 */
	public void testLogin() throws Exception {
		 content = "进入成功!";// 测试邮件
		 Send.sendContent(Set.ip, Set.port, "不管你信不信，我反正是信了");
		// try {
		if (solo.waitForText(Set.network, 1, 2000, false) || solo.waitForText(Set.timeOut, 1, 2000, false)
				|| solo.waitForText(Set.networkAnomaly, 1, 2000, false)) {
			content = Set.network;// 网络连接异常邮件
			Send.sendContent(Set.ip, Set.port, content);
		} else {
			Base.closeWindows(solo);
			// AdbDevice adb = new AdbDevice();
			// Position position = new Position();
			// String number = "18501221993";
			// adb.shell("am start -a android.intent.action.CALL -d tel:" +
			// number);
			// adb.tap(position.findElementById(Rid.tv_more));
			// solo.sleep(5000);
			// adb.tap(position.findElementById(Rid.rb_time1));
			// adb.sendKeyEvent(AndroidKeyCode.BACK);
			if (solo.waitForText(Set.my, 1, 10000, false, true)) {
				TextView tv = (TextView) solo.getView(Rid.tv_more);
				solo.clickOnView(tv);// 点击【我的】进入我的页面
				if (solo.waitForText(Set.network, 1, 2000, false)
						|| solo.waitForText(Set.networkAnomaly, 1, 2000, false)) {
					content = Set.network;// 网络连接异常邮件
					Send.sendContent(Set.ip, Set.port, content);
				} else if (sunnyValue(solo) > 0) {
					solo.clickOnText(Set.setting);// 点击【设置】按钮
					solo.clickOnView(solo.getView(Rid.outlogin));// 点击【注销账号】按钮
					for (int i = 0; i < Set.time; i++) {
						if (sunnyValue(solo) == 0 && solo.waitForText(Set.setting, 1, 1000, false)) {
							Log.i("Autologs", "退出登录成功");
							break;
						} else if (i == Set.outTime) {
							content = "退出登录失败用时" + i + "秒，请查看！";// 异常邮件内容
							Send.sendContent(Set.ip, Set.port, content);
							break;
						}
					}
				}
				solo.clickOnView(solo.getView(Rid.ll_01));// 点击【登录】
				solo.waitForText(Set.forgetPassword, 1, 5000, false, true);
				for (;;) {
					if (solo.waitForText(Set.userName, 1, 1000, false)
							&& solo.waitForText(Set.passWord, 1, 1000, false)) {
						break;
					} else {
						solo.clearEditText((EditText) solo.getView(Rid.userloginname));// 存在历史账号则清除
						solo.enterText((EditText) solo.getView(Rid.userloginname), // 输入账号
								Set.userName);
						solo.clearEditText((EditText) solo.getView(Rid.password));// 存在历史密码则清除
						solo.enterText((EditText) solo.getView(Rid.password), Set.passWord);// 输入密码
					}
				}
				solo.clickOnView(solo.getView(Rid.login_tv));// 点击【登录】按钮
				for (int i = 1; i < Set.time; i++) {
					if (sunnyValue(solo) > 0) {
						break;
					} else if (solo.waitForText(Set.network, 1, 1000, false)) {
						solo.clickOnView(solo.getView(Rid.login_tv));// 点击【登录】按钮
					} else if (solo.waitForText(Set.timeOut, 1, 1000, false)) {
						solo.clickOnView(solo.getView(Rid.login_tv));// 点击【登录】按钮
					} else if (solo.waitForText(Set.networkAnomaly, 1, 1000, false)) {
						solo.clickOnView(solo.getView(Rid.login_tv));// 点击【登录】按钮
					} else if ((i * 5) == Set.overTime && sunnyValue(solo) == 0) {
						content = "加载了" + Set.overTime + "秒，未登录成功！";// 异常邮件内容
						Send.sendContent(Set.ip, Set.port, content);
						break;
					}
				}
				solo.clickOnText(Set.setting);// 点击【设置】按钮
				solo.clickOnView(solo.getView(Rid.outlogin));// 点击【注销账号】按钮
				for (int i = 0; i < Set.time; i++) {
					if (sunnyValue(solo) == 0 && solo.waitForText(Set.notLogin, 1, 1000, false)) {
						Log.i("Autologs", "退出登录成功");
						break;
					} else if (i == Set.outTime) {
						content = "退出登录失败用时" + i + "秒，请查看！";// 异常邮件内容
						Send.sendContent(Set.ip, Set.port, content);
						break;
					}
				}
			} else {
				content = Set.network + "或服务器异常！";// 网络连接异常邮件
				Send.sendContent(Set.ip, Set.port, content);
			}
		}
		// } catch (Exception e) {
		// content = "登录失败！";// 网络连接异常邮件
		// Send.sendContent(Set.ip, Set.port, content);
		// }
	}

	/**
	 * 获取阳光值
	 */
	public static int sunnyValue(Solo solo) throws Exception {
		int sv = 0;
		if (null != solo) {
			TextView tvScore = (TextView) solo.getView(Rid.user_sunnyValue);
			if (null != tvScore && null != tvScore.getText() && !"".equals(tvScore.getText().toString().trim())) {
				String scoreSum = tvScore.getText().toString().trim();
				sv = Integer.parseInt(scoreSum);
			}
		}
		return sv;
	}
}