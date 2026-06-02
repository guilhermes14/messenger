package com.messenger.sender.service;

import com.messenger.sender.enums.StatusEmail;
import com.messenger.sender.model.MessageModel;
import com.messenger.sender.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {
    final MessageRepository messageRepository;
    final JavaMailSender messageSender;

    public MessageService(MessageRepository messageRepository, JavaMailSender messageSender){
        this.messageRepository = messageRepository;
        this.messageSender = messageSender;
    }

    @Value(value="${broker.queue.email.name}")
    private String emailFrom;

    @Transactional
    public MessageModel sendMessage (MessageModel messageModel){
        try{
            messageModel.setEmailDate(LocalDateTime.now());
            messageModel.setEmailFrom(emailFrom);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(messageModel.getEmailTo());
            message.setSubject(messageModel.getSubject());
            message.setText(messageModel.getText());

            messageSender.send(message);

            messageModel.setStatusEmail(StatusEmail.SENT);
        }catch (MailException e){
            messageModel.setStatusEmail(StatusEmail.ERROR);
        }finally {
            return messageRepository.save(messageModel);
        }
    }
}
