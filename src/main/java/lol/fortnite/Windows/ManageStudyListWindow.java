package lol.fortnite.Windows;

import imgui.ImGui;
import lol.fortnite.Study.StudyList;

public class ManageStudyListWindow implements IWindow{
    private StudyList studyList;
    private Runnable onCallback;
    private IWindow overrideWindow;

    public ManageStudyListWindow(StudyList studyList, Runnable onCallback){
        this.studyList = studyList;
        this.onCallback = onCallback;
    }

    @Override
    public String getWindowName() {
        if(overrideWindow != null)
            return overrideWindow.getWindowName();
        return studyList.ListName;
    }

    @Override
    public void DrawWindow(int width, int height) {
        if(overrideWindow != null)
            overrideWindow.DrawWindow(width, height);
        else{
            if(ImGui.button("Edit this StudyList"))
                overrideWindow = new CreateStudyListWindow(() -> {
                    overrideWindow = null;
                    // If it was deleted, then be done, or if it was updated, update our cache.
                    String n = studyList.ListName;
                    studyList = StudyList.GetStudyList(n);
                    if(studyList == null)
                        onCallback.run();
                }, studyList);
            ImGui.separator();
            if(ImGui.button("Flashcards"))
                overrideWindow = new FlashcardStudyWindow(studyList, () -> overrideWindow = null);
            ImGui.sameLine();
            if(ImGui.button("Fill-in"))
                overrideWindow = new FillStudyWindow(studyList, () -> overrideWindow = null);
            ImGui.separator();
            if(ImGui.button("Go Back"))
                onCallback.run();
        }
    }
}
