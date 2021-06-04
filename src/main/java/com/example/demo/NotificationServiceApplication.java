package com.example.demo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import skysoft.enterprise.backend.bots.BotsGrpc;
import skysoft.enterprise.backend.bots.BotsOuterClass;

import java.time.Instant;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication
public class NotificationServiceApplication {

    private static final Scanner scanner = new Scanner(System.in);
    public static final String chatId = "581548594";
//    public static final String chatId = "581548595"; //wrong
//    public static final String chatId = "524376687"; //tanya

    public static void main(String[] args) {
        try {
            SpringApplication.run(NotificationServiceApplication.class, args);

            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext() // TODO: 28.04.21 don`t use it
                    .build();

            BotsGrpc.BotsBlockingStub stub = BotsGrpc.newBlockingStub(channel);
            System.out.println("Open channel");

            while (true) {
                System.out.println("Choose scenario Mfa or Notification (enter \"0\" or \"1\") ");
                int scenarioId = scanner.nextInt();
                switch (scenarioId) {
                    case 0: {
                        System.out.println("Input OTP: ");
                        int otp = scanner.nextInt();

                        BotsOuterClass.Scenario scenario = BotsOuterClass.Scenario.newBuilder()
                                .setId(UUID.randomUUID().toString())
                                .setType(BotsOuterClass.ScenarioType.MfaScenario)
                                .setUserIdentifier(chatId)
                                .setData(BotsOuterClass.ScenarioData.newBuilder()
                                        .setMessage("Confirm authorisation in Time tracking on " + Date.from(Instant.now()))
                                        .putParameters("otp", String.valueOf(otp))
                                        .build())
                                .setBotType(BotsOuterClass.BotType.TelegramBot)
                                .build();
                        stub.runScenario(scenario);
                        break;
                    }
                    case 1: {
                        System.out.println("Input message: ");
                        String message = scanner.next();

                        BotsOuterClass.Scenario scenario = BotsOuterClass.Scenario.newBuilder()
                                .setId(UUID.randomUUID().toString())
                                .setType(BotsOuterClass.ScenarioType.NotificationScenario)
                                .setUserIdentifier(chatId)
                                .setData(BotsOuterClass.ScenarioData.newBuilder()
                                        .setMessage(message)
                                        .build())
                                .setBotType(BotsOuterClass.BotType.TelegramBot)
                                .build();
                        stub.runScenario(scenario);
                        break;
                    }

                    case 2:
                    default:
                }

                System.out.println("Input command \"c\" for continue work or \"t\" for close channel");
                String command = scanner.next();

                if (command.equals("t")) {
                    channel.shutdownNow();
                    System.out.println("Channel closed");
                    break;
                }
            }
        } catch (StatusRuntimeException e) {
            System.out.println("Status: " + e.getStatus());
            System.out.println("Code: " + e.getStatus().getCode());
            System.out.println("Desc: " + e.getStatus().getDescription());
            System.out.println(e.getTrailers().toString());
            System.out.println();
            e.printStackTrace();
        }
    }
}
