package com.example.pubsub.service;

import com.example.pubsub.domain.Msg;
import java.util.List;
import java.util.Optional;

public interface MsgService {
    Msg save(Msg msg);

    Optional<Msg> findById(Long id);

    Optional<Msg> findByUuid(String uuid);

    void deleteById(Long id);

    boolean existsByUuid(String uuid);
    List<Msg> findAll();
    Optional<Msg> findById(String uuid);

    List<Msg> findByClientId(Long clientId);
    List<Msg> findByTransactionId(String transactionId);
    Msg update(Msg msg);
    void deleteById(String uuid);

    boolean existsByMessageId(String messageId);
}