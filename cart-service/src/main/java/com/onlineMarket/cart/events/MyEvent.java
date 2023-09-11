package com.onlineMarket.cart.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MyEvent extends ApplicationEvent {
    private final String message;

    public MyEvent(Object source, String message) {
        super(source);
        this.message= message;
    }
}
