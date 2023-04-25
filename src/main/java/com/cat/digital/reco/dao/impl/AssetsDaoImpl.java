package com.cat.digital.reco.dao.impl;

import java.util.Optional;

import com.cat.digital.reco.dao.AssetsDao;
import com.cat.digital.reco.domain.models.AssetDetails;
import com.cat.digital.reco.repositories.AssetDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class AssetsDaoImpl implements AssetsDao {

  private AssetDetailsRepository assetDetailsRepository;
  private ModelMapper modelMapper = new ModelMapper();

  public AssetsDaoImpl(final AssetDetailsRepository assetDetailsRepository) {
    this.assetDetailsRepository = assetDetailsRepository;
  }

  @Override
  public Optional<AssetDetails> findByMakeAndSerialNumberAndPrimaryCustomerNumber(
      final String make, final String serialNumber, final String primaryCustomerNumber) {
    var assetDetailsEntityOptional =
        assetDetailsRepository.findByMakeAndSerialNumberAndPrimaryCustomerNumber(
        make, serialNumber, primaryCustomerNumber
    );
    return assetDetailsEntityOptional.map(asset -> modelMapper.map(asset, AssetDetails.class));
  }
}
