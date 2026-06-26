package com.messenger.user.service;

import com.messenger.user.models.UserModel;
import com.messenger.user.producer.UserProducer;
import com.messenger.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    final UserRepository userRepository;
    final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userProducer = userProducer;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserModel create(UserModel userModel){
        logger.info("Criando usuário com email: {}", userModel.getEmail());
        userModel = userRepository.save(userModel);
        logger.info("Usuário criado com sucesso. ID: {}", userModel.getUserId());
        userProducer.publishMessage(userModel);
        logger.info("Mensagem publicada para o sender. ID: {}", userModel.getUserId());
        return userModel;
    }
}
