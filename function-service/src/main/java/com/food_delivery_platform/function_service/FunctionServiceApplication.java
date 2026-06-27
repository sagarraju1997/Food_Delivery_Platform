package com.food_delivery_platform.function_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class FunctionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FunctionServiceApplication.class, args);
    }


//    @Bean
//    public Function<Order, String> sendOrderConfirmation() {
//        return order -> {
//            SesClient sesClient = SesClient.create();
//            SendEmailRequest request = SendEmailRequest.builder()
//                    .destination(Destination.builder()
//                            .toAddresses(order.getCustomerEmail())
//                            .build())
//                    .message(Message.builder()
//                            .subject(Content.builder().data("Order Confirmation").build())
//                            .body(Body.builder()
//                                    .text(Content.builder()
//                                            .data("Thank you for your order #" + order.getId())
//                                            .build())
//                                    .build())
//                            .build())
//                    .source("noreply@yourdomain.com")
//                    .build();
//            sesClient.sendEmail(request);
//            return "Email sent to " + order.getCustomerEmail();
//        };
//    }

    @Bean
    public Function<String, String> reverseString() {
        return input -> new StringBuilder(input).reverse().toString();
    }
}
