package top.thevsk.annotation;

import top.thevsk.enums.MessageType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BotMessage {
    MessageType messageType() default MessageType.DEFAULT;
}