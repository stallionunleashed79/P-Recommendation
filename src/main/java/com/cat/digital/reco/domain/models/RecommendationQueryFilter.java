package com.cat.digital.reco.domain.models;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cat.digital.reco.domain.entities.RecommendationDynamicDataSortHelper;
import com.cat.digital.reco.filter.Filter;
import com.cat.digital.reco.filter.type.BooleanFilter;
import com.cat.digital.reco.filter.type.ContainsFilter;
import com.cat.digital.reco.filter.type.StringEqualsFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Query Filter class that builds predicates based on search filters.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationQueryFilter implements Specification<RecommendationDynamicDataSortHelper> {

  private List<Filter> searchFilters;
  private Map<String, String> optionValueOptionNameMap;

  @Override
  public Predicate toPredicate(final Root<RecommendationDynamicDataSortHelper> root,
                               final CriteriaQuery<?> criteriaQuery,
                               final CriteriaBuilder criteriaBuilder) {

    var predicates = new ArrayList<Predicate>();
    searchFilters.forEach(searchFilter -> {
      var propertyName = searchFilter.getPropertyName();
      var filterType = searchFilter.getType();
      var expression = root.get(propertyName).as(String.class);
      switch (filterType) {
        case stringEquals:
          predicates.add(buildPredicate((StringEqualsFilter) searchFilter, criteriaBuilder.lower(expression)));
          break;
        case contains:
          var containsFilter = (ContainsFilter) searchFilter;
          var value = containsFilter.getValue().toLowerCase();
          predicates.add(criteriaBuilder.like(criteriaBuilder.lower(expression), "%" + value + "%"));
          break;
        case booleanFilter:
          var booleanFilter = (BooleanFilter) searchFilter;
          var booleanValue = String.valueOf(booleanFilter.isValue());
          predicates.add(criteriaBuilder.equal(expression, booleanValue));
          break;
        default:
      }
    });
    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }

  /**
   * Builds a predicate for an input stringEquals search filter.
   *
   * @param searchFilter The stringEquals filter
   * @param expression   The expression corresponding to the filter
   * @return The predicate for the stringEquals filter
   */
  private Predicate buildPredicate(final StringEqualsFilter searchFilter,
                                   final Expression<String> expression) {
    var values = searchFilter.getValues();
    var mappedValues = values.stream().map(elem ->
        optionValueOptionNameMap.getOrDefault(elem.toLowerCase(), elem.toLowerCase())).collect(Collectors.toList());
    return expression.in(mappedValues);
  }
}
