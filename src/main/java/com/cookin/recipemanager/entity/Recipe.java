package com.cookin.recipemanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_vegetarian")
    private Boolean isVegetarian;

    private int serving;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name="recipe_ingredients", joinColumns=@JoinColumn(name="ingredient_id"))
    @Column(name = "ingredients")
    private List<String> ingredients;

    @Column(columnDefinition = "TEXT")
    private String instructions;
}
