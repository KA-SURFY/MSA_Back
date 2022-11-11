package surfy.comfy.data.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class MyPageResponse {
    private List<GetPostResponse> bookmarks;
    private List<GetPostResponse> myposts;
}
