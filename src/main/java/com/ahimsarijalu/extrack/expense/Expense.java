package com.ahimsarijalu.extrack.expense;

import com.ahimsarijalu.extrack.fund.Fund;
import com.ahimsarijalu.extrack.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "expense")
@Data
@SqlResultSetMapping(
        name = "TopCategoryDTOResult",
        classes = @ConstructorResult(
                targetClass = TopCategoryDTO.class,
                columns = {
                        @ColumnResult(name = "top_category", type = String.class),
                        @ColumnResult(name = "total_amount", type = Long.class)
                }
        )
)
@NamedNativeQuery(
        name = "Expense.findTopCategoryByUserId",
        query = "SELECT e.category as top_category, SUM(e.amount) as total_amount " +
                "FROM expense e " +
                "JOIN \"users\" u ON e.user_id = u.id " +
                "WHERE e.user_id = CAST(:userId AS varchar) " +
                "GROUP BY e.category " +
                "ORDER BY total_amount DESC " +
                "LIMIT 1",
        resultSetMapping = "TopCategoryDTOResult"
)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "expense_date")
    private LocalDateTime expenseDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fund_id", nullable = false)
    private Fund fund;
}
