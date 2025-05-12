package root.storygram.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import root.storygram.dto.request.post.PostRequest;
import root.storygram.dto.response.PostMediaResponse;
import root.storygram.dto.response.PostResponseDto;
import root.storygram.entity.Post;
import root.storygram.entity.PostMedia;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "likesCount", expression = "java(post.getLikes() != null ? (long) post.getLikes().size() : 0L)")
    @Mapping(target = "commentsCount", expression = "java(post.getComments() != null ? (long) post.getComments().size() : 0L)")
    PostResponseDto postToPostResponseDto(Post post);

    void updatePost(PostRequest postRequest, @MappingTarget Post post);

    List<PostResponseDto> postListToPostResponseDtoList(List<Post> posts);

    PostMediaResponse postMediaToPostMediaResponse(PostMedia postMedia);
    List<PostMediaResponse> postMediaListToPostMediaResponseList(List<PostMedia> postMediaList);
}
