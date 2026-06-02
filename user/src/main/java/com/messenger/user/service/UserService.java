package com.messenger.user.service;

import com.messenger.user.models.UserModel;
import com.messenger.user.producer.UserProducer;
import com.messenger.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    final UserRepository userRepository;
    final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userProducer = userProducer;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserModel create(UserModel userModel){
        userModel = userRepository.save(userModel);
        userProducer.publishMessage(userModel);

        return userModel;
    }
}
