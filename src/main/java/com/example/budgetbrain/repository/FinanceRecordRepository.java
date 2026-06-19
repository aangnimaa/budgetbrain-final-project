package com.example.budgetbrain.repository;

import com.example.budgetbrain.model.AppUser;
import com.example.budgetbrain.model.FinanceRecord;
import com.example.budgetbrain.model.RecordType;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long> {
    List<FinanceRecord> findByOwnerOrderByRecordDateDescCreatedAtDesc(AppUser owner);

    @Query("""
        SELECT r FROM FinanceRecord r
        WHERE r.owner = :owner AND (
            LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(r.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(r.note) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        ORDER BY r.recordDate DESC, r.createdAt DESC
    """)
    List<FinanceRecord> searchByOwner(@Param("owner") AppUser owner, @Param("keyword") String keyword);

    @Query("SELECT SUM(r.amount) FROM FinanceRecord r WHERE r.owner = :owner AND r.type = :type")
    BigDecimal totalByOwnerAndType(@Param("owner") AppUser owner, @Param("type") RecordType type);

    long countByOwner(AppUser owner);
}
