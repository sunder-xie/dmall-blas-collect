package com.dmall.blas.collect.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 构造解析后的事件数据查询对象
 * @Author qiang.shao
 * @Email qiang.shao@dmall.com
 * @Date 2017/3/30
 */
public class EventDataQuery implements Serializable {

    private String UUID;
    private String mwebUUID;
    private String userId;
    private Long eventId;
    private String eventCode;
    private String reportUri;

    private Date startReceiveTime;
    private Date endReceiveTime;

    public String getUUID() {
        return UUID;
    }

    public EventDataQuery setUUID(String UUID) {
        this.UUID = UUID;
        return this;
    }

    public String getMwebUUID() {
        return mwebUUID;
    }

    public EventDataQuery setMwebUUID(String mwebUUID) {
        this.mwebUUID = mwebUUID;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public EventDataQuery setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public EventDataQuery setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getEventCode() {
        return eventCode;
    }

    public EventDataQuery setEventCode(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public String getReportUri() {
        return reportUri;
    }

    public EventDataQuery setReportUri(String reportUri) {
        this.reportUri = reportUri;
        return this;
    }

    public Date getStartReceiveTime() {
        return startReceiveTime;
    }

    public EventDataQuery setStartReceiveTime(Date startReceiveTime) {
        this.startReceiveTime = startReceiveTime;
        return this;
    }

    public Date getEndReceiveTime() {
        return endReceiveTime;
    }

    public EventDataQuery setEndReceiveTime(Date endReceiveTime) {
        this.endReceiveTime = endReceiveTime;
        return this;
    }
}
