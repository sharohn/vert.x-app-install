package com.miko.mini.verticles;

import com.miko.mini.constants.EventBusTopics;
import com.miko.mini.entity.Apps;
import com.miko.mini.service.InstallService;
import io.vertx.core.AbstractVerticle;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class MainVerticle extends AbstractVerticle {

    private final InstallService installService;

    public MainVerticle(InstallService installService) {
        this.installService = installService;
    }

    @Override
    public void start() {
        startAppInstall();
    }

    private void startAppInstall() {
        installService.getRepository().fetchAllApps().onSuccess(this::scheduleAppInstall).onFailure(err -> {
            System.err.println("Failed to fetch records: " + err.getMessage());
        });
    }

    private void scheduleAppInstall(RowSet<Row> rows) {
        for (Row row : rows) {
            Apps app = new Apps(row.getInteger("id"), row.getString("app_name"), row.getString("version"));
            System.out.println("Initiated app installation for " + app);
            installService.scheduleInstall(1, row.getString("app_name"), row.getString("version"))
                .onSuccess(scheduledApps -> {
                    for (Row scheduledApp : scheduledApps) {
                        System.out.println("App scheduled for installation: " + scheduledApp.getString("app_name"));
                        vertx.eventBus().publish(EventBusTopics.APP_INSTALL_TOPIC, scheduledApp.getInteger("id"));
                    }
                })
                .onFailure(err -> System.err.println("Failed to install app: " + app + " " + err.getMessage()));
        }
    }
}
