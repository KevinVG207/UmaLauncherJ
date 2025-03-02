package com.kevinvg.umalauncherj.selenium;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@EnsureTabOpen
@Priority(2020)
@Interceptor
class TabOpenInterceptor {
    private Horsium horsium;

    @Inject
    TabOpenInterceptor(Horsium horsium) {
        this.horsium = horsium;
    }

    @AroundInvoke
    Object invoke(InvocationContext ctx) throws Exception {
        this.horsium.ensureTabOpen();
        return ctx.proceed();
    }
}
