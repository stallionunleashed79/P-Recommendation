package com.cat.digital.reco.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.domain.entities.RecommendationCollectionOptionsEntity;
import com.cat.digital.reco.domain.entities.RecommendationDynamicDataSortHelper;
import com.cat.digital.reco.domain.models.Authorization;
import com.cat.digital.reco.domain.models.RecommendationField;
import com.cat.digital.reco.domain.models.RecommendationQueryFilter;
import com.cat.digital.reco.domain.requests.RecommendationSearchRequest;
import com.cat.digital.reco.exceptions.CustomMethodArgumentNotValidException;
import com.cat.digital.reco.filter.Filter;
import com.cat.digital.reco.filter.type.BooleanFilter;
import com.cat.digital.reco.filter.type.StringEqualsFilter;
import com.cat.digital.reco.repositories.RecommendationCollectionOptionsRepository;
import com.cat.digital.reco.repositories.RecommendationDynamicSortHelperRepository;
import com.cat.digital.reco.service.RecommendationQueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Queries the recommendation database and filters recommendations by search criteria.
 */
@Log4j2
@Service
public class RecommendationQueryServiceImpl implements RecommendationQueryService {

  final RecommendationCollectionOptionsRepository recommendationCollectionOptionsRepository;
  final RecommendationDynamicSortHelperRepository recommendationDynamicSortHelperRepository;
  final List<RecommendationField> stringEqualsFilterAllowedFieldEnums = List.of(RecommendationField.RECOMMENDATION_STATUS,
      RecommendationField.RECOMMENDATION_PRIORITY, RecommendationField.RECOMMENDATION_NUMBER,
      RecommendationField.WORK_ORDER_ID, RecommendationField.DEALER_CODE, RecommendationField.IS_DEALER_RECOMMENDATION);
  final List<RecommendationField> containsFilterAllowedFieldEnums = List.of(RecommendationField.SERIAL_NUMBER,
      RecommendationField.ASSET_NAME, RecommendationField.TITLE,
      RecommendationField.SITE, RecommendationField.CUSTOMER_UCID,
      RecommendationField.IS_DEALER_RECOMMENDATION, RecommendationField.OWNER);
  private Map<String, String> optionValueOptionNameMap;

  @Autowired
  public RecommendationQueryServiceImpl(final RecommendationCollectionOptionsRepository recommendationCollectionOptionsRepository,
                                        final RecommendationDynamicSortHelperRepository recommendationDynamicSortHelperRepository) {
    this.recommendationCollectionOptionsRepository = recommendationCollectionOptionsRepository;
    this.recommendationDynamicSortHelperRepository = recommendationDynamicSortHelperRepository;
  }


  @Override
  public List<RecommendationDynamicDataSortHelper> filterRecommendations(
      final Authorization authorization,
      final RecommendationSearchRequest recommendationSearchRequest,
      final Sort recommendationSort) {
    this.optionValueOptionNameMap = recommendationCollectionOptionsRepository.findAll().stream().collect(Collectors.toMap(
        k -> k.getOptionName().toLowerCase(), RecommendationCollectionOptionsEntity::getOptionValue));
    buildEntitlementFilters(authorization, recommendationSearchRequest.getFilters());
    return applySearchFiltersToRecommendations(recommendationSearchRequest.getFilters(), recommendationSort);
  }

  /**
   * Adds filters to existing list of filters based on entitlements.
   * @param authorization The authorization object
   * @param initialFilterSet The initial list of filters
   */
  private void buildEntitlementFilters(
      final Authorization authorization,
      final List<Filter> initialFilterSet) {
    var dealers = authorization.getAuthorizedDealers();
    var isLoggedInUserADealer = authorization.isDealer();
    // Adds a boolean filter to input filters based on logged in user
    if (isLoggedInUserADealer) {
      var isDealerRecommendationFilter = new BooleanFilter();
      isDealerRecommendationFilter.setPropertyName("dealerRecommendation");
      isDealerRecommendationFilter.setValue(true);
      var partyNumberMatchFilter = new StringEqualsFilter();
      partyNumberMatchFilter.setPropertyName("dealerCode");
      partyNumberMatchFilter.setValues(List.copyOf(dealers));
      initialFilterSet.addAll(List.of(isDealerRecommendationFilter, partyNumberMatchFilter));
    }
  }

  /**
   * Filters recommendations based on search filters and sort criteria.
   *
   * @param searchFilters      Search filters to filter recommendations by
   * @param recommendationSort Sort criteria to sort recommendations
   * @return The filtered and sorted recommendations
   */
  private List<RecommendationDynamicDataSortHelper> applySearchFiltersToRecommendations(
      final List<Filter> searchFilters, final Sort recommendationSort) {
    validateSearchFilters(searchFilters);
    var specification =
        buildSearchCriteriaQueryForTheViewTable(searchFilters);
    return recommendationDynamicSortHelperRepository.findAll(specification, recommendationSort);
  }

  /**
   * Validates that the search filters are for valid recommendation fields.
   *
   * @param searchFilters The list of search filters to validate
   */
  private void validateSearchFilters(final List<Filter> searchFilters) {
    var stringEqualsFilterAllowedFields = getFieldValues(stringEqualsFilterAllowedFieldEnums);
    var containsFilterAllowedFields = getFieldValues(containsFilterAllowedFieldEnums);
    final BindingResult bindingResult = new BeanPropertyBindingResult(searchFilters, "Invalid filters error");
    searchFilters.forEach(searchFilter -> {
      var propertyName = searchFilter.getPropertyName();
      var filterType = searchFilter.getType();
      switch (filterType) {
        case stringEquals:
          if (!stringEqualsFilterAllowedFields.contains(propertyName)) {
            bindingResult.addError(new FieldError(propertyName, propertyName,
                String.format("%s is not a valid stringEquals filter.", propertyName)));
          }
          break;
        case contains:
          if (!containsFilterAllowedFields.contains(propertyName)) {
            bindingResult.addError(new FieldError(propertyName, propertyName,
                String.format("%s is not a valid contains filter.", propertyName)));
          }
          break;
        default:
      }
    });
    if (bindingResult.hasErrors()) {
      throw new CustomMethodArgumentNotValidException(CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT, "Invalid filter fields", bindingResult);
    }
  }

  /**
   * Builds a search criteria query dynamically for the view table from the list of filters as input.
   *
   * @param searchFilters The input search filters to build the query from
   * @return The dynamic query based on input search filters
   */
  private Specification<RecommendationDynamicDataSortHelper> buildSearchCriteriaQueryForTheViewTable(
      final List<Filter> searchFilters) {
    return RecommendationQueryFilter.builder().searchFilters(
        searchFilters).optionValueOptionNameMap(optionValueOptionNameMap).build();
  }

  /**
   * Returns the mappped field values for the list of enum fields.
   *
   * @param recommendationFields The list of input enum fields
   * @return The list of mapped enum field values
   */
  private List<String> getFieldValues(final List<RecommendationField> recommendationFields) {
    return recommendationFields.stream().map(RecommendationField::getValue).collect(Collectors.toList());
  }
}
