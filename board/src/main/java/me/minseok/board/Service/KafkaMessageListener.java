package me.minseok.board.Service;

import me.minseok.board.Mapper.CommentMapper;
import me.minseok.board.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {
    @Autowired
    private CommentMapper commentMapper;

    @KafkaListener(topics = "${kafka.predict.topic.name}"
            , groupId = "${kafka.predict.topic.group.name}"
            , containerFactory = "pushEntityKafkaListenerContainerFactory")
    public void listenWithHeaders(@Payload Comment comment,
                                  @Headers MessageHeaders messageHeaders) {

        commentMapper.updateCommentClean(comment);
        System.out.println(
                "Received Message: " + comment.toString() +
                        " headers: " + messageHeaders);
    }
}
