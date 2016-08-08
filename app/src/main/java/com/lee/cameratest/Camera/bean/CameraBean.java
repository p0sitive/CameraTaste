package com.lee.cameratest.Camera.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraBean implements Parcelable{
	public static final String TYPE_C1 = "type-C1";
	public static final String TYPE_M1 = "type-M1";

	private String ip;
	private String mac;
	private String name;
	private String type;

	public CameraBean(String ip, String mac, String name, String type) {
		super();
		this.ip = ip;
		this.mac = mac;
		this.name = name;
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "LMInfo{" + "ip='" + ip + '\'' + ", mac='" + mac + '\''
				+ ", name='" + name + '\'' + ", type='" + type + '\'' + '}';
	}

	public static CameraBean parseUdpMsg(String udpMsg) {
		StringTokenizer stringTokenizer = new StringTokenizer(udpMsg);
		if (stringTokenizer.countTokens() >= 3) {
			String ipToken = stringTokenizer.nextToken();
			String macToken = stringTokenizer.nextToken();
			String nameToken = stringTokenizer.nextToken();
			String typeToken = null;
			if (stringTokenizer.hasMoreTokens()) {
				typeToken = stringTokenizer.nextToken();
			}
			if (checkIp(ipToken)) {
				return new CameraBean(ipToken, macToken, nameToken, typeToken);
			}
		}
		return null;
	}

	private static boolean checkIp(String ip) {

		if (ip != null) {

			Pattern pattern = Pattern
					.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
			Matcher matcher = pattern.matcher(ip);
			return matcher.matches();
		}
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.ip);
		dest.writeString(this.mac);
		dest.writeString(this.name);
		dest.writeString(this.type);
	}

	protected CameraBean(Parcel in) {
		this.ip = in.readString();
		this.mac = in.readString();
		this.name = in.readString();
		this.type = in.readString();
	}

	public static final Creator<CameraBean> CREATOR = new Creator<CameraBean>() {
		@Override
		public CameraBean createFromParcel(Parcel source) {
			return new CameraBean(source);
		}

		@Override
		public CameraBean[] newArray(int size) {
			return new CameraBean[size];
		}
	};
}
