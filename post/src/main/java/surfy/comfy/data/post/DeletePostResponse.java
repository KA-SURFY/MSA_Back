package surfy.comfy.data.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor @NoArgsConstructor
public class DeletePostResponse {
    private Long postId;
    private Long memberId;
}
