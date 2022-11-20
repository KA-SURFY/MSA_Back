package surfy.comfy.data.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.write.Post;

@Data
@AllArgsConstructor @NoArgsConstructor
public class GetPostResponse {
    private Long postId;
    private String title;
    private String contents;

    //private String thumbnail;
    private Long surveyId;
    private String surveyTitle;
    private String authorName;
    private Long authorId;
    private Boolean isBookmarked;
    private Boolean member_case; // true면 회원, false면 비회원
    private String uploadDate;
    private Long mySatisfaction;
    private int averageSatisfaction;
    private Long thumbnail;


    public GetPostResponse(Post post, Boolean isBookmarked, Boolean member_case, Long mySatisfaction, int averageSatisfaction){
        this.postId=post.getId();
        this.title=post.getTitle();
        this.contents=post.getContents();
        this.surveyId=post.getSurveyId();
        this.surveyTitle=post.getTitle();
        this.authorName=post.getName();
        this.authorId=post.getMemberId();
        this.isBookmarked=isBookmarked;
        this.member_case=member_case;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            this.uploadDate=objectMapper.writeValueAsString(post.getUploadDate());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.mySatisfaction=mySatisfaction;
        this.averageSatisfaction=averageSatisfaction;
        this.thumbnail= post.getThumbnail();

    }

    public GetPostResponse(Post post,Boolean isBookmarked,Boolean member_case){
        this.postId=post.getId();
        this.title=post.getTitle();
        this.contents=post.getContents();
        this.surveyId=post.getSurveyId();
        this.surveyTitle=post.getTitle();
        this.authorName=post.getName();
        this.authorId=post.getMemberId();
        this.isBookmarked=isBookmarked;
        this.member_case=member_case;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            this.uploadDate=objectMapper.writeValueAsString(post.getUploadDate());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.thumbnail= post.getThumbnail();
    }

}
