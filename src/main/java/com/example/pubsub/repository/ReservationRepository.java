package com.example.pubsub.repository;

import com.example.pubsub.domain.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Busca reservas por transactionId
    List<Reservation> findByTransactionId(String transactionId);

    // Busca reservas por clientId
    List<Reservation> findByClientId(Long clientId);

    // Busca reservas por categoria
    List<Reservation> findByCategoryId(String categoryId);

    // Busca reservas por subcategoria
    List<Reservation> findBySubCategoryId(String subCategoryId);

    // Busca reservas por período
    List<Reservation> findByReservationDateBetween(String startDate, String endDate);

    // Busca reservas com valor total acima de um determinado valor
    @Query("SELECT r FROM Reservation r WHERE (r.dailyRate * r.numberOfDays) > :minTotalValue")
    List<Reservation> findByTotalValueGreaterThan(@Param("minTotalValue") Double minTotalValue);

    // Soma do valor total das reservas por cliente
    @Query("SELECT r.clientId, SUM(r.dailyRate * r.numberOfDays) FROM Reservation r GROUP BY r.clientId")
    List<Object[]> sumTotalValueByClient();

    // Contagem de reservas por categoria
    @Query("SELECT r.categoryId, COUNT(r) FROM Reservation r GROUP BY r.categoryId")
    List<Object[]> countByCategory();

    // Atualizar consulta para usar explicitamente o externalId
    @Query("SELECT r FROM Reservation r WHERE (r.dailyRate * r.numberOfDays) > :minTotalValue")
    List<Reservation> findByTotalValueGreaterThan(@Param("minTotalValue") BigDecimal minTotalValue);  // Mudar para BigDecimal

    @Modifying
    @Transactional
    @Query("DELETE FROM Reservation r WHERE r.transactionId = :transactionId")
    void deleteByTransactionId(@Param("transactionId") String transactionId);
}