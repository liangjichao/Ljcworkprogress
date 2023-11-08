package com.jdl.ljc.joyworkprogress.ui.editor.preview;

import kotlin.text.Charsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class IncrementalDOMBuilder {
    private Document document;
    private StringBuilder builder;

    public IncrementalDOMBuilder(String html) {
        document = Jsoup.parse(html);
        builder = new StringBuilder();

    }

//    public String generateCode() {
//        return String.format("""
//                    ()=>{
//                        const o=(tag,...attrs) => IncrementalDOM.elementOpen(tag,...attrs.map(decodeURIComponent));
//                        const t = content => IncrementalDOM.text(decodeURIComponent(content));
//                        const c = IncrementalDOM.elementClose;
//                        %s
//                    }
//                """, buildDomCode());
//    }

    private String buildDomCode() {
        return buildCode(document.body());
    }

    private String buildCode(Node node) {
        if (node instanceof TextNode) {
            textElement(((TextNode) node).getWholeText());
        } else if (node instanceof DataNode) {
            textElement(((DataNode) node).getWholeData());
        } else {
            openTag(node);
            for (Node childNode : node.childNodes()) {
                buildCode(childNode);
            }
            closeTag(node);
        }
        return builder.toString();
    }

    private void openTag(Node node) {
        builder.append("o('");
        builder.append(ensureCorrectTag(node.nodeName()));
        builder.append("'");
        for (Attribute attribute : node.attributes()) {
            if (node.nodeName().equals("a")) {
                builder.append(",null,null");
            }
            builder.append(",'");
            builder.append(attribute.getKey());
            builder.append('\'');
            String value = attribute.getValue();
            if (value != null) {
                builder.append(",'");
                builder.append(encodeArgument(value));
                builder.append("'");
            }
        }

        builder.append(");");
    }

    private void closeTag(Node node) {
        builder.append("c('");
        builder.append(ensureCorrectTag(node.nodeName()));
        builder.append("');");
    }

    private String ensureCorrectTag(String name) {
        if (name.equals("body")) {
            return "div";
        }
        return name;
    }

    private void textElement(String text) {
        builder.append("t(`");
        builder.append(encodeArgument(text));
        builder.append("`);");
    }

    private String encodeArgument(String argument) {
        try {
            return URLEncoder.encode(argument, Charsets.UTF_8.toString()).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
