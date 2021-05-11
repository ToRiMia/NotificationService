package com.example.demo;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Component
public class NotificationServer {

    private Server server;

    @PostConstruct
    private void startServer() {
        server = ServerBuilder
                .forPort(8083)
                .addService(new NotificationService())
                .build();

        try {
            server.start();
            System.out.println("Server for connection to Notification service starts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void stopServer() {
        server.shutdown();
        System.out.println("Server for connection to Notification service stopped");
    }
}
