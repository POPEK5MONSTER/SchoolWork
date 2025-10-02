/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class Anagrams {

    private Map<String, List<String>> anagramMap;
    public Anagrams(String fileName) {
        anagramMap = new HashMap<>();
        try{
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNext()){
                String word = sc.next();
                String sortedWord = sortString(word);
                anagramMap.computeIfAbsent(sortedWord, key -> new ArrayList<>()).add(word);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<List<String>> getSortedByAnQty(){
        List<List<String>> result = new ArrayList<>(anagramMap.values());
        result.sort((list1, list2) -> {
            if (list1.size() == list2.size()){
                return list1.get(0).compareTo(list2.get(0));
            } else {
                return list2.size() - list1.size();
            }
        });
        return result;
    }

    public String  getAnagramsFor(String word){
        String sortedWord = sortString(word);
        List<String> anagrams = anagramMap.get(sortedWord);
        anagrams.remove(word);
        return word + ": " + anagrams;
    }

    private String sortString(String input){
        char[] charArray = input.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }
}
