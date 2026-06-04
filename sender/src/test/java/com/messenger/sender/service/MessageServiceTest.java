package com.messenger.sender.service;

import com.messenger.sender.enums.StatusEmail;
import com.messenger.sender.model.MessageModel;
import com.messenger.sender.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private MessageService messageService;

    @Test
    void sendMessageSuccess() {
        MessageModel email = new MessageModel();
        email.setEmailTo("destinatario@teste.com");
        email.setSubject("Assunto Teste");
        email.setText("Corpo do e-mail");

        when(messageRepository.save(any(MessageModel.class))).thenReturn(email);
        MessageModel resultado = messageService.sendMessage(email);
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));

        // verifica se o status esta SENT e se salvou no banco
        assertEquals(StatusEmail.SENT, resultado.getStatusEmail());
        assertNotNull(resultado.getEmailDate());
        verify(messageRepository, times(1)).save(email);
    }

    @Test
    void sendMessageExceptionError() {
        MessageModel email = new MessageModel();
        email.setEmailTo("destinatario@teste.com");
        email.setSubject("Assunto Teste");
        email.setText("Corpo do e-mail");

        doThrow(new MailException("Erro ao conectar no servidor SMTP") {}).when(emailSender).send(any(SimpleMailMessage.class));
        when(messageRepository.save(any(MessageModel.class))).thenReturn(email);
        MessageModel resultado = messageService.sendMessage(email);

        // buscando status error
        assertEquals(StatusEmail.ERROR, resultado.getStatusEmail());
        assertNotNull(resultado.getEmailDate());
        verify(messageRepository, times(1)).save(email);
    }
}