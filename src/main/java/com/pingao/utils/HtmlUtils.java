package com.pingao.utils;

import com.pingao.Main;
import com.pingao.enums.MiMeType;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by pingao on 2018/7/13.
 */
public class HtmlUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlUtils.class);

    private static final List<Extension> EXTENSIONS =
        Arrays.asList(TablesExtension.create(), YamlFrontMatterExtension.create());
    private static final Parser PARSER = Parser.builder().extensions(EXTENSIONS).build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTENSIONS).build();

    private static final String MARKER = "_markdown_preview_sync_bottom_marker";

    private static final String MARKER_HTML = "<a href='#' id='" + MARKER + "'></a>";

    public static final String TEST_MD = "---\n"
                                         + "title: 统计中的p值\n"
                                         + "date: 2017-11-14 10:39:23\n"
                                         + "categories: 技术人生\n"
                                         + "tags: 统计\n"
                                         + "---\n"
                                         + "\n"
                                         + "在我看来，假设检验从本质上是一种反证法。当你想证明一样事物是对的，有时候不太好证明，因为一件你以为对的东西可能只是因为你还没发现它错的一面，相反你想证明一件事物是错的就容易多了。在假设检验中，证明备择假设H1存在困难，我们就去证明它的反面原假设H0。\n"
                                         + "\n"
                                         + "<!-- more -->\n"
                                         + "\n"
                                         + "p值一直是一个令人迷惑的地方，p值实际上是当H0假设为真，一些极端情况出现的概率。即\n"
                                         + "$$\n"
                                         + "p值 = \\{极端情况概率|H0\\}\n"
                                         + "$$\n"
                                         + "\n"
                                         + "那么极端情况是什么呢？在H0的前提下，假设样本均值符合正态分布，我们都知道偏离均值3个均方差的概率几乎为0，但是这种情况还是出现了，我们就有理由判断前提条件错了，即H0是错的，由此我们拒绝H0。\n"
                                         + "\n"
                                         + "上面提到在正态分布的情况下，偏离均值3个均方差的概率几乎为0，但毕竟不是0，事实上约为0.27%。虽然概率很小，但是还是有一定的可能性会拒绝本是正确的H0，这个犯错概率称为第一类错误，也称为显著性水平$\\alpha$。\n"
                                         + "\n"
                                         + "那么，这个显著性水平$\\alpha$和p值有什么关系呢？在我看来就是拒绝一个真H0所允许的最大错误概率，也就是这种极端情况出现的最高概率，当p小于等于$\\alpha$时我们拒绝H0，否则不能拒绝H0。\n"
                                         + "\n"
                                         + "在假设检验中，通常的流程为：\n"
                                         + "1. 提出原假设和备择假设。\n"
                                         + "2. 指定显著性水平$\\alpha$，通常取0.01或0.05。\n"
                                         + "3. 搜集样本数据计算检验统计量的值。\n"
                                         + "4. 利用检验统计量的值计算p值。\n"
                                         + "5. 如果p值<=$\\alpha$，则拒绝H0，否则不能拒绝H0。\n";

    public static String TEST_MD_LONG = "---\n"
                                        + "title: Java的equals和hashCode方法浅谈\n"
                                        + "date: 2018-07-02 16:20:39\n"
                                        + "categories: 技术人生\n"
                                        + "tags: Java\n"
                                        + "---\n"
                                        + "### 一、概述\n"
                                        + "\n"
                                        + "`equals`和`hashCode`作为Java基础经常在面试中提到，比如下面几个问题：\n"
                                        + "\n"
                                        + "1. `equals`和`==`有什么区别？\n"
                                        + "2. `equals`和`hashCode`有什么关系？\n"
                                        + "3. `equals`和`hashCode`如何编写？\n"
                                        + "\n"
                                        + "对于第一个问题不少人只停留在字符串`equals`比较的是内容，`==`比较的是内存地址，而对`equals`的本质极少过问。第二个问题，大多数都知道答案，也有不少记反了，但是更进一步为什么是那样的关系，就不知道了。对于第三个问题，大部分人一上手就把方法签名写错了，就别谈正确的写出实现了。带着这些问题，接下来谈谈自己的一点理解。\n"
                                        + "\n"
                                        + "<!--more-->\n"
                                        + "\n"
                                        + "### 二、equals方法\n"
                                        + "\n"
                                        + "先来看见`equals`方法的签名，\n"
                                        + "```java\n"
                                        + "public boolean equals(Object obj) {\n"
                                        + "  return (this == obj);\n"
                                        + "}\n"
                                        + "```\n"
                                        + "\n"
                                        + "可以看到入参是`Object`，很多人没有注意到这一点，上来就写错了。equals方法顾名思义就判断对象的相等性，默认实现就是`==`，那么说到二者的区别，个人理解，`equals`方法是一种用户定义的“逻辑等”，而`==`是一种“物理等”，用俗语解释就是，`equals`判断是否相同，`==`判断是否一样。\n"
                                        + "\n"
                                        + "`equals`方法在编写的时候需要遵循以下原则：\n"
                                        + "\n"
                                        + "- 自反性\n"
                                        + "- 对称性\n"
                                        + "- 传递性\n"
                                        + "- 一致性\n"
                                        + "\n"
                                        + "下面展开说一下，\n"
                                        + "\n"
                                        + "1. 自反性的意思是，对于一个非`null`的对象x，`x.equals(x)`一定为`true`，这是显而易见的，无须赘述。\n"
                                        + "2. 对称性，对于非`null`对象x、y，`x.equals(y) == true`，当且仅当`y.equals(x) == true`。来看一个来自《Effective Java》的例子，\n"
                                        + "\n"
                                        + "    ```java\n"
                                        + "    // Broken - violates symmetry!\n"
                                        + "    public final class CaseInsensitiveString {\n"
                                        + "      private final String s;\n"
                                        + "\n"
                                        + "      public CaseInsensitiveString(String s) {\n"
                                        + "        this.s = Objects.requireNonNull(s);\n"
                                        + "      }\n"
                                        + "\n"
                                        + "      // Broken - violates symmetry!\n"
                                        + "      @Override public boolean equals(Object o) {\n"
                                        + "        if (o instanceof CaseInsensitiveString)\n"
                                        + "          return s.equalsIgnoreCase(\n"
                                        + "              ((CaseInsensitiveString) o).s);\n"
                                        + "        if (o instanceof String)  // One-way interoperability!\n"
                                        + "          return s.equalsIgnoreCase((String) o);\n"
                                        + "        return false;\n"
                                        + "      }\n"
                                        + "      ...  // Remainder omitted\n"
                                        + "    }\n"
                                        + "    CaseInsensitiveString cis = new CaseInsensitiveString(\"Polish\");\n"
                                        + "    String s = \"polish\";\n"
                                        + "    List<CaseInsensitiveString> list = new ArrayList<>();\n"
                                        + "    list.add(cis);\n"
                                        + "    // true or false\n"
                                        + "    list.contains(s);\n"
                                        + "    ```\n"
                                        + "    在JDK8运行`list.contains(s)`返回`false`，但是有的JDK可能会返回`true`，甚至直接崩溃，所以如果违反了对称性，程序的行为是不可预测的。\n"
                                        + "\n"
                                        + "3. 传递性，对于非`null`对象x、y、z，如果`x.equals(y) == true`且`y.equals(z) == true`，那么`x.equals(z) == true`。同样是来自《Effective Java》的一个例子，\n"
                                        + "\n"
                                        + "    ```java\n"
                                        + "    public class Point {\n"
                                        + "      private final int x;\n"
                                        + "      private final int y;\n"
                                        + "\n"
                                        + "      public Point(int x, int y) {\n"
                                        + "        this.x = x;\n"
                                        + "        this.y = y;\n"
                                        + "      }\n"
                                        + "\n"
                                        + "      @Override public boolean equals(Object o) {\n"
                                        + "        if (!(o instanceof Point))\n"
                                        + "          return false;\n"
                                        + "        Point p = (Point)o;\n"
                                        + "        return p.x == x && p.y == y;\n"
                                        + "      }\n"
                                        + "\n"
                                        + "      ...  // Remainder omitted\n"
                                        + "    }\n"
                                        + "\n"
                                        + "    public class ColorPoint extends Point {\n"
                                        + "      private final Color color;\n"
                                        + "\n"
                                        + "      public ColorPoint(int x, int y, Color color) {\n"
                                        + "        super(x, y);\n"
                                        + "        this.color = color;\n"
                                        + "      }\n"
                                        + "\n"
                                        + "      // Broken - violates transitivity!\n"
                                        + "      @Override public boolean equals(Object o) {\n"
                                        + "        if (!(o instanceof Point))\n"
                                        + "          return false;\n"
                                        + "\n"
                                        + "        // If o is a normal Point, do a color-blind comparison\n"
                                        + "        if (!(o instanceof ColorPoint))\n"
                                        + "          return o.equals(this);\n"
                                        + "\n"
                                        + "        // o is a ColorPoint; do a full comparison\n"
                                        + "        return super.equals(o) && ((ColorPoint) o).color == color;\n"
                                        + "      }\n"
                                        + "\n"
                                        + "      ...  // Remainder omitted\n"
                                        + "    }\n"
                                        + "\n"
                                        + "    ColorPoint p1 = new ColorPoint(1, 2, Color.RED);\n"
                                        + "    Point p2 = new Point(1, 2);\n"
                                        + "    ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);\n"
                                        + "    ```\n"
                                        + "    显然`ColorPoint`的`equals`实现违反了传递性，`p1.equals(p2) == p2.equals(p3) != p1.equals(p3)`。假如`Point`有两个子类`ColorPoint`和`SmellPoint`，`colorPoint.equals(smellPoint)`将会导致无限递归，最终导致内存耗尽。引用《Effective Java》的说法，\n"
                                        + "\n"
                                        + "    > There is no way to extend an instantiable class and add a value component while preserving the equals contract, unless you’re willing to forgo the benefits of object-oriented abstraction.\n"
                                        + "\n"
                                        + "    这句话的大意是如果你继承扩展一个类，就没法再保持`equals`的原则了，除非放弃使用继承。放弃继承？这不是让我们因噎废食嘛，咦，别说，还真能放弃继承，那就是组合，因为本文的重点是`equals`和`hashCode`就不展开了。\n"
                                        + "\n"
                                        + "4. 一致性，对于非`null`对象x、y，多次调用`x.equals(y)`返回一致。一致性意味着`equals`方法不要依赖不可靠的变量，这里“可靠”的意思不光意味着“不该变时不变”，还意味着“想获取时能获取到”，比如`java.net.URL`的`equals`实现依赖了ip地址，而网络故障时无法获取ip，这是一个不好的实现。\n"
                                        + "\n"
                                        + "说了那么多，有人可能会说，哎呀这么多原则顾头不顾尾，都要满足，太难了吧，下面列出实现`equals`的一些tips，照着做实现起来就易如反掌，\n"
                                        + "\n"
                                        + "1. 使用`==`判断是否为`null`或`this`，如果是前者返回`false`，后者就返回`true`。\n"
                                        + "2. 使用`instanceof`检测是否是正确的类型，如果不是直接返回`false`，如果是，强制转换为正确的类型，然后比较与“逻辑等”相关的变量。\n"
                                        + "\n"
                                        + "### 三、hashCode方法\n"
                                        + "\n"
                                        + "`hashCode`主要用来在Java中哈希数据结构`HashMap`、`HashSet`生成哈希值，`hashCode`的方法签名，\n"
                                        + "\n"
                                        + "```java\n"
                                        + "public native int hashCode();\n"
                                        + "```\n"
                                        + "\n"
                                        + "默认实现会将对象的内存地址转化为一个整数，因此只有同一个对象`hashCode`才一样，即使两个`equals`返回`true`的对象`hashCode`也不一样，如果不进行重写。和`equals`一样，`hashCode`也需要满足一些原则：\n"
                                        + "\n"
                                        + "1. 一致性，和`equals`相关的变量没有变化，`hashCode`返回值也不能变化。\n"
                                        + "\n"
                                        + "2. 两个对象`equals`返回`true`，`hashCode`返回值应该相等。由上面得知，`hashCode`默认实现不满足这一条件，因此任何类如果实现了`equals`就必须实现`hashCode`，确保二者的步调一致，下面来看一个反例，\n"
                                        + "\n"
                                        + "    ```java\n"
                                        + "    public class Person {\n"
                                        + "      private int age;\n"
                                        + "      private String name;\n"
                                        + "\n"
                                        + "      public Person(int age, String name) {\n"
                                        + "        this.age = age;\n"
                                        + "        this.name = name;\n"
                                        + "      }\n"
                                        + "\n"
                                        + "      @Override\n"
                                        + "        public boolean equals(Object obj) {\n"
                                        + "          if (obj == null) {\n"
                                        + "            return false;\n"
                                        + "          }\n"
                                        + "          if (obj == this) {\n"
                                        + "            return true;\n"
                                        + "          }\n"
                                        + "\n"
                                        + "          if (obj instanceof Person) {\n"
                                        + "            Person that = (Person) obj;\n"
                                        + "            return age == that.age && Objects.equals(name, that.name);\n"
                                        + "          }\n"
                                        + "\n"
                                        + "          return false;\n"
                                        + "        }\n"
                                        + "    }\n"
                                        + "\n"
                                        + "    Map<Person, Integer> map = new HashMap<>();\n"
                                        + "    map.put(new Person(10, \"小明\"), 1);\n"
                                        + "    map.get(new Person(10, \"小明\"));\n"
                                        + "    ```\n"
                                        + "\n"
                                        + "    初学者可能觉得最后一条语句会返回1，事实上返回的是`null`，为什么会这样呢？明明将数据放进去了，而数据却像被黑洞吞噬一样，要解释得从`HashMap`的数据结构说起，`HashMap`是由数组和链表组成的一种组合结构，如下图，往里存放时，`hashCode`决定数组的下标，而`equals`用于查找值是否已存在，存在的话替换，否则插入；往外取时，先用`hashCode`找到对应数组下标，然后用`equals`挨个比较直到链表的尾部，找到返回相应值，找不到返回null。再回过头看刚才的问题，先放进去一个`new Person(10, \"小明\")`，然后取的时候又新建了一个`new Person(10, \"小明\")`，由于没有重写`hashCode`，这两个对象的`hashCode`是不一样的，存和取的数组下标也就不一样，自然取不出来了。\n"
                                        + "\n"
                                        + "    ![HashMap数据结构](http://ozgrgjwvp.bkt.clouddn.com/Java%E7%9A%84equals%E5%92%8ChashCode%E6%96%B9%E6%B3%95%E6%B5%85%E8%B0%88/hashmap.png)\n"
                                        + "\n"
                                        + "3. 两个对象`equals`返回`false`，`hashCode`返回值可以相等，但是如果不等的话，可以改进哈希数据结构的性能。这条原则也可以用`HashMap`的数据结构解释，举一个极端的例子，假如`Person`所有对象的`hashCode`都一样，那么`HashMap`内部数组的下标都一样，数据就会进到同一张链表里，这张链表比正常情况下要长的多，而遍历链表是一项耗时的工作，性能也就下来了。\n"
                                        + "\n"
                                        + "那么如何写一个好的`hashCode`呢？\n"
                                        + "\n"
                                        + "1. 声明一个变量`int`的变量`result`，将第一个和`equals`相关的实例变量的`hashCode`赋值给它。\n"
                                        + "2. 然后按照下列规则依次计算剩下的实例变量的`hashCode`值`c`。\n"
                                        + "    1. 如果是null，设置一个常数，通常为0\n"
                                        + "    2. 如果是原始类型，使用`Type.hashCode(f)`，`Type`为它们的装箱类型\n"
                                        + "    3. 如果是数组，如果每一个元素都是相关的，可以使用`Arrays.hashCode`；否则将相关元素看作独立的变量计算\n"
                                        + "3. 使用`result = 31 * result + c`的形式将每个变量的哈希值组合起来，最后返回`result`。\n"
                                        + "\n"
                                        + "参考资料：《Effective Java》\n";

    private HtmlUtils() {
    }

    public static Map<String, List<String>> getParameters(HttpRequest request) {
        return new QueryStringDecoder(request.uri()).parameters();
    }

    public static String getRequestPath(String uri) {
        return uri.contains("?") ? uri.substring(0, uri.indexOf('?')) : uri;
    }

    public static MiMeType getMiMeTypeOfStaticFile(String path) {
        if (path.endsWith(".js")) {
            return MiMeType.JAVASSCRIPT;
        } else if (path.endsWith("css")) {
            return MiMeType.CSS;
        } else {
            return MiMeType.PLAIN;
        }
    }

    public static String readIndexHtml(String theme) {
        String html = readAsString(Main.ROOT_PATH + "/webapp/index.html");
        int index = html.indexOf("${theme}");
        return html.substring(0, index) + theme + html.substring(index + "${theme}".length());
    }

    public static String readAsString(String path) {
        try {
            return Files.lines(Paths.get(path), Charset.forName("UTF-8")).collect(Collectors.joining("\n"));
        } catch (Exception e) {
            LOGGER.error("Error occurs on reading content of {}", path, e);
        }
        return "";
    }

    public static String markdown2Html(List<String> lines, int bottom) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int nu = i + 1;
            sb.append(line);

            if (nu < lines.size()) {
                if (nu == bottom) {
                    if ("".equals(line.trim()) || isCommentOrBlock(line)) {
                        bottom++;
                    } else {
                        sb.append(MARKER);
                    }
                }
                sb.append('\n');
            } else {
                if (bottom >= lines.size()) {
                    sb.append(isCommentOrBlock(line) ? '\n' + MARKER : MARKER);
                }
            }
        }

        Node document = PARSER.parse(sb.toString());
        String html = RENDERER.render(document);
        int index = html.indexOf(MARKER);
        if (index >= 0) {
            return html.substring(0, index) + MARKER_HTML + html.substring(index + MARKER.length());
        } else {
            return html;
        }
    }

    private static boolean isCommentOrBlock(String line) {
        String l = line.trim();
        return l.startsWith("<!--")
               || l.startsWith("```")
               || l.startsWith("$$");
    }

    public static List<String> split2Lines(String content) {
        List<String> lines = new ArrayList<>();
        for (int i = 0, start = 0; i < content.length(); i++) {
            if (i == content.length() - 1) {
                if (content.charAt(i) == '\n') {
                    lines.add(content.substring(start, i));
                } else {
                    lines.add(content.substring(start, i + 1));
                }
                //
            } else {
                if (content.charAt(i) == '\n') {
                    if (start == i) {
                        lines.add("");
                    } else {
                        lines.add(content.substring(start, i));
                    }

                    start = i + 1;
                }
            }
        }
        return lines;
    }
}
