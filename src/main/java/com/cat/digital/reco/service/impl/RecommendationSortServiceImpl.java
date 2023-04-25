package com.cat.digital.reco.service.impl;

import java.util.List;
import java.util.Optional;

import com.cat.digital.reco.domain.models.OrderByParam;
import com.cat.digital.reco.domain.models.RecommendationSortByParam;
import com.cat.digital.reco.service.RecommendationSortService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RecommendationSortServiceImpl implements RecommendationSortService {

  @Override
  public Sort getRecommendationSort(final List<RecommendationSortByParam> columns, final List<OrderByParam> directions) {
    var column = columns.stream().findFirst();
    var direction = directions.stream().findFirst();

    if (column.isPresent() && direction.isPresent()) {
      Sort sortForColumn = Sort.by(column.get().getValue());

      // using opposite direction of datetime to sort by number of days since date
      if (column.get().equals(RecommendationSortByParam.NUMBER_OF_DAYS_SINCE_MODIFIED)) {
        if (direction.get().equals(OrderByParam.DESC)) {
          return sortForColumn.ascending();
        } else {
          return sortForColumn.descending();
        }
      }

      return fetchSortOrder(direction, sortForColumn);
    }

    return Sort.unsorted();
  }

  /**
   * Compute Sort order based on direction.
   * @param direction Input direction
   * @param sortForColumn The column to sort by
   * @return The sort order
   */
  private Sort fetchSortOrder(final Optional<OrderByParam> direction, final Sort sortForColumn) {
    if (direction.isPresent() && direction.get().equals(OrderByParam.DESC)) {
      return sortForColumn.descending();
    } else {
      return sortForColumn.ascending();
    }
  }
}
