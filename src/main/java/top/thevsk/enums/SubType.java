package top.thevsk.enums;

public enum SubType {

    DEFAULT(""),
    FRIEND("friend"),
    GROUP("group"),
    DISCUSS("discuss"),
    OTHER("other"),
    NORMAL("normal"),
    ANONYMOUS("anonymous"),
    NOTICE("notice"),
    SET("set"),
    UNSET("unset"),
    LEAVE("leave"),
    KICK("kick"),
    KICK_ME("kick_me"),
    APPROVE("approve"),
    INVITE("invite"),
    ADD("add");

    private String code;

    public String getCode() {
        return code;
    }

    SubType(String code) {
        this.code = code;
    }
}