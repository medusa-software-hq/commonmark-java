package org.commonmark.ext.cc;

import org.commonmark.node.CustomNode;

/**
 * Inline code delimited by ASCII SO and SI.
 */
public class CcInlineCode extends CustomNode {

    private String literal;

    public CcInlineCode() {
    }

    public CcInlineCode(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }
}
