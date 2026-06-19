package com.example.budgetbrain.repository;

import com.example.budgetbrain.model.AppUser;
import com.example.budgetbrain.model.Budget;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByOwnerOrderByCreatedAtDesc(AppUser owner);

    @Query("""
        SELECT b FROM Budget b
        WHERE b.owner = :owner AND (
            LOWER(b.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(b.month) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(b.note) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        ORDER BY b.createdAt DESC
    """)
    List<Budget> searchByOwner(@Param("owner") AppUser owner, @Param("keyword") String keyword);

    @Query("SELECT SUM(b.limitAmount) FROM Budget b WHERE b.owner = :owner")
    BigDecimal totalBudgetByOwner(@Param("owner") AppUser owner);
}
