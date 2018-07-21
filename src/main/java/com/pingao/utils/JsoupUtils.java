package com.pingao.utils;

import com.pingao.enums.Operate;
import com.pingao.model.MarkDownUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by pingao on 2018/7/21.
 */
public class JsoupUtils {
    private static final Map<String, List<String>> CACHE = new HashMap<>();

    // Prevent instance
    public JsoupUtils() {
    }

    public static List<MarkDownUnit> diff(String path, String html) {
        List<String> old = CACHE.get(path);

        Document document = Jsoup.parse(html);
        Elements elements = document.body().children();

        List<MarkDownUnit> units = new ArrayList<>(elements.size());

        if (old == null) {
            List<String> fresh = new ArrayList<>(elements.size());
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                String id = "element-" + i;
                element.attr("id", id);
                String content = element.outerHtml();
                fresh.add(content);
                units.add(new MarkDownUnit(id, Operate.APPEND, content, isMathJax(content)));
            }
            CACHE.put(path, fresh);
        } else {
            int min = Math.min(old.size(), elements.size());
            int max = Math.max(old.size(), elements.size());

            for (int i = 0; i < min; i++) {
                Element element = elements.get(i);
                String id = "element-" + i;
                element.attr("id", id);
                String content = element.outerHtml();
                if (!old.get(i).equals(content)) {
                    old.set(i, content);
                    units.add(new MarkDownUnit(id, Operate.REPLACE, content, isMathJax(content)));
                }
            }

            if (old.size() > elements.size()) {
                for (int i = max - 1; i >= min; i--) {
                    old.remove(i);
                    String id = "element-" + i;
                    units.add(new MarkDownUnit(id, Operate.REMOVE, "", 0));
                }
            } else {
                for (int i = min; i < max; i++) {
                    Element element = elements.get(i);
                    String id = "element-" + i;
                    element.attr("id", id);
                    String content = element.outerHtml();
                    old.add(content);
                    units.add(new MarkDownUnit(id, Operate.APPEND, content, isMathJax(content)));
                }
            }
        }

        return units;
    }

    private static int isMathJax(String content) {
        int first = content.indexOf('$');
        return first > -1 && first != content.lastIndexOf('$') ? 1 : 0;
    }
}
