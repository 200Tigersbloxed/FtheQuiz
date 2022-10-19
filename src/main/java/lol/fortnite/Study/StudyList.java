package lol.fortnite.Study;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lol.fortnite.Configuration.Config;
import lol.fortnite.util.Dictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class StudyList extends Config {
    public static StudyList[] StudyLists = new StudyList[0];

    private static String removeExtention(String filePath) {
        // These first few lines the same as Justin's
        File f = new File(filePath);

        // if it's a directory, don't remove the extension
        if (f.isDirectory()) return filePath;

        String name = f.getName();

        // Now we know it's a file - don't need to do any special hidden
        // checking or contains() checking because of:
        final int lastPeriodPos = name.lastIndexOf('.');
        if (lastPeriodPos <= 0)
        {
            // No period after first character - return name as it was passed in
            return filePath;
        }
        else
        {
            // Remove the last period and everything after it
            File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
            return renamed.getPath();
        }
    }

    public static void LoadStudyLists(){
        File fObj = new File("Lists");
        File[] files = fObj.listFiles();
        if(files == null)
            return;
        ArrayList<StudyList> studyLists = new ArrayList<>();
        for (File f : files){
            StudyList studyList = new StudyList(removeExtention(f.getName()));
            studyList.Deserialize();
            studyLists.add(studyList);
        }
        StudyLists = new StudyList[studyLists.size()];
        StudyLists = studyLists.toArray(StudyLists);
    }

    public static void AddStudyList(StudyList studyList){
        ArrayList<StudyList> studyLists = new ArrayList<>();
        for (StudyList studyList1 : StudyLists)
            studyLists.add(studyList1);
        studyLists.add(studyList);
        studyList.SaveToDisk();
        StudyLists = new StudyList[studyLists.size()];
        StudyLists = studyLists.toArray(StudyLists);
    }

    public static void RemoveStudyList(StudyList studyList){
        ArrayList<StudyList> studyLists = new ArrayList<>();
        for (StudyList studyList1 : StudyLists)
            if(studyList1 != studyList)
                studyLists.add(studyList1);
        StudyLists = new StudyList[studyLists.size()];
        StudyLists = studyLists.toArray(StudyLists);
    }

    public static StudyList GetStudyList(String name){
        for (StudyList studyList : StudyLists)
            if(Objects.equals(studyList.ListName, name))
                return studyList;
        return null;
    }

    @SerializedName("ListName")
    public String ListName = "";
    @SerializedName("Keys")
    public String[] Keys = new String[0];
    @SerializedName("Values")
    public String[] Values = new String[0];

    public StudyList(String FileListName) {
        super("Lists/" + FileListName + ".json");
        ListName = FileListName;
    }

    @Expose(serialize = false)
    private transient final Dictionary<String, String> StudyTerms = new Dictionary<>();

    public void AddTerm(String term, String definition){
        if(!StudyTerms.doesKeyExist(term.toLowerCase(Locale.ROOT)))
            StudyTerms.add(term, definition);
    }
    public void AddTerm(StudyTerm studyTerm){
        if(!StudyTerms.doesKeyExist(studyTerm.Term.toLowerCase(Locale.ROOT)))
            StudyTerms.add(studyTerm.Term, studyTerm.Definition);
    }
    public void RemoveTerm(int i){StudyTerms.remove(i);}

    public StudyTerm[] GetStudyTerms(){
        ArrayList<StudyTerm> studyTerms = new ArrayList<>();
        for (Dictionary.DictionaryEntry dictionaryEntry : StudyTerms){
            StudyTerm studyTerm = new StudyTerm((String) dictionaryEntry.Key, (String) dictionaryEntry.Value);
            studyTerms.add(studyTerm);
        }
        StudyTerm[] terms = new StudyTerm[studyTerms.size()];
        terms = studyTerms.toArray(terms);
        return terms;
    }

    public StudyTerm GetStudyTerm(int i){
        StudyTerm[] studyTerms = GetStudyTerms();
        if(studyTerms.length > i)
            return studyTerms[i];
        return null;
    }

    @Override
    public String Serialize() {
        Keys = new String[StudyTerms.getSize()];
        Keys = StudyTerms.keysToArrayList().toArray(Keys);
        Values = new String[StudyTerms.getSize()];
        Values = StudyTerms.valuesToArrayList().toArray(Values);
        return super.Serialize();
    }

    @Override
    public void OnDeserialize() {
        int i = 0;
        for (String key : Keys){
            String value = Values[i];
            StudyTerms.add(key, value);
            i++;
        }
    }
}
