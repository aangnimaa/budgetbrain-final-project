package com.example.budgetbrain.repository;

import com.example.budgetbrain.model.AppUser;
import com.example.budgetbrain.model.SavingsGoal;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    List<SavingsGoal> findByOwnerOrderByCreatedAtDesc(AppUser owner);

    @Query("""
        SELECT g FROM SavingsGoal g
        WHERE g.owner = :owner AND (
            LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(g.note) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        ORDER BY g.createdAt DESC
    """)
    List<SavingsGoal> searchByOwner(@Param("owner") AppUser owner, @Param("keyword") String keyword);

    @Query("SELECT SUM(g.savedAmount) FROM SavingsGoal g WHERE g.owner = :owner")
    BigDecimal totalSavedByOwner(@Param("owner") AppUser owner);
}
