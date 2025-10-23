package com.fpt.evplatform.modules.salepost.repository;

import java.math.BigDecimal;

public interface PostCardProjection {
    Integer getListingId();
    String  getProductName();   // sp.title
    BigDecimal getAskPrice();
    String  getProductType();
    Integer getProvinceCode();
    Integer getDistrictCode();
    Integer getWardCode();
    String  getStreet();
    String  getCoverPublicId();
    String  getCoverType();     // IMAGE | VIDEO | null
}
