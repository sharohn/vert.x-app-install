package com.miko.mini.repository;

import com.miko.mini.constants.AppState;
import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class DatabaseRepository {

    private final SqlClient client;

    public DatabaseRepository(SqlClient client) {
        this.client = client;
    }

    public Future<RowSet<Row>> fetchAllApps() {
        return client.query("SELECT * FROM apps").execute();
    }

    public Future<Void> updateAppInstallStatus(int id, AppState appState) {
        return client.preparedQuery("UPDATE install_progress SET app_state = $1 WHERE id = $2")
            .execute(Tuple.of(appState.name(), id))
            .mapEmpty();
    }

    public Future<RowSet<Row>> scheduleAppForInstallation(int robotId, String appName, String version) {
        return client.preparedQuery("INSERT INTO install_progress (robot_id, app_name, version, app_state, retry_count)" +
                "VALUES($1, $2, $3, $4, $5) RETURNING *;")
            .execute(Tuple.of(robotId, appName, version, AppState.SCHEDULED.name(), 0));
    }
}
