package surfy.comfy.data.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.write.Post;

import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PostResponse {
    private String title;
    private String authorName;
    private Long postId;
    private LocalDate uploadDate;
    private Long authorId;
    private Long thumbnail;

    public PostResponse(Post post){
        this.title=post.getTitle();
        this.authorName=post.getName();
        this.postId=post.getId();
        this.uploadDate=post.getUploadDate();
        this.authorId=post.getMemberId();
        this.thumbnail=post.getThumbnail();
//        this.type="post";
    }
}
