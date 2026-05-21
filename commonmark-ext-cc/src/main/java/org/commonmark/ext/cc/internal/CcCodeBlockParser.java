package org.commonmark.ext.cc.internal;

import org.commonmark.ext.cc.CcCodeBlock;
import org.commonmark.node.Block;
import org.commonmark.parser.SourceLine;
import org.commonmark.parser.block.AbstractBlockParser;
import org.commonmark.parser.block.AbstractBlockParserFactory;
import org.commonmark.parser.block.BlockContinue;
import org.commonmark.parser.block.BlockStart;
import org.commonmark.parser.block.MatchedBlockParser;
import org.commonmark.parser.block.ParserState;
import org.commonmark.text.Characters;

public class CcCodeBlockParser extends AbstractBlockParser {

    private static final int CODE_BLOCK_INDENT = 4;
    private static final char OPEN = '\u0002';
    private static final char CLOSE = '\u0003';

    private final CcCodeBlock block = new CcCodeBlock();
    private final int openingIndent;
    private final StringBuilder literal = new StringBuilder();
    private boolean openingLineConsumed;

    private CcCodeBlockParser(int openingIndent) {
        this.openingIndent = openingIndent;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        int nextNonSpace = state.getNextNonSpaceIndex();
        int newIndex = state.getIndex();
        CharSequence line = state.getLine().getContent();
        if (state.getIndent() < CODE_BLOCK_INDENT && nextNonSpace < line.length() && tryClosing(line, nextNonSpace)) {
            return BlockContinue.finished();
        }

        int i = openingIndent;
        int length = line.length();
        while (i > 0 && newIndex < length && line.charAt(newIndex) == ' ') {
            newIndex++;
            i--;
        }
        return BlockContinue.atIndex(newIndex);
    }

    @Override
    public void addLine(SourceLine line) {
        if (!openingLineConsumed) {
            openingLineConsumed = true;
            return;
        }
        literal.append(line.getContent());
        literal.append('\n');
    }

    @Override
    public void closeBlock() {
        block.setLiteral(literal.toString());
    }

    private static boolean isOpening(CharSequence line, int index) {
        return index < line.length() && line.charAt(index) == OPEN && index + 1 == line.length();
    }

    private boolean tryClosing(CharSequence line, int index) {
        if (index >= line.length() || line.charAt(index) != CLOSE) {
            return false;
        }
        int after = Characters.skipSpaceTab(line, index + 1, line.length());
        return after == line.length();
    }

    public static class Factory extends AbstractBlockParserFactory {

        @Override
        public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
            int indent = state.getIndent();
            if (indent >= CODE_BLOCK_INDENT) {
                return BlockStart.none();
            }

            int nextNonSpace = state.getNextNonSpaceIndex();
            if (isOpening(state.getLine().getContent(), nextNonSpace)) {
                return BlockStart.of(new CcCodeBlockParser(indent)).atIndex(nextNonSpace + 1);
            }
            return BlockStart.none();
        }
    }
}
