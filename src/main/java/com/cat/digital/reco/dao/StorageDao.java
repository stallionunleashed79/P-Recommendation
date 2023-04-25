package com.cat.digital.reco.dao;

import java.util.*;

import com.cat.digital.reco.domain.models.Attachment;

public interface StorageDao {
  List<Attachment> retrieveFileMetadata(String recommendationNumber, String partyNumber);
}
