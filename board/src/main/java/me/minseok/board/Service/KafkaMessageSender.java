package me.minseok.board.Service;

import me.minseok.board.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaMessageSender {

    @Autowired
    private KafkaTemplate<String, Comment> kafkaTemplate;

    @Value("${kafka.my.comment.topic.name}")
    private String topicName;

    public void sendMessage(Comment comment){

        Message<Comment> message = MessageBuilder
                .withPayload(comment)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();



        ListenableFuture<SendResult<String, Comment>> future = kafkaTemplate.send(message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Comment>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Unable to send message=[" + message + "] due to : " + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Comment> stringStringSendResult) {
                System.out.println("Sent message=[" + stringStringSendResult.getProducerRecord().value().toString() + "] with offset=[" + stringStringSendResult.getRecordMetadata().offset() + "]");
            }
        });
    }
}
