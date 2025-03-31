package com.example.pubsub.repository;

import com.example.pubsub.domain.Msg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MsgRepository extends JpaRepository<Msg, String> {

    // Busca mensagens por clientId
    Optional<Msg> findByUuid(String uuid);
    boolean existsByUuid(String uuid);
    // Busca mensagens por transactionId
    List<Msg> findByTransactionId(String transactionId);

    // Busca mensagens por tipo
    List<Msg> findByType(String type);

    // Busca mensagens dentro de um período
    List<Msg> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Busca mensagens por tipo e cliente
    List<Msg> findByTypeAndClientId(String type, Long clientId);

    // Contagem de mensagens por tipo
    @Query("SELECT m.type, COUNT(m) FROM Msg m GROUP BY m.type")
    List<Object[]> countByType();

    // Últimas N mensagens de um cliente
    @Query("SELECT m FROM Msg m WHERE m.clientId = :clientId ORDER BY m.createdAt DESC LIMIT :limit")
    List<Msg> findLatestByClient(@Param("clientId") Long clientId, @Param("limit") int limit);

    // Verifica se existe mensagem para um transactionId
    boolean existsByTransactionId(String transactionId);

//    boolean existsByMessageId(String messageId);

    List<Msg> findByClientId(Long clientId);
}