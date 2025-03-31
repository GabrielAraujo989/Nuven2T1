package com.example.pubsub.service;

import com.example.pubsub.domain.Msg;
import com.example.pubsub.repository.MsgRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MsgServiceImpl implements MsgService {

    private final MsgRepository msgRepository;

    @Autowired
    public MsgServiceImpl(MsgRepository msgRepository) {
        this.msgRepository = msgRepository;
    }

    @Override
    public Msg save(Msg msg) {
        return msgRepository.findByUuid(msg.getUuid())
                .map(existing -> {
                    existing.setCreatedAt(msg.getCreatedAt());
                    existing.setType(msg.getType());
                    existing.setClientId(msg.getClientId());
                    existing.setTransactionId(msg.getTransactionId());
                    return msgRepository.save(existing);
                })
                .orElseGet(() -> msgRepository.save(msg));
    }

    @Override
    public List<Msg> findAll() {
        return msgRepository.findAll();
    }

    @Override
    public Optional<Msg> findById(String uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Msg> findById(Long id) {
        return msgRepository.findById(String.valueOf(id));
    }

    @Override
    public Optional<Msg> findByUuid(String uuid) {
        return msgRepository.findByUuid(uuid);
    }

    @Override
    public List<Msg> findByClientId(Long clientId) {
        return msgRepository.findByClientId(clientId);
    }

    @Override
    public List<Msg> findByTransactionId(String transactionId) {
        return msgRepository.findByTransactionId(transactionId);
    }

    @Override
    public Msg update(Msg msg) {
        return msgRepository.save(msg);
    }

    @Override
    public void deleteById(String uuid) {

    }

    @Override
    public boolean existsByMessageId(String messageId) {
        return false;
    }

    @Override
    public void deleteById(Long id) {
        msgRepository.deleteById(String.valueOf(id));
    }

    @Override
    public boolean existsByUuid(String uuid) {
        return msgRepository.existsByUuid(uuid);
    }
}