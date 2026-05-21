package org.commonmark.ext.cc;

import org.commonmark.Extension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.testutil.RenderingTestCase;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CcCodeBlockTest extends RenderingTestCase {

    private static final char STX = '\u0002';
    private static final char ETX = '\u0003';

    private static final Set<Extension> EXTENSIONS = Set.of(CcExtension.create());
    private static final Parser PARSER = Parser.builder().extensions(EXTENSIONS).build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTENSIONS).build();

    @Test
    public void basicBlock() {
        String input = STX + "\ncode\n" + ETX;

        Node document = PARSER.parse(input);
        CcCodeBlock codeBlock = (CcCodeBlock) document.getFirstChild();
        assertThat(codeBlock.getLiteral()).isEqualTo("code\n");
        assertRendering(input, "<pre><code>code\n</code></pre>\n");
    }

    @Test
    public void emptyBlock() {
        assertRendering(STX + "\n" + ETX, "<pre><code></code></pre>\n");
    }

    @Test
    public void supportsFencedCodeBlockSyntaxInside() {
        String input = STX + "\n```java\ncode\n```\n" + ETX;

        Node document = PARSER.parse(input);
        CcCodeBlock codeBlock = (CcCodeBlock) document.getFirstChild();
        assertThat(codeBlock.getLiteral()).isEqualTo("```java\ncode\n```\n");
        assertRendering(input, "<pre><code>```java\ncode\n```\n</code></pre>\n");
    }

    @Test
    public void closingCanHaveSpacesAfter() {
        assertRendering(STX + "\ncode\n" + ETX + "   ", "<pre><code>code\n</code></pre>\n");
    }

    @Test
    public void closingCanNotHaveNonSpaces() {
        String input = STX + "\ncode\n" + ETX + " a";

        Node document = PARSER.parse(input);
        CcCodeBlock codeBlock = (CcCodeBlock) document.getFirstChild();
        assertThat(codeBlock.getLiteral()).isEqualTo("code\n" + ETX + " a\n");
        assertRendering(input, "<pre><code>code\n" + ETX + " a\n</code></pre>\n");
    }

    @Test
    public void unterminatedBlockRunsToEndOfDocument() {
        String input = STX + "\ncode\nmore";

        Node document = PARSER.parse(input);
        CcCodeBlock codeBlock = (CcCodeBlock) document.getFirstChild();
        assertThat(codeBlock.getLiteral()).isEqualTo("code\nmore\n");
        assertRendering(input, "<pre><code>code\nmore\n</code></pre>\n");
    }

    @Test
    public void etxAloneDoesNotStartBlock() {
        String input = ETX + "\ntext";

        assertRendering(input, "<p>" + ETX + "\ntext</p>\n");
    }

    @Test
    public void openingAllowsUpToThreeSpacesIndent() {
        assertRendering("   " + STX + "\ncode\n   " + ETX, "<pre><code>code\n</code></pre>\n");
    }

    @Test
    public void fourSpaceIndentDoesNotOpenBlock() {
        assertRendering("    " + STX + "\ncode\n" + ETX,
                "<pre><code>" + STX + "\n</code></pre>\n<p>code\n" + ETX + "</p>\n");
    }

    @Test
    public void closingIndentedTooFarDoesNotClose() {
        String input = STX + "\ncode\n    " + ETX + "\nend";

        Node document = PARSER.parse(input);
        CcCodeBlock codeBlock = (CcCodeBlock) document.getFirstChild();
        assertThat(codeBlock.getLiteral()).isEqualTo("code\n    " + ETX + "\nend\n");
        assertRendering(input, "<pre><code>code\n    " + ETX + "\nend\n</code></pre>\n");
    }

    @Override
    protected String render(String source) {
        return RENDERER.render(PARSER.parse(source));
    }
}
