package surfy.comfy.data.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import surfy.comfy.entity.read.Grid;
import surfy.comfy.entity.read.Option;
import surfy.comfy.entity.write.Essay;
import surfy.comfy.entity.write.Satisfaction;
import surfy.comfy.entity.write.Slider;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDataResponse {
    Option option;
    Grid grid;
    Essay essay;
    Satisfaction satisfaction;
    Slider slider;
}
