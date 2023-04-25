package com.cat.digital.reco.service

import com.cat.digital.reco.common.Constants
import com.cat.digital.reco.common.CustomResponseCodes
import com.cat.digital.reco.common.RecommendationFieldDataTypes
import com.cat.digital.reco.dao.AssetsDao
import com.cat.digital.reco.dao.NotesDao
import com.cat.digital.reco.dao.StorageDao
import com.cat.digital.reco.dao.UserManagementDao
import com.cat.digital.reco.dao.impl.AssetsDaoImpl
import com.cat.digital.reco.domain.entities.*
import com.cat.digital.reco.domain.models.*
import com.cat.digital.reco.domain.requests.RecommendationPostRequest
import com.cat.digital.reco.domain.requests.RecommendationPutRequest
import com.cat.digital.reco.domain.requests.RecommendationSearchRequest
import com.cat.digital.reco.domain.responses.RecommendationDetailsResponse
import com.cat.digital.reco.entitlement.EntitlementUtils
import com.cat.digital.reco.exceptions.*
import com.cat.digital.reco.filter.type.ContainsFilter
import com.cat.digital.reco.filter.type.StringEqualsFilter
import com.cat.digital.reco.repositories.*
import com.cat.digital.reco.service.impl.AuditServiceImpl
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.ResourceLoader
import org.springframework.data.domain.Sort
import spock.lang.Shared
import spock.lang.Specification

import java.security.SecureRandom
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.time.ZoneOffset

import static com.cat.digital.reco.common.Constants.ASSET_ID_DELIMITER

@WebMvcTest(RecommendationService.class)
class RecommendationServiceTest extends Specification {

    @SpringBean
    RecommendationCommonDataRepository recommendationCommonDataRepository = Mock()
    @SpringBean
    RecommendationTemplateRepository templateRepository = Mock()
    @SpringBean
    RecommendationCustomDataRepository recommendationCustomDataRepository = Mock()
    @SpringBean
    RecommendationUsersRepository usersRepository = Mock()
    @SpringBean
    UserManagementDao userManagementDao = Mock()
    @SpringBean
    AssetDetailsRepository assetDetailsRepository = Mock()
    @SpringBean
    RecommendationDynamicSortHelperRepository recommendationDynamicSortHelperRepository = Mock()
    @SpringBean
    AuditServiceImpl auditService = Mock()
    //DO NOT REMOVE of unit test will fail
    @SpringBean
    SecureRandom secureRandom = Spy(SecureRandom.class)
    @SpringBean
    AssetsDao assetDao = Mock()
    @SpringBean
    NotesDao notesDao = Mock()
    @SpringBean
    StorageDao storageDao = Mock()
    @SpringBean
    EntitlementUtils entitlementUtils = Mock()
    @SpringBean
    ResourceLoader loader = Mock()

    @SpringBean
    RecommendationSortService recommendationSortService = Mock()

    @SpringBean
    RecommendationQueryService recommendationQueryService = Mock()

    @SpringBean
    PDFGenerator pdfGenerator = Mock()

    @Autowired
    RecommendationService recommendationService

    @Shared
    def expectedRecommendationDetailsGet

    @Shared
    CommonFieldsResponse expectedRecommendationHeader
    @Shared
    RecommendationCommonDataEntity recommendationHeaderEntity

    static def recommendationNumber = "REC 123-456-789"

    static def fieldId = 2
    static def fieldName = "fieldName"
    static def value = "expected value"

    static def assetId = "CAT|RXZ00353|2969412354"
    static def templateName = "Default template"
    /*
        To be added when implementing the external API calls
     */
//    static def exceptions = "https://services.cat.com/catDigital/conditionMonitoring/v1/1234"
//    static def events = "https://services.cat.com/catDigital/conditionMonitoring/v1/events"

    static def ownerCatRecid = "ownerCatRecid"
    static def ownerFirstName = "ownerFirstName"
    static def ownerLastName = "ownerLastName"
    static def ownerCWSId = "ownerCWSId"

    static def createdByCatRecid = "createdByCatRecid"
    static def createdByFirstName = "createdByFirstName"
    static def createdByLastName = "createdByLastName"
    static def createdByCWSId = "createdByCWSId"

    static def updatedByCatRecid = "updatedByCatRecid"
    static def updatedByFirstName = "updatedByFirstName"
    static def updatedByLastName = "updatedByLastName"
    static def updatedByCWSId = "updatedByCWSId"
    static def smu = 100
    static def site = "site"
    static def model = "model"
    static def dealerCode = "dealerCode"
    static def dealerName = "dealerName"
    static def customerUcid = "customerUcid"
    static def customerName = "customerName"
    static def ownerCatRecId = 'AA-12434'
    static def title = 'Recommendation Title'
    static def recommendationId = 123
    static def putHeader = new PutCommonFields()
    static def owner = new BaseOwner()
    static def assetName = 'CAT|RJG04184|2969484566'
    static def assetDetails = new AssetDetails()
    static def make = 'CAT'
    static def serialNumber = 'RJG04184'
    static def primaryCustomerNumber = '2969484566'
    static def attachment = new Attachment()

    @Shared
    def recommendationFieldEntity
    @Shared
    def recommendationTemplateEntity
    @Shared
    def authorization = new Authorization()
    @Shared
    def template
    @Shared
    def links
    @Shared
    def recommendationUsersEntityOptional

    /*
    Set up
     */

    def setup() {
        authorization.setCatRecId("auth cat rec id")
        def recommendationField = new RecommendationFieldsEntity(
                fieldId: 1,
                fieldName: "recommendationPriority",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"))
        def recommendationField2 = new RecommendationFieldsEntity(
                fieldId: 2,
                fieldName: "recommendationStatus",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"))
        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField.getFieldId(),
                recommendationField: recommendationField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recoTemplateSectionFieldMapping2 = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField2.getFieldId(),
                recommendationField: recommendationField2,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping,
                        recoTemplateSectionFieldMapping2)
        )
        template = new RecommendationTemplatesEntity(
                templateName: templateName,
                templateId: 1,
                templateSections: Set.of(recommendationTemplateSection)
        )
    }

    def setupSpec() {
        recommendationFieldEntity = RecommendationFieldsEntity.builder().fieldId(fieldId).fieldName(fieldName).build()
        recommendationUsersEntityOptional = Optional.of(new RecommendationUsersEntity())

        def recommendationStatusOptions = RecommendationCollectionOptionsEntity.builder().optionValue("3").optionName("Draft").build()
        def recommendationGenericOptions = RecommendationCollectionOptionsEntity.builder().optionValue("1").optionName("Option 1").build()

        def statusGroupEntity = RecommendationFieldCollectionsEntity.builder().collectionOptions(List.of(recommendationStatusOptions)).build()
        def genericGroupEntity = RecommendationFieldCollectionsEntity.builder().collectionOptions(List.of(recommendationGenericOptions)).build()

        def recommendationStatusFieldEntity = setupRecommendationFieldEntityWithOptions(statusGroupEntity, "recommendationStatus", 3)
        def recommendationPriorityFieldEntity = setupRecommendationFieldEntityWithOptions(genericGroupEntity, "recommendationGenericGroup", 4)

        def recommendationDetailsEntity = setupRecommendationDetailsEntity(recommendationFieldEntity, null, value)
        def recommendationDetailsEntityStatus = setupRecommendationDetailsEntity(recommendationStatusFieldEntity, null, "3")
        def recommendationDetailsEntityPriority = setupRecommendationDetailsEntity(recommendationPriorityFieldEntity, null, "1")

        recommendationTemplateEntity = RecommendationTemplatesEntity.builder().templateName(templateName).build()

        def ownedByUserEntity = createUserEntity(ownerCatRecid, ownerFirstName, ownerLastName, ownerCWSId)
        def createdByUserEntity = createUserEntity(createdByCatRecid, createdByFirstName, createdByLastName, createdByCWSId)
        def updateByUserEntity = createUserEntity(updatedByCatRecid, updatedByFirstName, updatedByLastName, updatedByCWSId)

        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)

        recommendationHeaderEntity = setupRecommendationHeaderEntity(
                Timestamp.valueOf(expirationDateOffset.toLocalDateTime()),
                Timestamp.valueOf(createdDateOffset.toLocalDateTime()),
                Timestamp.valueOf(updatedDateOffset.toLocalDateTime()),
                [recommendationDetailsEntity, recommendationDetailsEntityStatus, recommendationDetailsEntityPriority],
                recommendationTemplateEntity,
                ownedByUserEntity,
                createdByUserEntity,
                updateByUserEntity,
        )

        def ownerUser = createUser(ownerCatRecid, ownerFirstName, ownerLastName)
        def createdByUser = createUser(createdByCatRecid, createdByFirstName, createdByLastName)
        def updateByUser = createUser(updatedByCatRecid, updatedByFirstName, updatedByLastName)
        def hoursReading = HoursReading.builder().reading(BigDecimal.valueOf(100)).build()

        def assetOwnershipAtRecommendation = AssetOwnershipAtRecommendation.builder()
                .dealerCode(dealerCode)
                .dealerName(dealerName)
                .ucid(customerUcid)
                .ucidName(customerName)
                .build()

        def assetMetadata = AssetMetaData.builder()
                .serialNumber(serialNumber)
                .make(make)
                .model(model)
                .name(assetId)
                .build()

        def header = setUpRecommendationGetHeader(
                expirationDateOffset,
                createdDateOffset,
                updatedDateOffset,
                ownerUser,
                createdByUser,
                updateByUser,
                hoursReading,
                assetOwnershipAtRecommendation,
                assetMetadata,
                site
        )

        def field = TemplateCustomField.builder().propertyName(fieldName).propertyValue(value).build()
        def recommendationStatusField = TemplateCustomField.builder().propertyName("recommendationStatus").propertyValue("Draft").build()
        def recommendationGenericField = TemplateCustomField.builder().propertyName("recommendationGenericGroup").propertyValue("Option 1").build()

        def fields = [field, recommendationStatusField, recommendationGenericField]

        attachment.name = "File Name"
        attachment.fileId = "File Id"
        attachment.fileAttachedTime = OffsetDateTime.now()

        def linkAttachedTime = OffsetDateTime.parse("2016-01-31T22:59:59.999Z")
        def link = RecommendationLink.builder().attachedTime(linkAttachedTime).url("https://example.com").build()
        links = [link]
        def attachments = [attachment]
        expectedRecommendationHeader = header

        expectedRecommendationDetailsGet = setupRecommendationDetailsGet(header, fields, attachments, links)

        def hoursReadingAtRecommendation = new HoursReading()
        hoursReadingAtRecommendation.reading = 2000
        hoursReadingAtRecommendation.unitOfMeasure = "hours"
        owner.catrecid = ownerCatRecId
        putHeader.owner = owner
        putHeader.title = title
        putHeader.expirationTime = OffsetDateTime.now()
        putHeader.site = site
        putHeader.hoursReading = hoursReadingAtRecommendation

        assetDetails.enabled = true
    }

    def "create recommendation success"() {
        given:
        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                        expirationTime: OffsetDateTime.now(),
                        site: "site one",
                        hoursReading: new HoursReading(
                                reading: 1000,
                                unitOfMeasure: "hours"),
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        when:
        def result = recommendationService.createRecommendation(recData, authorization)

        then:
        assert result != null
        assert result.templateName == templateName
        assert result.templateCustomProperties == dynamicFields
        assert result.assetId == assetId
        assert result.attachments == [attachment]
        assert result.links == links
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        1 * recommendationCustomDataRepository.saveAll(_ as List<?>)
        0 * userManagementDao.getUserData(_ as String)
        1 * storageDao.retrieveFileMetadata(_ as String, _ as String) >> [attachment]
        1 * notesDao.getLinks(_ as String) >> links
    }

    def "create recommendation with invalid asset id structure"() {
        given:
        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))
        def recData = new RecommendationPostRequest(
                assetId: "assetId",
                templateName: "Template 1 - Default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                        expirationTime: OffsetDateTime.now(),
                        site: "site one",
                        hoursReading: new HoursReading(
                                reading: 1000,
                                unitOfMeasure: "hours")
                ,
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        thrown InvalidInputRequestException
        1 * templateRepository.findByTemplateName("Template 1 - Default") >> Optional.of(template)
        0 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        0 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        0 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        0 * recommendationCustomDataRepository.saveAll(_ as List<?>)
    }

    def "create recommendation with asset disabled"() {
        given:
        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: false)

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "Template 1 - Default",
                        title: "some title",
                        owner: new BaseOwner(catrecid: "cat rec id"),
                        expirationTime: OffsetDateTime.now(),
                        site: "site one",
                        hoursReading: new HoursReading(
                                reading: 1000,
                                unitOfMeasure: "hours")
                ,
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        thrown InvalidInputRequestException
        1 * templateRepository.findByTemplateName("Template 1 - Default") >> Optional.of(template)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        0 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        0 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        0 * recommendationCustomDataRepository.saveAll(_ as List<?>)
    }

    def "create recommendation with template not found"() {
        given:
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                hoursReading: new HoursReading(
                    reading: 1000,
                    unitOfMeasure: "hours")
                ,
                templateCustomProperties: new ArrayList<TemplateCustomField>())

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        thrown EntityNotFoundException

        1 * templateRepository.findByTemplateName(_ as String) >> Optional.empty()
        0 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String)
    }

    def "create recommendation without title field"() {
        given:
        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                owner: new BaseOwner(catrecid: "cat rec id"),
                hoursReading: new HoursReading(
                    reading: 1000,
                    unitOfMeasure: "hours")
                ,
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)
        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)


        when:
        def result = recommendationService.createRecommendation(recData, authorization)

        then:
        assert result != null

        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        1 * recommendationCustomDataRepository.saveAll(_ as List<?>)
        0 * userManagementDao.getUserData(recData.getOwner().getCatrecid())
    }

    def "create recommendation with expiration date"() {
        given:
        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))

        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours")
                ,
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)


        when:
        def result = recommendationService.createRecommendation(recData, authorization)

        then:
        assert result != null
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        1 * recommendationCustomDataRepository.saveAll(_ as List<?>)
        0 * userManagementDao.getUserData(recData.getOwner().getCatrecid())
    }

    def "create recommendation without hoursReading object"() {
        given:
        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)


        when:
        def result = recommendationService.createRecommendation(recData, authorization)

        then:
        assert result != null
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        1 * recommendationCustomDataRepository.saveAll(_ as List<?>)
        0 * userManagementDao.getUserData(_ as String)
    }

        def "create recommendation without reading field"() {
            given:
            def assetData = new AssetDetails(
                    serialNumber: serialNumber,
                    make: "asset make",
                    primaryCustomerNumber: "",
                    primaryCustomerName: "",
                    dealerCode: "",
                    dealerName: "",
                    site: "asset site",
                    smu: 0,
                    model: "model",
                    enabled: true)

            def dynamicFields = new ArrayList<TemplateCustomField>()
            dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))
            def recData = new RecommendationPostRequest(
                    assetId: assetId,
                    templateName: "default",
                    title: "some title",
                    owner: new BaseOwner(catrecid: "cat rec id"),
                    expirationTime: OffsetDateTime.now(),
                    site: "site one",
                    hoursReading: new HoursReading(),
                    templateCustomProperties: dynamicFields)

            def assetSplit = assetId.split(ASSET_ID_DELIMITER)

            when:
            def result = recommendationService.createRecommendation(recData, authorization)

            then:
            assert result != null
            1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
            1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
            1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
            1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
            1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
            1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
            1 * recommendationCustomDataRepository.saveAll(_ as List<?>)
            0 * userManagementDao.getUserData(_ as String)
        }


    def "create recommendation with site"() {
        given:
        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))

        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours")
                ,
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)


        when:
        def result = recommendationService.createRecommendation(recData, authorization)

        then:
        assert result != null
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        1 * recommendationCustomDataRepository.saveAll(_ as List<?>)
        0 * userManagementDao.getUserData(recData.getOwner().getCatrecid())
    }

    def "create recommendation with invalid dynamic fields"() {
        given:
        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))

        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours")
                ,
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        dynamicFields.size() == 2
        1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        1 * recommendationCustomDataRepository.saveAll(_ as List<?>)
        0 * userManagementDao.getUserData(recData.getOwner().getCatrecid())
    }

    def "create recommendation without user on DB and exist on user management service"() {
        given:
        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours"),
                templateCustomProperties: dynamicFields)


        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)
        def ownerData = new UserManagementData(firstName: "owner first name", lastName: "owner last name")

        when:
        def result = recommendationService.createRecommendation(recData, authorization)

        then:
        assert result != null
        assert result.templateName == templateName
        assert result.templateCustomProperties == dynamicFields
        assert result.assetId == assetId
        assert result.attachments == [attachment]
        assert result.links == links
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(authorization.getCatRecId()) >> Optional.empty()
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        1 * userManagementDao.getUserData(authorization.getCatRecId()) >> ownerData
        1 * usersRepository.saveAndFlush(_ as RecommendationUsersEntity) >> recommendationUsersEntityOptional.get()
        1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        1 * recommendationCustomDataRepository.saveAll(_ as List<?>)
        0 * userManagementDao.getUserData(recData.getOwner().getCatrecid())
        1 * storageDao.retrieveFileMetadata(_ as String, _ as String) >> [attachment]
        1 * notesDao.getLinks(_ as String) >> links
    }

    def "create recommendation without user on DB and user management service throw IOException"() {
        given:
        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "At Next Service"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours"),
                templateCustomProperties: dynamicFields)

        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        thrown InvalidInputRequestException
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> Optional.empty()
        1 * userManagementDao.getUserData(recData.getOwner().getCatrecid()) >> { throw new IOException() }
        0 * usersRepository.existsById(authorization.getCatRecId()) >> true
    }

    def "retry generate recommendation number ten times"() {

        given:
        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours"),
                templateCustomProperties: dynamicFields)

        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        thrown RecommendationNumberException
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        0 * usersRepository.findById(recData.getOwner().getCatrecid()) >> new RecommendationUsersEntity()
        0 * usersRepository.findById(authorization.getCatRecId()) >> new RecommendationUsersEntity()
        10 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> true
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
    }

    def "create recommendation without required dynamic fields"() {
        given:
        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationAction", propertyValue: "new"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "Template 1 - Default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours")
                ,
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        thrown CustomMethodArgumentNotValidException
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        0 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        0 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        0 * recommendationCustomDataRepository.saveAll(_ as List<?>)
    }

    def "create recommendation throw UnsupportedDataTypeException"() {
        given:
        def recommendationField = new RecommendationFieldsEntity(
                fieldId: 1,
                fieldName: "recommendationPriority",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "Option"))
        def recommendationField2 = new RecommendationFieldsEntity(
                fieldId: 2,
                fieldName: "recommendationStatus",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"))
        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField.getFieldId(),
                recommendationField: recommendationField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recoTemplateSectionFieldMapping2 = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField2.getFieldId(),
                recommendationField: recommendationField2,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping,
                        recoTemplateSectionFieldMapping2)
        )
        def template = new RecommendationTemplatesEntity(
                templateName: "default",
                templateId: 1,
                templateSections: Arrays.asList(recommendationTemplateSection))
        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "new"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours")
                ,
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split("\\|")
        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        thrown UnsupportedDataTypeException
        1 * templateRepository.findByTemplateName(_ as String) >> Optional.of(template)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        0 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        0 * usersRepository.findById(recData.getOwner().getCatrecid()) >> new RecommendationUsersEntity()
        0 * usersRepository.findById(authorization.getCatRecId()) >> new RecommendationUsersEntity()
        0 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        0 * recommendationCustomDataRepository.saveAll(_ as List<?>)
    }

    def "create recommendation with dynamic fields have options"() {
        given:
        def collectionOptions = new ArrayList<RecommendationCollectionOptionsEntity>()
        collectionOptions.add(new RecommendationCollectionOptionsEntity(
                optionName: "Draft",
                optionValue: "0"
        ))
        def recommendationField = new RecommendationFieldsEntity(
                fieldId: 1,
                fieldName: "recommendationPriority",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"),
                collection: new RecommendationFieldCollectionsEntity(
                        collectionOptions: collectionOptions
                ))
        def recommendationField2 = new RecommendationFieldsEntity(
                fieldName: "recommendationStatus",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"))
        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField.getFieldId(),
                recommendationField: recommendationField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recoTemplateSectionFieldMapping2 = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField2.getFieldId(),
                recommendationField: recommendationField2,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping,
                        recoTemplateSectionFieldMapping2)
        )
        def template = new RecommendationTemplatesEntity(
                templateName: "default",
                templateId: 1,
                templateSections: Arrays.asList(recommendationTemplateSection))
        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "Draft"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours"),
                templateCustomProperties: dynamicFields)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)


        when:
        def result = recommendationService.createRecommendation(recData, authorization)

        then:
        assert result != null
        assert dynamicFields.get(0).propertyValue == "0"
        1 * templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        1 * usersRepository.findById(recData.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        1 * recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)
        1 * recommendationCustomDataRepository.saveAll(_ as List<?>)
        0 * userManagementDao.getUserData(_ as String)
    }

    def "create recommendation fails when max length of string is exceeded in dynamic fields"() {
        given:
        def collectionOptions = new ArrayList<RecommendationCollectionOptionsEntity>()
        collectionOptions.add(new RecommendationCollectionOptionsEntity(
                optionName: "Draft",
                optionValue: "12"
        ))
        def recommendationField = new RecommendationFieldsEntity(
                fieldId: 1,
                fieldName: "recommendationPriority",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"),
                collection: new RecommendationFieldCollectionsEntity(
                        collectionOptions: collectionOptions
                ))
        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField.getFieldId(),
                recommendationField: recommendationField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
                maxLength: 1,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping)
        )
        def template = new RecommendationTemplatesEntity(
                templateName: "default",
                templateId: 1,
                templateSections: Arrays.asList(recommendationTemplateSection))

        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "Draft"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours"),
                templateCustomProperties: dynamicFields)

        templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        usersRepository.existsById(recData.getOwner().getCatrecid()) >> true
        usersRepository.existsById(authorization.getCatRecId()) >> true
        recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT
    }

    def "create recommendation fails when min length of string is not met in dynamic fields"() {
        given:
        def collectionOptions = new ArrayList<RecommendationCollectionOptionsEntity>()
        collectionOptions.add(new RecommendationCollectionOptionsEntity(
                optionName: "Draft",
                optionValue: "12"
        ))
        def recommendationField = new RecommendationFieldsEntity(
                fieldId: 1,
                fieldName: "recommendationPriority",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"),
                collection: new RecommendationFieldCollectionsEntity(
                        collectionOptions: collectionOptions
                ))
        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField.getFieldId(),
                recommendationField: recommendationField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
                minLength: 3,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping)
        )
        def template = new RecommendationTemplatesEntity(
                templateName: "default",
                templateId: 1,
                templateSections: Arrays.asList(recommendationTemplateSection))

        def assetData = new AssetDetails(
                serialNumber: serialNumber,
                make: "asset make",
                primaryCustomerNumber: "",
                primaryCustomerName: "",
                dealerCode: "",
                dealerName: "",
                site: "asset site",
                smu: 0,
                model: "model",
                enabled: true)

        def assetSplit = assetId.split(ASSET_ID_DELIMITER)

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "Draft"))
        def recData = new RecommendationPostRequest(
                assetId: assetId,
                templateName: "default",
                title: "some title",
                owner: new BaseOwner(catrecid: "cat rec id"),
                expirationTime: OffsetDateTime.now(),
                site: "site one",
                hoursReading: new HoursReading(
                        reading: 1000,
                        unitOfMeasure: "hours"),
                templateCustomProperties: dynamicFields)

        templateRepository.findByTemplateName(recData.getTemplateName()) >> Optional.of(template)
        usersRepository.existsById(recData.getOwner().getCatrecid()) >> true
        usersRepository.existsById(authorization.getCatRecId()) >> true
        recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> false
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(assetSplit[0], assetSplit[1], assetSplit[2]) >> Optional.of(assetData)

        when:
        recommendationService.createRecommendation(recData, authorization)

        then:
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT
    }

    def "Update recommendation fails when max length of string is exceeded in dynamic fields"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        def requestField = new TemplateCustomField()
        requestField.propertyName = "recommendationStatus"
        requestField.propertyValue = 'Expired'
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationPriority"
        secondRequestField.propertyValue = 'Expired'
        def fields = List.of(requestField, secondRequestField)
        def recommendationPutRequest = new RecommendationPutRequest()

        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)

        def collectionOptions = new ArrayList<RecommendationCollectionOptionsEntity>()
        collectionOptions.add(new RecommendationCollectionOptionsEntity(
                optionName: "Draft",
                optionValue: "12"
        ))
        def recommendationField = new RecommendationFieldsEntity(
                fieldId: 1,
                fieldName: "recommendationPriority",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"),
                collection: new RecommendationFieldCollectionsEntity(
                        collectionOptions: collectionOptions
                ))
        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField.getFieldId(),
                recommendationField: recommendationField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
                maxLength: 1,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping)
        )
        def template = new RecommendationTemplatesEntity(
                templateName: "default",
                templateId: 1,
                templateSections: Arrays.asList(recommendationTemplateSection))


        recommendationHeaderEntity.template = template

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "Draft"))

        def userManagementData = new UserManagementData(firstName: "owner first name", lastName: "owner last name")

        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        usersRepository.findById(_ as String) >> recommendationUsersEntityOptional
        recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber) >> Optional.of(recommendationHeaderEntity)
        usersRepository.existsById(ownerCatRecId) >> false
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        userManagementDao.getUserData(ownerCatRecId) >> userManagementData

        when:
        recommendationService.updateRecommendation(recommendationPutRequest, recommendationNumber, authorization)

        then:
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT
    }

    def "Update recommendation fails when min length of string is not met in dynamic fields"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        def requestField = new TemplateCustomField()
        requestField.propertyName = "recommendationStatus"
        requestField.propertyValue = 'Expired'
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationPriority"
        secondRequestField.propertyValue = 'Expired'
        def fields = List.of(requestField, secondRequestField)
        def recommendationPutRequest = new RecommendationPutRequest()

        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)

        def collectionOptions = new ArrayList<RecommendationCollectionOptionsEntity>()
        collectionOptions.add(new RecommendationCollectionOptionsEntity(
                optionName: "Draft",
                optionValue: "12"
        ))
        def recommendationField = new RecommendationFieldsEntity(
                fieldId: 1,
                fieldName: "recommendationPriority",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"),
                collection: new RecommendationFieldCollectionsEntity(
                        collectionOptions: collectionOptions
                ))
        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField.getFieldId(),
                recommendationField: recommendationField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
                minLength: 3,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping)
        )
        def template = new RecommendationTemplatesEntity(
                templateName: "default",
                templateId: 1,
                templateSections: Arrays.asList(recommendationTemplateSection))


        recommendationHeaderEntity.template = template

        def dynamicFields = new ArrayList<TemplateCustomField>()
        dynamicFields.add(new TemplateCustomField(propertyName: "recommendationPriority", propertyValue: "Draft"))

        def userManagementData = new UserManagementData(firstName: "owner first name", lastName: "owner last name")

        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        usersRepository.findById(_ as String) >> recommendationUsersEntityOptional
        recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber) >> Optional.of(recommendationHeaderEntity)
        usersRepository.existsById(ownerCatRecId) >> false
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        userManagementDao.getUserData(ownerCatRecId) >> userManagementData

        when:
        recommendationService.updateRecommendation(recommendationPutRequest, recommendationNumber, authorization)

        then:
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT
    }

    def "when PUT is called from service layer mock owner already exists in database"() {
        given: "PUT recommendation called from service layer"

        def ownerCatRecId = 'AA-12434'
        def title = 'Recommendation Title'
        def site = 'Site'
        def owner = new BaseOwner()
        def hoursReadingAtRecommendation = new HoursReading()
        hoursReadingAtRecommendation.reading = 2000
        hoursReadingAtRecommendation.unitOfMeasure = "hours"
        owner.catrecid = ownerCatRecId
        putHeader.owner = owner
        putHeader.title = title
        putHeader.expirationTime = OffsetDateTime.now()
        putHeader.site = site
        putHeader.hoursReading = hoursReadingAtRecommendation
    }

    def "Update recommendation called from service layer and mock owner already exists in database"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        recommendationHeaderEntity.template = getValidTemplateWithThreeFields();
        def requestField = new TemplateCustomField()
        requestField.propertyName = "recommendationStatus"
        requestField.propertyValue = 'Expired'
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationPriority"
        secondRequestField.propertyValue = 'Expired'
        def thirdRequestField = new TemplateCustomField()
        thirdRequestField.propertyName = "recommendationAction"
        thirdRequestField.propertyValue = "<p><img src=\"data:image/png;base64,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\" alt=\"\" width=\"1168\" height=\"183\" /><span/></p>"
        def fields = List.of(requestField, secondRequestField, thirdRequestField)
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        recommendationHeaderEntity.expirationDate = Timestamp.valueOf(expirationDateOffset.toLocalDateTime());
        recommendationHeaderEntity.createdDate = Timestamp.valueOf(createdDateOffset.toLocalDateTime())
        recommendationHeaderEntity.updatedDate = Timestamp.valueOf(updatedDateOffset.toLocalDateTime())
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        def user = new RecommendationUsersEntity(firstName: 'John', lastName: 'Embury',
         catrecid: 'QBE-1123', cwsId: 'cws123')
        recommendationHeaderEntity.createdBy = user
        recommendationHeaderEntity.updatedBy = user
        recommendationHeaderEntity.ownedBy = user
        recommendationHeaderEntity.recommendationNumber = 'REC-100-100'

        def recommendationPutRequest = new RecommendationPutRequest()
        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        def result = recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber) >> Optional.of(recommendationHeaderEntity)
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(ownerCatRecId) >> recommendationUsersEntityOptional
        1 * recommendationCommonDataRepository.saveAndFlush(recommendationHeaderEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        1 * storageDao.retrieveFileMetadata(_ as String, null) >> [attachment]
        1 * notesDao.getLinks(_ as String) >> links
        assert result != null
        assert result.templateName == templateName
        assert result.templateCustomProperties == fields
        assert result.assetId == assetName
        assert result.attachments == [attachment]
        assert result.links == links
    }

    def "Update recommendation called with recommendation action having invalid recommendation action rich text throws exception"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        def requestField = new TemplateCustomField()
        requestField.propertyName = "recommendationStatus"
        requestField.propertyValue = 'Expired'
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationPriority"
        secondRequestField.propertyValue = 'Expired'
        def thirdRequestField = new TemplateCustomField()
        thirdRequestField.propertyName = "recommendationAction"
        thirdRequestField.propertyValue = '<p><strong>This is sample text to be tested.</example></p></strong>\\n<p><em><span style=\"text-decoration: underline;\"><strong>This is what recommendation api works.</strong></span></em></p>'
        def fields = List.of(requestField, secondRequestField, thirdRequestField)
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        recommendationHeaderEntity.expirationDate = Timestamp.valueOf(expirationDateOffset.toLocalDateTime());
        recommendationHeaderEntity.createdDate = Timestamp.valueOf(createdDateOffset.toLocalDateTime())
        recommendationHeaderEntity.updatedDate = Timestamp.valueOf(updatedDateOffset.toLocalDateTime())
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        def user = new RecommendationUsersEntity(firstName: 'John', lastName: 'Embury',
                catrecid: 'QBE-1123', cwsId: 'cws123')
        recommendationHeaderEntity.createdBy = user
        recommendationHeaderEntity.updatedBy = user
        recommendationHeaderEntity.ownedBy = user
        recommendationHeaderEntity.recommendationNumber = 'REC-100-100'

        def recommendationPutRequest = new RecommendationPutRequest()
        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber) >> Optional.of(recommendationHeaderEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT
    }

    def "Update recommendation called with recommendation action having just plain text is valid input"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        recommendationHeaderEntity.template = getValidTemplateWithThreeFields();
        def requestField = new TemplateCustomField()
        requestField.propertyName = "recommendationStatus"
        requestField.propertyValue = 'Expired'
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationPriority"
        secondRequestField.propertyValue = 'Expired'
        def thirdRequestField = new TemplateCustomField()
        thirdRequestField.propertyName = "recommendationAction"
        thirdRequestField.propertyValue = 'This is sample text to be tested. This is what recommendation api works.'
        def fields = List.of(requestField, secondRequestField, thirdRequestField)
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        recommendationHeaderEntity.expirationDate = Timestamp.valueOf(expirationDateOffset.toLocalDateTime());
        recommendationHeaderEntity.createdDate = Timestamp.valueOf(createdDateOffset.toLocalDateTime())
        recommendationHeaderEntity.updatedDate = Timestamp.valueOf(updatedDateOffset.toLocalDateTime())
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        def user = new RecommendationUsersEntity(firstName: 'John', lastName: 'Embury',
                catrecid: 'QBE-1123', cwsId: 'cws123')
        recommendationHeaderEntity.createdBy = user
        recommendationHeaderEntity.updatedBy = user
        recommendationHeaderEntity.ownedBy = user
        recommendationHeaderEntity.recommendationNumber = 'REC-100-100'

        def recommendationPutRequest = new RecommendationPutRequest()
        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        def result = recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber) >> Optional.of(recommendationHeaderEntity)
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(ownerCatRecId) >> recommendationUsersEntityOptional
        1 * recommendationCommonDataRepository.saveAndFlush(recommendationHeaderEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        1 * storageDao.retrieveFileMetadata(_ as String, null) >> [attachment]
        1 * notesDao.getLinks(_ as String) >> links
        assert result != null
        assert result.templateName == templateName
        assert result.templateCustomProperties == fields
        assert result.assetId == assetName
        assert result.attachments == [attachment]
        assert result.links == links
    }

    def "Update recommendation called with recommendation action having invalid recommendation details rich text throws exception"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        def requestField = new TemplateCustomField()
        requestField.propertyName = "recommendationStatus"
        requestField.propertyValue = 'Expired'
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationPriority"
        secondRequestField.propertyValue = 'Expired'
        def thirdRequestField = new TemplateCustomField()
        thirdRequestField.propertyName = "recommendationDetails"
        thirdRequestField.propertyValue = '<p>This is sample text to be tested</p></strong>\\n<p><em><span style=\"text-decoration: underline;\"><strong>This is what recommendation api works.</strong></span></em></p>'
        def fields = List.of(requestField, secondRequestField, thirdRequestField)
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        recommendationHeaderEntity.expirationDate = Timestamp.valueOf(expirationDateOffset.toLocalDateTime());
        recommendationHeaderEntity.createdDate = Timestamp.valueOf(createdDateOffset.toLocalDateTime())
        recommendationHeaderEntity.updatedDate = Timestamp.valueOf(updatedDateOffset.toLocalDateTime())
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        def user = new RecommendationUsersEntity(firstName: 'John', lastName: 'Embury',
                catrecid: 'QBE-1123', cwsId: 'cws123')
        recommendationHeaderEntity.createdBy = user
        recommendationHeaderEntity.updatedBy = user
        recommendationHeaderEntity.ownedBy = user
        recommendationHeaderEntity.recommendationNumber = 'REC-100-100'

        def recommendationPutRequest = new RecommendationPutRequest()
        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber) >> Optional.of(recommendationHeaderEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT
    }

    def "Update recommendation called with recommendation action having invalid value estimate description rich text throws exception"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        def requestField = new TemplateCustomField()
        requestField.propertyName = "recommendationStatus"
        requestField.propertyValue = 'Expired'
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationPriority"
        secondRequestField.propertyValue = 'Expired'
        def thirdRequestField = new TemplateCustomField()
        thirdRequestField.propertyName = "valueEstimateDescription"
        thirdRequestField.propertyValue = '<p>This is sample text to be tested.</p></strong>\\n<p><em><span style=\\"text-decoration: underline;\\"><strong>This is what recommendation api works.</strong></span></em></p>'
        def fields = List.of(requestField, secondRequestField, thirdRequestField)
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        recommendationHeaderEntity.expirationDate = Timestamp.valueOf(expirationDateOffset.toLocalDateTime());
        recommendationHeaderEntity.createdDate = Timestamp.valueOf(createdDateOffset.toLocalDateTime())
        recommendationHeaderEntity.updatedDate = Timestamp.valueOf(updatedDateOffset.toLocalDateTime())
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        def user = new RecommendationUsersEntity(firstName: 'John', lastName: 'Embury',
                catrecid: 'QBE-1123', cwsId: 'cws123')
        recommendationHeaderEntity.createdBy = user
        recommendationHeaderEntity.updatedBy = user
        recommendationHeaderEntity.ownedBy = user
        recommendationHeaderEntity.recommendationNumber = 'REC-100-100'

        def recommendationPutRequest = new RecommendationPutRequest()
        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber) >> Optional.of(recommendationHeaderEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT
    }

    def "Update recommendation is called from service layer mock owner does not exist in database"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        def requestField = new TemplateCustomField()
        requestField.propertyName = "recommendationStatus"
        requestField.propertyValue = 'Expired'
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationPriority"
        secondRequestField.propertyValue = 'Expired'
        def fields = List.of(requestField, secondRequestField)
        def recommendationPutRequest = new RecommendationPutRequest()

        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)

        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        recommendationHeaderEntity.expirationDate = Timestamp.valueOf(expirationDateOffset.toLocalDateTime());
        recommendationHeaderEntity.createdDate = Timestamp.valueOf(createdDateOffset.toLocalDateTime())
        recommendationHeaderEntity.updatedDate = Timestamp.valueOf(updatedDateOffset.toLocalDateTime())
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        def user = new RecommendationUsersEntity(firstName: 'John', lastName: 'Embury',
                catrecid: 'QBE-1123', cwsId: 'cws123')
        recommendationHeaderEntity.createdBy = user
        recommendationHeaderEntity.updatedBy = user
        recommendationHeaderEntity.ownedBy = user
        recommendationHeaderEntity.recommendationNumber = 'REC-100-100'

        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        usersRepository.findById(_ as String) >> Optional.empty()
        def userManagementData = new UserManagementData(firstName: "owner first name", lastName: "owner last name")

        when:
        def result = recommendationService.updateRecommendation(
                recommendationPutRequest, recommendationNumber, authorization)

        then:
        1 * recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber) >> Optional.of(recommendationHeaderEntity)
        1 * usersRepository.findById(recommendationPutRequest.getOwner().getCatrecid()) >> recommendationUsersEntityOptional
        1 * usersRepository.findById(authorization.getCatRecId()) >> Optional.empty()
        1 * recommendationCommonDataRepository.saveAndFlush(_ as RecommendationCommonDataEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        1 * userManagementDao.getUserData(authorization.getCatRecId()) >> userManagementData
        1 * storageDao.retrieveFileMetadata(_ as String, null) >> [attachment]
        1 * notesDao.getLinks(_ as String) >> links
        assert result != null
        assert result.templateName == templateName
        assert result.templateCustomProperties == fields
        assert result.assetId == assetName
        assert result.attachments == [attachment]
        assert result.links == links
    }

    def "Update recommendation is called from service layer with missing property value in request template fields"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()

        def requestField = new TemplateCustomField()
        requestField.propertyName = "recommendationPriority"
        requestField.propertyValue = '3 - At Next Service'
        def fields = List.of(requestField)

        def recommendationPutRequest = new RecommendationPutRequest()

        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)

        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        0 * usersRepository.existsById(recommendationPutRequest.getOwner().getCatrecid()) >> true
        def e = thrown(CustomMethodArgumentNotValidException)
        e.code == CustomResponseCodes.BAD_REQUEST_INVALID_BODY_INPUT

    }

    def "Update recommendation is called from service layer with missing property name in request template fields"() {
        given: "PUT recommendation called from service layer"

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()

        def firstRequestField = new TemplateCustomField()
        firstRequestField.propertyName = ""
        firstRequestField.propertyValue = "Some Value"
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationStatus"
        secondRequestField.propertyValue = 'Expired'
        def thirdRequestField = new TemplateCustomField()
        thirdRequestField.propertyName = "recommendationPriority"
        thirdRequestField.propertyValue = '3 - At Next Service'
        def fields = List.of(firstRequestField, secondRequestField, thirdRequestField)

        def recommendationPutRequest = new RecommendationPutRequest()

        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)

        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        def e = thrown(CustomMethodArgumentNotValidException)
        e.code == CustomResponseCodes.BAD_REQUEST_MISSING_REQUIRED_FIELDS

    }

    def "Update recommendation is called from service layer with request fields in not part of the template"() {
        given: "PUT recommendation called from service layer"

        def invalidRequestFieldName = 'foo'
        def templateField = new RecommendationFieldsEntity()
        templateField.fieldName = 'recommendationAction'
        def recommendationHeaderEntity = setUpTestRecommendationCommonData()

        def requestField = new TemplateCustomField()
        requestField.setPropertyName(invalidRequestFieldName)
        requestField.setPropertyValue(invalidRequestFieldName)
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationStatus"
        secondRequestField.propertyValue = 'Expired'
        def thirdRequestField = new TemplateCustomField()
        thirdRequestField.propertyName = "recommendationPriority"
        thirdRequestField.propertyValue = '3 - At Next Service'
        def fields = Arrays.asList(requestField, secondRequestField, thirdRequestField)
        def recommendationPutRequest = new RecommendationPutRequest()

        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)

        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(
                recommendationPutRequest, recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        def e = thrown(CustomMethodArgumentNotValidException)
        e.code == CustomResponseCodes.BAD_REQUEST_MISSING_REQUIRED_FIELDS

    }

    def "Update recommendation is called from service layer with request field matching a currency data type"() {
        given: "PUT recommendation called from service layer"

        def invalidRequestFieldName = 'valueEstimateFailureCost'
        def recommendationDataType = new RecommendationPayloadTypesEntity()
        recommendationDataType.payloadName = RecommendationFieldDataTypes.DOUBLE.getValue()

        def templateField = new RecommendationFieldsEntity()
        templateField.fieldName = invalidRequestFieldName
        templateField.payloadType = recommendationDataType

        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: templateField.getFieldId(),
                recommendationField: templateField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping)
        )
        def template = new RecommendationTemplatesEntity(
                templateName: "default",
                templateId: 1,
                templateSections: Arrays.asList(recommendationTemplateSection))

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        recommendationHeaderEntity.template = template
        def requestField = new TemplateCustomField()
        requestField.setPropertyName(invalidRequestFieldName)
        requestField.setPropertyValue('20')
        def secondRequestField = new TemplateCustomField()
        secondRequestField.propertyName = "recommendationStatus"
        secondRequestField.propertyValue = 'Expired'
        def thirdRequestField = new TemplateCustomField()
        thirdRequestField.propertyName = "recommendationPriority"
        thirdRequestField.propertyValue = '3 - At Next Service'

        def fields = Arrays.asList(requestField, secondRequestField, thirdRequestField)
        def recommendationPutRequest = new RecommendationPutRequest()

        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        def recommendationNumber = '1'

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        def e = thrown(CustomMethodArgumentNotValidException)
        e.code == CustomResponseCodes.BAD_REQUEST_MISSING_REQUIRED_FIELDS

    }

    def "Update recommendation is called from service layer with request field having an invalid option value"() {
        given: "PUT recommendation called from service layer"

        def option = new RecommendationCollectionOptionsEntity()
        option.optionName = 'Sent'
        option.optionValue = 1
        def recommendationGroupsEntity = new RecommendationFieldCollectionsEntity()
        recommendationGroupsEntity.collectionOptions = List.of(option)
        def invalidRequestFieldName = 'recommendationStatus'
        def recommendationDataType = new RecommendationPayloadTypesEntity()
        recommendationDataType.payloadName = RecommendationFieldDataTypes.STRING.getValue()

        def templateField = new RecommendationFieldsEntity()
        templateField.fieldName = invalidRequestFieldName
        templateField.payloadType = recommendationDataType
        templateField.collection = recommendationGroupsEntity

        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: templateField.getFieldId(),
                recommendationField: templateField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping)
        )
        def template = new RecommendationTemplatesEntity(
                templateName: "default",
                templateId: 1,
                templateSections: Arrays.asList(recommendationTemplateSection))

        def recommendationHeaderEntity = setUpTestRecommendationCommonData()
        recommendationHeaderEntity.template = template

        def requestField = new TemplateCustomField()
        requestField.setPropertyName(invalidRequestFieldName)
        requestField.setPropertyValue('2')

        def secondRequestField = new TemplateCustomField()
        secondRequestField.setPropertyName('recommendationPriority')
        secondRequestField.setPropertyValue('3 - At Next Service')

        def fields = Arrays.asList(requestField, secondRequestField)
        def recommendationPutRequest = new RecommendationPutRequest()

        recommendationPutRequest.setHoursReading(putHeader.getHoursReading())
        recommendationPutRequest.setExpirationTime(putHeader.getExpirationTime())
        recommendationPutRequest.setOwner(putHeader.getOwner())
        recommendationPutRequest.setTemplateCustomProperties(fields)
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        1 * usersRepository.findById(authorization.getCatRecId()) >> recommendationUsersEntityOptional
        def e = thrown(CustomMethodArgumentNotValidException)
        e.code == CustomResponseCodes.BAD_REQUEST_MISSING_REQUIRED_FIELDS
    }

    def "Update recommendation is called from service layer with an invalid asset name"() {
        given: "PUT recommendation called from service layer"

        def recommendationPutRequest = new RecommendationPutRequest()
        def recommendationHeaderEntity = new RecommendationCommonDataEntity()
        def assetName = 'CAT'
        recommendationHeaderEntity.assetName = assetName
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        0 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.BAD_REQUEST_MISSING_REQUIRED_REQUEST_BODY
        e.message == String.format(Constants.ASSET_INVALID_FORMAT, assetName)
    }

    def "Update recommendation is called from service layer with a non existing asset"() {
        given: "PUT recommendation called from service layer"

        def recommendationPutRequest = new RecommendationPutRequest()
        def recommendationHeaderEntity = new RecommendationCommonDataEntity()
        recommendationHeaderEntity.assetName = assetName
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.empty()
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.NOT_FOUND
        e.message == String.format(Constants.ASSET_WAS_NOT_FOUND, assetName)
    }

    def "Update recommendation is called from service layer with an asset not enrolled in Foresight"() {
        given: "PUT recommendation called from service layer"

        def recommendationPutRequest = new RecommendationPutRequest()
        def recommendationHeaderEntity = new RecommendationCommonDataEntity()
        recommendationHeaderEntity.assetName = assetName
        assetDetails.enabled = false
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationService.updateRecommendation(recommendationPutRequest,
                recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.NOT_FOUND
        e.message == String.format("Asset %s is not enrolled.", assetName)
    }

    def "Verify the results of fetching asset via assetDao"() {
        given: "AssetDao class that fetches asset by asset parameters"
        def assetDetailsEntity = new AssetDetailsEntity()
        def assetsDaoImpl = new AssetsDaoImpl(assetDetailsRepository)
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        def model = 'model'
        def dealerCode = 'dealerCode'
        assetDetailsEntity.make = make
        assetDetailsEntity.serialNumber = serialNumber
        assetDetailsEntity.primaryCustomerNumber = primaryCustomerNumber
        assetDetailsEntity.model = model
        assetDetailsEntity.dealerCode = dealerCode

        when:
        def resultOptional = assetsDaoImpl.findByMakeAndSerialNumberAndPrimaryCustomerNumber(
                make, serialNumber, primaryCustomerNumber)
        def result = resultOptional.get()

        then:
        1 * assetDetailsRepository.findByMakeAndSerialNumberAndPrimaryCustomerNumber(
                make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetailsEntity)

        result.make == make
        result.serialNumber == serialNumber
        result.primaryCustomerNumber == primaryCustomerNumber
        result.dealerCode == dealerCode
        result.model == model
    }

    def "when getRecommendationDetails is called, call getRecommendation on recommendationsRepository"() {
        given:
        def make = 'CAT'
        def serialNumber = 'RJG04184'
        def primaryCustomerNumber = '2969484566'
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        recommendationHeaderEntity.template = template
        assetDetails.enabled = true

        when:
        recommendationService.getRecommendationDetails(recommendationNumber, authorization)

        then:
        1 * recommendationCommonDataRepository.findByRecommendationNumber(recommendationNumber) >> Optional.of(recommendationHeaderEntity)
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
    }

    def "when getRecommendationDetails is called and recommendation does not exist, throw not found"() {
        given:
        def make = 'CAT'
        def serialNumber = 'RJG04184'
        def primaryCustomerNumber = '2969484566'
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        assetDetails.enabled = true

        when:
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.empty()
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        recommendationService.getRecommendationDetails(recommendationNumber, authorization)

        then:
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.NOT_FOUND
        e.message == String.format("Recommendation %s does not exist.", recommendationNumber)
    }

    def "when getRecommendationDetails is called and asset does not exist, throw not found"() {
        given:
        def make = 'CAT'
        def serialNumber = 'RJG04184'
        def primaryCustomerNumber = '2969484566'
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        assetDetails.enabled = true

        when:
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        recommendationService.getRecommendationDetails(recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.empty()
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.NOT_FOUND
        e.message == String.format(Constants.ASSET_WAS_NOT_FOUND, assetName)
    }

    def "when getRecommendationDetails is called and asset is not enrolled, throw not found"() {
        given:
        def make = 'CAT'
        def serialNumber = 'RJG04184'
        def primaryCustomerNumber = '2969484566'
        def assetName = 'CAT|RJG04184|2969484566'
        recommendationHeaderEntity.assetName = assetName
        assetDetails.enabled = false
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)

        when:
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        recommendationService.getRecommendationDetails(recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        def e = thrown(InvalidInputRequestException)
        e.code == CustomResponseCodes.NOT_FOUND
        e.message == String.format("Asset %s is not enrolled.", assetName)
    }

    def "get recommendation details maps and returns the result"() {
        given:
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.enabled = true
        recommendationHeaderEntity.assetName = assetId
        storageDao.retrieveFileMetadata(_ as String, _ as String) >> [attachment]

        notesDao.getLinks(recommendationNumber) >> links

        when:
        def result = recommendationService.getRecommendationDetails(recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        result == expectedRecommendationDetailsGet
    }

    def "retrieve the recommendation PDF file"() {
        given:
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.enabled = true
        recommendationHeaderEntity.assetName = assetId
        storageDao.retrieveFileMetadata(_ as String, _ as String) >> [attachment]

        notesDao.getLinks(recommendationNumber) >> links

        InputStreamResource stream = Mock()
        pdfGenerator.generatePDF(_ as String, _ as Object) >> stream


        when:
        def result = recommendationService.getRecommendationPDF(recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        result != null

    }

    def "retrieve the recommendation PDF file throwing an exception"() {
        given:
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.enabled = true
        recommendationHeaderEntity.assetName = assetId
        storageDao.retrieveFileMetadata(_ as String, _ as String) >> [attachment]

        notesDao.getLinks(recommendationNumber) >> links

        InputStreamResource stream = Mock()
        pdfGenerator.generatePDF(_ as String, _ as Object) >> { throw new IOException() }

        when:
        def result = recommendationService.getRecommendationPDF(recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        thrown RecoServerException
    }


    def "recommendation not exist on DB"() {
        given:
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.empty()

        when:
        recommendationService.getRecommendationDetails(recommendationNumber, authorization)

        then:
        thrown InvalidInputRequestException
    }

    def "Validate if the recommendation exist"() {
        given:
            def recommendationData = new RecommendationCommonDataEntity()
            recommendationCommonDataRepository.existsByRecommendationNumber(_ as String) >> true
            recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationData)
            def assetDetails = new AssetDetails()
            def make = 'CAT'
            def serialNumber = 'RXZ00353'
            def primaryCustomerNumber = '2969412354'
            recommendationData.assetName = assetId
            assetDetails.enabled = true
        when:
            def result = recommendationService.validateRecommendationExists("REC-123-456-789", CustomResponseCodes.NOT_FOUND)

        then:
            1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
            assert result != null
    }

    def "Validate if the recommendation number not exist should throw exception"() {
        given:
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.empty()
        when:
        recommendationService.validateRecommendationExists("REC-123-456-789", CustomResponseCodes.NOT_FOUND)

        then:
        thrown InvalidInputRequestException
    }

    def "Get recommendation details makes a call to get links from the Notes Dao"() {
        given:
            recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
            def assetDetails = new AssetDetails()
            def make = 'CAT'
            def serialNumber = 'RXZ00353'
            def primaryCustomerNumber = '2969412354'
            assetDetails.enabled = true
            recommendationHeaderEntity.assetName = assetId
            assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        when:
            recommendationService.getRecommendationDetails(recommendationNumber, authorization)
        then:
            1 * notesDao.getLinks(recommendationNumber) >> links
    }
    
    def "Getting recommendation details get attachments"() {
        given:
        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.enabled = true
        recommendationHeaderEntity.assetName = assetId

        when:
        def result = recommendationService.getRecommendationDetails(recommendationNumber, authorization)

        then:
        1 * assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        1 * storageDao.retrieveFileMetadata(recommendationNumber, dealerCode) >> [attachment]
        1 * notesDao.getLinks(recommendationNumber) >> links
        result == expectedRecommendationDetailsGet
    }

    def "Getting recommendation details throws an error when option value not found"() {
        given:
        def invalidValue = "-1"
        def recommendationGenericOptions = RecommendationCollectionOptionsEntity.builder().optionValue("1").optionName("Option 1").build()
        def genericGroupEntity = RecommendationFieldCollectionsEntity.builder().collectionOptions(List.of(recommendationGenericOptions)).build()
        def invalidRecommendationField = setupRecommendationFieldEntityWithOptions(genericGroupEntity, "recommendationStatus", 4)
        def invalidRecommendationDetailsEntity = setupRecommendationDetailsEntity(invalidRecommendationField, null, invalidValue)

        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.enabled = true
        recommendationHeaderEntity.assetName = assetId
        recommendationHeaderEntity.recommendationDetails = [invalidRecommendationDetailsEntity]

        recommendationCommonDataRepository.findByRecommendationNumber(_ as String) >> Optional.of(recommendationHeaderEntity)
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)
        storageDao.retrieveFileMetadata(_ as String, _ as String) >> [attachment]

        when:
        recommendationService.getRecommendationDetails(recommendationNumber, authorization)

        then:
        thrown RecoServerException
    }

    def "Getting list of recommendation"() {
        given:
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)

        def sortBody = Sort.by("sortBy").descending()
        List<RecommendationSortByParam> sortByParams = [RecommendationSortByParam.TITLE]
        List<OrderByParam> orderByParams = [OrderByParam.DESC]
        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName("Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()
        1 * recommendationDynamicSortHelperRepository.findAll(sortBody) >> Collections.singletonList(recommendationDynamicSortHelper)
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)

        when:
        def result = recommendationService.getRecommendations(authorization, sortByParams, orderByParams, new RecommendationSearchRequest())

        then:
        assert result.size() == 1
    }

    def "Getting list of recommendation with sort"() {
        given:
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)

        def sortBody = Sort.by("sortBy").descending()
        List<RecommendationSortByParam> sortByParams = [RecommendationSortByParam.TITLE]
        List<OrderByParam> orderByParams = [OrderByParam.DESC]

        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName("Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()
        1 * recommendationDynamicSortHelperRepository.findAll(sortBody) >> Collections.singletonList(recommendationDynamicSortHelper)

        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)

        when:
        def result = recommendationService.getRecommendations(authorization, sortByParams, orderByParams, new RecommendationSearchRequest())

        then:
        assert result.size() == 1
    }

    def "Getting list of recommendation throws an error when option value not found"() {
        given:
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)

        def sortBody = Sort.by("sortBy").descending()
        List<RecommendationSortByParam> sortByParams = [RecommendationSortByParam.TITLE]
        List<OrderByParam> orderByParams = [OrderByParam.DESC]

        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName(null).optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName("Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()
        1 * recommendationDynamicSortHelperRepository.findAll(sortBody) >> Collections.singletonList(
                recommendationDynamicSortHelper)
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("B030")
        authorization.setAuthorizedDealers(dealerCode)
        authorization.setDealer(true)

        when:
        recommendationService.getRecommendations(authorization, sortByParams, orderByParams, new RecommendationSearchRequest())

        then:
        thrown RecoServerException
    }

    def "Getting list of recommendation when user is dealer and recommendation is not a dealer recommendation"() {
        given:
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)

        def sortBody = Sort.by("sortBy").descending()
        List<RecommendationSortByParam> sortByParams = [RecommendationSortByParam.TITLE]
        List<OrderByParam> orderByParams = [OrderByParam.DESC]

        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .dealerRecommendation(false)
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName(null).optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName("Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()
        1 * recommendationDynamicSortHelperRepository.findAll(sortBody) >> Collections.singletonList(recommendationDynamicSortHelper)

        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        authorization.setDealer(true)

        when:
        def result = recommendationService.getRecommendations(authorization, sortByParams, orderByParams, new RecommendationSearchRequest())

        then:
        thrown RecoServerException
    }

    def "Getting list of recommendation when user is dealer and recommendation is a dealer recommendation"() {
        given:
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)

        def sortBody = Sort.by("sortBy").descending()
        List<RecommendationSortByParam> sortByParams = [RecommendationSortByParam.TITLE]
        List<OrderByParam> orderByParams = [OrderByParam.DESC]

        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .dealerRecommendation(true)
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName("Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()
        1 * recommendationDynamicSortHelperRepository.findAll(sortBody) >> Collections.singletonList(
                recommendationDynamicSortHelper)
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        authorization.setDealer(true)

        when:
        def result = recommendationService.getRecommendations(authorization, sortByParams, orderByParams, new RecommendationSearchRequest())

        then:
        assert result.size() == 1
    }

    def "Getting list of recommendation when user is not a dealer"() {
        given:
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)

        def sortBody = Sort.by("sortBy").descending()
        List<RecommendationSortByParam> sortByParams = [RecommendationSortByParam.TITLE]
        List<OrderByParam> orderByParams = [OrderByParam.DESC]

        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName("Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()
        1 * recommendationDynamicSortHelperRepository.findAll(sortBody) >> Collections.singletonList(
                recommendationDynamicSortHelper)
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        authorization.setDealer(false)

        when:
        def result = recommendationService.getRecommendations(authorization, sortByParams, orderByParams, new RecommendationSearchRequest())

        then:
        assert result.size() == 1
    }

    def "Export CSV list of recommendation"() {
        given:
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)

        def sortBody = Sort.by("sortBy").descending()
        List<RecommendationSortByParam> sortByParams = [RecommendationSortByParam.TITLE]
        List<OrderByParam> orderByParams = [OrderByParam.DESC]

        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .updatedDate(Timestamp.valueOf(updatedDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName("Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()
        1 * recommendationDynamicSortHelperRepository.findAll(sortBody) >> Collections.singletonList(
                recommendationDynamicSortHelper)
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)

        when:
        def result = recommendationService.getRecommendationsExport(authorization, sortByParams, orderByParams, new RecommendationSearchRequest())

        then:
        assert result != null
    }

    def "Filter recommendations by filter criteria for stringEquals filter"() {
        given:
        def sortBody = Sort.by("sortBy").descending()
        def sortByParams = [RecommendationSortByParam.RECOMMENDATION_NUMBER]
        def orderByParams = [OrderByParam.DESC]
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                        .dealerCode("T030")
                        .recommendationDetails(Arrays.asList(
                                RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                        Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                                .build())).build()).build()).build(),
                                RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                        RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                        Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                                .build())).build()).build()).build()))
                        .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                        .recommendationNumber("REC-112-113")
                        .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                        .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                        .updatedDate(Timestamp.valueOf(updatedDateOffset.toLocalDateTime()))
                        .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName(
                "Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()


        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        def filterList = List.of(StringEqualsFilter.builder().propertyName(
                RecommendationField.RECOMMENDATION_NUMBER.value).values(List.of("REC-112-113")).build());
        def searchRequest = new RecommendationSearchRequest()
        searchRequest.filters = filterList
        searchRequest.logicalExpression = "\$0 & \$1 & \$2 & \$3 & \$4 & \$5 & \$6 & \$7 & \$8 & \$9 & \$10 & \$11 & \$12"

        when:
        def result = recommendationService.getRecommendations(authorization, sortByParams, orderByParams, searchRequest)

        then:
        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        1 * recommendationQueryService.filterRecommendations(_ as Authorization, _ as RecommendationSearchRequest,
        _ as Sort) >> List.of(recommendationDynamicSortHelper)
        assert result != null
    }

    def "Filter recommendations by filter criteria for containsFilter"() {
        given:
        def sortBody = Sort.by("sortBy").descending()
        def sortByParams = [RecommendationSortByParam.RECOMMENDATION_NUMBER]
        def orderByParams = [OrderByParam.DESC]
        def expirationDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def createdDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def updatedDateOffset = OffsetDateTime.of(2020, 10, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        def recommendationCommonData = RecommendationCommonDataEntity.builder().serialNumber("1")
                .dealerCode("T030")
                .recommendationDetails(Arrays.asList(
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                .recommendationField(RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("1 - Immediate Attention").optionValue(RecommendationField.RECOMMENDATION_PRIORITY.getValue())
                                                        .build())).build()).build()).build(),
                        RecommendationCustomDataEntity.builder().value(RecommendationField.RECOMMENDATION_STATUS.getValue()).recommendationField(
                                RecommendationFieldsEntity.builder().fieldName(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                        .collection(RecommendationFieldCollectionsEntity.builder().collectionOptions(
                                                Collections.singletonList(RecommendationCollectionOptionsEntity.builder().optionName("Draft").optionValue(RecommendationField.RECOMMENDATION_STATUS.getValue())
                                                        .build())).build()).build()).build()))
                .ownedBy(RecommendationUsersEntity.builder().cwsId("johnSmith").firstName("John").lastName("smith").build())
                .recommendationNumber("REC-112-113")
                .createdDate(Timestamp.valueOf(createdDateOffset.toLocalDateTime()))
                .expirationDate(Timestamp.valueOf(expirationDateOffset.toLocalDateTime()))
                .updatedDate(Timestamp.valueOf(updatedDateOffset.toLocalDateTime()))
                .assetName(assetId).template(RecommendationTemplatesEntity.builder().templateName(
                "Template - 1").build()).build()
        def recommendationDynamicSortHelper = RecommendationDynamicDataSortHelper.builder().
                recommendationNumber(recommendationCommonData.recommendationNumber).recommendationId(
                recommendationCommonData.recommendationId).dealerCode(
                recommendationCommonData.dealerCode).dealerRecommendation(
                recommendationCommonData.dealerRecommendation).recommendationCommonData(
                recommendationCommonData).build()

        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        def containsFilter = new ContainsFilter()
        containsFilter.propertyName = RecommendationField.RECOMMENDATION_NUMBER.value
        containsFilter.value = 'REC'
        def filterList = List.of(containsFilter);
        def searchRequest = new RecommendationSearchRequest()
        searchRequest.filters = filterList
        searchRequest.logicalExpression = "\$0 & \$1 & \$2 & \$3 & \$4 & \$5 & \$6 & \$7 & \$8 & \$9 & \$10 & \$11 & \$12"

        when:
        def result = recommendationService.getRecommendations(authorization, sortByParams, orderByParams, searchRequest)

        then:
        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        1 * recommendationQueryService.filterRecommendations(_ as Authorization, _ as RecommendationSearchRequest,
                _ as Sort) >> List.of(recommendationDynamicSortHelper)
        assert result != null
    }

    def "Filter recommendations by filter criteria for containsFilter returns no results"() {
        given:
        def sortBody = Sort.by("sortBy").descending()
        def sortByParams = [RecommendationSortByParam.RECOMMENDATION_NUMBER]
        def orderByParams = [OrderByParam.DESC]
        def assetDetails = new AssetDetails()
        def make = 'CAT'
        def serialNumber = 'RXZ00353'
        def primaryCustomerNumber = '2969412354'
        assetDetails.primaryCustomerName = 'Waste Management'
        assetDao.findByMakeAndSerialNumberAndPrimaryCustomerNumber(make, serialNumber, primaryCustomerNumber) >> Optional.of(assetDetails)

        Set<String> dealerCode = new HashSet<>()
        dealerCode.add("T030")
        authorization.setAuthorizedDealers(dealerCode)
        def containsFilter = new ContainsFilter()
        containsFilter.propertyName = RecommendationField.RECOMMENDATION_NUMBER.value
        containsFilter.value = 'REC'
        def filterList = List.of(containsFilter);
        def searchRequest = new RecommendationSearchRequest()
        searchRequest.filters = filterList
        searchRequest.logicalExpression = "\$0 & \$1 & \$2 & \$3 & \$4 & \$5 & \$6 & \$7 & \$8 & \$9 & \$10 & \$11 & \$12"

        when:
        def result = recommendationService.getRecommendations(authorization, sortByParams, orderByParams, searchRequest)

        then:
        1 * recommendationSortService.getRecommendationSort(sortByParams, orderByParams) >> sortBody
        1 * recommendationQueryService.filterRecommendations(_ as Authorization, _ as RecommendationSearchRequest,
                _ as Sort) >> Collections.EMPTY_LIST
        thrown RecoServerException
    }

    /*
    Helper Methods
     */

    private static RecommendationUsersEntity createUserEntity(catrecId, firstName, lastName, cwsId) {
        return RecommendationUsersEntity.builder()
                    .catrecid(catrecId)
                    .firstName(firstName)
                    .lastName(lastName)
                    .cwsId(cwsId)
                    .build()
    }

    private static User createUser(catrecid, firstName, lastName) {
        return User.builder()
                .catrecid(catrecid)
                .firstName(firstName)
                .lastName(lastName)
                .build()
    }

    private static def setupRecommendationFieldEntityWithOptions(group, fieldName, fieldId) {
        return RecommendationFieldsEntity.builder()
                .fieldName(fieldName)
                .fieldId(fieldId)
                .collection(group)
                .build()
    }

    private static def setupRecommendationDetailsEntity(recommendationField, recommendationHeader, value) {
        return RecommendationCustomDataEntity.builder()
                .fieldId(recommendationField.fieldId)
                .recommendationId(recommendationId)
                .value(value)
                .recommendationHeader(recommendationHeader as RecommendationCommonDataEntity)
                .recommendationField(recommendationField)
                .build()
    }

    private static def setupRecommendationDetailsGet(
            header,
            fields,
            attachments,
            links) {
        return RecommendationDetailsResponse.builder()
                .recommendationNumber(recommendationNumber)
                .assetId(assetId)
                .templateName(templateName)
                .commonFields(header)
                .templateCustomProperties(fields)
                .links(links)
                .attachments(attachments) 
//                .exceptions(exceptions) // TODO: Add when implementing the external API calls
//                .events(events)
                .build()
    }

    /**
     * Builds a sample common data / header entity for tests.
     * @return Sample test header entity
     */
    private def setUpTestRecommendationCommonData() {
        def recommendationHeaderEntity = new RecommendationCommonDataEntity()
        recommendationHeaderEntity.assetName = assetName
        recommendationHeaderEntity.template = template
        recommendationHeaderEntity.recommendationId = recommendationId
        recommendationHeaderEntity.smu = 0
        recommendationHeaderEntity.recommendationDetails = new ArrayList<>()
        return recommendationHeaderEntity
    }

    private static def setUpRecommendationGetHeader(
            expirationDate,
            createdDate,
            updatedDate,
            owner,
            createdBy,
            updatedBy,
             hoursReading,
            assetOwnershipAtRecommendation,
            assetMetadata,
            String site
    ) {
        return CommonFieldsResponse.builder()
                .title(title)
                .site(site)
                .expirationTime(expirationDate)
                .createdTime(createdDate)
                .updatedTime(updatedDate)
                .owner(owner)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .hoursReading(hoursReading)
                .assetOwnershipAtRecommendation(assetOwnershipAtRecommendation)
                .assetMetadata(assetMetadata)
                .build()
    }

    private static def setupRecommendationHeaderEntity(
            expirationDate,
            createdDate,
            updatedDate,
            recommendationDetailsEntities,
            template,
            ownedBy,
            createdBy,
            updatedBy
    ) {
        return RecommendationCommonDataEntity.builder()
                .recommendationId(recommendationId)
                .recommendationNumber(recommendationNumber)
                .title(title)
                .smu(smu)
                .owner(ownerCatRecid)
                .assetName(assetId)
                .site(site)
                .expirationDate(expirationDate)
                .createdDate(createdDate)
                .updatedDate(updatedDate)
                .createdByCatrecid(createdByCatRecid)
                .updatedByCatrecid(updatedByCatRecid)
                .serialNumber(serialNumber)
                .make(make)
                .model(model)
                .dealerCode(dealerCode)
                .dealerName(dealerName)
                .customerUcid(customerUcid)
                .customerName(customerName)
                .attachmentSize(0 as BigDecimal)
                .recommendationDetails(recommendationDetailsEntities)
                .template(template)
                .ownedBy(ownedBy)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .build()
    }

    private static def getValidTemplateWithThreeFields() {
        def recommendationField = new RecommendationFieldsEntity(
                fieldId: 1,
                fieldName: "recommendationPriority",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"))
        def recommendationField2 = new RecommendationFieldsEntity(
                fieldId: 2,
                fieldName: "recommendationStatus",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"))
        def recommendationField3 = new RecommendationFieldsEntity(
                fieldId: 3,
                fieldName: "recommendationAction",
                payloadType: new RecommendationPayloadTypesEntity(
                        payloadName: "String"))
        def recoTemplateSectionFieldMapping = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField.getFieldId(),
                recommendationField: recommendationField,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recoTemplateSectionFieldMapping2 = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField2.getFieldId(),
                recommendationField: recommendationField2,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recoTemplateSectionFieldMapping3 = new RecommendationTemplateSectionFieldMappingsEntity(
                fieldId: recommendationField3.getFieldId(),
                recommendationField: recommendationField3,
                templateSectionId: 1,
                fieldPositionNumber: 1,
        )
        def recommendationTemplateSection = new RecommendationTemplateSectionsEntity(
                templateSectionId: 1,
                sectionName: "Recommendation Header",
                sectionPositionNumber: 1,
                templateSectionTypeId: 1,
                templateSectionMappings: Arrays.asList(recoTemplateSectionFieldMapping,
                        recoTemplateSectionFieldMapping2, recoTemplateSectionFieldMapping3)
        )
        def template = new RecommendationTemplatesEntity(
                templateName: "Default template",
                templateId: 1,
                templateSections: Arrays.asList(recommendationTemplateSection))
        return template;
    }
}
