package br.com.personalFinanceApp.bnpl.model;

import br.com.personalFinanceApp.bnpl.enums.InstallmentPlanProvider;
import br.com.personalFinanceApp.bnpl.enums.InstallmentPlanStatus;
import br.com.personalFinanceApp.core.model.Household;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "installment_plans",
    indexes = {
        @Index(name = "idx_installment_plans_household", columnList = "household_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InstallmentPlanProvider provider;

    @Column(nullable = false, name = "total_amount")
    private BigDecimal totalAmount;

    @Column(nullable = false, name = "number_of_installments")
    private int numberOfInstallments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InstallmentPlanStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
