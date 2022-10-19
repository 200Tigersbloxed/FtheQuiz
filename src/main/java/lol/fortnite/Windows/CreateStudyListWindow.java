package lol.fortnite.Windows;

import imgui.ImGui;
import imgui.type.ImString;
import lol.fortnite.Study.StudyList;
import lol.fortnite.Study.StudyTerm;

import java.io.File;
import java.util.ArrayList;

public class CreateStudyListWindow implements IWindow{
    private ImString studySetName = new ImString();

    private ArrayList<StudyTerm> studyTerms = new ArrayList<>();
    private ArrayList<StudyTerm> termsToRemove = new ArrayList<>();

    private Runnable onDone;
    private StudyList studyList;
    private boolean editMode = false;

    @Override
    public String getWindowName() {
        if(editMode)
            return "Edit " + studyList.ListName;
        return "Create New StudyList";
    }

    public CreateStudyListWindow(Runnable onDone){
        this.onDone = onDone;
        studySetName.set("My cool study set!");
    }

    public CreateStudyListWindow(Runnable onDone, StudyList edit){
        this.onDone = onDone;
        for(StudyTerm studyTerm : edit.GetStudyTerms()){
            studyTerms.add(studyTerm);
        }
        studySetName.set(edit.ListName);
        studyList = edit;
        editMode = true;
    }

    @Override
    public void DrawWindow(int width, int height) {
        if(!editMode){
            ImGui.text("Enter a name for your StudySet");
            ImGui.inputText("StudySet Name", studySetName);
            ImGui.separator();
        }
        ImGui.text("Study Terms");
        if(ImGui.button("Create StudyTerm")){
            StudyTerm studyTerm = new StudyTerm("StudyTerm" + studyTerms.size(), "");
            studyTerms.add(studyTerm);
        }
        for(StudyTerm term : termsToRemove)
            studyTerms.remove(term);
        termsToRemove.removeAll(termsToRemove);
        ImGui.beginChild("CreateStudyListWindowChild1", width, height - 150);
        for (StudyTerm studyTerm : studyTerms){
            if(ImGui.collapsingHeader(studyTerm.Term)){
                ImGui.inputText("Term", studyTerm.copyTerm);
                ImGui.inputText("Definition", studyTerm.copyDefinition);
                if(ImGui.button("Apply")){
                    studyTerm.Term = studyTerm.copyTerm.get();
                    studyTerm.Definition = studyTerm.copyDefinition.get();
                }
                ImGui.sameLine();
                if(ImGui.button("Remove")){
                    termsToRemove.add(studyTerm);
                }
            }
        }
        ImGui.endChild();
        ImGui.separator();
        String bt = "Create";
        if(editMode)
            bt = "Save";
        if(ImGui.button(bt)){
            if(editMode){
                StudyList sl = StudyList.GetStudyList(studyList.ListName);
                StudyList.RemoveStudyList(sl);
            }
            StudyList studyList = new StudyList(studySetName.get());
            for (StudyTerm studyTerm : studyTerms){
                studyList.AddTerm(studyTerm);
            }
            StudyList.AddStudyList(studyList);
            onDone.run();
        }
        if(editMode){
            ImGui.sameLine();
            if(ImGui.button("Delete")){
                File f = new File(studyList.ConfigLocation);
                f.delete();
                StudyList.RemoveStudyList(studyList);
                onDone.run();
            }
        }
        ImGui.sameLine();
        if(ImGui.button("Cancel")){
            onDone.run();
        }
    }
}
