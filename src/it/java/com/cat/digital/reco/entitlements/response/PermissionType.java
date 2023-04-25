package com.cat.digital.reco.entitlements.response;

import lombok.Data;

import java.util.List;

@Data
public class PermissionType {
    private List<String> allowedFields;
    private List<FilterCondition> filterConditions;
}
