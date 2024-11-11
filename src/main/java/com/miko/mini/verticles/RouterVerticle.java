package com.miko.mini.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class RouterVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.get("/apps/schedule").handler(req -> {
            req.request().response().end("Scheduled apps for installation");
            vertx.deployVerticle(new DatabaseVerticle());
        });
        vertx.createHttpServer().requestHandler(router).listen(8081);
    }
}
