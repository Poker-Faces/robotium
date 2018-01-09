package com.sunnymum.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.robotium.solo.Solo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 常用方法
 * 
 * @author lucky
 *
 */
@SuppressLint("SimpleDateFormat")
public class Base {
	public final static int match = 1;
	public final static String tag = "Logs";
	public final static int time = 5;

	/**
	 * 获取钱包余额
	 * 
	 * @param solo
	 * @return sum
	 */
	public final static Float getBlack(Solo solo) {
		TextView tvGold = (TextView) solo.getView(Rid.tv_cost);
		String goldSum = tvGold.getText().toString();// 获取余额.substring(0, 5)
		Float sum = Float.valueOf(goldSum);
		return sum;
	}

	/**
	 * 获取支付金额已完成页面金额获取一致
	 * 
	 * @param solo
	 * @return sum
	 */
	public final static Float getPaymentAmount(Solo solo) {
		TextView tvGold = (TextView) solo.getView(Rid.tv_paymoney);
		String goldSum = tvGold.getText().toString().substring(1);// 合计金额从¥后开始截取，例¥5.00，取5.00；
		Float sum = Float.valueOf(goldSum);
		return sum;
	}

	/**
	 * 获取抵用券金额
	 * 
	 * @param solo
	 * @return sum
	 */
	public final static Float getCoupons(Solo solo) {
		TextView tvGold = (TextView) solo.getView(Rid.tv_discount);
		String goldSum = tvGold.getText().toString();// .substring()
		Float sum = Float.valueOf(goldSum);
		return sum;
	}

	/**
	 * 获取医院名称
	 * 
	 * @param solo
	 * @return name
	 */
	public final static String getHospitalName(Solo solo) {
		TextView tvGold = (TextView) solo.getView(Rid.tv_hospital);
		String name = tvGold.getText().toString();
		return name;
	}

	/**
	 * 获取就诊地址
	 * 
	 * @param solo
	 * @return address
	 */
	public final static String getAddress(Solo solo) {
		TextView tvGold = (TextView) solo.getView(Rid.tv_address);
		String address = tvGold.getText().toString();
		return address;
	}

	/**
	 * 获取就诊时间
	 * 
	 * @param solo
	 * @return time
	 */
	public static final String getTime(Solo solo) {
		TextView tvGold = (TextView) solo.getView(Rid.tv_time);
		String time = tvGold.getText().toString().substring(11);
		return time;
	}

	/**
	 * 进入就我的页面
	 */
	public final static void goToMy(Solo solo) {
		solo.waitForText(Set.my, match, Set.waitTime);
		TextView tv = (TextView) solo.getView(Rid.tv_more);
		solo.clickOnView(tv);// 点击【我的】进入我的页面
		Measure.waitText(solo, Set.mall, time);
		// solo.waitForText(Set.mall, match, Set.waitTime, false);
	}

	/**
	 * 关闭更新和评价APP弹窗
	 */
	public final static void closeWindows(Solo solo) {
		String update = Set.downloadUpdate;
		String id = Rid.tv_cancle;
		Measure.waitText_and_clickOnId(solo, update, id, time);
		String evaluate = Set.evaluate;
		String id_1 = Rid.tv_clear;
		Measure.waitText_and_clickOnId(solo, evaluate, id_1, time);
	}

	/**
	 * 更换头像
	 */
	public final static void niceName(Solo solo) {
		ImageView iv = (ImageView) solo.getView(Rid.mepage_layout_top_user_img);
		solo.clickOnView(iv);// 点击头像
		EditText ev = (EditText) solo.getView(Rid.nikename);
		solo.waitForView(ev);// 清理昵称
		solo.clearEditText(ev);// 清除历史昵称
		int niceName = new Random().nextInt(Set.niceName.length);
		solo.enterText(ev, Set.niceName[niceName]);// 输入昵称
		TextView tv = (TextView) solo.getView(Rid.yuyue);
		solo.clickOnView(tv);// 点击保存按钮
		Measure.waitText(solo, Set.niceName[niceName], time);
		// solo.waitForText(Set.niceName[niceName], match, Set.waitTime, false);
	}

	/**
	 * 输入账号密码点击登录按钮
	 */
	public final static void inputNP(Solo solo) {
		for (;;) {
			boolean username = Measure.waitText(solo, Set.userName, time);
			if (username) {
				Log.i(tag, "Input-Success------------->");
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
		Measure.waitText(solo, Set.mall, time);
		// solo.waitForText(Set.mall, match, Set.waitTime,false);
	}

	/**
	 * 医生详情页购买诊室服务(不使用抵用券)
	 */
	@SuppressWarnings("unused")
	public final static void consultingRoom(Solo solo) {
		solo.clickOnView(solo.getView(Rid.date, match));
		solo.waitForText(Set.costDescription, match, Set.waitTime);
		for (int visits = 0; visits < 20; visits++) {
			solo.clickOnView(solo.getView(Rid.tv_time, visits));
			if (solo.waitForText(Set.bookingTime, match, Set.waitTime)
					&& solo.waitForText(Set.reservationsDoctor, match, Set.waitTime)
					&& solo.waitForText(Set.visitAddress, match, Set.waitTime)) {
				Log.i(tag, "The consultation times can be approximately------->");
				break;
			}
		}
		solo.clickOnView(solo.getView(Rid.toggleButton1));
		solo.scrollDown();
		solo.enterText((EditText) solo.getView(Rid.et_description), Set.description);
		solo.clickOnView(solo.getView(Rid.bt_next));
		if (solo.waitForText(Set.repeat, match, time)) {
			for (int i = 0; i < 3; i++) {
				solo.clickOnView(solo.getView(Rid.img_b));
				solo.clickOnView(solo.getView(Rid.bt_next));
				if (solo.waitForText(Set.repeat, match, time)) {
					solo.clickOnView(solo.getView(Rid.img_g));
					solo.clickOnView(solo.getView(Rid.bt_next));
					break;
				} else {
					break;
				}
			}
		}
		solo.waitForText(Set.payPrompt, match, Set.waitTime);
		// int amount = getPaymentAmount(solo);
		solo.clickOnView(solo.getView(Rid.tv_pay));
		if (solo.waitForText(Set.paymentSuccess, match, Set.waitTime, false)) {
			for (int i = 0; i < 4; i++) {
				solo.goBack();
			}
		} else {
			Log.i(tag, "Pay-Failure------------->");
		}
	}

	/**
	 * 医生详情页购买诊室服务(使用抵用券)
	 */
	@SuppressWarnings("unused")
	public final static void useCoupons(Solo solo) {
		solo.clickOnView(solo.getView(Rid.date, match));
		solo.waitForText(Set.costDescription, match, Set.waitTime);
		for (int visits = 0; visits < 20; visits++) {
			solo.clickOnView(solo.getView(Rid.tv_time, visits));
			if (solo.waitForText(Set.bookingTime, match, Set.waitTime)
					&& solo.waitForText(Set.reservationsDoctor, match, Set.waitTime)
					&& solo.waitForText(Set.visitAddress, match, Set.waitTime)) {
				Log.i(tag, "The consultation times can be approximately------->");
				break;
			}
		}
		solo.clickOnView(solo.getView(Rid.toggleButton1));
		solo.scrollDown();
		solo.enterText((EditText) solo.getView(Rid.et_description), Set.description);
		solo.clickOnView(solo.getView(Rid.bt_next));
		if (solo.waitForText(Set.repeat, match, time)) {
			for (int i = 0; i < 3; i++) {
				solo.clickOnView(solo.getView(Rid.img_b));
				solo.clickOnView(solo.getView(Rid.bt_next));
				if (solo.waitForText(Set.repeat, match, time)) {
					solo.clickOnView(solo.getView(Rid.img_g));
					solo.clickOnView(solo.getView(Rid.bt_next));
					break;
				} else {
					break;
				}
			}
		}
		solo.waitForText(Set.payPrompt, match, Set.waitTime);
		Float amount = getPaymentAmount(solo);
		solo.clickOnView(solo.getView(Rid.tv_preferential));
		Float coupons = getCoupons(solo);
		Float sum = amount - coupons;
		solo.clickOnView(solo.getView(Rid.tv_discount));
		Float afterAmount = getPaymentAmount(solo);
		if (afterAmount == sum || afterAmount != 0) {
			solo.clickOnView(solo.getView(Rid.tv_pay));
			if (solo.waitForText(Set.paymentSuccess, match, Set.waitTime, false)) {
				for (int i = 0; i < 4; i++) {
					solo.goBack();
				}
			} else {
				Log.i(tag, "Pay-Failure------------->");
			}
		} else {
			Log.i(tag, "UseFailure---------------------->");
		}
	}

	/**
	 * 1、判断登录 2、如果登录了就退出登录后重新登录 3、如果没有登录直接登录
	 */
	public final static void judgeLogin(Solo solo) {
		closeWindows(solo);
		goToMy(solo);
		boolean notlogin = Measure.waitText(solo, Set.name, time);
		if (notlogin == false) {
			Log.i(tag, "-------------------->0");
			solo.clickOnView(solo.getView(Rid.ll_01));
			Measure.waitText(solo, Set.forgetPassword, time);
			inputNP(solo);
		} else {
			solo.scrollDown();
			Log.i(tag, "-------------------->1");
			solo.clickOnView(solo.getView(Rid.setting_layout));
			solo.scrollDown();
			solo.clickOnView(solo.getView(Rid.outlogin));
			Measure.waitText(solo, Set.setting, time);
			// solo.waitForText(Set.setting, match, Set.waitTime, false);
			solo.scrollUp();
			boolean notLogin = Measure.waitText(solo, Set.notLogin, time);
			if (notLogin) {
				Log.i(tag, "-------------------->2");
				solo.clickOnView(solo.getView(Rid.ll_01));
				Measure.waitText(solo, Set.forgetPassword, time);
				// solo.waitForText(Set.forgetPassword, match, Set.waitTime,
				// false);
				inputNP(solo);
			} else {
				Log.i(tag, "-------------------->3");
				solo.scrollDown();
				solo.clickOnView(solo.getView(Rid.setting_layout));
				solo.scrollDown();
				solo.clickOnView(solo.getView(Rid.login_tv));
				Measure.waitText(solo, Set.forgetPassword, time);
				// solo.waitForText(Set.forgetPassword, match, Set.waitTime,
				// false);
				inputNP(solo);
			}
		}
	}

	/**
	 * 进入诊室页面
	 */
	public final static void goToRoom(Solo solo) {
		TextView tv = (TextView) solo.getView(Rid.tv_comment);
		solo.clickOnView(tv);
		// solo.waitForText(Set.searchText, match, Set.waitTime, false);
		Measure.waitText(solo, Set.searchText, time);
	}

	/**
	 * 点击诊室一级页面专家图标进入专家列表
	 */
	public static void goToSpecialist(Solo solo) {
		solo.clickOnView(solo.getView(Rid.rb_time1));
		solo.waitForView(solo.getView(Rid.vist_item_goodat, match));
	}

	/**
	 * 诊室医院页面进入医生详情
	 */
	public static void goToDOCHomepage(Solo solo) throws Exception {
		TextView tv = (TextView) solo.getView(Rid.vist_item_hospital, match);
		solo.clickOnView(tv);
		solo.waitForText(Set.concern, match, Set.waitTime, false);
		goToDocDetails(solo);
	}

	/**
	 * 我的页面进入钱包详情页面
	 */
	public static void goToPrice(Solo solo) {
		goToMy(solo);
		solo.clickOnView(solo.getView(Rid.user_price));
		solo.waitForText(Set.transactionLog, match, Set.waitTime);
	}

	/**
	 * 医生列表进入医生详情页
	 */
	public final static void goToDocDetails(Solo solo) {
		solo.clickOnView(solo.getView(Rid.vist_item_hospital));
		solo.waitForText(Set.concern, match, Set.waitTime);
	}

	/**
	 * 选择地区
	 */
	public final static void selectArea(Solo solo) {
		solo.clickOnView(solo.getView(Rid.location_tv));
		solo.clickOnView(solo.getView(Rid.city, Set.cityIndex));
		solo.waitForText(Set.my, match, Set.waitTime);
	}

	/**
	 * 搜索服务费医生
	 */
	public final static void searchServiceDoctor(Solo solo) {
		solo.clickOnView(solo.getView(Rid.ll_search));
		solo.enterText((EditText) solo.getView(Rid.et_content), Set.searchText1);
		solo.sendKey(KeyEvent.KEYCODE_ENTER);
		solo.waitForText(Set.searchText1, match, Set.waitTime);
	}

	/**
	 * 搜索零元医生
	 */
	public final static void searchZeroDoctor(Solo solo) {
		solo.clickOnView(solo.getView(Rid.ll_search));
		solo.enterText((EditText) solo.getView(Rid.et_content), Set.searchText2);
		solo.sendKey(KeyEvent.KEYCODE_ENTER);
		solo.waitForText(Set.searchText2, match, Set.waitTime);
	}

	/**
	 * 搜索保证金医生
	 */
	public final static void searchBondDoctorZero(Solo solo) {
		solo.clickOnView(solo.getView(Rid.ll_search));
		solo.enterText((EditText) solo.getView(Rid.et_content), Set.searchText3);
		solo.sendKey(KeyEvent.KEYCODE_ENTER);
		solo.waitForText(Set.searchText3, match, Set.waitTime);
	}

	/**
	 * 购买医生服务
	 */
	@SuppressWarnings("unused")
	public final static void payDoctorService(Solo solo) {
		Base.judgeLogin(solo);
		Base.goToPrice(solo);
		Float walletAmount = Base.getBlack(solo);
		solo.goBack();
		Base.goToRoom(solo);
		Base.searchServiceDoctor(solo);
		Base.goToDocDetails(solo);
		// solo.clickOnView(solo.getView(Rid.date, match));
		solo.waitForText(Set.costDescription, match, Set.waitTime, false);
		for (int visits = 0; visits < 20; visits++) {// 点击第一个有效诊次
			solo.clickOnView(solo.getView(Rid.tv_time, visits));
			if (solo.waitForText(Set.bookingTime, match, Set.waitTime, false)
					&& solo.waitForText(Set.reservationsDoctor, match, Set.waitTime, false)
					&& solo.waitForText(Set.visitAddress, match, Set.waitTime, false)) {
				Log.i(tag, "The consultation times can be approximately------->");
				break;
			}
		}
		String hospitalName = Base.getHospitalName(solo);
		String time = Base.getTime(solo);
		Log.i(tag, "address:" + hospitalName + "time:" + time);
		solo.clickOnView(solo.getView(Rid.toggleButton1));
		solo.scrollDown();
		solo.enterText((EditText) solo.getView(Rid.et_description), Set.description);
		solo.clickOnView(solo.getView(Rid.bt_next));
		if (solo.waitForText(Set.repeat, match, 5, false)) {// 如果已经约过诊次则修改一下性别
			for (int i = 0; i < 3; i++) {
				solo.clickOnView(solo.getView(Rid.img_b));
				solo.clickOnView(solo.getView(Rid.bt_next));
				if (solo.waitForText(Set.repeat, match, 5, false)) {
					solo.clickOnView(solo.getView(Rid.img_g));
					solo.clickOnView(solo.getView(Rid.bt_next));
					break;
				} else {
					break;
				}
			}
		}
		solo.waitForText(Set.payPrompt, match, Set.waitTime);
		// Float amount = Base.getPaymentAmount(solo);// 获取实际需要支付金额
		solo.clickOnView(solo.getView(Rid.tv_pay));
		if (solo.waitForText(Set.paymentSuccess, match, Set.waitTime, false)) {
			for (int i = 0; i < 5; i++) {// 购买成功后返回
				solo.goBack();
			}
		} else {
			Log.i(tag, "Pay-Failure------------->");
		}

	}

	/**
	 * 
	 * 进入诊室服务页面
	 * 
	 */
	public final static void goToRoomService(Solo solo) {
		Measure.waitText(solo, Set.roomService, time);
		solo.clickOnView(solo.getView(Rid.tv_yuyue));
		Measure.waitText(solo, Set.myConcern, time);
	}

	/**
	 * 上拉加载更多
	 */
	public final static void gdropUp(Solo solo, String viewId) {
		AbsListView TextView = (AbsListView) solo.getView(viewId);
		int[] location = new int[2];
		TextView.getLocationOnScreen(location);
		location[1] = location[1] + TextView.getBottom();
		// Log.i(TAG, "[Location x]: " + Integer.toString(location[0]));
		// Log.i(TAG, "[Location y]: " + Integer.toString(location[1]));
		// 获取上拉加载更多拖动点的坐标
		if (solo.waitForView(TextView)) {
			int newlistcount, listcount = TextView.getCount();
			for (int a = 0; a < 10; a++) {
				solo.scrollListToLine(TextView, listcount);
				solo.sleep(500);
				solo.drag(location[0] + 10f, location[0] + 10f, location[1] - 10f, location[0] - 100f, 50);
				solo.takeScreenshot();
				solo.sleep(2000);
				newlistcount = TextView.getCount();
				if (newlistcount == listcount) {
					break;
				} else {
					listcount = newlistcount;
					// Log.i(TAG, "[List count]: " +
					// Integer.toString(listcount));
				}
			}
		}
	}

	/**
	 * 
	 * 下拉刷新
	 */
	public final static void dropDown(Solo solo, String viewId) {
		View TextView = (View) solo.getView(viewId);
		int[] location = new int[2];
		TextView.getLocationOnScreen(location);// 获取TextView的坐标
		solo.sleep(1000);
		solo.drag(location[0] + 10, location[0] + 10, location[1], location[1] + TextView.getHeight(), 3);
	}

	/**
	 * 刷新等待
	 */
	public final static void refreshWait(Solo solo) {
		for (;;) {
			// dropDown(solo, Rid.vist_img_pho);
			String finish = Set.finished;
			Measure.waitText(solo, finish, time);
			solo.clickOnText(finish);
			solo.clickOnText(Set.ing);
			try {
				boolean wait = Measure.waitText(solo, Set.waitEvaluate, Set.actionTime);
				if (wait) {
					Log.i(tag, "get value");
					break;
				}
			} catch (Exception e) {
				Log.i(tag, "finding value");
			}
		}
	}

	/**
	 * 清理进行中列表所有订单
	 */
	@SuppressWarnings("unused")
	public final static void cleanOrders(Solo solo) {
		solo.clickOnView(solo.getView(Rid.tv_yuyue));
		for (int i = 0; i < 100; i++) {
			boolean view = Measure.waitView(solo, Rid.vist_img_pho, time);
			int count = 0;
			if (view) {
				for (int index = 0; index < 100; index++) {
					try {
						long Sys = Base.getStyTime(solo);
						Log.i(tag, "获取系统当前时间" + Sys);
						long vist = Base.getVistTime(solo, count);
						Log.i(tag, "就诊时间" + vist);
						if (vist > Sys) {
							boolean have = Measure.waitText(solo, Set.waitEvaluate, time);
							if (have) {
								evaluateOrders(solo);
							} else {
								solo.clickOnView(solo.getView(Rid.vist_img_pho));
								boolean waitT = Measure.waitText(solo, Set.cancelOder, time);
								if (waitT) {
									solo.clickOnView(solo.getView(Rid.tv_cancle));
									boolean sure = Measure.waitText(solo, Set.cancelOrder, time);
									if (sure) {
										solo.clickOnView(solo.getView(Rid.dialog_confirm));
									} else {
										solo.goBack();
									}
								} else {
									solo.goBack();
								}
							}
						}
						count += 1;
					} catch (Exception e) {
						// TODO: handle exception
						break;
					}
				}
			}
			break;
		}
		solo.goBack();
	}

	/**
	 * 评价诊室订单
	 */
	public final static void evaluateOrders(Solo solo) {
		for (;;) {
			// String act = getActivity(solo);
			boolean have = Measure.waitText(solo, Set.waitEvaluate, time);
			if (have) {
				solo.clickOnView(solo.getView(Rid.vist_item_reason));
				solo.enterText((EditText) solo.getView(Rid.question_text), Set.comment);
				boolean rid = Measure.waitView(solo, Rid.commentTag, time);
				if (rid) {
					for (int i = 0; i < 2; i++) {
						try {
							int sum = new Random().nextInt(time);
							solo.clickOnView(solo.getView(Rid.commentTag, sum));
						} catch (Exception e) {
							Log.i(tag, "can't find");
						}
					}
				}
				solo.clickOnView(solo.getView(Rid.commnit));
				boolean window = Measure.waitText(solo, Set.successfull, time);
				if (window) {
					solo.clickOnView(solo.getView(Rid.tv_confirm));
				}
				solo.goBack();
				// solo.goBackToActivity(act);
			} else {
				Log.i(tag, "All order already evaluated");
				break;
			}
		}
	}

	/**
	 * 获取当前页面的activity name
	 * 
	 * @return
	 */
	public final static String getActivity(Solo solo) {
		Activity act = solo.getCurrentActivity();// 获取Activity
		String Activity = act.getCallingActivity().toString();
		return Activity;
	}

	/**
	 * 获取当前时间
	 */
	public final static long getStyTime(Solo solo) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHHmmss");
		Date curDate = new Date(System.currentTimeMillis());
		String time = formatter.format(curDate);
		return Long.valueOf(time);
	}

	/**
	 * 获取订单就诊时间
	 * 
	 * @param solo
	 * @return time
	 */
	public static final long getVistTime(Solo solo, int index) {
		TextView tvGold = (TextView) solo.getView(Rid.vist_item_time, index);
		String yy = tvGold.getText().toString().substring(1, 5);
		String ms = tvGold.getText().toString().substring(6, 8);
		String hh = tvGold.getText().toString().substring(9, 11);
		String mm = tvGold.getText().toString().substring(12, 14);
		String ss = tvGold.getText().toString().substring(15, 17);
		String time = yy + ms + hh + mm + ss;
		Log.i(tag, "yy" + yy + "ms" + ms + "hh" + hh + "mm" + mm + "ss" + ss);
		return Long.valueOf(time);
	}
}
