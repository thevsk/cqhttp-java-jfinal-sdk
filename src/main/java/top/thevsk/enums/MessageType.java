package top.thevsk.enums;

public enum MessageType {

    DEFAULT(""),
    PRIVATE("private"),
    GROUP("group"),
    DISCUSS("discuss");

    private String code;

    public String getCode() {
        return code;
    }

    MessageType(String code) {
        this.code = code;
    }
}