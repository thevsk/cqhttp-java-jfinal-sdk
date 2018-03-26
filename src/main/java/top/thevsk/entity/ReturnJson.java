package top.thevsk.entity;

import java.util.Map;

public class ReturnJson {
    private String status;

    private Integer retcode;

    private Map<String, Object> data;

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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public ReturnJson(String status, Integer retcode, Map<String, Object> data) {
        this.status = status;
        this.retcode = retcode;
        this.data = data;
    }

    public ReturnJson() {
    }
}
