package com.bank.recommendation.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Cacheable(value = "sumByProdTypeAndTransType", key = "#userId + ':' + #prodType + ':' + #transType")
    public int getSumByProdTypeAndTransactionsType(UUID userId, String prodType, String transType) {
        System.out.println(">>> getSumByProdTypeAndTransactionsType вызван: userId=" + userId + ", prodType=" + prodType + ", transType=" + transType);
        return jdbcTemplate.queryForObject("""
                        SELECT COALESCE(SUM(t.AMOUNT),0)
                        FROM TRANSACTIONS AS t
                        JOIN USERS u ON t.USER_ID = u.ID
                        JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p.TYPE = ? AND t.TYPE = ?
                        """,
                Integer.class,
                userId.toString(),
                prodType,
                transType);
    }

    @Cacheable(value = "hasProduct", key = "#userId + ':' + #prodType")
    public boolean hasProduct(UUID userId, String prodType) {
        System.out.println(">>> hasProduct вызван: userId=" + userId + ", prodType=" + prodType);
        int requestResult = jdbcTemplate.queryForObject("""
                        SELECT COALESCE(COUNT(*),0)
                        FROM TRANSACTIONS AS t
                        JOIN USERS u ON t.USER_ID = u.ID
                        JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p.TYPE = ?
                        """,
                Integer.class,
                userId.toString(),
                prodType);
        return requestResult > 0;
    }

    @Cacheable(value = "countTransactions", key = "#userId + ':' + #productType")
    public int countTransactions(UUID userId, String productType) {
        String sql = """
                SELECT COUNT(*)
                FROM TRANSACTIONS t
                JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
                WHERE t.USER_ID = ? AND p.TYPE = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType);
    }

    public Optional<UUID> findUserIdByUsername(String username) {
        String sql = "SELECT ID FROM USERS WHERE NAME = ?";
        try {
            UUID userId = jdbcTemplate.queryForObject(sql, UUID.class, username);
            return Optional.of(userId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}