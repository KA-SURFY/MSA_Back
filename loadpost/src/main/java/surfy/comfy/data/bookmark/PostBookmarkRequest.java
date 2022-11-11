package surfy.comfy.data.bookmark;

import lombok.Data;

@Data
public class PostBookmarkRequest {
    private Long postId;
    private Long memberId;
}
