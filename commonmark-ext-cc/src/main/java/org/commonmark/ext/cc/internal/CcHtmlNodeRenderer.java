package org.commonmark.ext.cc.internal;

import org.commonmark.ext.cc.CcCodeBlock;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Map;
import java.util.Set;

public class CcHtmlNodeRenderer implements NodeRenderer {

    private final HtmlNodeRendererContext context;
    private final HtmlWriter html;

    public CcHtmlNodeRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.html = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Set.of(CcCodeBlock.class);
    }

    @Override
    public void render(Node node) {
        CcCodeBlock ccCodeBlock = (CcCodeBlock) node;
        html.line();
        html.tag("pre", getAttrs(ccCodeBlock, "pre"));
        html.tag("code", getAttrs(ccCodeBlock, "code"));
        html.text(ccCodeBlock.getLiteral());
        html.tag("/code");
        html.tag("/pre");
        html.line();
    }

    private Map<String, String> getAttrs(Node node, String tagName) {
        return context.extendAttributes(node, tagName, Map.of());
    }
}
