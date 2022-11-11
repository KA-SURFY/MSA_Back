package surfy.comfy.data.survey;

import lombok.Data;
import surfy.comfy.entity.Grid;
@Data
public class GetGridResponse {
    private Long temid;
    private Long rootid;
    private Long id;
    private String value;
    public GetGridResponse(){}
    public GetGridResponse(Grid grid) {
        this.temid = 0L;
        this.rootid = grid.getQuestion().getId();
        this.id = grid.getId();
        this.value = grid.getContents();
    }
}