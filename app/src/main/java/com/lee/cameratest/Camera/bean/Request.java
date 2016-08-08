package com.lee.cameratest.Camera.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 发送信息
 */
public class Request {

    // {"msg_id" : 9,"param" : "camera_clock","token" : 2}
    // {"msg_id" : 15,"token" : 6,"type" : "current"}
    /**
     * 消息定义名称
     */
    public String msgName;
    /**
     * 消息id
     */
    public int msg_id;
    /**
     * 令牌
     */
    public int token;
    /**
     * 传递参数
     */
    public String param;
    /**
     * 类型
     */
    public String type;


    public Request() {
        super();
    }

    /**
     * @param msgName 消息定义名称
     * @param msg_id  消息id
     */
    public Request(String msgName, int msg_id) {
        super();
        this.msgName = msgName;
        this.msg_id = msg_id;
    }

    /**
     * @param msgName 消息定义名称
     * @param msg_id  消息id
     * @param token   令牌
     * @param param   传递参数
     * @param type    类型
     */

    public Request(String msgName, int msg_id, int token, String param, String type) {
        super();
        this.msgName = msgName;
        this.msg_id = msg_id;
        this.token = token;
        this.param = param;
        this.type = type;
    }

    /**
     * @param msg_id 消息id
     * @param param  传递参数
     * @param type   类型
     */
    public Request(int msg_id, String param, String type) {
        super();
        this.msg_id = msg_id;
        this.param = param;
        this.type = type;
    }

    public Request(int msg_id){
        this.msg_id = msg_id;
    }



    /**
     * @param msg_id 消息id
     * @param token  令牌
     * @param param  传递参数
     * @param type   类型
     */
    public Request(int msg_id, int token, String param, String type) {
        super();
        this.msg_id = msg_id;
        this.token = token;
        this.param = param;
        this.type = type;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }


    @Override
    public String toString() {
        return "Request{" +
                "msgName='" + msgName + '\'' +
                ", msg_id=" + msg_id +
                ", token=" + token +
                ", param='" + param + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    /**
     * 产生json字符串
     *
     * @return
     */
    public String toJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("msg_id", msg_id);
            jsonObj.put("token", token);
            if (param != null) {
                jsonObj.put("param", param);
            }
            if (type != null){
                jsonObj.put("type", type);
            }
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

}
