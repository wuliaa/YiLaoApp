package com.example.yilaoapp.chat.bean;


public  class Message {

     private String uuid;//发送消息者的uuid
     private String msgId; //信息id
     private MsgType msgType; //信息类型
     private MsgBody body;  //信息body
     private MsgSendStatus sentStatus; //信息发送状态
     private String senderId;
     private String targetId;
     private long sentTime;  //发送时间


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public MsgBody getBody() {
        return body;
    }

    public void setBody(MsgBody body) {
        this.body = body;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public MsgSendStatus getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(MsgSendStatus sentStatus) {
        this.sentStatus = sentStatus;
    }



    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }
}
