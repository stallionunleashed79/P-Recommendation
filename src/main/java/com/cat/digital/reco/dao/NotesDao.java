package com.cat.digital.reco.dao;

import java.util.List;

import com.cat.digital.reco.domain.models.RecommendationLink;

public interface NotesDao {
  List<RecommendationLink> getLinks(String recommendationNumber);
}
