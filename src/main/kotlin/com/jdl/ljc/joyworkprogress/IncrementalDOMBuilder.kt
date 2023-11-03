package com.jdl.ljc.joyworkprogress

import org.jsoup.Jsoup
import org.jsoup.nodes.DataNode
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.net.URLEncoder

class IncrementalDOMBuilder(html: String?
) {
    private val document = Jsoup.parse(html ?: "")
    private val builder = StringBuilder()

    fun generateCode(): String {
        return """
            ()=>{
                const o=(tag,...attrs) => IncrementalDOM.elementOpen(tag,...attrs.map(decodeURIComponent));
                const t = content => IncrementalDOM.text(decodeURIComponent(content));
                const c = IncrementalDOM.elementClose;
                ${buildDomCode()}
            }
        """.trimIndent()
    }

    private fun buildDomCode(): String {
        return buildCode(document.body())
    }

    private fun buildCode(node: Node): String {
        when (node) {
            is TextNode -> textElement { node.wholeText }
            is DataNode -> textElement { node.wholeData }
            else -> {
                openTag(node)
                for (childNode in node.childNodes()) {
                    buildCode(childNode)
                }
                closeTag(node)
            }

        }
        return builder.toString();
    }

    private fun openTag(node: Node) {
        with(builder) {
            append("o('")
            append(ensureCorrectTag(node.nodeName()))
            append("'")
            for (attribute in node.attributes()) {
                if (node.nodeName().equals("a")) {
                    append(",null,null")
                }
                append(",'")
                append(attribute.key)
                append('\'')
                val value = attribute.value
                @Suppress("SENSELESS_COMPARISON")
                if (value != null) {
                    append(",'")
                    append(encodeArgument(value))
                    append("'")
                }
            }
            append(");")
        }
    }

    private fun closeTag(node: Node) {
        with(builder) {
            append("c('")
            append(ensureCorrectTag(node.nodeName()))
            append("');")
        }
    }

    private fun textElement(getter: () -> String) {
        with(builder) {
            append("t(`")
            append(encodeArgument(getter.invoke()))
            append("`);")
        }
    }

    private fun ensureCorrectTag(name: String): String {
        return when (name) {
            "body" -> "div"
            else -> name
        }
    }

    private fun encodeArgument(argument: String): String {
        return URLEncoder.encode(argument, Charsets.UTF_8.toString()).replace("+", "%20")
    }


}

