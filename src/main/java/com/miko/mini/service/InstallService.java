package com.miko.mini.service;

import com.miko.mini.constants.AppState;
import com.miko.mini.repository.DatabaseRepository;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class InstallService {

    private final DatabaseRepository repository;
    private static final int MAX_RETRIES = 3;

    public InstallService(DatabaseRepository repository) {
        this.repository = repository;
    }



    public Future<Void> installApp(int robotId, String appName, String version) {
        Promise<Void> promise = Promise.promise();
        repository.insertAppProgressStatus(robotId, appName, version).onSuccess(rows -> {
            for (Row row : rows) {
                sendAppForInstallation(row, promise);
            }
        }).onFailure(error -> {
            System.out.println("Failed to insert app progress: " + error);
        });
//        processRecordWithRetry(row, MAX_RETRIES, promise);
        return promise.future();
    }



    private void sendAppForInstallation(Row row, Promise<Void> promise) {
        String appName = row.getString("app_name");
        int id = row.getInteger("id");
        Future<Void> processingFuture = dummyApiForInstallation(appName);

        processingFuture.onComplete(ar -> {
            if (ar.succeeded()) {
                repository.updateAppInstallStatus(id, AppState.COMPLETED)
                    .onComplete(promise::handle);
            } else {
                repository.updateAppInstallStatus(id, AppState.ERROR)
                    .onComplete(promise::handle);
            }
        });
    }

    private Future<Void> dummyApiForInstallation(String appName) {
        if (Math.random() > 0.7) {
            return Future.failedFuture("Installation failed");
        } else {
            return Future.succeededFuture();
        }
    }

    public DatabaseRepository getRepository() {
        return this.repository;
    }
}
