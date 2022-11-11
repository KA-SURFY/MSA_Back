package surfy.comfy.data.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Post;

import java.time.LocalDate;

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
    private LocalDate uploadDate;
    private Long mySatisfaction;
    private int averageSatisfaction;
    private Long thumbnail;


    public GetPostResponse(Post post,Boolean isBookmarked,Boolean member_case,Long mySatisfaction,int averageSatisfaction){
        this.postId=post.getId();
        this.title=post.getTitle();
        this.contents=post.getContents();
        //this.thumbnail=post.getThumbnail();
        this.surveyId=post.getSurvey().getId();
        this.surveyTitle=post.getSurvey().getTitle();
        this.authorName=post.getMember().getName();
        this.authorId=post.getMember().getId();
        this.isBookmarked=isBookmarked;
        this.member_case=member_case;
        this.uploadDate=post.getUploadDate();
        this.mySatisfaction=mySatisfaction;
        this.averageSatisfaction=averageSatisfaction;
        this.thumbnail= post.getSurvey().getThumbnail();

    }

    public GetPostResponse(Post post,Boolean isBookmarked,Boolean member_case){
        this.postId=post.getId();
        this.title=post.getTitle();
        this.contents=post.getContents();
        //this.thumbnail=post.getThumbnail();
        this.surveyId=post.getSurvey().getId();
        this.surveyTitle=post.getSurvey().getTitle();
        this.authorName=post.getMember().getName();
        this.authorId=post.getMember().getId();
        this.isBookmarked=isBookmarked;
        this.member_case=member_case;
        this.uploadDate=post.getUploadDate();
        this.thumbnail= post.getSurvey().getThumbnail();
    }

}
