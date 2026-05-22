package org.commonmark.ext.cc.internal;

import org.commonmark.ext.cc.CcInlineCode;
import org.commonmark.node.Text;
import org.commonmark.parser.beta.InlineContentParser;
import org.commonmark.parser.beta.InlineContentParserFactory;
import org.commonmark.parser.beta.InlineParserState;
import org.commonmark.parser.beta.ParsedInline;
import org.commonmark.parser.beta.Position;
import org.commonmark.parser.beta.Scanner;

import java.util.Set;

public class CcInlineCodeParser implements InlineContentParser {

    private static final char OPEN = '\u000e';
    private static final char CLOSE = '\u000f';

    @Override
    public ParsedInline tryParse(InlineParserState inlineParserState) {
        Scanner scanner = inlineParserState.scanner();
        Position start = scanner.position();
        scanner.next();
        Position afterOpening = scanner.position();

        int end = scanner.find(CLOSE);
        if (end == -1) {
            return ParsedInline.of(new Text(String.valueOf(OPEN)), afterOpening);
        }

        String content = scanner.getSource(afterOpening, scanner.position()).getContent();
        scanner.next();
        return ParsedInline.of(new CcInlineCode(content), scanner.position());
    }

    public static class Factory implements InlineContentParserFactory {

        @Override
        public Set<Character> getTriggerCharacters() {
            return Set.of(OPEN);
        }

        @Override
        public InlineContentParser create() {
            return new CcInlineCodeParser();
        }
    }
}
