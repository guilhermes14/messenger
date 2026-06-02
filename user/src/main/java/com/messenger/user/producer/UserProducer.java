package com.messenger.user.producer;

import com.messenger.user.dto.EmailDTO;
import com.messenger.user.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishMessage(UserModel userModel){
        var emailDto = new EmailDTO();
        emailDto.setUserId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Usuario cadastrado");
        emailDto.setText("Usuario "+userModel.getName()+" criado");

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }
}
