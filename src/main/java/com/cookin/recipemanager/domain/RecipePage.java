package com.cookin.recipemanager.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public class RecipePage {
    @Schema(name = "Page Number", example = "0", required = false)
    private int pageNumber = 0;
    @Schema(name = "Page Size", example = "10", required = false)
    private int pageSize = 10;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
