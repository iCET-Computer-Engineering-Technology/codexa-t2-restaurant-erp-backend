package edu.icet.ecom.repository;

import edu.icet.ecom.entity.AutomatedMessage;

import java.util.List;
import java.util.Optional;

public interface AutomatedMessageRepository {

    List<AutomatedMessage> findActiveMessages();
    List<AutomatedMessage> findInactiveMessages();
    List<AutomatedMessage> findActiveMessagesByTriggerType(AutomatedMessage.TriggerType triggerType);
    Optional<AutomatedMessage> findById(Integer id);
    Integer save(AutomatedMessage message);
    boolean update(AutomatedMessage message);
    boolean delete(Integer id);
}

