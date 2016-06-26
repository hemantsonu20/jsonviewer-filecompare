package com.github.jutil.compare.parser;

import java.awt.Color;

public class DiffNotice {

    // to denote a code was changed/ added at right side
    private static final Color GREEN_COLOR = new Color(204, 255, 204);

    // to denote code changed at left side, to denote code deleted at right side
    private static final Color RED_COLOR = new Color(255, 230, 230);

    // to denote color at left side, when code was added at right side
    // to denote color at right side when code was removed at left side
    private static final Color GREY_COLOR = new Color(242, 242, 242);

    private DiffType diffType;
    private int line;
    private int startOffset;
    private int length;

    public DiffType getDiffType() {

        return diffType;
    }

    public DiffNotice setDiffType(DiffType diffType) {

        this.diffType = diffType;
        return this;
    }

    public int getLine() {

        return line;
    }

    public DiffNotice setLine(int line) {

        this.line = line;
        return this;
    }

    public int getStartOffset() {

        return startOffset;
    }

    public DiffNotice setStartOffset(int startOffset) {

        this.startOffset = startOffset;
        return this;
    }

    public int getLength() {

        return length;
    }

    public DiffNotice setLength(int length) {

        this.length = length;
        return this;
    }

    public boolean containsPosition(int pos) {

        return (startOffset <= pos) && (pos < (startOffset + length));
    }

    public static enum DiffType {

        ADDED(GREY_COLOR, GREEN_COLOR), CHANGED(RED_COLOR, GREEN_COLOR), REMOVED(RED_COLOR, GREY_COLOR);

        private Color leftColor;
        private Color rightColor;

        private DiffType(Color leftColor, Color rightColor) {

            this.leftColor = leftColor;
            this.rightColor = rightColor;
        }

        public Color getLeftColor() {

            return leftColor;
        }

        public Color getRightColor() {

            return rightColor;
        }
    }
}
