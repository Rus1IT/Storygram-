package root.storygram.mapper;

import org.mapstruct.Mapper;
import root.storygram.dto.response.PostMediaResponse;
import root.storygram.entity.PostMedia;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMediaMapper {

    PostMediaResponse postMediaToPostMediaResponse(PostMedia postMedia);
    List<PostMediaResponse> postMediaListToPostMediaResponseList(List<PostMedia> postMediaList);
}
