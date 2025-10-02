package zad3;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class ProgLang {

    Map<String, Set<String>> langsMap;
    Map<String, Set<String>> progsMap;

    public ProgLang(String fileName) {
        this.langsMap = new LinkedHashMap<>();
        this.progsMap = new LinkedHashMap<>();

        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");
                String lang = parts[0];
                langsMap.put(lang, new LinkedHashSet<>(Arrays.asList(parts).subList(1, parts.length)));
                for (int i = 1; i < parts.length; i++) {
                    String prog = parts[i];
                    progsMap.computeIfAbsent(prog, l -> new LinkedHashSet<>()).add(lang);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Set<String>> getLangsMap() {
        return new LinkedHashMap<>(langsMap);
    }

    public Map<String, Set<String>> getProgsMap() {
        return new LinkedHashMap<>(progsMap);
    }

    public Map<String, Set<String>> getLangsMapSortedByNumOfProgs() {
        return sorted(langsMap, (entry1, entry2) ->
            Integer.compare(entry2.getValue().size(), entry1.getValue().size())
        );
    }

    public Map<String, Set<String>> getProgsMapForNumOfLangsGreaterThan(int i) {
        return filtered(progsMap, entry -> entry.getValue().size() > i);
    }

    public Map<String, Set<String>> getProgsMapSortedByNumOfLangs() {
        return sorted(progsMap, (entry1, entry2) ->{
            if (entry1.getValue().size() != entry2.getValue().size()){
                return Integer.compare(entry2.getValue().size(), entry1.getValue().size());
            } else {
                return entry1.getKey().compareTo(entry2.getKey());
            }
        });
    }

    private static <K, V> Map<K, V> sorted(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        List<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());
        entries.sort(comparator);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static <K, V> Map<K, V> filtered(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream()
                .filter(predicate)
                .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }
}
