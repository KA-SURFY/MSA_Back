

package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import surfy.comfy.entity.write.Option;
@Data
@AllArgsConstructor
public class GetOptionResponse {
    private Long temid;
    private Long rootid;
    private Long id;
    private String value;
    public GetOptionResponse(){}
}