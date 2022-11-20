package surfy.comfy.data;

import lombok.Data;

@Data
public class ThumbnailRequest {
    private String imgSrc;
    private String email;
    private String bgColor;
    private Long surveyId;
}
