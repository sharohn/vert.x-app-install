package com.miko.mini.verticles;

import com.miko.mini.repository.DatabaseRepository;
import com.miko.mini.service.InstallService;
import io.vertx.core.AbstractVerticle;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        PgConnectOptions connectOptions = new PgConnectOptions()
            .setHost("localhost")
            .setPort(5432)
            .setDatabase("miko")
            .setUser("postgres")
            .setPassword("postgres");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        PgPool client = PgPool.pool(vertx, connectOptions, poolOptions);

        DatabaseRepository repository = new DatabaseRepository(client);
        InstallService installService = new InstallService(repository);

        vertx.deployVerticle(new InstallVerticle(installService));
    }
}
