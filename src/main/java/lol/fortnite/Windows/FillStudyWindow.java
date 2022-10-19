package lol.fortnite.Windows;

import imgui.ImGui;
import imgui.type.ImString;
import lol.fortnite.Study.StudyList;
import lol.fortnite.Study.StudyTerm;

import java.util.Locale;

public class FillStudyWindow implements IWindow{
    private Runnable onDone;
    private StudyList studyList;
    private StudyTerm[] studyTerms;

    public FillStudyWindow(StudyList studyList, Runnable onDone){
        this.onDone = onDone;
        this.studyList = studyList;
        studyTerms = studyList.GetStudyTerms();
    }

    @Override
    public String getWindowName() {
        return "Fill - " + studyList.ListName;
    }

    private int i = 0;
    private ImString input = new ImString();

    private int right = 0;
    private int wrong = 0;

    @Override
    public void DrawWindow(int width, int height) {
        StudyTerm studyTerm = studyList.GetStudyTerm(i);
        if(studyTerm == null){
            ImGui.text("You finished!");
            ImGui.text("You got " + right + " question(s) correct,");
            ImGui.text("and " + wrong + " question(s) wrong,");
            ImGui.text("out of " + studyTerms.length + " question(s).");
            if (ImGui.button("Try Again?")){
                i = 0;
                right = 0;
                wrong = 0;
            }
            ImGui.sameLine();
        }
        else{
            ImGui.text(studyTerm.Term);
            ImGui.inputText("Response", input);
            if(ImGui.button("Submit")){
                if(input.get().toLowerCase(Locale.ROOT).equals(studyTerm.Definition))
                    right++;
                else
                    wrong++;
                input.set("");
                i++;
            }
        }
        if(ImGui.button("Return"))
            onDone.run();
    }
}
