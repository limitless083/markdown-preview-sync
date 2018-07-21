import com.google.gson.Gson;
import com.pingao.enums.Operate;
import com.pingao.model.MarkDownUnit;
import com.pingao.utils.HtmlUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


/**
 * Created by pingao on 2018/7/19.
 */
public class CommonTest {
    @Test
    public void testCommon() {
        List<String> list = Arrays.asList("a", "b");
        Assert.assertEquals(list.subList(0, 0).size(), 0);
    }

    @Test
    public void testSplitLines() {
    }

    @Test
    public void testHtml() {
        //System.out.println("aa".substring(0, 0).equals(""));
        //System.out.println(HtmlUtils.markdown2Html(Arrays.asList("", "", "  ", ""), 3));
        //System.out.println(HtmlUtils.markdown2Html(Arrays.asList("aaa", "", "  ", "bbb"), 3));
        //System.out.println(HtmlUtils.markdown2Html(Arrays.asList("aaa", "", "  ", ""), 5));
        //System.out.println(HtmlUtils.split2Lines(""));
        //System.out.println(HtmlUtils.split2Lines("\n"));
        //System.out.println(HtmlUtils.split2Lines("    \n"));
        //System.out.println(HtmlUtils.split2Lines("    \n\n"));
        //System.out.println(HtmlUtils.split2Lines("    \naaa"));
        //System.out.println(HtmlUtils.split2Lines("    \naaa\nbb"));
        //System.out.println(HtmlUtils.split2Lines("    \naaa\n\nbb"));
        //System.out.println(HtmlUtils.buildIndexHtml());
        String html = HtmlUtils.markdown2Html(HtmlUtils.split2Lines(HtmlUtils.TEST_MD), 22);
        System.out.println();
        Document document = Jsoup.parse(html);
        //System.out.println(Paths.get(ClassLoader.getSystemResource("").getFile()).getParent().toString());
        //System.out.println(ClassLoader.getSystemResource("").getPath().substring(1));
        //System.out.println(HtmlUtils.class.getResource("."));
        //System.out.println(Main.ROOT_PATH);
        //System.out.println(new Gson().toJson(new WebSocketMsg("command", "d:/path", "<a href='aaa'></a>")));
        //System.out.println(new GsonBuilder().disableHtmlEscaping().create().toJson(new WebSocketMsg("command", "d:/path", "<a href='aaa'></a>")));
        //System.out.println(Charset.defaultCharset());
        //System.out.println(HtmlUtils.readAsString(Main.ROOT_PATH + "/webapp/index.html"));
    }

    @Test
    public void testGson() {
        Assert.assertEquals("{\"id\":\"id\",\"operate\":\"APPEND\",\"content\":\"content\",\"isMathJax\":1}",
                            new Gson().toJson(new MarkDownUnit("id", Operate.APPEND, "content", 1)));
    }
}
