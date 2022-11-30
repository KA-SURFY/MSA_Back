package surfy.comfy.data.result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.type.SurveyType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResultResponse {

    private Long id;
    private String title;
    private String contents;
    private double satisfaction;
    // 문서 타입이 넘어오면 저장해주면 될 듯
    private SurveyType type;

    private String end;

}