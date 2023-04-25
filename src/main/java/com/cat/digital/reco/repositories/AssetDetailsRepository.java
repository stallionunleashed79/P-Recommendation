package com.cat.digital.reco.repositories;

import java.util.Optional;

import com.cat.digital.reco.domain.entities.AssetDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetDetailsRepository extends JpaRepository<AssetDetailsEntity, Integer> {

  Optional<AssetDetailsEntity> findByMakeAndSerialNumberAndPrimaryCustomerNumber(
      final String make, final String serialNumber, final String primaryCustomerNumber);
}
