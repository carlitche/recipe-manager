package com.cookin.recipemanager.repository.specification;

public enum Operation {
    EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_EQUAL, GREAT_THAN,
    GREAT_THAN_EQUAL, CONTAIN, NOT_CONTAIN, IN, NOT_IN;

    public static Operation getOperation(String input) {
        return switch (input) {
            case "eq" -> EQUAL;
            case "ne" -> NOT_EQUAL;
            case "lt" -> LESS_THAN;
            case "le" -> LESS_THAN_EQUAL;
            case "gt" -> GREAT_THAN;
            case "ge" -> GREAT_THAN_EQUAL;
            case "cn" -> CONTAIN;
            case "nc" -> NOT_CONTAIN;
            case "in" -> IN;
            case "ni" -> NOT_IN;
            default -> throw new IllegalStateException("Unexpected value " + input);
        };
    }
}
