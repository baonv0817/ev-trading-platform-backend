package com.fpt.evplatform.modules.salepost.mapper;

import com.fpt.evplatform.modules.salepost.dto.request.CreatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalePostMapper {

    PostResponse toPostResponse(SalePost salePost);
}
