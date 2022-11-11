package surfy.comfy.data.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.Satisfaction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespondentsResponse {
    private Long id;

    public RespondentsResponse(Satisfaction satisfaction){
        this.id = satisfaction.getId();
    }
}
