package com.cat.digital.reco.dao;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.cat.digital.reco.exceptions.DealerDetailException;

public interface DealerDetailsDao {

  Set<String> getDealerCodes(final List<String> regionCodes) throws IOException, DealerDetailException;

  String addQueryParams(final List<String> regionCodes);
}


