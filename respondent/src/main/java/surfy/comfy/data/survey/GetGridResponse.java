package surfy.comfy.data.survey;

import lombok.Data;
import surfy.comfy.entity.read.Grid;
@Data
public class GetGridResponse {
    private Long temid;
    private Long rootid;
    private Long id;
    private String value;
    public GetGridResponse(){}
}