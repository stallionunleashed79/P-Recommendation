package com.cat.digital.reco.entitlements.response;

import lombok.Data;

@Data
public class Recommendation {
    private PermissionType view;
    private PermissionType create;
    private PermissionType update;
    private PermissionType delete;
}
