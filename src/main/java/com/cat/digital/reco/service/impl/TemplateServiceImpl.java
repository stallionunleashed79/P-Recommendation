package com.cat.digital.reco.service.impl;

import static com.cat.digital.reco.common.Constants.TEMPLATE_WAS_NOT_FOUND;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.domain.models.ContainerType;
import com.cat.digital.reco.domain.models.DataType;
import com.cat.digital.reco.domain.models.InputType;
import com.cat.digital.reco.domain.models.PropertyOption;
import com.cat.digital.reco.domain.models.RecommendationField;
import com.cat.digital.reco.domain.models.TemplateProperties;
import com.cat.digital.reco.domain.models.TemplateSectionProperty;
import com.cat.digital.reco.exceptions.EntityNotFoundException;
import com.cat.digital.reco.repositories.RecommendationTemplateRepository;
import com.cat.digital.reco.service.TemplateService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemplateServiceImpl implements TemplateService {

  final RecommendationTemplateRepository templateRepository;

  public TemplateServiceImpl(RecommendationTemplateRepository templateRepository) {
    this.templateRepository = templateRepository;
  }

  @Override
  public List<TemplateProperties> getTemplateDetails(String templateName) {
    var templateEntity = templateRepository.findByTemplateName(templateName).orElseThrow(() -> new EntityNotFoundException(CustomResponseCodes.NOT_FOUND, String.format(TEMPLATE_WAS_NOT_FOUND, templateName)));
    return templateEntity.getTemplateSections().stream().map(section ->
        TemplateProperties.builder()
            .sectionName(section.getSectionName())
            .sectionPosition(section.getSectionPositionNumber())
            .sectionContainerType(ContainerType.builder().type(ContainerType.TypeEnum.fromValue(section.getRecommendationTemplateSectionTypesEntity().getSectionTypeName())).build())
            .sectionProperties(
                section.getTemplateSectionMappings().stream().map(mapping -> {
                  var recommendationField = mapping.getRecommendationField();
                  var fieldName = recommendationField.getFieldName();
                  var sectionPropertiesResponse = TemplateSectionProperty.builder()
                      .propertyName(fieldName)
                      .displayName(recommendationField.getDisplayName())
                      .dataType(DataType.builder().type(DataType.TypeEnum.fromValue(recommendationField.getPayloadType().getPayloadName())).build())
                      .inputType(InputType.builder().type(InputType.TypeEnum.fromValue(recommendationField.getFieldType().getFieldTypeName())).build())
                      .isRequired(mapping.isFieldRequired())
                      .isReadOnly(mapping.isReadOnly())
                      .position(mapping.getFieldPositionNumber())
                      .defaultValue(mapping.getDefaultValue())
                      .minimumPropertyLength(mapping.getMinLength())
                      .maximumPropertyLength(mapping.getMaxLength())
                      .minimumPropertyValue(mapping.getMinPropertyValue())
                      .maximumPropertyValue(fieldName.equalsIgnoreCase(
                          RecommendationField.EXPIRATION_TIME.getValue())
                          ? OffsetDateTime.now().plusYears(1).toString()
                          : mapping.getMaxPropertyValue()
                      ).propertyOptions(
                      Optional.ofNullable(recommendationField.getCollection())
                          .map(collection -> collection.getCollectionOptions()
                              .stream()
                              .map(option -> PropertyOption.builder().name(option.getOptionName()).value(option.getOptionValue()).build())
                              .collect(Collectors.toList()))
                          .orElse(null)
                  ).build();
                  return sectionPropertiesResponse;
                }).collect(Collectors.toList())
            )
            .build()
    ).sorted(Comparator.comparing(TemplateProperties::getSectionPosition)).collect(Collectors.toList());
  }
}
