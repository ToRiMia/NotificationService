package com.example.demo;

import io.grpc.stub.StreamObserver;
import skysoft.enterprise.backend.notification.NotificationGrpc;
import skysoft.enterprise.backend.notification.NotificationOuterClass;

public class NotificationService extends NotificationGrpc.NotificationImplBase {

    @Override
    public void botsReply(NotificationOuterClass.BotResponse request, StreamObserver<NotificationOuterClass.Empty> responseObserver) {
        System.out.println("-------------------------");
        System.out.print(request.toString());
        System.out.println("-------------------------");

        responseObserver.onNext(NotificationOuterClass.Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
