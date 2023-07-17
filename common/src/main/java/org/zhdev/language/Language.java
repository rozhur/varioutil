package org.zhdev.language;

import org.zhdev.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Language {
    protected final Map<String, PhraseSection> sectionMap = new HashMap<>();

    public String getPhrase(String sectionKey, String phraseKey, Object... params) {
        PhraseSection section = sectionMap.get(sectionKey);
        if (section == null) {
            return sectionKey + ':' + phraseKey;
        }

        String phrase = section.getOrDefault(phraseKey, phraseKey);
        return phrase == null ? sectionKey + ':' + phraseKey : StringUtils.replaceKeyValue(phrase, params);
    }

    public String addPhrase(String sectionKey, String phraseKey, String phrase) {
        PhraseSection section = sectionMap.computeIfAbsent(sectionKey, s -> new PhraseSection());
        return section.put(phraseKey, phrase);
    }

    public String removePhrase(String sectionKey, String phraseKey) {
        PhraseSection section = sectionMap.get(sectionKey);
        return section == null ? null : (section.remove(phraseKey));
    }

    public int size() {
        return sectionMap.size();
    }

    public int size(String sectionKey) {
        PhraseSection section = sectionMap.get(sectionKey);
        return section == null ? 0 : section.size();
    }

    public void clear() {
        sectionMap.clear();
    }

    public boolean clear(String sectionKey) {
        PhraseSection section = sectionMap.get(sectionKey);
        if (section != null) {
            section.clear();
            return true;
        }
        return false;
    }

    private static class PhraseSection extends HashMap<String, String> {}
}
