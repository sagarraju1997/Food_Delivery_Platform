package com.food_delivery_platform.function_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class FunctionServiceApplication {
    private final SesV2Client sesClient = SesV2Client.create();
    public static void main(String[] args) {
        SpringApplication.run(FunctionServiceApplication.class, args);
    }


    @Bean
    public Function<Order, Map<String, String>> sendOrderConfirmation() {
        System.out.println("sendOrderConfirmation invoked..");
        return order -> {
            try {
                // Correctly build the message content
                Message message = Message.builder()
                        .subject(Content.builder().data("Order Confirmation").build())
                        .body(Body.builder().text(Content.builder().data("Thank you for your order #" + order.getId()).build()).build())
                        .build();
                System.out.println("sendOrderConfirmation MSG build..");
                // Build request using correct v2 fluent methods: fromEmailAddress, content, destination
                SendEmailRequest request = SendEmailRequest.builder()
                        .fromEmailAddress("vsagarraju1997@gmail.com")
                        .destination(Destination.builder().toAddresses(order.getCustomerEmail()).build())
                        .content(EmailContent.builder().simple(message).build())
                        .build();
                System.out.println("sendOrderConfirmation req build..");
                sesClient.sendEmail(request);
                System.out.println("sendOrderConfirmation req build..");
                return Map.of("result","Email sent to " + order.getCustomerEmail());
            } catch (Exception e) {
                throw new RuntimeException("Email failed", e);
            }
        };
    }

    @Bean
    public Function<String, Map<String, String>> reverseString() {
        System.out.println("reverseString invoked..");
        return input -> Map.of("Result",new StringBuilder(input).reverse().toString());
    }
}
