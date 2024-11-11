package com.miko.mini.verticles;

import com.miko.mini.constants.EventBusTopics;
import com.miko.mini.service.InstallService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

public class InstallVerticle extends AbstractVerticle {

    private InstallService installService;

    public InstallVerticle(InstallService installService) {
        this.installService = installService;
    }

    @Override
    public void start() {
        vertx.eventBus().consumer(EventBusTopics.APP_INSTALL_TOPIC, this::pickupAppForInstallation);
    }

    private void pickupAppForInstallation(Message<Object> message) {
        System.out.println("Picked up app for installation: " + message.body());
        installService.installApp((Integer) message.body())
            .onSuccess(v -> {
            System.out.println("Successfully installed app: " + message.body());
        }).onFailure(err -> {
            // Retry this app
                System.out.println("Installation failed for app: " + message.body() + " reason: " + err.getMessage());
            });
    }
}
