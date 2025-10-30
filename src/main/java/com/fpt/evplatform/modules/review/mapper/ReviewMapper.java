package com.fpt.evplatform.modules.review.mapper;

import com.fpt.evplatform.modules.deal.entity.Deal;
import com.fpt.evplatform.modules.review.dto.request.ReviewRequest;
import com.fpt.evplatform.modules.review.dto.response.ReviewResponse;
import com.fpt.evplatform.modules.review.entity.Review;
import com.fpt.evplatform.modules.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ReviewMapper {

    @Mapping(target = "reviewId", ignore = true)
    @Mapping(target = "deal", source = "deal")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "target", source = "target")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Review toEntity(ReviewRequest req, Deal deal, User author, User target);

    @Mapping(target = "dealId", source = "deal.dealId")
    @Mapping(target = "authorId", source = "author.userId")
    @Mapping(target = "authorName", source = "author.username")
    @Mapping(target = "targetId", source = "target.userId")
    @Mapping(target = "targetName", source = "target.username")
    ReviewResponse toResponse(Review review);
}
