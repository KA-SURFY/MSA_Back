

package surfy.comfy.data.survey;

import lombok.Data;
import surfy.comfy.entity.read.Option;
@Data
public class GetOptionResponse {
    private Long temid;
    private Long rootid;
    private Long id;
    private String value;
    public GetOptionResponse(){}
}