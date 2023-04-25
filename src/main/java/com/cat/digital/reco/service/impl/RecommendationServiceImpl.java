package com.cat.digital.reco.service.impl;

import static com.cat.digital.reco.common.Constants.*;

import javax.transaction.Transactional;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.common.RecommendationFieldDataTypes;
import com.cat.digital.reco.dao.AssetsDao;
import com.cat.digital.reco.dao.NotesDao;
import com.cat.digital.reco.dao.StorageDao;
import com.cat.digital.reco.dao.UserManagementDao;
import com.cat.digital.reco.domain.entities.RecommendationCollectionOptionsEntity;
import com.cat.digital.reco.domain.entities.RecommendationCommonDataEntity;
import com.cat.digital.reco.domain.entities.RecommendationCustomDataEntity;
import com.cat.digital.reco.domain.entities.RecommendationDynamicDataSortHelper;
import com.cat.digital.reco.domain.entities.RecommendationFieldsEntity;
import com.cat.digital.reco.domain.entities.RecommendationTemplateSectionFieldMappingsEntity;
import com.cat.digital.reco.domain.entities.RecommendationTemplateSectionsEntity;
import com.cat.digital.reco.domain.entities.RecommendationTemplatesEntity;
import com.cat.digital.reco.domain.entities.RecommendationUsersEntity;
import com.cat.digital.reco.domain.models.AssetDetails;
import com.cat.digital.reco.domain.models.AssetMetaData;
import com.cat.digital.reco.domain.models.AssetOwnershipAtRecommendation;
import com.cat.digital.reco.domain.models.Authorization;
import com.cat.digital.reco.domain.models.CommonFieldsResponse;
import com.cat.digital.reco.domain.models.HoursReading;
import com.cat.digital.reco.domain.models.OrderByParam;
import com.cat.digital.reco.domain.models.Owner;
import com.cat.digital.reco.domain.models.RecommendationField;
import com.cat.digital.reco.domain.models.RecommendationRichTextFields;
import com.cat.digital.reco.domain.models.RecommendationSortByParam;
import com.cat.digital.reco.domain.models.TemplateCustomField;
import com.cat.digital.reco.domain.models.User;
import com.cat.digital.reco.domain.requests.RecommendationPostRequest;
import com.cat.digital.reco.domain.requests.RecommendationPutRequest;
import com.cat.digital.reco.domain.requests.RecommendationSearchRequest;
import com.cat.digital.reco.domain.responses.Recommendation;
import com.cat.digital.reco.domain.responses.RecommendationCustomOptionData;
import com.cat.digital.reco.domain.responses.RecommendationDetailsResponse;
import com.cat.digital.reco.domain.responses.RecommendationExport;
import com.cat.digital.reco.entitlement.EntitlementUtils;
import com.cat.digital.reco.exceptions.AuthorizationException;
import com.cat.digital.reco.exceptions.CustomMethodArgumentNotValidException;
import com.cat.digital.reco.exceptions.EntityNotFoundException;
import com.cat.digital.reco.exceptions.InvalidInputRequestException;
import com.cat.digital.reco.exceptions.RecoServerException;
import com.cat.digital.reco.exceptions.RecommendationNumberException;
import com.cat.digital.reco.repositories.RecommendationCommonDataRepository;
import com.cat.digital.reco.repositories.RecommendationCustomDataRepository;
import com.cat.digital.reco.repositories.RecommendationDynamicSortHelperRepository;
import com.cat.digital.reco.repositories.RecommendationTemplateRepository;
import com.cat.digital.reco.repositories.RecommendationUsersRepository;
import com.cat.digital.reco.service.PDFGenerator;
import com.cat.digital.reco.service.RecommendationQueryService;
import com.cat.digital.reco.service.RecommendationService;
import com.cat.digital.reco.service.RecommendationSortService;
import com.cat.digital.reco.utils.CsvConversionHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Service layer implementation class for the recommendation module.
 */
@Log4j2
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecommendationServiceImpl implements RecommendationService {

  static final int ZERO = 0;
  static final String REGEX_SPLIT_NUMBER = "(?<=\\G...)";
  static final String DELIMITER = ",";
  static final String EXPORT_DATE_FORMAT = "dd/mm/yy";

  final RecommendationCommonDataRepository recommendationCommonDataRepository;
  final RecommendationUsersRepository recommendationUsersRepository;
  final RecommendationCustomDataRepository recommendationCustomDataRepository;
  final RecommendationTemplateRepository recommendationTemplateRepository;
  final UserManagementDao userManagementDao;
  final SecureRandom random;
  final AssetsDao assetsDao;
  final NotesDao notesDao;
  final StorageDao storageDao;
  final EntitlementUtils entitlementUtils;
  final PDFGenerator pdfGenerator;
  final AuditServiceImpl auditService;
  final RecommendationDynamicSortHelperRepository recommendationDynamicSortHelperRepository;
  final RecommendationSortService recommendationSortService;
  final RecommendationQueryService recommendationQueryService;

  @Value("#{${cat.template.common.create.default.fields:{recommendationStatus:\"Draft\"}}}")
  Map<String, String> defaultFieldsWithValues;

  //TODO change this to retrieve the information from DB
  @Value("${cat.template.common.create.required.fields:recommendationPriority}")
  List<String> requiredFieldsForCreate;

  @Value("${cat.template.common.update.required.fields:recommendationStatus, recommendationPriority}")
  List<String> requiredFieldsForUpdate;

  @Value("${cat.timezone.default:UTC}")
  String defaultTimeZone;

  @Value("${cat.recommendation.number.generator.biggest:999999998}")
  int biggestNumber;

  @Value("${cat.recommendation.number.generator.retry:10}")
  int maxRetryNumber;


  public RecommendationServiceImpl(
      final RecommendationCommonDataRepository recommendationCommonDataRepository,
      final RecommendationUsersRepository recommendationUsersRepository,
      final RecommendationCustomDataRepository recommendationCustomDataRepository,
      final RecommendationTemplateRepository recommendationTemplateRepository,
      final SecureRandom random,
      final AssetsDao assetsDao,
      final EntitlementUtils entitlementUtils,
      final UserManagementDao userManagementDao,
      final NotesDao notesDao,
      final StorageDao storageDao,
      final PDFGenerator pdfGenerator,
      final AuditServiceImpl auditService,
      final RecommendationSortService recommendationSortService,
      final RecommendationDynamicSortHelperRepository recommendationDynamicSortHelperRepository,
      final RecommendationQueryService recommendationQueryService) {
    this.recommendationCommonDataRepository = recommendationCommonDataRepository;
    this.recommendationUsersRepository = recommendationUsersRepository;
    this.recommendationCustomDataRepository = recommendationCustomDataRepository;
    this.recommendationTemplateRepository = recommendationTemplateRepository;
    this.random = random;
    this.assetsDao = assetsDao;
    this.userManagementDao = userManagementDao;
    this.entitlementUtils = entitlementUtils;
    this.notesDao = notesDao;
    this.storageDao = storageDao;
    this.pdfGenerator = pdfGenerator;
    this.auditService = auditService;
    this.recommendationSortService = recommendationSortService;
    this.recommendationQueryService = recommendationQueryService;
    this.recommendationDynamicSortHelperRepository = recommendationDynamicSortHelperRepository;
  }

  /**
   * Creates a recommendation from request.
   *
   * @param payload Body of the request
   * @return the response from the create method
   */
  @Override
  @Transactional
  public RecommendationDetailsResponse createRecommendation(
      final RecommendationPostRequest payload,
      final Authorization authorization) throws AuthorizationException {
    // validate the template name
    var template = recommendationTemplateRepository.findByTemplateName(payload.getTemplateName()).orElseThrow(() -> new EntityNotFoundException(CustomResponseCodes.NOT_FOUND, String.format(TEMPLATE_WAS_NOT_FOUND, payload.getTemplateName())));

    var templateFields = getRecommendationFields(template);

    // validate asset
    var assetData = validateAssetEnrolled(payload.getAssetId());
    entitlementUtils.validatePartyNumbers(authorization, assetData.getDealerCode());

    // Validator for requires fields in the payload
    businessValidationForCreate(payload.getTemplateCustomProperties());

    setDefaultValues(payload, assetData);

    // Validator for custom/dynamic properties
    validateDynamicFields(payload.getTemplateCustomProperties(), templateFields, template);

    var header = new RecommendationCommonDataEntity();

    header.setRecommendationNumber(generateRecommendationNumber());

    populateFieldsForCreate(payload, template, header, assetData, authorization);
    var owner = registerNewUser(payload.getOwner().getCatrecid());
    header.setOwnedBy(owner);

    var createdBy = registerNewUser(authorization.getCatRecId());
    header.setCreatedBy(createdBy);
    header.setUpdatedBy(createdBy);

    recommendationCommonDataRepository.saveAndFlush(header);

    List<RecommendationCustomDataEntity> details = mapTemplateFieldsToRecoDetails(
        payload.getTemplateCustomProperties(), templateFields, header);

    recommendationCustomDataRepository.saveAll(details);
    header.setRecommendationDetails(details);

    return mapRecommendationHeaderEntityToRecommendationDetailsGet(header);
  }

  /**
   * Fetches the recommendation fields for a given template.
   *
   * @param template The template to fetch fields from
   * @return the list of recommendation fields
   */
  private List<RecommendationFieldsEntity> getRecommendationFields(final RecommendationTemplatesEntity template) {
    return template.getTemplateSections().stream()
        .flatMap(recommendationTemplateSection -> recommendationTemplateSection.getTemplateSectionMappings().stream())
        .map(RecommendationTemplateSectionFieldMappingsEntity::getRecommendationField)
        .collect(Collectors.toList());
  }

  /**
   * Populates optional and default fields for POST recommendation.
   *
   * @param payload   Body of the request
   * @param assetData details of the asset
   */
  private void setDefaultValues(RecommendationPostRequest payload, AssetDetails assetData) {

    if (Objects.isNull(payload.getHoursReading()) || Objects.isNull(payload.getHoursReading().getReading())) {
      var hoursReading = new HoursReading();
      hoursReading.setReading(BigDecimal.valueOf(assetData.getSmu()));
      payload.setHoursReading(hoursReading);
    }

    if (Objects.isNull(payload.getExpirationTime())) {
      payload.setExpirationTime(OffsetDateTime.now().plusMonths(1));
    }

    if (Objects.isNull(payload.getSite())) {
      payload.setSite(assetData.getSite());
    }

    // if the payload have some values contained on the defaultFieldsWithValues, they will be removed,
    // after that, all requiredFields will be created as part of the payload
    var defaultFields = defaultFieldsWithValues.keySet();
    payload.getTemplateCustomProperties().removeIf(field -> defaultFields.contains(field.getPropertyName()));
    defaultFieldsWithValues.forEach((k, v) -> payload.getTemplateCustomProperties().add(TemplateCustomField.builder().propertyName(k).propertyValue(v).build()));
  }

  /**
   * Validating for required fields for the Create / POST request.
   *
   * @param templateCustomFields The custom template properties for create request
   */
  private void businessValidationForCreate(final List<TemplateCustomField> templateCustomFields) {
    validateForRequiredFields(templateCustomFields, this.requiredFieldsForCreate);
  }

  /**
   * Validating for required fields for the Update / PUT request.
   *
   * @param templateCustomFields The custom template properties for update request
   */
  private void businessValidationForUpdate(final List<TemplateCustomField> templateCustomFields) {
    validateForRequiredFields(templateCustomFields, this.requiredFieldsForUpdate);
    templateCustomFields.forEach(customField -> validateForRichTextContent(
        customField.getPropertyName(), customField.getPropertyValue()));
  }

  /**
   * Validates required fields and generates validation errors (if any).
   *
   * @param templateCustomFields The custom template proprties of request body
   * @param requiredFields       the list of required fields for that request to validate against
   */
  private void validateForRequiredFields(final List<TemplateCustomField> templateCustomFields,
                                         final List<String> requiredFields) {
    var payloadFieldNames = templateCustomFields.stream().map(TemplateCustomField::getPropertyName).collect(Collectors.toList());
    //TODO change this part to response with the list of required fields
    var requiredFieldsClone = new ArrayList<>(requiredFields);
    requiredFieldsClone.removeAll(payloadFieldNames);
    final BindingResult bindingResult = new BeanPropertyBindingResult(templateCustomFields, "Required fields error");
    requiredFieldsClone.forEach(fieldName -> {
      bindingResult.addError(new FieldError(fieldName, fieldName, String.format("The %s is a required field.", fieldName)));
    });
    if (bindingResult.hasErrors()) {
      throw new CustomMethodArgumentNotValidException(CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT, "Missing required fields", bindingResult);
    }
  }

  /**
   * Service method to update a recommendation.
   *
   * @param recommendationPutRequest The request body for Recommendation PUT end point
   * @param recommendationNumber     The unique identifier number for the recommendation
   * @param authorization            The authorized party numbers for the logged in user
   */
  @Transactional
  public RecommendationDetailsResponse updateRecommendation(final RecommendationPutRequest recommendationPutRequest,
                                                            final String recommendationNumber,
                                                            final Authorization authorization) throws AuthorizationException {
    final RecommendationCommonDataEntity recommendationToBeUpdated =
        validateRecommendationExists(recommendationNumber, CustomResponseCodes.NOT_FOUND);
    businessValidationForUpdate(recommendationPutRequest.getTemplateCustomProperties());
    entitlementUtils.validateUpdateAction(authorization, recommendationToBeUpdated.isDealerRecommendation());
    entitlementUtils.validatePartyNumbers(authorization, recommendationToBeUpdated.getDealerCode());
    registerNewUser(authorization.getCatRecId());
    recommendationToBeUpdated.setUpdatedByCatrecid(authorization.getCatRecId());
    populateRecoDetailsInHeader(recommendationToBeUpdated, recommendationPutRequest);
    populateHeaderForUpdate(recommendationToBeUpdated, recommendationPutRequest);
    recommendationCommonDataRepository.saveAndFlush(recommendationToBeUpdated);
    return mapRecommendationHeaderEntityToRecommendationDetailsGet(recommendationToBeUpdated);
  }

  /**
   * Maps a list of dynamic fields from request to recommendation details list.
   *
   * @param requestFields  List of fields from PUT request
   * @param templateFields List of fields belonging to template
   * @param header         The header entity of the recommendation
   * @return the list of recommendation details entities
   */
  private List<RecommendationCustomDataEntity> mapTemplateFieldsToRecoDetails(
      final List<TemplateCustomField> requestFields,
      final List<RecommendationFieldsEntity> templateFields,
      final RecommendationCommonDataEntity header) {
    return requestFields.stream().map(dynamicField -> {
      var field = templateFields.stream()
          .filter(templateField -> templateField.getFieldName().equalsIgnoreCase(
              dynamicField.getPropertyName())).findFirst().get();
      return RecommendationCustomDataEntity.builder().recommendationHeader(header)
          .recommendationId(header.getRecommendationId()).recommendationField(field).fieldId(field.getFieldId()).value(dynamicField.getPropertyValue()).build();
    }).collect(Collectors.toList());
  }

  @Override
  public RecommendationDetailsResponse getRecommendationDetails(final String recommendationNumber,
                                                                final Authorization authorization) throws AuthorizationException {
    RecommendationCommonDataEntity recommendationCommonDataEntity = validateRecommendationExists(
        recommendationNumber, CustomResponseCodes.NOT_FOUND);
    entitlementUtils.validateViewAction(authorization, recommendationCommonDataEntity.isDealerRecommendation());
    entitlementUtils.validatePartyNumbers(authorization, recommendationCommonDataEntity.getDealerCode());
    return mapRecommendationHeaderEntityToRecommendationDetailsGet(recommendationCommonDataEntity);
  }

  @Override
  public InputStreamResource getRecommendationPDF(String recommendationNumber, final Authorization authorization) throws AuthorizationException {
    try {
      var response = this.getRecommendationDetails(recommendationNumber, authorization);
      response.getCommonFields().setTitle(response.getCommonFields().getTitle().toUpperCase(Locale.ROOT));
      return this.pdfGenerator.generatePDF(response.getTemplateName(), response);
    } catch (IOException e) {
      log.error("failed to load the PDF");
      throw new RecoServerException(CustomResponseCodes.INTERNAL_SERVER_ERROR, "could not load the PDF file");
    } catch (TransformerException e) {
      log.error("Something went wrong with the transformer failed to create the PDF file for recommendation");
      throw new RecoServerException(CustomResponseCodes.INTERNAL_SERVER_ERROR, "could not load the PDF file");
    }
  }

  /**
   * Populates the header from POST request.
   *
   * @param payload  the request body
   * @param template the template name
   * @param header   the common header of the POST request
   */
  private void populateFieldsForCreate(RecommendationPostRequest payload, RecommendationTemplatesEntity template, RecommendationCommonDataEntity header, AssetDetails assetData, Authorization authorization) {
    header.setTitle(payload.getTitle());
    header.setSmu(payload.getHoursReading().getReading().intValue());
    header.setOwner(payload.getOwner().getCatrecid());
    header.setAssetName(payload.getAssetId());
    header.setSite(payload.getSite());

    header.setExpirationDate(Timestamp.valueOf(payload.getExpirationTime().toLocalDateTime()));
    header.setCreatedDate(Timestamp.from(ZonedDateTime.now(ZoneId.of(defaultTimeZone)).toInstant()));
    header.setUpdatedDate(Timestamp.from(ZonedDateTime.now(ZoneId.of(defaultTimeZone)).toInstant()));

    header.setCreatedByCatrecid(authorization.getCatRecId());
    header.setUpdatedByCatrecid(authorization.getCatRecId());

    header.setTemplate(template);

    header.setSerialNumber(assetData.getSerialNumber());
    header.setMake(assetData.getMake());
    header.setModel(assetData.getModel());

    header.setDealerCode(assetData.getDealerCode());
    header.setDealerName(assetData.getDealerName());

    header.setDealerRecommendation(authorization.isDealer());
  }

  @Override
  public List<Recommendation> getRecommendations(final Authorization authorization,
                                                 final List<RecommendationSortByParam> sortByParams,
                                                 final List<OrderByParam> orderByParams,
                                                 final RecommendationSearchRequest searchRequest) throws AuthorizationException {
    var filteredRecoCommonDataEntitiesFromDB = getRecommendationsByFilters(authorization, sortByParams, orderByParams, searchRequest);
    return filteredRecoCommonDataEntitiesFromDB.stream().map(
        item -> {
          var customData = item.getRecommendationDetails();
          var priorityField = getCollectionOptionName(customData, RecommendationField.RECOMMENDATION_PRIORITY.getValue(), item.getRecommendationNumber());
          var status = getCollectionOptionName(customData, RecommendationField.RECOMMENDATION_STATUS.getValue(), item.getRecommendationNumber());
          var ownedBy = item.getOwnedBy();
          var primaryCustomerName = getPrimaryCustomerName(item.getAssetName());
          return populateRecommendation(item, priorityField, status, ownedBy, primaryCustomerName);
        }
    ).collect(Collectors.toList());
  }

  @Override
  public InputStreamResource getRecommendationsExport(final Authorization authorization,
                                                      final List<RecommendationSortByParam> sortByParams,
                                                      final List<OrderByParam> orderByParams,
                                                      final RecommendationSearchRequest recommendationSearchRequest
  ) {
    var filteredRecommendationList = getRecommendationsByFilters(
        authorization, sortByParams, orderByParams, recommendationSearchRequest);
    var recommendationList = filteredRecommendationList.stream().map(
        item -> {
          var customData = item.getRecommendationDetails();
          var priorityField = getCollectionOptionName(customData, RecommendationField.RECOMMENDATION_PRIORITY.getValue(), item.getRecommendationNumber());
          var status = getCollectionOptionName(customData, RecommendationField.RECOMMENDATION_STATUS.getValue(), item.getRecommendationNumber());
          var ownedBy = item.getOwnedBy();
          var primaryCustomerName = getPrimaryCustomerName(item.getAssetName());
          var customerName = getCustomOptionValue(customData, RecommendationField.CUSTOMER.getValue());
          var estimateCurrency = getCustomOptionValue(customData, RecommendationField.VALUE_ESTIMATE_CURRENCY.getValue());
          var estimatedFailureCost = getCustomOptionValue(customData, RecommendationField.VALUE_ESTIMATE_FAILURE_COST.getValue());
          var estimateRepairCost = getCustomOptionValue(customData, RecommendationField.VALUE_ESTIMATE_REPAIR_COST.getValue());

          var recommendationCustomOptionData = RecommendationCustomOptionData.builder()
              .customer(customerName)
              .valueEstimateCurrency(estimateCurrency)
              .valueEstimateFailureCost(estimatedFailureCost)
              .valueEstimateRepairCost(estimateRepairCost)
              .build();

          return populateRecommendationExport(item, priorityField, status, ownedBy, primaryCustomerName, recommendationCustomOptionData);
        }
    ).collect(Collectors.toList());
    if (recommendationList.isEmpty()) {
      throw new RecoServerException(CustomResponseCodes.NOT_FOUND, "Error exporting recommendation data as csv file based on the filtering criteria");
    }
    var csvResult = CsvConversionHelper.toCsv(recommendationList, DELIMITER).toString();
    var stream = new ByteArrayInputStream(csvResult.getBytes(StandardCharsets.UTF_8));
    return new InputStreamResource(stream);
  }

  /**
   * Perform filtering on recommendations from the set of filters passed in.
   *
   * @param authorization Authorization
   * @param sortByParams  The parameters to sort recommendations by
   * @param orderByParams The parameters to order recommendations by
   * @param searchRequest The search request to search recommendations by
   * @return The filtered recommendations
   */
  private List<RecommendationCommonDataEntity> getRecommendationsByFilters(
      final Authorization authorization, final List<RecommendationSortByParam> sortByParams,
      final List<OrderByParam> orderByParams, final RecommendationSearchRequest searchRequest) {
    var logicalExpression = searchRequest.getLogicalExpression();
    var searchFilters = searchRequest.getFilters();
    var noFiltersApplied = StringUtils.isEmpty(logicalExpression) || searchFilters.isEmpty();
    var recommendationSort = recommendationSortService.getRecommendationSort(sortByParams, orderByParams);
    var resultSet = noFiltersApplied
        ? recommendationDynamicSortHelperRepository.findAll(recommendationSort)
        : recommendationQueryService.filterRecommendations(authorization, searchRequest,
        recommendationSort);
    if (resultSet.isEmpty()) {
      throw new RecoServerException(CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT,
          NO_SORT_FILTER_RECOMMENDATION);
    }
    return resultSet.stream().map(RecommendationDynamicDataSortHelper::getRecommendationCommonData)
        .collect(Collectors.toList());
  }

  private Recommendation populateRecommendation(RecommendationCommonDataEntity item, String priorityField, String status,
                                                RecommendationUsersEntity ownedBy, String primaryCustomerName) {
    return Recommendation.builder()
        .serialNumber(item.getSerialNumber())
        .make(item.getMake())
        .model(item.getModel())
        .createdDate(OffsetDateTime.of(item.getCreatedDate().toLocalDateTime(), ZoneOffset.UTC))
        .title(item.getTitle())
        .recommendationPriority(priorityField)
        .recommendationStatus(status)
        .expirationDate(OffsetDateTime.of(item.getExpirationDate().toLocalDateTime(), ZoneOffset.UTC))
        .ucidName(primaryCustomerName)
        .site(item.getSite())
        .assetName(item.getAssetName())
        .owner(Owner.builder().firstName(ownedBy.getFirstName()).lastName(ownedBy.getLastName())
            .catrecid(ownedBy.getCatrecid()).build())
        .recommendationNumber(item.getRecommendationNumber())
        .templateName(item.getTemplate().getTemplateName())
        .build();
  }

  private RecommendationExport populateRecommendationExport(RecommendationCommonDataEntity item, String priorityField, String status,
                                                            RecommendationUsersEntity ownedBy, String primaryCustomerName, RecommendationCustomOptionData recommendationCustomOptionData) {
    return RecommendationExport.builder()
        .serialNumber(item.getSerialNumber())
        .model(item.getModel())
        .createdDate(item.getCreatedDate().toLocalDateTime().format(DateTimeFormatter.ofPattern(EXPORT_DATE_FORMAT)))
        .title(item.getTitle())
        .recommendationPriority(priorityField)
        .recommendationStatus(status)
        .expirationDate(item.getExpirationDate().toLocalDateTime().format(DateTimeFormatter.ofPattern(EXPORT_DATE_FORMAT)))
        .ucidName(primaryCustomerName)
        .site(item.getSite())
        .assetName(item.getAssetName())
        .owner(ownedBy.getFirstName() + " " + ownedBy.getLastName())
        .recommendationNumber(item.getRecommendationNumber())
        .updatedDate(item.getUpdatedDate().toLocalDateTime().format(DateTimeFormatter.ofPattern(EXPORT_DATE_FORMAT)))
        .currency(recommendationCustomOptionData.getValueEstimateCurrency())
        .estimatedFailureCost(recommendationCustomOptionData.getValueEstimateFailureCost())
        .repairCost(recommendationCustomOptionData.getValueEstimateRepairCost())
        .build();
  }

  /**
   * Validates an asset exists.
   *
   * @param assetName The name of the asset to validate against
   * @return the asset model for the asset name
   */
  private AssetDetails getAssetDetails(final String assetName) {
    var assetDecomposed = assetName.split(ASSET_ID_DELIMITER);
    if (assetDecomposed.length < 3) {
      throw new InvalidInputRequestException(CustomResponseCodes.BAD_REQUEST_MISSING_REQUIRED_REQUEST_BODY,
          String.format(ASSET_INVALID_FORMAT, assetName));
    }
    var make = assetDecomposed[0];
    var serialNumber = assetDecomposed[1];
    var primaryCustomerNumber = assetDecomposed[2];
    var assetDetailsOptional =
        assetsDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(
            make, serialNumber, primaryCustomerNumber);
    return assetDetailsOptional.orElseThrow(() ->
        new InvalidInputRequestException(CustomResponseCodes.NOT_FOUND,
            String.format(ASSET_WAS_NOT_FOUND, assetName)));
  }

  /**
   * Retrieve primary customer name corresponding to the assetId.
   *
   * @param assetName The name of the asset
   * @return primary name of the customer owning the asset
   */
  private String getPrimaryCustomerName(final String assetName) {
    AssetDetails asset = getAssetDetails(assetName);
    return asset.getPrimaryCustomerName();
  }

  /**
   * Retrieve matching custom entity for the corresponding fieldName.
   *
   * @param customData the list of custom data entity corresponding to a recommendation
   * @param fieldName  the value representing custom data attributes name i.e. recommendationStatus
   * @return the entity that has information about the template field
   */
  private Optional<RecommendationCustomDataEntity> getMatchingDetail(
      List<RecommendationCustomDataEntity> customData, String fieldName) {
    return customData.stream().filter(it -> it.getRecommendationField().getFieldName().equalsIgnoreCase(fieldName)).findFirst();
  }

  /**
   * Retrieve custom data value for the corresponding fieldName.
   *
   * @param customData the list of custom data entity corresponding to a recommendation
   * @param fieldName  the value representing custom data attributes name i.e. recommendationStatus
   * @return the recommendation custom data value
   */
  private String getCustomOptionValue(List<RecommendationCustomDataEntity> customData, String fieldName) {
    Optional<RecommendationCustomDataEntity> matchingDetailOptional = getMatchingDetail(customData, fieldName);
    return matchingDetailOptional.isEmpty() ? Strings.EMPTY : matchingDetailOptional.get().getValue();
  }

  /**
   * Retrieve options name for the corresponding fieldName.
   *
   * @param customData           the list of custom data entity corresponding to a recommendation
   * @param fieldName            the value representing custom data attributes name i.e. recommendationStatus
   * @param recommendationNumber unique identifier for a recommendation
   * @return the recommendation custom data entity name e.g. Draft
   */
  private String getCollectionOptionName(List<RecommendationCustomDataEntity> customData, String fieldName, String recommendationNumber) {
    var matchingDetailOptional = getMatchingDetail(customData, fieldName);
    return matchingDetailOptional.map(detail -> findOptionByOptionValue(detail.getRecommendationField().getCollection().getCollectionOptions(),
        detail.getValue(), fieldName, recommendationNumber).getOptionName()).orElseThrow(() ->
        new RecoServerException(
            CustomResponseCodes.INTERNAL_SERVER_ERROR,
            String.format("Recommendation field %s has an invalid value for recommendationNumber: %s", fieldName, recommendationNumber)));
  }

  /**
   * Validates if recommendation exists in the database for the given recommendationNumber.
   *
   * @param recommendationNumber The recommendation number of the recommendation to validate
   * @return the recommendation from the database (if it exists)
   */
  public RecommendationCommonDataEntity validateRecommendationExists(
      final String recommendationNumber, CustomResponseCodes errorCode) {
    var recommendationHeaderOptional =
        recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber);
    var headerEntity = recommendationHeaderOptional.orElseThrow(() -> new InvalidInputRequestException(errorCode,
        String.format("Recommendation %s does not exist.", recommendationNumber)));

    validateAssetEnrolled(headerEntity.getAssetName());
    return headerEntity;
  }

  /**
   * Validates an asset exists is enrolled.
   *
   * @param assetName The name of the asset to validate against
   * @return the asset model for the asset name
   */
  private AssetDetails validateAssetEnrolled(final String assetName) {
    AssetDetails asset = getAssetDetails(assetName);
    if (!asset.isEnabled()) {
      throw new InvalidInputRequestException(CustomResponseCodes.NOT_FOUND,
          String.format(ASSET_IS_NOT_ENROLLED, assetName));
    }
    return asset;
  }

  /**
   * Populate recommendation details in recommendation header.
   *
   * @param recommendationToBeUpdated The existing recommendation in database to be updated
   */
  private void populateRecoDetailsInHeader(
      final RecommendationCommonDataEntity recommendationToBeUpdated,
      final RecommendationPutRequest recommendationPutRequest) {

    RecommendationTemplatesEntity template = recommendationToBeUpdated.getTemplate();
    var templateFields = getRecommendationFields(template);
    var dynamicFieldsInRequest =
        recommendationPutRequest.getTemplateCustomProperties();
    //Validate all dynamic fields in the request body.
    validateDynamicFields(dynamicFieldsInRequest, templateFields, template);

    auditService.setAuditFields(recommendationPutRequest, recommendationToBeUpdated, templateFields);
    //Generate recommendation details from fields in the request body.
    var recommendationDetails =
        mapTemplateFieldsToRecoDetails(dynamicFieldsInRequest, templateFields,
            recommendationToBeUpdated);

    var recommendationDetailsMap = recommendationDetails.stream().collect(Collectors.toMap(RecommendationCustomDataEntity::getFieldId, detail -> detail));

    //Clearing out the fields was removed from the payload, the remain fields will be updated.
    var detailsToBeDeleted = recommendationToBeUpdated.getRecommendationDetails().stream().filter(detail -> !recommendationDetailsMap.containsKey(detail.getFieldId())).collect(Collectors.toList());
    recommendationCustomDataRepository.deleteInBatch(detailsToBeDeleted);

    /*
     * Now re-inserts all of the fields that come as part of the request that have
     * been cleared out in the line above
     */
    recommendationToBeUpdated.setRecommendationDetails(recommendationDetails);
  }

  /**
   * Populate existing recommendation header from request body.
   *
   * @param recommendationToBeUpdated The existing recommendation in database to be updated
   * @param putRequest                The static part of the recommendation PUT request body
   */
  private void populateHeaderForUpdate(
      final RecommendationCommonDataEntity recommendationToBeUpdated,
      final RecommendationPutRequest putRequest) {
    recommendationToBeUpdated.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
    recommendationToBeUpdated.setSmu(
        putRequest.getHoursReading().getReading().intValue());
    recommendationToBeUpdated.setTitle(putRequest.getTitle());
    recommendationToBeUpdated.setExpirationDate(Timestamp.valueOf(putRequest.getExpirationTime().toLocalDateTime()));
    registerNewUser(putRequest.getOwner().getCatrecid());
    recommendationToBeUpdated.setOwner(putRequest.getOwner().getCatrecid());
  }


  /**
   * register a new user when it doesn't exist on DB.
   *
   * @param catRecId user id for register i case doesn't exist on DB
   */
  private RecommendationUsersEntity registerNewUser(final String catRecId) {
    var userOptional = recommendationUsersRepository.findById(catRecId);
    return userOptional.orElseGet(() -> {
      try {
        var userData = userManagementDao.getUserData(catRecId);
        var recommendationUsersEntity = new RecommendationUsersEntity();
        recommendationUsersEntity.setCatrecid(catRecId);
        recommendationUsersEntity.setFirstName(userData.getFirstName());
        recommendationUsersEntity.setLastName(userData.getLastName());
        recommendationUsersEntity.setCwsId(userData.getUsername());
        return recommendationUsersRepository.saveAndFlush(recommendationUsersEntity);
      } catch (IOException e) {
        throw new InvalidInputRequestException(CustomResponseCodes.INTERNAL_SERVER_ERROR, "Unexpected error occurred trying to get the information from user");
      }
    });
  }

  /**
   * Generates recommendation number for recommendation.
   *
   * @return return the generated recommendation number
   */
  private String generateRecommendationNumber() {
    String recommendationNumber;
    var retry = maxRetryNumber;

    // generate number until they are not unique or retry are minor of the maximum attempts
    do {
      var split = String.join("-",
          String.format("%09d", random.nextInt(biggestNumber) + 1)
              .split(REGEX_SPLIT_NUMBER));

      recommendationNumber = String.format("REC-%s", split);
    } while (recommendationCommonDataRepository.existsByRecommendationNumber(recommendationNumber) && --retry > ZERO);

    if (retry == ZERO) {
      throw new RecommendationNumberException(String.format("maximum number of attempt to generate the recommendation number: %d", maxRetryNumber),
          CustomResponseCodes.REACHED_MAX_RETRY_FOR_GENERATE_RECOMMENDATION_NUMBER);
    }
    return recommendationNumber;
  }

  /**
   * Validate the dynamic fields from request, throwing exception for invalid fields.
   */
  private void validateDynamicFields(final List<TemplateCustomField> requestFields,
                                     final List<RecommendationFieldsEntity> templateFields,
                                     final RecommendationTemplatesEntity template) {
    BindingResult bindingResult = new BeanPropertyBindingResult(requestFields, "Custom error");
    var mappings = template
        .getTemplateSections().stream()
        .map(RecommendationTemplateSectionsEntity::getTemplateSectionMappings)
        .flatMap(Collection::stream)
        .collect(Collectors.toMap(mapping -> mapping.getRecommendationField().getFieldName(), mapping -> mapping));

    requestFields.forEach(requestField -> {
      var fieldName = requestField.getPropertyName();
      var fieldValue = requestField.getPropertyValue();
      if (StringUtils.isBlank(fieldName)) {
        bindingResult.addError(new FieldError(fieldName, fieldName, "The propertyName is required"));
      }
      if (StringUtils.isBlank(fieldValue)) {
        bindingResult.addError(new FieldError(fieldName, fieldName, String.format("The propertyValue for %s is required.", fieldName)));
      }

      var matchingRecommendationFieldOptional =
          templateFields.stream().filter(
              recoField -> recoField.getFieldName().equalsIgnoreCase(
                  fieldName)).findFirst();
      if (matchingRecommendationFieldOptional.isEmpty()) {
        bindingResult.addError(new FieldError(fieldName, fieldName, String.format("The %s is not a valid field name for the given template", fieldName)));
      }
      if (matchingRecommendationFieldOptional.isPresent()) {
        var matchingRecommendationField = matchingRecommendationFieldOptional.get();
        // Applies business logic validations on dynamic template fields
        validateFieldsByBusinessRules(requestField, matchingRecommendationField, bindingResult);
      }
      RecommendationTemplateSectionFieldMappingsEntity fieldInfo = mappings.get(fieldName);
      if (!Objects.isNull(fieldInfo) && !Objects.isNull(fieldInfo.getMaxLength()) && fieldValue.length() > fieldInfo.getMaxLength()) {
        var errorMessage = String.format("Field %s has maximum length of %s", fieldName, fieldInfo.getMaxLength());
        throw new InvalidInputRequestException(CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT, errorMessage);
      }

      if (!Objects.isNull(fieldInfo) && !Objects.isNull(fieldInfo.getMinLength()) && fieldValue.length() > fieldInfo.getMinLength()) {
        var errorMessage = String.format("Field %s has minimum length of %s", fieldName, fieldInfo.getMinLength());
        throw new InvalidInputRequestException(CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT, errorMessage);
      }

      /**
       *  Validates that this field has valid well formatted rich text content
       */
      validateForRichTextContent(fieldName, fieldValue);

    });
    if (bindingResult.hasErrors()) {
      throw new CustomMethodArgumentNotValidException(CustomResponseCodes.BAD_REQUEST_MISSING_REQUIRED_FIELDS, "Some dynamic fields are required %s", bindingResult);
    }

  }

  /**
   * Validates that the input field has valid rich text content.
   *
   * @param fieldName  The name of the field
   * @param fieldValue The value of the field
   */
  private void validateForRichTextContent(final String fieldName, final String fieldValue) {
    if (RecommendationRichTextFields.isARichTextField(fieldName) && !isValidRichText(fieldValue)) {
      var errorMessage = String.format("Field %s is not valid rich text", fieldName);
      throw new InvalidInputRequestException(CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT, errorMessage);
    }
  }

  /**
   * Validates if the input is valid RTF format.
   *
   * @param input The input string to validate
   * @return if the input is valid or not
   */
  private boolean isValidRichText(final String input) {
    var htmlPattern = Pattern.compile("(?!<[^>]+/>)<[^>]*>", Pattern.DOTALL);
    var results = htmlPattern.matcher(input).results().collect(
        Collectors.toList());
    var matchingOccurences = results.stream().map(result -> input.substring(
        result.start(), result.end())).collect(Collectors.toList());
    if (matchingOccurences.isEmpty()) {
      return true;
    }
    var openingAndClosingTagsLists = new ArrayList<>(
        matchingOccurences.stream()
            .collect(Collectors.partitioningBy(s -> s.startsWith("</")))
            .values());
    var subListWithOpeningTags = openingAndClosingTagsLists.get(0);
    var subListWithClosingTags = openingAndClosingTagsLists.get(1);
    return subListWithClosingTags.size() == subListWithOpeningTags.size();
  }

  /**
   * Validates and populates the request with matching option value from database.
   *
   * @param requestField                The dynamic template field in the request
   * @param matchingRecommendationField The matching field entity from the database for request field
   */
  private void validateFieldsByBusinessRules(final TemplateCustomField requestField,
                                             final RecommendationFieldsEntity matchingRecommendationField,
                                             BindingResult bindingResult) {
    var dataType = matchingRecommendationField.getPayloadType().getPayloadName();
    var fieldValue = requestField.getPropertyValue();
    var fieldName = requestField.getPropertyName();
    switch (RecommendationFieldDataTypes.getValue(dataType)) {
      case DOUBLE:
        validateTemplateFieldType(fieldValue, fieldName, DOUBLE_REGEX, bindingResult);
        break;

      case INTEGER:
      case STRING:
        validateForFieldOptions(requestField, matchingRecommendationField, fieldValue, fieldName, bindingResult);
        break;

      default:
    }
  }

  /**
   * Validates that the template request field value is a valid option if it is part of an option group.
   *
   * @param requestField                The request field
   * @param matchingRecommendationField The matching recommendation field from the database
   * @param fieldValue                  The value of the template request field
   * @param fieldName                   The name of the template request field
   */
  private void validateForFieldOptions(final TemplateCustomField requestField,
                                       final RecommendationFieldsEntity matchingRecommendationField,
                                       final String fieldValue,
                                       final String fieldName,
                                       BindingResult bindingResult) {
    if (!Objects.isNull(matchingRecommendationField.getCollection())) {
      var option = matchingRecommendationField.getCollection().getCollectionOptions()
          .stream().filter(item -> item.getOptionName().equalsIgnoreCase(fieldValue))
          .findFirst();

      option.ifPresentOrElse((optionValue) -> {
        requestField.setPropertyValue(optionValue.getOptionValue());
      }, () -> {
        bindingResult.addError(new FieldError("object", fieldName, String.format("The propertyValue for %s is not a valid option", fieldName)));
      });
    }

  }

  /**
   * Validates that the template field data type matches the REGEX for that type.
   *
   * @param fieldValue The template field value
   * @param fieldName  The template field name
   */
  private void validateTemplateFieldType(final String fieldValue, final String fieldName,
                                         final String regex, BindingResult bindingResult) {
    if (!Pattern.matches(regex, fieldValue)) {
      bindingResult.addError(new FieldError(fieldName, fieldName, String.format("The %s has an invalid currency format. The format should be #.##", fieldName)));
    }
    try {
      if (StringUtils.isNotEmpty(fieldValue)) {
        Double doubleValue = Double.valueOf(fieldValue);
        if (doubleValue <= NumberUtils.DOUBLE_ZERO) {
          bindingResult.addError(new FieldError(fieldName, fieldName, String.format("The %s must be greater than zero.", fieldName)));
        }
      }
    } catch (NumberFormatException numberFormatException) {
      bindingResult.addError(new FieldError(fieldName, fieldName, String.format("The propertyValue for %s is incorrect dataType.", fieldName)));
    }
  }

  private User createUser(RecommendationUsersEntity usersEntity) {
    return User.builder()
        .catrecid(usersEntity.getCatrecid())
        .firstName(usersEntity.getFirstName())
        .lastName(usersEntity.getLastName())
        .build();
  }

  private AssetOwnershipAtRecommendation createOwnershipAtRecommendation(RecommendationCommonDataEntity recommendationHeaderEntity) {
    return AssetOwnershipAtRecommendation.builder()
        .dealerCode(recommendationHeaderEntity.getDealerCode())
        .dealerName(recommendationHeaderEntity.getDealerName())
        .ucid(recommendationHeaderEntity.getCustomerUcid())
        .ucidName(recommendationHeaderEntity.getCustomerName())
        .build();
  }

  private AssetMetaData createAssetMetadata(RecommendationCommonDataEntity recommendationHeaderEntity) {
    return AssetMetaData.builder()
        .serialNumber(recommendationHeaderEntity.getSerialNumber())
        .make(recommendationHeaderEntity.getMake())
        .model(recommendationHeaderEntity.getModel())
        .name(recommendationHeaderEntity.getAssetName())
        .build();
  }

  private CommonFieldsResponse createGetHeader(RecommendationCommonDataEntity recommendationHeaderEntity) {
    return CommonFieldsResponse.builder()
        .expirationTime(OffsetDateTime.of(recommendationHeaderEntity.getExpirationDate().toLocalDateTime(), ZoneOffset.UTC))
        .createdTime(OffsetDateTime.of(recommendationHeaderEntity.getCreatedDate().toLocalDateTime(), ZoneOffset.UTC))
        .updatedTime(OffsetDateTime.of(recommendationHeaderEntity.getUpdatedDate().toLocalDateTime(), ZoneOffset.UTC))
        .owner(createUser(recommendationHeaderEntity.getOwnedBy()))
        .createdBy(createUser(recommendationHeaderEntity.getCreatedBy()))
        .updatedBy(createUser(recommendationHeaderEntity.getUpdatedBy()))
        //TODO unitsOfMeassure is pending until DB keep that data
        .hoursReading(HoursReading.builder().reading(BigDecimal.valueOf(recommendationHeaderEntity.getSmu())).build())
        .assetOwnershipAtRecommendation(createOwnershipAtRecommendation(recommendationHeaderEntity))
        .assetMetadata(createAssetMetadata(recommendationHeaderEntity))
        .title(recommendationHeaderEntity.getTitle())
        .site(recommendationHeaderEntity.getSite())
        .build();
  }

  private RecommendationCollectionOptionsEntity findOptionByOptionValue(final List<RecommendationCollectionOptionsEntity> options, final String fieldValue, final String fieldName, final String recommendationNumber) {
    return options.stream()
        .filter(item -> item.getOptionValue().equalsIgnoreCase(fieldValue))
        .findFirst()
        .orElseThrow(() -> new RecoServerException(
            CustomResponseCodes.INTERNAL_SERVER_ERROR,
            String.format("Recommendation field %s has an invalid value for recommendationNumber: %s", fieldName, recommendationNumber)
        ));
  }

  private String getPropertyValue(RecommendationCustomDataEntity recommendationDetailsEntity, String recommendationNumber) {
    var recommendationFieldGroup = recommendationDetailsEntity.getRecommendationField().getCollection();
    var detailValue = recommendationDetailsEntity.getValue();
    var fieldName = recommendationDetailsEntity.getRecommendationField().getFieldName();

    if (recommendationFieldGroup == null) {
      return detailValue;
    }

    return findOptionByOptionValue(recommendationFieldGroup.getCollectionOptions(), detailValue, fieldName, recommendationNumber).getOptionName();
  }

  private TemplateCustomField createRecommendationField(RecommendationCustomDataEntity recommendationDetailsEntity, String recommendationNumber) {
    return TemplateCustomField.builder()
        .propertyName(recommendationDetailsEntity.getRecommendationField().getFieldName())
        .propertyValue(getPropertyValue(recommendationDetailsEntity, recommendationNumber))
        .build();
  }

  private List<TemplateCustomField> createFields(RecommendationCommonDataEntity recommendationHeaderEntity) {
    return recommendationHeaderEntity.getRecommendationDetails().stream().map(customField -> createRecommendationField(customField, recommendationHeaderEntity.getRecommendationNumber())).collect(Collectors.toList());
  }

  private RecommendationDetailsResponse mapRecommendationHeaderEntityToRecommendationDetailsGet(RecommendationCommonDataEntity recommendationHeaderEntity) {
    var recommendationNumber = recommendationHeaderEntity.getRecommendationNumber();

    return RecommendationDetailsResponse.builder()
        .recommendationNumber(recommendationNumber)
        .assetId(recommendationHeaderEntity.getAssetName())
        .templateName(recommendationHeaderEntity.getTemplate().getTemplateName())
        .commonFields(createGetHeader(recommendationHeaderEntity))
        .templateCustomProperties(createFields(recommendationHeaderEntity))
        .links(notesDao.getLinks(recommendationNumber))
        .attachments(storageDao.retrieveFileMetadata(recommendationHeaderEntity.getRecommendationNumber(), recommendationHeaderEntity.getDealerCode()))
        .build();
  }
}
