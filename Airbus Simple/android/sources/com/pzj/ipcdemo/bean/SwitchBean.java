package com.pzj.ipcdemo.bean;

import com.litesuits.http.data.Consts;
import java.io.Serializable;

/* loaded from: classes.dex */
public class SwitchBean implements Serializable {
    private static final long serialVersionUID = 1;
    private int has_record_plan;
    private String motion_push_plan_enable;
    private String motion_record_plan_enable;
    private int oldfirmware;
    private String record_plan_enable;

    public int getHas_record_plan() {
        return this.has_record_plan;
    }

    public void setHas_record_plan(int i) {
        this.has_record_plan = i;
    }

    public int getOldfirmware() {
        return this.oldfirmware;
    }

    public void setOldfirmware(int i) {
        this.oldfirmware = i;
    }

    public String getMotion_record_plan_enable() {
        return this.motion_record_plan_enable;
    }

    public void setMotion_record_plan_enable(String str) {
        this.motion_record_plan_enable = str;
    }

    public String getMotion_push_plan_enable() {
        return this.motion_push_plan_enable;
    }

    public void setMotion_push_plan_enable(String str) {
        this.motion_push_plan_enable = str;
    }

    public String getRecord_plan_enable() {
        return this.record_plan_enable;
    }

    public void setRecord_plan_enable(String str) {
        this.record_plan_enable = str;
    }

    public String toString() {
        return "SdcardBean [motion_record_plan_enable=" + this.motion_record_plan_enable + ", motion_push_plan_enable=" + this.motion_push_plan_enable + ", record_plan_enable=" + this.record_plan_enable + Consts.ARRAY_ECLOSING_RIGHT;
    }
}
