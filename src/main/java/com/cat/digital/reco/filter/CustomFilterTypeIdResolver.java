/*
 *  Copyright (c) 2020 Caterpillar Inc. All Rights Reserved.
 *
 *  This work contains Caterpillar Inc.'s unpublished
 *  proprietary information which may constitute a trade secret
 *  and/or be confidential. This work may be used only for the
 *  purposes for which it was provided, and may not be copied
 *  or disclosed to others. Copyright notice is precautionary
 *  only, and does not imply publication.
 */

package com.cat.digital.reco.filter;

import com.cat.digital.reco.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.util.StringUtils;

/**
 * Custom type id resolver to identify Filter subclasses based on the type identifier.
 * In case type property is empty then StringEqualsFilter.class is resolved as type, for all other invalid entries a ValidationException is thrown.
 */
public class CustomFilterTypeIdResolver implements TypeIdResolver {

  private JavaType mbaseType;

  @Override
  public void init(JavaType javaType) {
    mbaseType = javaType;
  }

  @Override
  public String idFromValue(Object o) {
    return idFromValueAndType(o, o.getClass());
  }

  @Override
  public String idFromValueAndType(Object o, Class<?> clazz) {
    return clazz.getName();

  }

  @Override
  public String idFromBaseType() {
    return idFromValueAndType(null, mbaseType.getRawClass());
  }

  @Override
  public JavaType typeFromId(DatabindContext databindContext, String s) {

    s = StringUtils.isEmpty(s) ? FilterType.stringEquals.name() : s;
    try {
      return TypeFactory.defaultInstance().constructSpecializedType(mbaseType, FilterType.valueOf(s).getClassType());
    } catch (IllegalArgumentException e) {
      throw new ValidationException("Filter type not valid");
    }
  }

  @Override
  public String getDescForKnownTypeIds() {
    return null;
  }

  @Override
  public JsonTypeInfo.Id getMechanism() {
    return JsonTypeInfo.Id.CUSTOM;
  }
}
