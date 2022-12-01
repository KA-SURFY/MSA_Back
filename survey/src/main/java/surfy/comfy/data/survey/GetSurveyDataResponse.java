package surfy.comfy.data.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import surfy.comfy.entity.read.Answer;
import surfy.comfy.entity.write.Survey;
import surfy.comfy.type.QuestionType;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class GetSurveyDataResponse {
    private String intro0;
    private String intro1;
    private List<GetQuestionResponse> ques_list;
    private List<GetOptionResponse> ans_list;
    private List<GetGridResponse> choice_list;
    private Long satis;
    private String status;
    private String start;
    private String end;
    public GetSurveyDataResponse(){}
    @SneakyThrows
    public GetSurveyDataResponse(Survey survey,Boolean loadAnswer,Long submitid){
        this.status = String.valueOf(survey.getStatus());
        this.start = String.valueOf(survey.getStart());
        this.end = String.valueOf(survey.getEnd());

        this.intro0=survey.getTitle();
        this.intro1=survey.getContents();
        this.ques_list=new ArrayList<>();
        this.ans_list=new ArrayList<>();
        this.choice_list=new ArrayList<>();

        for(int i=0;i<survey.getQuestions().size();i++){
            if(survey.getQuestions().get(i).getQuestionType()== QuestionType.만족도){
                try{
                    if(loadAnswer){

                        List<Answer> answers= new GetAnswerResponse().GetAnswers(survey.getQuestions().get(i).getId());
                        for(int k=0;k<answers.size();k++){
                            if(answers.get(k).getSubmit()==submitid) {
                                this.satis=answers.get(k).getSatisfaction().getPercent();
                                break;
                            }
                        }
                    }

                }
                catch(Exception e){
                    this.satis=null;
                }
                continue;
            }
            this.ques_list.add(new GetQuestionResponse(survey.getQuestions().get(i),loadAnswer,submitid));

            for(int k=0;k<survey.getQuestions().get(i).getOptions().size();k++){
                GetOptionResponse opt = new GetOptionResponse(survey.getQuestions().get(i).getOptions().get(k));

                int t=0;
                for(;t<this.ans_list.size();t++){
                    if(this.ans_list.get(t).getId()>opt.getId()){
                        this.ans_list.add(t,opt);
                        break;
                    }
                }
                if(t==this.ans_list.size()){
                    this.ans_list.add(opt);
                }
            }
            for(int k=0;k<survey.getQuestions().get(i).getGrids().size();k++){
                GetGridResponse grid = new GetGridResponse(survey.getQuestions().get(i).getGrids().get(k));


                int t=0;
                for(;t<this.choice_list.size();t++){
                    if(this.choice_list.get(t).getId()>grid.getId()){
                        this.choice_list.add(t,grid);
                        break;
                    }
                }
                if(t==this.choice_list.size()){
                    this.choice_list.add(grid);
                }
            }
        }
    }
}