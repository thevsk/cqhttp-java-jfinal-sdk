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
        if (filter.contains("|")) {
            String[] filters = filter.split("\\|");
            for (String _filter : filters) {
                if (!filterInv(_filter, request)) return false;
            }
            return true;
        } else {
            return filterInv(filter, request);
        }
    }

    private boolean filterInv(String filter, ApiRequest request) {
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
        for (String _filter : filters) {
            if (_filter.equals(message)) return true;
        }
        return false;
    }

    private boolean likes(String filter, String message) {
        String[] filters = filter.split(",");
        for (String _filter : filters) {
            if (message.contains(_filter)) {
                return true;
            }
        }
        return false;
    }

    private boolean startWiths(String filter, ApiRequest apiRequest) {
        String[] filters = filter.split(",");
        for (String _filter : filters) {
            if (apiRequest.getMessage().startsWith(_filter)) {
                apiRequest.set("message", apiRequest.getMessage().substring(_filter.length(), apiRequest.getMessage().length()));
                return true;
            }
        }
        return false;
    }

    private boolean idEqs(String filter, Long id) {
        String[] filters = filter.split(",");
        for (String _filter : filters) {
            if (Long.valueOf(_filter).equals(id)) {
                return true;
            }
        }
        return false;
    }
}
