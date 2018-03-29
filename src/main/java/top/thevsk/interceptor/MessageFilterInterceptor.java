package top.thevsk.interceptor;

import top.thevsk.entity.ApiRequest;

public class MessageFilterInterceptor {

    private MessageFilterInterceptor() {
    }

    private static MessageFilterInterceptor messageFilterInterceptor = new MessageFilterInterceptor();

    public static MessageFilterInterceptor getInstance() {
        return messageFilterInterceptor;
    }

    public boolean filter(String filter, ApiRequest request) {
        if (filter.startsWith("eq:")) {
            return eq(filter.replace("eq:", ""), request.getMessage());
        }
        if (filter.startsWith("eqs:")) {
            return eqs(filter.replace("eqs:", ""), request.getMessage());
        }
        if (filter.startsWith("like:")) {
            return like(filter.replace("like:", ""), request.getMessage());
        }
        if (filter.startsWith("likes:")) {
            return likes(filter.replace("likes:", ""), request.getMessage());
        }
        if (filter.startsWith("startWith:")) {
            return startWith(filter.replace("startWith:", ""), request);
        }
        if (filter.startsWith("startWiths:")) {
            return startWiths(filter.replace("startWiths:", ""), request);
        }
        if (filter.startsWith("groupId:")) {
            return idEqs(filter.replace("groupId:", ""), request.getGroupId());
        }
        if (filter.startsWith("groupIds:")) {
            return idEqs(filter.replace("groupIds:", ""), request.getGroupId());
        }
        if (filter.startsWith("userId:")) {
            return idEqs(filter.replace("userId:", ""), request.getUserId());
        }
        if (filter.startsWith("userIds:")) {
            return idEqs(filter.replace("userIds:", ""), request.getUserId());
        }
        return false;
    }

    private boolean eq(String filter, String message) {
        return filter.equals(message);
    }

    private boolean eqs(String filter, String message) {
        String[] filters = filter.split(",");
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
        String[] filters = filter.split(",");
        for (int i = 0; i < filters.length; i++) {
            if (like(filters[i], message)) {
                return true;
            }
        }
        return false;
    }

    private boolean startWith(String filter, ApiRequest apiRequest) {
        if (apiRequest.getMessage().startsWith(filter)) {
            apiRequest.set("message", apiRequest.getMessage().substring(filter.length(), apiRequest.getMessage().length()));
            return true;
        }
        return false;
    }

    private boolean startWiths(String filter, ApiRequest apiRequest) {
        String[] filters = filter.split(",");
        for (int i = 0; i < filters.length; i++) {
            if (startWith(filters[i], apiRequest)) {
                return true;
            }
        }
        return false;
    }

    private boolean idEq(String filter, Long id) {
        return Long.valueOf(filter.trim()).equals(id);
    }

    private boolean idEqs(String filter, Long id) {
        String[] filters = filter.split(",");
        for (int i = 0; i < filters.length; i++) {
            if (idEq(filters[i], id)) {
                return true;
            }
        }
        return false;
    }
}
