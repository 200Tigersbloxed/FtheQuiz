package lol.fortnite.Windows;

import imgui.ImGui;
import lol.fortnite.Study.StudyList;
import lol.fortnite.Study.StudyTerm;

public class FlashcardStudyWindow implements IWindow{
    private Runnable onDone;
    private StudyList studyList;
    public FlashcardStudyWindow(StudyList studyList, Runnable onDone){
        this.onDone = onDone;
        this.studyList = studyList;
    }

    @Override
    public String getWindowName() {
        return "Flashcards - " + studyList.ListName;
    }

    private StudyTerm currentTerm;
    private boolean isDefinition;
    private int i = 0;

    @Override
    public void DrawWindow(int width, int height) {
        currentTerm = studyList.GetStudyTerm(i);
        while (currentTerm == null){
            i = 0;
            currentTerm = studyList.GetStudyTerm(i);
        }
        String text = currentTerm.Term;
        if(isDefinition)
            text = currentTerm.Definition;
        if(ImGui.button(text + " ", width, height - 150)){
            if(!isDefinition){
                isDefinition = true;
            }
            else{
                i++;
                isDefinition = false;
            }
        }
        if(ImGui.button("Back"))
            onDone.run();
    }
}
