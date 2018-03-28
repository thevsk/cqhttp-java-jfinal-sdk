package top.thevsk.interceptor;

public class MessageFilterInterceptor {

    private MessageFilterInterceptor(){}

    private static MessageFilterInterceptor messageFilterInterceptor = new MessageFilterInterceptor();

    public static MessageFilterInterceptor getInstance() {
        return messageFilterInterceptor;
    }

    public boolean filter(String filter, String message) {
        if (filter.startsWith("eq:")) {
            return eq(filter.replace("eq:", ""), message);
        }
        if (filter.startsWith("eqs:")) {
            return eqs(filter.replace("eqs:", ""), message);
        }
        if (filter.startsWith("like:")) {
            return like(filter.replace("like:", ""), message);
        }
        if (filter.startsWith("likes:")) {
            return likes(filter.replace("likes:", ""), message);
        }
        if (filter.startsWith("startWith:")) {
            return startWith(filter.replace("startWith:", ""), message);
        }
        if (filter.startsWith("startWiths:")) {
            return startWiths(filter.replace("startWiths:", ""), message);
        }
        return false;
    }

    private boolean eq(String filter, String message) {
        return filter.equals(message);
    }

    private boolean eqs(String filter, String message) {
        String[] filters = filter.split("|");
        for (int i = 0; i < filters.length; i++) {
            if (eq(filters[i], message)) {
                return true;
            }
        }
        return false;
    }

    private boolean like(String filter, String message) {
        return message.contains(filter);
    }

    private boolean likes(String filter, String message) {
        String[] filters = filter.split("|");
        for (int i = 0; i < filters.length; i++) {
            if (like(filters[i], message)) {
                return true;
            }
        }
        return false;
    }

    private boolean startWith(String filter, String message) {
        return message.startsWith(filter);
    }

    private boolean startWiths(String filter, String message) {
        String[] filters = filter.split("|");
        for (int i = 0; i < filters.length; i++) {
            if (startWith(filters[i], message)) {
                return true;
            }
        }
        return false;
    }
}
