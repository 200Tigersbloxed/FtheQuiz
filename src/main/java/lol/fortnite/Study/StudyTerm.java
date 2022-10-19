package lol.fortnite.Study;

import imgui.type.ImString;

public class StudyTerm {
    public String Term = "";
    public String Definition = "";

    public ImString copyTerm = new ImString();
    public ImString copyDefinition = new ImString();

    public StudyTerm(String Term, String Definition){
        this.Term = Term;
        copyTerm.set(Term);
        this.Definition = Definition;
        copyDefinition.set(Definition);
    }
}