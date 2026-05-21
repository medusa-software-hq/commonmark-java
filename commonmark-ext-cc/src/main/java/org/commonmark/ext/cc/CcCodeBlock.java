package org.commonmark.ext.cc;

import org.commonmark.node.CustomBlock;

/**
 * A control-code-delimited code block using ASCII STX as opener and ASCII ETX as closer.
 */
public class CcCodeBlock extends CustomBlock {

    private String literal;

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }
}
