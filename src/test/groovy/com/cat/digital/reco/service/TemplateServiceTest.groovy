package com.cat.digital.reco.service

import com.cat.digital.reco.domain.entities.RecommendationCollectionOptionsEntity
import com.cat.digital.reco.domain.entities.RecommendationFieldCollectionsEntity
import com.cat.digital.reco.domain.entities.RecommendationFieldTypesEntity
import com.cat.digital.reco.domain.entities.RecommendationFieldsEntity
import com.cat.digital.reco.domain.entities.RecommendationPayloadTypesEntity
import com.cat.digital.reco.domain.entities.RecommendationTemplateSectionFieldMappingsEntity
import com.cat.digital.reco.domain.entities.RecommendationTemplateSectionTypesEntity
import com.cat.digital.reco.domain.entities.RecommendationTemplateSectionsEntity
import com.cat.digital.reco.domain.entities.RecommendationTemplatesEntity
import com.cat.digital.reco.domain.models.ContainerType
import com.cat.digital.reco.domain.models.DataType
import com.cat.digital.reco.domain.models.InputType
import com.cat.digital.reco.domain.models.RecommendationField
import com.cat.digital.reco.domain.models.TemplateProperties
import com.cat.digital.reco.domain.models.TemplateSectionProperty
import com.cat.digital.reco.exceptions.EntityNotFoundException
import com.cat.digital.reco.repositories.RecommendationTemplateRepository
import com.cat.digital.reco.service.impl.TemplateServiceImpl
import org.spockframework.spring.SpringBean
import spock.lang.Specification

class TemplateServiceTest extends Specification {

    private static templateName = 'Template 1 - Default'

    TemplateService templateService

    @SpringBean
    RecommendationTemplateRepository templateRepository = Mock()

    def setup() {
        templateService = new TemplateServiceImpl(templateRepository)
    }

    def "get template details with options"() {

        given:
        RecommendationCollectionOptionsEntity option = new RecommendationCollectionOptionsEntity(optionName: "Draft")

        List<RecommendationCollectionOptionsEntity> collectionOptions = new ArrayList<>()
        collectionOptions.add(option)

        RecommendationTemplateSectionFieldMappingsEntity templateSectionField = new RecommendationTemplateSectionFieldMappingsEntity(
                recommendationField: new RecommendationFieldsEntity(
                        fieldName: "recommendationStatus",
                        payloadType: new RecommendationPayloadTypesEntity(payloadName: DataType.TypeEnum.STRING.value),
                        fieldType: new RecommendationFieldTypesEntity(fieldTypeName: InputType.TypeEnum.DROPDOWN.value),
                        collection: new RecommendationFieldCollectionsEntity(
                                collectionOptions: collectionOptions
                        )
                )
        )

        List<RecommendationTemplateSectionFieldMappingsEntity> templateSectionMappings = new ArrayList<>()
        templateSectionMappings.add(templateSectionField)

        RecommendationTemplateSectionsEntity sectionsEntity = new RecommendationTemplateSectionsEntity(
                recommendationTemplateSectionTypesEntity: new RecommendationTemplateSectionTypesEntity(sectionTypeName: ContainerType.TypeEnum.ACCORDION.value),
                templateSectionMappings: templateSectionMappings)

        List<RecommendationTemplateSectionsEntity> templateSections = new ArrayList<>()
        templateSections.add(sectionsEntity)

        def templateEntity = new RecommendationTemplatesEntity(templateSections: templateSections)
        1 * templateRepository.findByTemplateName(templateName) >> Optional.of(templateEntity)

        when:
        List<TemplateProperties> result = templateService.getTemplateDetails(templateName)

        then:
        result.size() == 1
        result.get(0).getSectionProperties().get(0).getPropertyOptions().size() == 1
    }

    def "get template details without options"() {

        given:
        RecommendationTemplateSectionFieldMappingsEntity templateSectionField = new RecommendationTemplateSectionFieldMappingsEntity(
                recommendationField: new RecommendationFieldsEntity(
                        fieldName: "recommendationStatus",
                        payloadType: new RecommendationPayloadTypesEntity(payloadName: DataType.TypeEnum.STRING.value),
                        fieldType: new RecommendationFieldTypesEntity(fieldTypeName: InputType.TypeEnum.BUTTON.value)
                )
        )

        List<RecommendationTemplateSectionFieldMappingsEntity> templateSectionMappings = new ArrayList<>()
        templateSectionMappings.add(templateSectionField)

        RecommendationTemplateSectionsEntity sectionsEntity = new RecommendationTemplateSectionsEntity(
                recommendationTemplateSectionTypesEntity: new RecommendationTemplateSectionTypesEntity(sectionTypeName: ContainerType.TypeEnum.ACCORDION.value),
                templateSectionMappings: templateSectionMappings)

        List<RecommendationTemplateSectionsEntity> templateSections = new ArrayList<>()
        templateSections.add(sectionsEntity)

        def templateEntity = new RecommendationTemplatesEntity(templateSections: templateSections)
        1 * templateRepository.findByTemplateName(templateName) >> Optional.of(templateEntity)

        when:
        List<TemplateProperties> result = templateService.getTemplateDetails(templateName)

        then:
        result.size() == 1
        result.get(0).getSectionProperties().get(0).getPropertyOptions() == null
    }

    def "get template details when template is not found"() {

        given:
        1 * templateRepository.findByTemplateName(templateName) >> Optional.empty()

        when:
        templateService.getTemplateDetails(templateName)

        then:
        thrown(EntityNotFoundException)
    }

    def "validate the maximum property value for expirationTime"() {

        given:
        RecommendationTemplateSectionFieldMappingsEntity recoStatusFieldMappingEntity = new RecommendationTemplateSectionFieldMappingsEntity(
                recommendationField: new RecommendationFieldsEntity(
                        fieldName: "recommendationStatus",
                        payloadType: new RecommendationPayloadTypesEntity(payloadName: DataType.TypeEnum.STRING.value),
                        fieldType: new RecommendationFieldTypesEntity(fieldTypeName: InputType.TypeEnum.BUTTON.value)
                )
        )

        RecommendationTemplateSectionFieldMappingsEntity expirationDateFieldMappingEntity = new RecommendationTemplateSectionFieldMappingsEntity(
                recommendationField: new RecommendationFieldsEntity(
                        fieldName: "expirationTime",
                        payloadType: new RecommendationPayloadTypesEntity(payloadName: DataType.TypeEnum.TIMESTAMP.value),
                        fieldType: new RecommendationFieldTypesEntity(fieldTypeName: InputType.TypeEnum.DATE.value),
                )
        )

        def templateSectionMappings = Arrays.asList(recoStatusFieldMappingEntity, expirationDateFieldMappingEntity)
        RecommendationTemplateSectionsEntity sectionsEntity = new RecommendationTemplateSectionsEntity(
                recommendationTemplateSectionTypesEntity: new RecommendationTemplateSectionTypesEntity(sectionTypeName: ContainerType.TypeEnum.ACCORDION.value),
                templateSectionMappings: templateSectionMappings)

        List<RecommendationTemplateSectionsEntity> templateSections = new ArrayList<>()
        templateSections.add(sectionsEntity)

        def templateEntity = new RecommendationTemplatesEntity(templateSections: templateSections)
        1 * templateRepository.findByTemplateName(templateName) >> Optional.of(templateEntity)

        when:
        def result = templateService.getTemplateDetails(templateName)

        then:
        result.size() == 1
        result[0].sectionProperties.size() == 2
        List<TemplateSectionProperty> templateSectionProperties = result[0].sectionProperties
        TemplateSectionProperty expirationDateEntry = templateSectionProperties.stream().find(
                { templateSectionProperty ->
                    templateSectionProperty.propertyName.equalsIgnoreCase(RecommendationField.EXPIRATION_TIME.value)
                });
        assert expirationDateEntry != null
        assert expirationDateEntry.propertyName.equalsIgnoreCase(RecommendationField.EXPIRATION_TIME.value)
        assert expirationDateEntry.maximumPropertyValue != null
    }
}
