package com.cat.digital.reco.dao;

import java.util.Optional;

import com.cat.digital.reco.domain.models.AssetDetails;

public interface AssetsDao {

  Optional<AssetDetails> findByMakeAndSerialNumberAndPrimaryCustomerNumber(
      final String make, final String serialNumber, final String primaryCustomerNumber);
}
