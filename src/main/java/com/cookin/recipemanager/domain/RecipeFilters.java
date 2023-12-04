package com.cookin.recipemanager.domain;

import com.cookin.recipemanager.validator.IsNumeric;
import com.cookin.recipemanager.validator.ValidOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFilters {

        @Schema(name = "Recipe Name", example = "eq:{recipe_name}",
                description = "allowed prefix: 'eg:' -> equal,  'cn:' -> contain, 'ne:' -> not equal; no prefix include is the same as 'eq:'{recipe_name}")
        @ValidOperation(prefixes = {"eq", "cn", "nc"})
        private String name;

        @Schema(name = "Vegetarian", example = "eq:{true} or eq:{false}",
                description = "allowed prefix: 'eg:' -> equal, 'ne:' -> not equal; no prefix include is the same as 'eq:'{vegetarian}, any word different than 'true' or 'false' will be treated as 'false'")
        @ValidOperation(prefixes = {"eq", "ne"})
        private String isVegetarian;

        @Schema(name = "Serving", example = "eq:{serving}",
                description = "allowed prefix: 'eg:' -> equal, 'ne:' -> not equal, 'lt:' -> less than, 'le:' -> less than equal, 'gt:' -> greater than, 'ge:' -> greater than equal, ; no prefix include is the same as 'eq:'{Serving}")
        @IsNumeric
        @ValidOperation(prefixes = {"eq", "ne", "lt", "le", "gt", "ge"})
        private String serving;

        @Schema(name = "Ingredient", example = "in:{ingredient}",
                description = "allowed prefix: 'in:' -> include,  'ni:' -> not include; no prefix include is the same as 'in:'{ingredient}")
        @ValidOperation(prefixes = {"in", "ni"})
        private String ingredients;

        @Schema(name = "Instruction", example = "cn:{instruction}",
                description = "allowed prefix: 'cn:' -> contain,  'nc:' -> not contain; no prefix include is the same as 'cn:'{instruction}")
        @ValidOperation(prefixes = {"cn", "nc"})
        private String instructions;
}
