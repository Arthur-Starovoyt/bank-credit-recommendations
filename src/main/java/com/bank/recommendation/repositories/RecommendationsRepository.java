package com.bank.recommendation.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getIntFromCustomRequest(String request) {
        int result;
        try {
            result = jdbcTemplate.queryForObject(
                    request,
                    Integer.class
            );
            return result;
        } catch (NullPointerException e) {
            return result = 0;
        }
    }

    public int getSumByProdTypeAndTransactionsType(UUID userId, String prodType, String transType) {
        return jdbcTemplate.queryForObject("""
                        SELECT COALESCE(SUM(t.AMOUNT),0)
                        FROM TRANSACTIONS AS t
                        JOIN USERS u ON t.USER_ID =u.ID
                        JOIN PRODUCTS p ON t.PRODUCT_ID =p.ID
                        WHERE t.USER_ID =? AND p.TYPE = ? AND t.TYPE = ?
                        """,
                Integer.class,
                userId.toString(),
                prodType,
                transType);
    }

    public boolean hasProduct(UUID userId, String prodType) {
        int requestResult = jdbcTemplate.queryForObject("""
                        SELECT COALESCE(COUNT(*),0)
                        FROM TRANSACTIONS AS t
                        JOIN USERS u ON t.USER_ID =u.ID
                        JOIN PRODUCTS p ON t.PRODUCT_ID =p.ID
                        WHERE t.USER_ID =? AND p.TYPE = ?
                        """,
                Integer.class,
                userId.toString(),
                prodType);
        if (requestResult > 0) return Boolean.TRUE;
        else return Boolean.FALSE;
    }

}
