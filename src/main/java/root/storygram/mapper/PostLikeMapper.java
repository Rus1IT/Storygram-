package root.storygram.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import root.storygram.dto.response.PostLikeResponse;
import root.storygram.entity.PostLike;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostLikeMapper {

    @Named("toPostLikeResponse")
    @Mapping(source = "profile.account.username", target = "username")
    @Mapping(source = "profile.profilePictureUrl", target = "profilePicture")
    @Mapping(source = "post.shortCode", target = "postShortCode")
    @Mapping(target = "fullName", expression = "java(postLike.getProfile().getFirstName() + \" \" + postLike.getProfile().getLastName())")
    PostLikeResponse postLikeToPostLikeResponse(PostLike postLike);

    @IterableMapping(qualifiedByName = "toPostLikeResponse")
    List<PostLikeResponse> postLikeListToPostLikeResponseList(List<PostLike> postLikes);
}
