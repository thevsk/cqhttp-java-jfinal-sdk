package top.thevsk.annotation;

import top.thevsk.enums.RequestType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BotRequest {
    RequestType requestType() default RequestType.DEFAULT;
}