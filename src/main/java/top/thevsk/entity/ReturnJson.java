package top.thevsk.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ReturnJson {
    private String status;

    private Integer retcode;

    private JSONObject data;

    private JSONArray dataList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRetcode() {
        return retcode;
    }

    public void setRetcode(Integer retcode) {
        this.retcode = retcode;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONArray getDataList() {
        return dataList;
    }

    public void setDataList(JSONArray dataList) {
        this.dataList = dataList;
    }

    public ReturnJson(String status, Integer retcode) {
        this.status = status;
        this.retcode = retcode;
    }

    public ReturnJson() {
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
