package com.pingao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py4j.GatewayServer;


/**
 * Created by pingao on 2018/7/12.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer();
        gatewayServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(gatewayServer::shutdown));
        LOGGER.info("Gateway server start success");
    }
}


