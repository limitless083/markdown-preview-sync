import com.google.gson.Gson;
import com.pingao.enums.Operate;
import com.pingao.model.MarkDownUnit;
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
        //System.out.println(HtmlUtils.split2Lines(""));
        //System.out.println(HtmlUtils.split2Lines("\n"));
        //System.out.println(HtmlUtils.split2Lines("    \n"));
        //System.out.println(HtmlUtils.split2Lines("    \n\n"));
        //System.out.println(HtmlUtils.split2Lines("    \naaa"));
        //System.out.println(HtmlUtils.split2Lines("    \naaa\nbb"));
        //System.out.println(HtmlUtils.split2Lines("    \naaa\n\nbb"));
        //System.out.println(HtmlUtils.buildIndexHtml());
        String md = "作为Vim和Markdown的双重粉丝一枚，一直在寻找一款好用的预览插件，功能不用太多，能在书写之余偶尔撇一下效果即可。偶然看到了Chrome一款插件[Markdown Viewer](https://chrome.google.com/webstore/detail/markdown-viewer/ckkdlimhmcjmikdlpkmbgfkaikojcbjk)，风格非常喜欢，遗憾的是不能与Vim同步，后来又发现一款Vim插件[markdown-preview.vim](https://github.com/iamcco/markdown-preview.vim)，但是我装了好几次，在我的机器上老是报错。碰巧会一点Vimscript，加之自己是爪哇岛岛民，干脆就用Java写一个吧。\n"
                    + "\n"
                    + "### 特性\n"
                    + "\n"
                    + "- 代码高亮\n"
                    + "- MathJax\n"
                    + "\n"
                    + "### 安装准备\n"
                    + "\n"
                    + "- Jre8及以上\n"
                    + "- Python的[py4j](https://www.py4j.org/)库\n"
                    + "\n"
                    + "### 安装方式\n"
                    + "\n"
                    + "#### pathogen插件安装\n"
                    + "\n"
                    + "```\n"
                    + "git clone git@github.com:pingao777/markdown-preview-sync.git\n"
                    + "```\n";
        String html = HtmlUtils.markdown2Html(HtmlUtils.split2Lines(md), 19);
        System.out.println(html);
        //Document document = Jsoup.parse(html);
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
