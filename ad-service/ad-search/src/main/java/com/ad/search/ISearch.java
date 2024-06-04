package com.ad.search;

import com.ad.search.vo.SearchRequest;
import com.ad.search.vo.SearchResponse;

public interface ISearch {

    SearchResponse fetchAds(SearchRequest request);
}