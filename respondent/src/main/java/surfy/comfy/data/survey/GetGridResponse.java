package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class GetGridResponse {
    private Long temid;
    private Long rootid;
    private Long id;
    private String value;
    public GetGridResponse(){}
}