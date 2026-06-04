package com.messenger.user.producer;

import com.messenger.user.dto.EmailDTO;
import com.messenger.user.models.UserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserProducerTest {
    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private UserProducer userProducer;

    @Test
    void publishMessageMontaEmail() {
        // injetando um valor falso
        ReflectionTestUtils.setField(userProducer, "routingKey", "fila.teste.email");

        UserModel userModel = new UserModel();
        userModel.setUserId(UUID.randomUUID());
        userModel.setName("Guilherme");
        userModel.setEmail("teste@teste.com");

        userProducer.publishMessage(userModel);

        //captura o EmailDTO que foi enviado
        ArgumentCaptor<EmailDTO> emailCaptor = ArgumentCaptor.forClass(EmailDTO.class);
        verify(rabbitTemplate).convertAndSend(eq(""), eq("fila.teste.email"), emailCaptor.capture());
        EmailDTO emailEnviado = emailCaptor.getValue();

        // Validacao
        assertEquals(userModel.getUserId(), emailEnviado.getUserId());
        assertEquals("teste@teste.com", emailEnviado.getEmailTo());
        assertEquals("Usuario cadastrado", emailEnviado.getSubject());
        assertEquals("Usuario Guilherme criado", emailEnviado.getText());
    }
}