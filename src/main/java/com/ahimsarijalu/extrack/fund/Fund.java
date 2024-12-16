package com.ahimsarijalu.extrack.fund;

import com.ahimsarijalu.extrack.expense.Expense;
import com.ahimsarijalu.extrack.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Table(name = "fund")
@Entity
@Data
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "fund", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private List<Expense> expenses = List.of();
}
