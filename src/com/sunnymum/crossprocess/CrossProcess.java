package com.sunnymum.crossprocess;

import com.robotium.solo.Solo;
import com.sunnymum.utils.Rid;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author 作者 : lucky
 * @date 2017年9月6日 下午2:40:18
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
@SuppressWarnings("rawtypes")
public class CrossProcess extends ActivityInstrumentationTestCase2 {

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
	public CrossProcess() throws ClassNotFoundException {
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
	 * adb for robotium 跨进程操作
	 * 
	 * @author 作者 : lucky
	 * @date 2017年9月6日 下午2:41:48
	 * @version 1.0
	 * @throws Exception
	 */
	public void testLogin() throws Exception {

	}
}