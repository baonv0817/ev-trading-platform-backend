package com.fpt.evplatform.modules.salepost.repository;

import com.fpt.evplatform.common.enums.PostStatus;

import java.math.BigDecimal;

public interface PostCardProjection {
    Integer getListingId();
    String  getProductName();   // sp.title
    BigDecimal getAskPrice();
    String  getProductType();
    Integer getProvinceCode();
    Integer getPriorityLevel();
    Integer getDistrictCode();
    Integer getWardCode();
    String getSellerUsername();
    PostStatus getStatus();
    String getInspectionStatus();
    String  getStreet();
    String  getCoverPublicId();
    String  getCoverType();     // IMAGE | VIDEO | null
}
