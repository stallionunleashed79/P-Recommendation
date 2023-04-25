package com.cat.digital.reco.entitlements.response;

import lombok.Data;

import java.util.List;

@Data
public class FilterCondition {
    private List<String> partyNumbers;
    private List<String> regions;

}
