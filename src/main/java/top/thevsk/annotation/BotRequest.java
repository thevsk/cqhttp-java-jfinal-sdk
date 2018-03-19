package top.thevsk.annotation;

import top.thevsk.enums.RequestType;
import top.thevsk.enums.SubType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BotRequest {
    RequestType requestType() default RequestType.DEFAULT;

    SubType subType() default SubType.DEFAULT;
}