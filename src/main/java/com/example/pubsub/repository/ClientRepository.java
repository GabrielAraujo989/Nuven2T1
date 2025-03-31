package com.example.pubsub.repository;

import com.example.pubsub.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByExternalId(Long externalId);
    List<Client> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT c FROM Client c JOIN Reservation r WHERE c.externalId = r.clientId AND r.categoryId = :categoryId")
    List<Client> findByReservationCategory(@Param("categoryId") String categoryId);

    @Query("SELECT COUNT(DISTINCT m.clientId) FROM Msg m WHERE m.type = :messageType")
    Long countByMessageType(@Param("messageType") String messageType);
}