package com.messenger.sender.consumer;

import com.messenger.sender.model.MessageModel;
import com.messenger.sender.record.MessageRecordDTO;
import com.messenger.sender.service.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    final MessageService messageService;

    public MessageConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

    @RabbitListener(queues="${broker.queue.email.name}")
    public void listenMessageQueue(@Payload MessageRecordDTO messageRecordDTO){
        var messageModel = new MessageModel();

        messageModel.setUserId(messageRecordDTO.userId());
        messageModel.setEmailTo(messageRecordDTO.emailTo());
        messageModel.setSubject(messageRecordDTO.subject());
        messageModel.setText(messageRecordDTO.text());

        messageService.sendMessage(messageModel);
        //var messageModel = new MessageModel();
        //BeanUtils.copyProperties(messageRecordDTO, messageModel);
        //messageService.sendMessage(messageModel);
    }
}
