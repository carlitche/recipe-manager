package com.cookin.recipemanager.domain;

import com.cookin.recipemanager.validator.IsNumeric;
import com.cookin.recipemanager.validator.ValidOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFilters {
        @ValidOperation(prefixes = {"eq", "cn", "nc"})
        private String name;

        @ValidOperation(prefixes = {"eq", "ne"})
        private String isVegetarian;

        @IsNumeric
        @ValidOperation(prefixes = {"eq", "ne", "lt", "le", "gt", "ge"})
        private String serving;

        @ValidOperation(prefixes = {"in", "ni"})
        private String ingredients;

        @ValidOperation(prefixes = {"cn", "nc"})
        private String instructions;
}
