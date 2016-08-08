package com.lee.cameratest.Camera.bean;

/**
 * wifi接口响应
 */
public class Response {


    /**
     * This error is returned for all miscellaneous problems. Other error values are relatively more speci c.
     If an attempted command results in a problem, but the actual cause is not known, this error code is returned.
     */
    public static final int UNKNOWN_ERROR = -1;
    /**
     * This is the response to AMBA_START_SESSION if session creation fails.
     */
    public static final int SESSION_START_FAIL = -3;
    /**
     * This error indicates that the client sends a command with invalid_token, which means that either the client has never successfully acquire a valid token or the client has lost its original session token to another device.
     */
    public static final int INVALID_TOKEN = -4;
    /**
     * This error indicates that the camera has reached the maximum number of simultaneous client con- nections. When this error is returned, the camera cannot accept new connection requests.
     */
    public static final int REACH_MAX_CLNT = -5;
    /**
     * A JSON command cannot be nested. This error message is sent when two successive left braces, “{ {” are received without a  rst matching right brace “}”.
     */
    public static final int JSON_PACKAGE_ERROR = -7;
    /**
     * This error is sent to indicate a timeout on incomplete JSON commands. If a matching closing right brace “}” is not received for an opening left brace “{” within a timeout length of 5 seconds, this error is generated.
     */
    public static final int JSON_PACKAGE_TIMEOUT = -8;
    /**
     * This error code is sent in response to JSON commands containing syntax errors. The key:value pairing accepts alphanumeric characters.
     */
    public static final int JSON_SYNTAX_ERROR = -9;
    /**
     * If the AMBA_SET_SETTING command is received with an invalid option, this error is returned.
     */
    public static final int INVALID_OPTION_VALUE = -13;
    /**
     * This error is returned when an invalid or unknown command is received.
     */
    public static final int INVALID_OPERATION = -14;
    /**
     * This error is returned when a handheld attempts to execute the AMBA_START_SESSION command while the camera is connected to a HDMI device.
     */
    public static final int HDMI_INSERTED = -16;
    /**
     * This error is returned when there no more space is available in the SD card of the camera. There will be no additional photos stored nor movies recorded.
     */
    public static final int NO_MORE_SPACE = -17;
    /**
     * The camera SD card currently is locked in read only mode. The camera cannot record movies or snap pictures until the SD card is unlocked.
     */
    public static final int CARD_PROTECTED = -18;
    /**
     * This error is returned when the latest command causes the camera to exhaust the available memo- ry.
     */
    public static final int NO_MORE_MEMORY = -19;
    /**
     * This error code indicates that the Photo in Video (PIV) operation is not currently permitted. This may be caused by dedicated video resolution settings or by enabling dedicated camera functions.
     */
    public static final int PIV_NOT_ALLOWED = -20;
    /**
     * This error code is returned when the client issues a request while the camera is not in idle mode. For example, this code will be returned if the client sends a set setting request while the camera is in record mode. Another example is if the client requests to format the SD card during a  le transfer operation.
     */
    public static final int SYSTEM_BUSY = -21;
    /**
     * This error code is returned when the camera application is not initialized. For example, this code would be returned if the client requests a command while the camera is switching modes.
     */
    public static final int APP_NOT_READY = -22;
    /**
     * This error code is returned when the camera application does not support the operation requested by client as that there is no such use case in the camera application.
     */
    public static final int OPERATION_UNSUPPORTED = -23;
    /**
     * This error code is returned when the “type”  eld following the requested command is illegal.
     */
    public static final int INVALID_TYPE = -24;
    /**
     * This error code is returned when the “param”  eld following the requested command is illegal.
     */
    public static final int INVALID_PARAM = -25;
    /**
     * This error code indicates that there is no such  le or directory.
     */
    public static final int INVALID_PATH = -26;


	/**
	 * 消息id
	 */
	private int msg_id;
	/**
	 * 返回值
	 */
	private int rval = UNKNOWN_ERROR;
	/**
	 * 返回参数
	 */
	private String param;
	/**
	 * 返回参数
	 */
	private Object listing;
	private String type;
	private String options;
	private String permission;
	
	private String md5sum;

	private String thumb_file;

	private String resolution;

	private String duration;

    private int rem_size;
    private int size;

	public int getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(int msg_id) {
		this.msg_id = msg_id;
	}

	public int getRval() {
		return rval;
	}

	public void setRval(int rval) {
		this.rval = rval;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Object getListing() {
		return listing;
	}

	public void setListing(Object listing) {
		this.listing = listing;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

    @Override
    public String toString() {
        return "Response{" +
                "msg_id=" + msg_id +
                ", rval=" + rval +
                ", param='" + param + '\'' +
                ", listing=" + listing +
                ", type='" + type + '\'' +
                ", options='" + options + '\'' +
                ", permission='" + permission + '\'' +
                ", md5sum='" + md5sum + '\'' +
                ", thumb_file='" + thumb_file + '\'' +
                ", resolution='" + resolution + '\'' +
                ", duration='" + duration + '\'' +
                ", rem_size=" + rem_size +
                ", size=" + size +
                '}';
    }

    public String getMd5sum() {
		return md5sum;
	}

	public void setMd5sum(String md5sum) {
		this.md5sum = md5sum;
	}


	public String getThumb_file() {
		return thumb_file;
	}

	public void setThumb_file(String thumb_file) {
		this.thumb_file = thumb_file;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

    public int getSize() {
        return size;
    }

    public int getRem_size() {
        return rem_size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setRem_size(int rem_size) {
        this.rem_size = rem_size;
    }
}
