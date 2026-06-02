package com.messenger.sender.record;

import java.util.UUID;

public record MessageRecordDTO(UUID userId, String emailTo, String subject, String text) {
}
