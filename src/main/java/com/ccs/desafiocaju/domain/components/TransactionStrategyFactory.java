package com.ccs.desafiocaju.domain.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TransactionStrategyFactory {

    private static final Map<String, TransactionStrategy> strategies = new ConcurrentHashMap<>();

    @Autowired
    public TransactionStrategyFactory(List<TransactionStrategy> strategies) {
        strategies.forEach(strategy ->
                strategy.getMccs().forEach(mcc ->
                        TransactionStrategyFactory
                                .strategies.put(mcc, strategy)));
    }

    public TransactionStrategy getStrategy(String mcc) {
        return strategies.getOrDefault(mcc, strategies.get("cash"));
    }
}