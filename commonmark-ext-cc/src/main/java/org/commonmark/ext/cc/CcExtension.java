package org.commonmark.ext.cc;

import org.commonmark.Extension;
import org.commonmark.ext.cc.internal.CcCodeBlockParser;
import org.commonmark.ext.cc.internal.CcHtmlNodeRenderer;
import org.commonmark.ext.cc.internal.CcInlineCodeParser;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Extension for control-code-delimited code blocks.
 */
public class CcExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    private CcExtension() {
    }

    public static Extension create() {
        return new CcExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new CcCodeBlockParser.Factory());
        parserBuilder.customInlineContentParserFactory(new CcInlineCodeParser.Factory());
    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.nodeRendererFactory(CcHtmlNodeRenderer::new);
    }
}
