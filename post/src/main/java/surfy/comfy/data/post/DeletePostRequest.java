package surfy.comfy.data.post;

import lombok.Data;

@Data
public class DeletePostRequest {
    private Long postId;
    private Long memberId;
}
