package lol.fortnite;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import lol.fortnite.Study.StudyList;
import lol.fortnite.Windows.CreateStudyListWindow;
import lol.fortnite.Windows.IWindow;
import lol.fortnite.Windows.ManageStudyListWindow;

public class MainUI extends Application {
    private Configuration config;

    private IWindow currentMainWindow;

    @Override
    protected void configure(final Configuration config){
        this.config = config;
        config.setTitle("FtheQuiz");
        config.setHeight(800);
        config.setWidth(1000);
    }

    @Override
    public void process(){
        ImGui.setNextWindowPos(0, 0);
        ImGui.setNextWindowSize(200, 800);
        ImGui.setNextWindowSizeConstraints(200, 800, 200, 800);
        ImGui.begin("Lists");
        if(ImGui.button("Create StudyList")){
            currentMainWindow = new CreateStudyListWindow(() -> {
                currentMainWindow = null;
                StudyList.LoadStudyLists();
            });
        }
        for (StudyList studyList : StudyList.StudyLists){
            if (ImGui.button(studyList.ListName)){
                currentMainWindow = new ManageStudyListWindow(studyList, () -> {
                    currentMainWindow = null;
                    StudyList.LoadStudyLists();
                });
            }
        }
        ImGui.end();
        if(currentMainWindow != null){
            ImGui.setNextWindowPos(200, 0);
            ImGui.setNextWindowSize(800, 800);
            ImGui.setNextWindowSizeConstraints(200, 0, 800, 800);
            ImGui.begin(currentMainWindow.getWindowName());
            currentMainWindow.DrawWindow(800, 800);
            ImGui.end();
        }
    }

    public static void main(final String[] args){
        StudyList.LoadStudyLists();
        launch(new MainUI());
    }
}
