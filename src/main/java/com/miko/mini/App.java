package com.miko.mini;

import com.miko.mini.verticles.RouterVerticle;
import io.vertx.core.Vertx;

public class App {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RouterVerticle());
    }
}
