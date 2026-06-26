package com.messenger.sender.service;

import com.messenger.sender.enums.StatusEmail;
import com.messenger.sender.model.MessageModel;
import com.messenger.sender.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    final MessageRepository messageRepository;
    final JavaMailSender messageSender;

    public MessageService(MessageRepository messageRepository, JavaMailSender messageSender){
        this.messageRepository = messageRepository;
        this.messageSender = messageSender;
    }

    @Value(value="${broker.queue.email.name}")
    private String emailFrom;

    @Transactional
    public MessageModel sendMessage(MessageModel messageModel){
        try{
            logger.info("Iniciando envio de email para: {}", messageModel.getEmailTo());
            messageModel.setEmailDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
            messageModel.setEmailFrom(emailFrom);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(messageModel.getEmailTo());
            message.setSubject(messageModel.getSubject());
            message.setText(messageModel.getText());

            messageSender.send(message);

            messageModel.setStatusEmail(StatusEmail.SENT);
            logger.info("Email enviado com sucesso para: {}", messageModel.getEmailTo());
        }catch (MailException e){
            logger.error("Erro ao enviar email para: {}. Erro: {}", messageModel.getEmailTo(), e.getMessage());
            messageModel.setStatusEmail(StatusEmail.ERROR);
        }finally {
            messageRepository.save(messageModel);
            logger.info("Status do email salvo: {}", messageModel.getStatusEmail());
        }
        return messageModel;
    }
}
