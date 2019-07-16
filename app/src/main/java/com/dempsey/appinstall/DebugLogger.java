package com.dempsey.appinstall;

import android.util.Log;

import java.util.Formatter;

/**
 * 日志工具类 在发布时不显示日志
 */
public class DebugLogger {

	static String className;
	static String methodName;
	static int lineNumber;
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	private DebugLogger() {
		/* Protect from instantiations */
	}

	public static boolean isDebuggable() {
		return BuildConfig.DEBUG;
	}

	private static String createLog(String log) {

		// StringBuffer buffer = new StringBuffer();
		// buffer.append("[");
		// buffer.append(methodName);
		// buffer.append(":");
		// buffer.append(lineNumber);
		// buffer.append("]");
		// buffer.append(LINE_SEPARATOR);
		// buffer.append(log);

		// return buffer.toString();
		String head = new Formatter().format(
				"Thread: %s, %s(%s.java:%d)" + LINE_SEPARATOR,
				Thread.currentThread().getName(), methodName, className,
				lineNumber).toString();
		return "║ " + head + "║ " + log;
	}

	private static void getMethodNames() {
		StackTraceElement[] targetElements = Thread.currentThread()
												   .getStackTrace();
		StackTraceElement   targetElement;
		if (targetElements.length <= 4) {
			targetElement = targetElements[targetElements.length - 1];
		} else {

			targetElement = targetElements[4];
		}

		// className = sElements[1].getFileName();
		// methodName = sElements[1].getMethodName();
		// lineNumber = sElements[1].getLineNumber();
		className = targetElement.getClassName();
		String[] classNameInfo = className.split("\\.");
		if (classNameInfo.length > 0) {
			className = classNameInfo[classNameInfo.length - 1];
		}
		if (className.contains("$")) {
			className = className.split("\\$")[0];
		}
		methodName = targetElement.getMethodName();
		lineNumber = targetElement.getLineNumber();
	}
	private static void getPreMethodNames(int index) {
		StackTraceElement[] targetElements = Thread.currentThread()
				.getStackTrace();
		StackTraceElement   targetElement;
		if (targetElements.length <= index) {
			targetElement = targetElements[targetElements.length -1];
		} else {

			targetElement = targetElements[index];
		}

		// className = sElements[1].getFileName();
		// methodName = sElements[1].getMethodName();
		// lineNumber = sElements[1].getLineNumber();
		className = targetElement.getClassName();
		String[] classNameInfo = className.split("\\.");
		if (classNameInfo.length > 0) {
			className = classNameInfo[classNameInfo.length - 1];
		}
		if (className.contains("$")) {
			className = className.split("\\$")[0];
		}
		methodName = targetElement.getMethodName();
		lineNumber = targetElement.getLineNumber();
	}
	public static void e(String message) {
		if (!isDebuggable())
			return;

		// Throwable instance must be created before any methods
		getMethodNames();
		Log.e(className, createLog(message));
	}

	public static void i(String message) {
		if (!isDebuggable())
			return;

		getMethodNames();
		Log.i(className, createLog(message));
	}

	public static void d(String message) {
		if (!isDebuggable())
			return;

		getMethodNames();
		Log.d(className, createLog(message));
	}

	public static void v(String message) {
		if (!isDebuggable())
			return;

		getMethodNames();
		Log.v(className, createLog(message));
	}

	public static void w(String message) {
		if (!isDebuggable())
			return;

		getMethodNames();
		Log.w(className, createLog(message));
	}

	public static void wtf(String message) {
		if (!isDebuggable())
			return;

		getMethodNames();
		Log.wtf(className, createLog(message));
	}

	public static void e(String message, Throwable t) {
		if (!isDebuggable()) {
			return;
		}

		// Throwable instance must be created before any methods
		getMethodNames();
		Log.e(className, createLog(message), t);
	}
	public static void printStack(){

		Log.e(className,  Log.getStackTraceString(new Exception()));
	}
	public static void printStack(String msg){

		Log.e(className,msg+"\n"+  Log.getStackTraceString(new Exception()));
	}
	public static void print2PreMethod(String message){

			if (!isDebuggable())
				return;

			getPreMethodNames(6);
			Log.i(className, createLog(message));

	}

	public static void print3PreMethod(String msg) {


			if (!isDebuggable())
				return;

			getPreMethodNames(7);
			Log.i(className, createLog(msg));


	}
}
