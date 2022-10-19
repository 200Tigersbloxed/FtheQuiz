package lol.fortnite.util;

import java.util.ArrayList;
import java.util.Iterator;

public class Dictionary<T1, T2> implements Iterable<Dictionary.DictionaryEntry> {
    private final ArrayList<DictionaryEntry> Cache = new ArrayList<>();

    public Dictionary(){}

    public Iterator<Dictionary.DictionaryEntry> iterator(){
        return new ArrayList<Dictionary.DictionaryEntry>(Cache).iterator();
    }

    public int getSize(){return Cache.size();}

    public void add(T1 key, T2 value){
        DictionaryEntry dc = new DictionaryEntry(key, value);
        Cache.add(dc);
    }
    public void remove(T1 key){Cache.removeIf(dc -> dc.Key == key);}
    public void remove(int i){Cache.remove(i);}

    public boolean doesKeyExist(T1 key){
        for (DictionaryEntry dictionaryEntry : Cache){
            if(dictionaryEntry.Key == key)
                return true;
        }
        return false;
    }

    public ArrayList<T1> keysToArrayList(){
        ArrayList<T1> keys = new ArrayList<>();
        for (DictionaryEntry dictionaryEntry : Cache){
            keys.add(dictionaryEntry.Key);
        }
        return keys;
    }

    public ArrayList<T2> valuesToArrayList(){
        ArrayList<T2> values = new ArrayList<>();
        for (DictionaryEntry dictionaryEntry : Cache){
            values.add(dictionaryEntry.Value);
        }
        return values;
    }

    public class DictionaryEntry {
        public T1 Key;
        public T2 Value;

        public DictionaryEntry(T1 Key, T2 Value){
            this.Key = Key;
            this.Value = Value;
        }
    }
}
