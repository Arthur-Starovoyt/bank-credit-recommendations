package com.bank.recommendation.service;

import com.bank.recommendation.entity.RuleCondition;
import com.bank.recommendation.repositories.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleEvaluator {

    private final RecommendationsRepository repository;

    public DynamicRuleEvaluator(RecommendationsRepository repository) {
        this.repository = repository;
    }

    /**
     * Главный метод оценки одного условия динамического правила.
     * @param condition объект условия (содержит query, arguments, negate)
     * @param userId идентификатор пользователя
     * @return true, если условие выполняется для данного пользователя (с учётом negate)
     */
    public boolean evaluate(RuleCondition condition, UUID userId) {
        String query = condition.getQuery();
        boolean negate = condition.isNegate();

        boolean result = switch (query) {
            case "USER_OF" -> evaluateUserOf(condition, userId);
            case "ACTIVE_USER_OF" -> evaluateActiveUserOf(condition, userId);
            case "TRANSACTION_SUM_COMPARE" -> evaluateTransactionSumCompare(condition, userId);
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> evaluateTransactionSumCompareDepositWithdraw(condition, userId);
            default -> false;
        };

        return negate ? !result : result;
    }

    /**
     * Обработка запроса USER_OF.
     * Проверяет, является ли пользователь клиентом продукта указанного типа.
     * Аргументы: [productType]
     */
    private boolean evaluateUserOf(RuleCondition condition, UUID userId) {
        List<String> args = condition.getArguments();
        if (args == null || args.isEmpty()) {
            return false;
        }
        String productType = args.get(0);
        return repository.hasProduct(userId, productType);
    }

    /**
     * Обработка запроса ACTIVE_USER_OF.
     * Проверяет, является ли пользователь активным клиентом (≥5 транзакций) продукта указанного типа.
     * Аргументы: [productType]
     */
    private boolean evaluateActiveUserOf(RuleCondition condition, UUID userId) {
        List<String> args = condition.getArguments();
        if (args == null || args.isEmpty()) {
            return false;
        }
        String productType = args.get(0);
        int transactionCount = repository.countTransactions(userId, productType);
        return transactionCount >= 5;
    }

    /**
     * Обработка запроса TRANSACTION_SUM_COMPARE.
     * Сравнивает сумму транзакций определённого типа по продуктам заданного типа с константой.
     * Аргументы: [productType, transactionType, operator, constant]
     */
    private boolean evaluateTransactionSumCompare(RuleCondition condition, UUID userId) {
        List<String> args = condition.getArguments();
        if (args == null || args.size() < 4) {
            return false;
        }
        String productType = args.get(0);
        String transactionType = args.get(1);
        String operator = args.get(2);
        int constant;
        try {
            constant = Integer.parseInt(args.get(3));
        } catch (NumberFormatException e) {
            return false;
        }

        int sum = repository.getSumByProdTypeAndTransactionsType(userId, productType, transactionType);

        return switch (operator) {
            case ">" -> sum > constant;
            case "<" -> sum < constant;
            case "=" -> sum == constant;
            case ">=" -> sum >= constant;
            case "<=" -> sum <= constant;
            default -> false;
        };
    }

    /**
     * Обработка запроса TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW.
     * Сравнивает сумму пополнений и сумму трат по продуктам одного типа.
     * Аргументы: [productType, operator]
     */
    private boolean evaluateTransactionSumCompareDepositWithdraw(RuleCondition condition, UUID userId) {
        List<String> args = condition.getArguments();
        if (args == null || args.size() < 2) {
            return false;
        }
        String productType = args.get(0);
        String operator = args.get(1);

        int depositSum = repository.getSumByProdTypeAndTransactionsType(userId, productType, "DEPOSIT");
        int withdrawSum = repository.getSumByProdTypeAndTransactionsType(userId, productType, "WITHDRAW");

        return switch (operator) {
            case ">" -> depositSum > withdrawSum;
            case "<" -> depositSum < withdrawSum;
            case "=" -> depositSum == withdrawSum;
            case ">=" -> depositSum >= withdrawSum;
            case "<=" -> depositSum <= withdrawSum;
            default -> false;
        };
    }
}