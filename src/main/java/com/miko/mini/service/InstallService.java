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

    public Future<RowSet<Row>> scheduleInstall(int robotId, String appName, String version) {
        return repository.scheduleAppForInstallation(robotId, appName, version).onSuccess(rows -> {
            System.out.println("Logged app schedule for: " + appName);
        });
    }

    public Future<Void> installApp(int id) {
        Promise<Void> promise = Promise.promise();
        repository.updateAppInstallStatus(id, AppState.PICKEDUP).onSuccess(rows -> {
            sendAppForInstallation(id, promise);
        }).onFailure(error -> {
            System.out.println("Failed to insert app progress: " + error);
        });
        return promise.future();
    }



    private void sendAppForInstallation(int id, Promise<Void> promise) {
        Future<Void> processingFuture = dummyApiForInstallation();
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

    private Future<Void> dummyApiForInstallation() {
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
