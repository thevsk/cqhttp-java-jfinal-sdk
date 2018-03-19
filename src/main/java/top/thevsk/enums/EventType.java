package top.thevsk.enums;

public enum EventType {

    DEFAULT(""),
    GROUP_UPLOAD("group_upload"),
    GROUP_ADMIN("group_admin"),
    GROUP_DECREASE("group_decrease"),
    GROUP_INCREASE("group_increase"),
    FRIEND_ADD("friend_add");

    private String code;

    public String getCode() {
        return code;
    }

    EventType(String code) {
        this.code = code;
    }
}