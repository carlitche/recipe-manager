package com.cookin.recipemanager.repository.specification;

import com.cookin.recipemanager.entity.Recipe;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification implements Specification<Recipe> {

    private List<SearchCriteria> criteriaList;

    public RecipeSpecification() {
        this.criteriaList = new ArrayList<>();
    }

    public void add(SearchCriteria criteria){
        criteriaList.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        for(SearchCriteria criteria: criteriaList) {
            if(criteria.getOperation().equals(Operation.EQUAL)) {
                predicates.add(criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(Operation.NOT_EQUAL)) {
                predicates.add(criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(Operation.LESS_THAN)) {
                predicates.add(criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(Operation.LESS_THAN_EQUAL)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(Operation.GREAT_THAN)) {
                predicates.add(criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(Operation.GREAT_THAN_EQUAL)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(Operation.CONTAIN)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())),
                                                        "%" + criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(Operation.NOT_CONTAIN)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())),
                                                        "%" + criteria.getValue().toString().toLowerCase() + "%").not());
            } else if (criteria.getOperation().equals(Operation.IN)) {
                predicates.add(criteriaBuilder.isMember(criteria.getValue().toString(), root.get(criteria.getKey())));
            } else if (criteria.getOperation().equals(Operation.NOT_IN)) {
                predicates.add(criteriaBuilder.isNotMember(criteria.getValue().toString(), root.get(criteria.getKey())));
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
