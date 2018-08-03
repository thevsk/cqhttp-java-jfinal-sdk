package top.thevsk.interceptor.interfaces;

import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;

/**
 * @author thevsk
 * @Title: BotServiceAop
 * @ProjectName cqhttp-java-jfinal-sdk
 * @date 2018-08-03 14:43
 */
public interface BotServiceInterceptor {

    boolean intercept(ApiRequest request, ApiResponse response);
}
