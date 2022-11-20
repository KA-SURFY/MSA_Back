package surfy.comfy.data.bookmark;

import lombok.Data;

import java.math.BigInteger;

@Data
public class PostBookmarkRequest {
    private Long postId;
    private Long memberId;
}
