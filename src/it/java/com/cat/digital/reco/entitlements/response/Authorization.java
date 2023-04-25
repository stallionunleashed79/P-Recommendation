package com.cat.digital.reco.entitlements.response;

import lombok.Data;

@Data
public class Authorization {
    private String catrecid;
    private Permission permissions;
}
