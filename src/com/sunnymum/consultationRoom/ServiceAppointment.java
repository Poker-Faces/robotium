package com.sunnymum.consultationRoom;

import com.robotium.solo.Solo;
import com.sunnymum.utils.Base;
import com.sunnymum.utils.Measure;
import com.sunnymum.utils.Rid;
import com.sunnymum.utils.Set;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

/**
 * @author 作者 :lucky
 * @date 2017年8月23日 下午5:52:46
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ServiceAppointment extends ActivityInstrumentationTestCase2 {

	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = Rid.mainActivity;
	private static Class<?> LaunchActivity;
	static {
		try {
			LaunchActivity = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	/** 元素数量 */
	public final static int match = 1;
	/** Logs日志 */
	public final static String tag = "Logs";
	/** 引用方法 */
	private Solo solo;

	@SuppressWarnings("unchecked")
	public ServiceAppointment() throws ClassNotFoundException {
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
	 * 预约服务费，并校验钱包金额变动是否正确
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public void testappointmentBond() throws Exception {
		Base.judgeLogin(solo);
		Base.cleanOrders(solo);
		Base.goToPrice(solo);
		Float walletAmount = Base.getBlack(solo);
		solo.goBack();
		Base.goToRoom(solo);
		Base.searchServiceDoctor(solo);
		Base.goToDocDetails(solo);
		solo.clickOnView(solo.getView(Rid.date, match));
		solo.waitForText(Set.costDescription, match, Set.waitTime, false);
		for (int visits = 0; visits < 20; visits++) {// 点击第一个有效诊次
			solo.clickOnView(solo.getView(Rid.tv_time, visits));
			boolean visitsAddress = Measure.waitText(solo, Set.visitAddress, 5);
			if (visitsAddress) {
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
		Float amount = Base.getPaymentAmount(solo);// 获取实际需要支付金额
		solo.clickOnView(solo.getView(Rid.tv_pay));
		if (solo.waitForText(Set.paymentSuccess, match, Set.waitTime, false)) {
			for (int i = 0; i < 5; i++) {// 购买成功后返回
				solo.goBack();
			}
		} else {
			Log.i(tag, "Pay-Failure------------->");
		}
		Base.goToPrice(solo);
		Float afterwalletAmount = Base.getBlack(solo);
		Float difference = walletAmount - afterwalletAmount;// 购买所需金额
		Log.i(tag, "fist:" + walletAmount + "-->end:" + afterwalletAmount + "-->actualPay:" + amount);
		assertEquals("Action Error------------->", amount, difference);
		solo.goBack();
		solo.clickOnView(solo.getView(Rid.tv_yuyue));
		if (solo.waitForText((hospitalName), match, Set.waitTime, false)
				|| solo.waitForText(time, match, Set.waitTime, false)) {// 判断订单是否正确
			solo.clickOnView(solo.getView(Rid.vist_img_pho));
			solo.waitForText(Set.cancelOder, match, Set.waitTime);
			solo.clickOnView(solo.getView(Rid.tv_cancle));
			if (solo.waitForText(Set.clasp, match, Set.waitTime, false)) {
				Float deducted = amount / 2;
				solo.clickOnView(solo.getView(Rid.dialog_confirm));
				solo.waitForText(Set.myConcern, match, Set.waitTime, false);
				solo.goBack();
				Base.goToPrice(solo);
				Float endAmount = Base.getBlack(solo);// 获取取消订单后余额
				Float sum = walletAmount - endAmount;// 取消订单后金额
				assertEquals("1/2Action Error------------->", deducted, sum);
			} else {
				solo.clickOnView(solo.getView(Rid.dialog_confirm));
				solo.waitForText(Set.myConcern, match, Set.waitTime, false);
				solo.goBack();
				Base.goToPrice(solo);
				Float endAmount = Base.getBlack(solo);// 获取取消订单后余额
				Float sum = walletAmount - endAmount;// 取消订单后金额
				Log.i(tag, "endAmount" + endAmount + "sum" + sum);
				assertEquals("All Action Error------------->", Set.clear, sum);
			}
		} else {
			Log.i(tag, "Oder can't find------------->");
		}
	}

	/**
	 * 购买诊室服务核销之后评价订单
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public void testVerification() throws Exception {
		Base.judgeLogin(solo);
		Base.cleanOrders(solo);
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
			boolean visitsAddress = Measure.waitText(solo, Set.visitAddress, 5);
			if (visitsAddress) {
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

		Base.goToMy(solo);
		Base.goToRoomService(solo);// 进入诊室服务页面
		for (;;) {
			Base.dropDown(solo, Rid.vist_img_pho);
			boolean wait = Measure.waitText(solo, Set.waitEvaluate, Set.actionTime);
			if (wait) {
				Log.i(tag, "wait for my want value");
				break;
			}
			solo.clickOnView(solo.getView(Rid.vist_item_reason));
			solo.enterText((EditText) solo.getView(Rid.question_text), Set.assessment);
			Measure.waitText(solo, Set.assessment, 3);
			solo.clickOnView(solo.getView(Rid.wz_p_tv));
			boolean success = Measure.waitText(solo, Set.successfull, 3);
			if (success) {
				solo.clickOnView(solo.getView(Rid.tv_confirm));
			}
			boolean txt = Measure.waitText(solo, Set.thanks, 5);
			if (txt) {
				Float singleAmount = Base.getPaymentAmount(solo);
				for (int i = 0; i < 2; i++) {
					solo.goBack();
				}
				Base.goToPrice(solo);
				Float black = Base.getBlack(solo);
				Float have = walletAmount - singleAmount;
				assertEquals("核销后金额错误", have, black);
			}
		}
	}
}