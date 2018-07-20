import com.pingao.Main;
import com.pingao.utils.HtmlUtils;
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
        //System.out.println(HtmlUtils.buildContentLines(""));
        //System.out.println(HtmlUtils.buildContentLines("\n"));
        //System.out.println(HtmlUtils.buildContentLines("    \n"));
        //System.out.println(HtmlUtils.buildContentLines("    \n\n"));
        //System.out.println(HtmlUtils.buildContentLines("    \naaa"));
        //System.out.println(HtmlUtils.buildContentLines("    \naaa\nbb"));
        //System.out.println(HtmlUtils.buildContentLines("    \naaa\n\nbb"));
        //System.out.println(HtmlUtils.buildIndexHtml());
        //System.out.println(HtmlUtils.markdown2Html(HtmlUtils.buildContentLines(HtmlUtils.TEST_MD_LONG), 22));
        //System.out.println(Paths.get(ClassLoader.getSystemResource("").getFile()).getParent().toString());
        //System.out.println(ClassLoader.getSystemResource("").getPath().substring(1));
        //System.out.println(HtmlUtils.class.getResource("."));
        //System.out.println(Main.ROOT_PATH);
        //System.out.println(new Gson().toJson(new WebSocketMsg("command", "d:/path", "<a href='aaa'></a>")));
        //System.out.println(new GsonBuilder().disableHtmlEscaping().create().toJson(new WebSocketMsg("command", "d:/path", "<a href='aaa'></a>")));
        //System.out.println(Charset.defaultCharset());
        System.out.println(HtmlUtils.readContentAsString(Main.ROOT_PATH + "/webapp/index.html"));
    }
}
