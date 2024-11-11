package com.miko.mini.verticles;

import com.miko.mini.service.InstallService;
import io.vertx.core.AbstractVerticle;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class InstallVerticle extends AbstractVerticle {

    private final InstallService installService;

    public InstallVerticle(InstallService installService) {
        this.installService = installService;
    }

    @Override
    public void start() {
        startAppInstall();
    }

    private void startAppInstall() {
        installService.getRepository().fetchAllApps().onSuccess(this::installApp).onFailure(err -> {
            System.err.println("Failed to fetch records: " + err.getMessage());
        });
    }

    private void installApp(RowSet<Row> rows) {
        for (Row row : rows) {
            installService.installApp(1, row.getString("app_name"), row.getString("version"))
                .onSuccess(v -> System.out.println("Installing app ID: " + row.getInteger("id") + " app_name: " +  row.getString("app_name") + " version: " + row.getString("version")))
                .onFailure(err -> System.err.println("Failed to install app ID: " + row.getInteger("id") + " app_name: " +  row.getString("app_name") + " version: " + row.getString("version")));
        }
    }
}
