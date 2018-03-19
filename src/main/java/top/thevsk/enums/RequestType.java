package top.thevsk.enums;

public enum RequestType {

    DEFAULT(""),
    FRIEND("friend"),
    GROUP("group");

    private String code;

    public String getCode() {
        return code;
    }

    RequestType(String code) {
        this.code = code;
    }
}