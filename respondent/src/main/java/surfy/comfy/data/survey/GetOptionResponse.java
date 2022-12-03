

package surfy.comfy.data.survey;

import lombok.Data;
@Data
public class GetOptionResponse {
    private Long temid;
    private Long rootid;
    private Long id;
    private String value;
    public GetOptionResponse(){}
}