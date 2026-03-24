package edu.icet.ecom.repository;

import edu.icet.ecom.entity.EmailSchedulerConfig;

import java.time.LocalTime;
import java.util.Optional;

public interface EmailSchedulerConfigRepository {

    Optional<EmailSchedulerConfig> findLatestConfig();

    EmailSchedulerConfig upsertSendTime(LocalTime sendTime);
}

