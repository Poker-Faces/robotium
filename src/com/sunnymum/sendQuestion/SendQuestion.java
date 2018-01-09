package com.sunnymum.sendQuestion;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.content.ContextWrapper;

import java.util.Random;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.sunnymum.utils.Base;
import com.sunnymum.utils.Send;
import com.sunnymum.utils.Set;

/**
 * 用户版自动派题
 * 
 * @author gds_lucky
 * 
 */
@SuppressWarnings("rawtypes")
public class SendQuestion extends ActivityInstrumentationTestCase2 {

	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.sunnymum.client.activity.main.MainLoadingActivity";
	private static Class<?> LaunchActivity;
	static {
		try {
			LaunchActivity = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	String content = "";
	public final static int match = 1;
	public final static String TAG = "Logs";
	private Solo solo;

	@SuppressWarnings("unchecked")
	public SendQuestion() throws ClassNotFoundException {
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
	 * 自动派题
	 * 
	 * @throws Exception
	 */
	public void testSQ() throws Exception {
		Base.closeWindows(solo);
		int sq = Set.question.length;// 数组区间
		Log.i(TAG, "共需派题" + sq + "道,还需派题:" + (sq - Set.sum - 1)
				+ "道----------->从第" + (Set.sum + 1)
				+ "道题开始------------------------------>Let's start.");
		for (int i = Set.sum; i < sq; i++) {
			solo.waitForText(Set.freeConsulting, match, Set.waitTime, false);
			solo.clickOnView(solo
					.getView("com.sunnymum.client:id/home_layout_search"));// 点击免费咨询
			if (solo.waitForText(Set.describe, match, Set.waitTime, false)) {
				// 登录状态了
			} else {
				Log.i(TAG, "未登录状态或登录状态被踢----->1");
				Base.inputNP(solo);
				if (solo.waitForText(Set.freeConsulting, match, Set.waitTime,
						false)) {
					solo.clickOnView(solo
							.getView("com.sunnymum.client:id/home_layout_search"));// 点击免费咨询
				}
				solo.waitForText(Set.patientVisits, match, Set.waitTime, false);
			}
			solo.clickOnView(solo
					.getView("com.sunnymum.client:id/layout_people"));// 点击健康档案
			if (solo.waitForText(Set.fileTips, match, Set.waitTime, false)) {
				// 登录状态了
			} else {
				Log.i(TAG, "未登录状态或登录状态被踢----->2");
				Base.inputNP(solo);
				if (solo.waitForText(Set.describe, match, Set.waitTime, false)) {
					solo.clickOnView(solo
							.getView("com.sunnymum.client:id/layout_people"));// 点击健康档案
					solo.waitForText(Set.fileTips, match, Set.waitTime, false);
				}
			}

			int number = new Random().nextInt(10);// 生成0~9的随机数
			if (i < 1) {
				Log.i(TAG, "生成档案随机数---->" + number);
			}
			solo.sleep(3000);
			TextView hv = (TextView) solo.getView(
					"com.sunnymum.client:id/tv_mess", number);
			solo.sleep(2000);
			solo.clickOnView(hv);// 随机选择一个健康档案
			// int dr = new Random().nextInt(Set.dossier.length);
			// solo.clickOnText(Set.dossier[dr], 0, true);
			// Log.i(TAG, "生成随机档案--------->" + Set.dossier[dr]);
			// solo.sleep(2000);
			solo.waitForText(Set.describe, match, Set.waitTime, false);
			// String[] abc = this.readFromAsset(getName());
			solo.enterText((EditText) solo
					.getView("com.sunnymum.client:id/question_text"),
					Set.question[i]);// 输入问题内容Set.question[i]
			solo.waitForText(Set.next, match, Set.waitTime, false);
			solo.clickOnView(solo
					.getView("com.sunnymum.client:id/title_right_tv"));// 点击下一步
			boolean flag = false;
			int a = 0;
			for (a = 0; a < Set.waitTime; a++) {
				if (solo.waitForText(Set.limited_0, match, 5000, false)
						&& (i + a < sq)) {
					flag = true;
					solo.clearEditText((EditText) solo
							.getView("com.sunnymum.client:id/question_text"));// 清理历史问题
					solo.enterText((EditText) solo
							.getView("com.sunnymum.client:id/question_text"),// 输入问题内容
							Set.question[i + a]);
					solo.clickOnView(solo
							.getView("com.sunnymum.client:id/title_right_tv"));// 点击下一步
					if (solo.waitForText(Set.assign, match, Set.waitTime, false)) {
						break;
					}
				} else {
					solo.waitForText(Set.assign, match, Set.waitTime, false);
					break;
				}
			}
			if (a + i >= sq) {
				break;
			}
			solo.clickOnView(solo
					.getView("com.sunnymum.client:id/has_free_doc_chec"));// 选择系统指派医生
			if (i < 1) {
				Log.i(TAG, "选择------->系统指派医生");
			}
			solo.waitForText(Set.submit, match, Set.waitTime, false);
			solo.clickOnView(solo.getView("com.sunnymum.client:id/tv_ask"));// 点击提问按钮
			if (solo.waitForText(Set.term, match, match * 2, false)) {
				for (int x = 0; x < 10; x++) {
					solo.clickOnView(solo
							.getView("com.sunnymum.client:id/tv_ask"));// 点击提问按钮
					if (x >= 3) {
						Log.i(TAG, Set.limited_1 + " <---或---> "
								+ Set.limited_2 + "或者其他原因");
						content = Set.limited_1 + " <---或---> " + Set.limited_2
								+ "或者其他原因";
						Send.sendContent(Set.ip, Set.port, content);
						break;
					} else if (solo.waitForText(Set.term, match, match * 2,
							false)) {
						solo.clickOnView(solo
								.getView("com.sunnymum.client:id/tv_ask"));// 点击提问按钮
					} else {
						break;
					}
				}
				if (solo.waitForText(Set.term, match, match * 2, false)) {
					if (flag == true) {
						Log.i(TAG, "外部原因导致派题结束，派至第" + (i + a) + "题:"
								+ Set.question[i + a - 1] + "----->还剩"
								+ (sq - i - a));
					} else {
						Log.i(TAG, "外部原因导致派题结束，派至第" + (i) + "题:"
								+ Set.question[i - 1] + "----->还剩" + (sq - i));
					}
					break;
				}
				break;
			} else if (solo.waitForText(Set.question[i], match, 2, false)
					|| solo.waitForText(Set.question[i + a], match, 2, false)) {
				if (solo.waitForText(Set.section, match, Set.waitTime, false)) {
					TextView submint = (TextView) solo
							.getView("com.sunnymum.client:id/tv_submint_01");
					solo.clickOnView(submint);// 点击倒计时按钮
					if (i < 1) {
						Log.i(TAG, "-------->点击倒计时按钮");
					}
				} else {
					solo.sleep(Set.waitTime);
					solo.goBack();
					Log.i(TAG, "--------->没有选择科室点击返回按钮");
				}
			} else {
				Log.i(TAG, "--------->其他未知情况，点击返回按钮！");
				solo.sleep(Set.waitTime / 2);
				solo.goBack();
			}
			if (flag == true) {
				Log.i(TAG, "第" + (i + 1 + a) + "题:" + Set.question[i + a]
						+ "----->还剩" + (sq - i - a - 1));
			} else {
				Log.i(TAG, "第" + (i + 1) + "题:" + Set.question[i] + "----->还剩"
						+ (sq - i - 1));
			}
			solo.waitForText(Set.admissionsTips, match, Set.waitTime, false);
			if (i == (sq - 1)) {
				break;
			} else {
				solo.goBack();
				solo.sleep(3000);
				Base.goToMy(solo);
				solo.waitForText(Set.mall, match, Set.waitTime, false);
				Base.niceName(solo);// 修改昵称
				if (i < 1) {
					Log.i(TAG, "昵称更换成功休息" + Set.interval + "秒");
				}
				solo.sleep(Set.interval);
				TextView th = (TextView) solo
						.getView("com.sunnymum.client:id/tv_home");
				solo.clickOnView(th);// 点击问诊
				if (flag == true) {
					i += a;
				}
			}
		}
		Log.i(TAG, "所有" + sq + "道问题全部发送完毕！");
	}

	/**
	 * 三、从asset中获取文件并读取数据（资源文件只能读不能写）
	 * 
	 * @param <res>
	 * 
	 * @param fileName
	 * @return
	 */
	public <res> String[] readFromAsset(String fileName) {
		String[] res = {};
		try {
			InputStream in = getResources().getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			fileName = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private ContextWrapper getResources() {
		// TODO Auto-generated method stub
		return null;
	}

}

// }