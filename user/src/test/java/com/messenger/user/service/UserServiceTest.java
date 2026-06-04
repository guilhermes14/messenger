package com.messenger.user.service;

import com.messenger.user.models.UserModel;
import com.messenger.user.producer.UserProducer;
import com.messenger.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserProducer userProducer;
    @InjectMocks
    private UserService userService;

    @Test
    void createUsuarioEPublicaMensagem() {
        UserModel usuarioTest = new UserModel();
        usuarioTest.setName("Guilherme");
        usuarioTest.setEmail("teste@teste.com");

        UserModel usuarioSalvo = new UserModel();
        usuarioSalvo.setUserId(UUID.randomUUID()); // Simulando que o banco gerou um ID
        usuarioSalvo.setName("Guilherme");

        // Verifica se salvou usuario
        when(userRepository.save(any(UserModel.class))).thenReturn(usuarioSalvo);
        UserModel resultado = userService.create(usuarioTest);
        assertNotNull(resultado, "O usuário retornado não deveria ser nulo");

        // Verifica se o repositório foi chamado
        verify(userRepository, times(1)).save(usuarioTest);

        // Verifica se o producer foi chamado
        verify(userProducer, times(1)).publishMessage(usuarioSalvo);
    }
}
