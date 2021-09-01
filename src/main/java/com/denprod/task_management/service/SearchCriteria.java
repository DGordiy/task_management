package com.denprod.task_management.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SearchCriteria {
    private String key;
    private Object value;
    private SearchOperator operator;
}
