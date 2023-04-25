package com.cat.digital.reco.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cat.digital.reco.domain.models.RecommendationExportFieldName;
import com.cat.digital.reco.domain.responses.Recommendation;
import com.cat.digital.reco.domain.responses.RecommendationExport;
import com.cat.digital.reco.exceptions.RecoServerException;
import com.cat.digital.reco.common.CustomResponseCodes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvConversionHelper {
  /**
   * Construct CSV value using from the generic model.
   *
   * @param items list of generic class that can be transformed into a CSV value
   * @param delimiter this is the delimiter value e.g. for csv it will be a comma (,)
   * @param <T> generic class that must be transformed
   * @return StringBuilder object which holds the CSV value
   */
  public static <T> StringBuilder toCsv(List<T> items, String delimiter) {
    var fields = items.get(0).getClass().getDeclaredFields();
    Arrays.asList(fields).forEach(field -> field.setAccessible(true));
    var columnNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList()).toArray(String[]::new);
    var modifiedColumnNames = Arrays.stream(columnNames).map(columnName -> RecommendationExportFieldName.valueOf(columnName.toUpperCase()).getValue()).collect(Collectors.toList());

    var csv = new StringBuilder();
    csv.append(String.join(delimiter, modifiedColumnNames));
    csv.append("\n");

    for (int i = 0; i < items.size(); i++) {
      ArrayList<String> results = new ArrayList<>();
      for (Field value : fields) {
        T item = items.get(i);
        var csvFieldBasedOnValue = getCsvValue(value, item, delimiter);
        results.add(csvFieldBasedOnValue.toString());
      }
      csv.append(String.join(delimiter, results));
      csv.append("\n");
    }
    return csv;
  }

  /**
   * Helper method that parses the generic field and returns the string value for CSV construction.
   *
   * @param field generic property
   * @param item list of generic class that can be transformed into a CSV value
   * @param delimiter generic class that must be transformed
   * @param <T> generic class that must be transformed
   * @return String value for the CSV
   */
  private static <T> CharSequence getCsvValue(Field field, T item, String delimiter) {
    String value;
    try {
      var o = field.get(item);
      if (Objects.isNull(o)) {
        return "NULL";
      }
      if (o.toString().trim().length() == 0) {
        return StringUtils.EMPTY;
      }
      value = o.toString().replace("\"", "\"\"");
      if (value.contains(delimiter)) {
        value = String.format("\"%s\"", value);
      }
    } catch (IllegalAccessException exception) {
      log.error("Error accessing field: {}", field.getName());
      throw new RecoServerException(CustomResponseCodes.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
    return value;
  }
}
