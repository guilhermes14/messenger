package com.messenger.sender.repository;

import com.messenger.sender.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageModel, UUID> {
}
