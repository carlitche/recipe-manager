package com.cookin.recipemanager.repository.specification;

public enum Operation {
    EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_EQUAL, GREAT_THAN,
    GREAT_THAN_EQUAL, CONTAIN, NOT_CONTAIN, IN, NOT_IN;

    public static final String[] SIMPLE_OPERATION_SET = { ":", "!:", "<", "<:", ">", ">:", "*", "!*", "^", "!^"};

    public static Operation getOperation(String input) {
        return switch (input) {
            case ":" -> EQUAL;
            case "!:" -> NOT_EQUAL;
            case "<" -> LESS_THAN;
            case "<:" -> LESS_THAN_EQUAL;
            case ">" -> GREAT_THAN;
            case ">:" -> GREAT_THAN_EQUAL;
            case "*" -> CONTAIN;
            case "!*" -> NOT_CONTAIN;
            case "^" -> IN;
            case "!^" -> NOT_IN;
            default -> throw new IllegalStateException("Unexpected value: " + input);
        };
    }
}
