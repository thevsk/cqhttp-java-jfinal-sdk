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
            return eqs(filter.replace("eq:", ""), request.getMessage());
        }
        if (filter.startsWith("like:")) {
            return likes(filter.replace("like:", ""), request.getMessage());
        }
        if (filter.startsWith("startWith:")) {
            return startWiths(filter.replace("startWith:", ""), request);
        }
        if (filter.startsWith("groupId:")) {
            return idEqs(filter.replace("groupId:", ""), request.getGroupId());
        }
        if (filter.startsWith("userId:")) {
            return idEqs(filter.replace("userId:", ""), request.getUserId());
        }
        return false;
    }

    private boolean eqs(String filter, String message) {
        String[] filters = filter.split(",");
        for (int i = 0; i < filters.length; i++) {
            if (filters[i].equals(message)) {
                return true;
            }
        }
        return false;
    }

    private boolean likes(String filter, String message) {
        String[] filters = filter.split(",");
        for (int i = 0; i < filters.length; i++) {
            if (message.contains(filters[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean startWiths(String filter, ApiRequest apiRequest) {
        String[] filters = filter.split(",");
        for (int i = 0; i < filters.length; i++) {
            if (apiRequest.getMessage().startsWith(filters[i])) {
                apiRequest.set("message", apiRequest.getMessage().substring(filters[i].length(), apiRequest.getMessage().length()));
                return true;
            }
        }
        return false;
    }

    private boolean idEqs(String filter, Long id) {
        String[] filters = filter.split(",");
        for (int i = 0; i < filters.length; i++) {
            if (Long.valueOf(filters[i]).equals(id)) {
                return true;
            }
        }
        return false;
    }
}
