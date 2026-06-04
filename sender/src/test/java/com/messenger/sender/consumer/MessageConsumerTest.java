package com.messenger.sender.consumer;

import com.messenger.sender.model.MessageModel;
import com.messenger.sender.record.MessageRecordDTO;
import com.messenger.sender.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageConsumer messageConsumer;

    @Test
    void listenMessageQueueConverteDtoChamaService() {
        // 1. Arrange: Preparando a mensagem fake que "chegou" da fila
        UUID userId = UUID.randomUUID();

        // ⚠️ ATENÇÃO: Ajuste a ordem das aspas abaixo para bater EXATAMENTE
        // com a ordem dos campos definidos no seu EmailRecordDTO!
        MessageRecordDTO dto = new MessageRecordDTO(userId, "destinatario@teste.com", "Assunto Teste", "Texto do email");

        // 2. Act: O Consumer "escuta" a mensagem
        messageConsumer.listenMessageQueue(dto);

        // 3. Assert: Validando se o Service foi chamado com os dados certos
        // Cria a rede para capturar o EmailModel
        ArgumentCaptor<MessageModel> emailCaptor = ArgumentCaptor.forClass(MessageModel.class);

        // Verifica se o emailService.sendEmail foi chamado exatamente 1 vez e captura o que foi passado
        verify(messageService, times(1)).sendMessage(emailCaptor.capture());

        // Pega o modelo capturado da rede
        MessageModel emailEnviado = emailCaptor.getValue();

        // Verifica se o BeanUtils copiou as propriedades corretamente do DTO para o Model
        assertEquals(userId, emailEnviado.getUserId());
        assertEquals("destinatario@teste.com", emailEnviado.getEmailTo());
        assertEquals("Assunto Teste", emailEnviado.getSubject());
        assertEquals("Texto do email", emailEnviado.getText());
    }
}