import java.util.BitSet;
public class Position
{
    final static int TabInc = 8;

    /** Standard indentation for subdiagnostics
     */
    final static int DiagInc = 4;

    /** Standard indentation for additional diagnostic lines
     */
    final static int DetailsInc = 2;

    /** Tabulator character.
     */
    final static byte TAB   = 0x9;

    /** Line feed character.
     */
    final static byte LF    = 0xA;

    /** Form feed character.
     */
    final static byte FF    = 0xC;

    /** Carriage return character.
     */
    final static byte CR    = 0xD;

    /** End of input character.  Used as a sentinel to denote the
     *  character one beyond the last defined character in a
     *  source file.
     */
    final static byte EOI   = 0x1A;
    public static final int NOPOS        = -1;

    public static final int FIRSTPOS     = 0;
    public static final int FIRSTLINE    = 1;
    public static final int FIRSTCOLUMN  = 1;

    public static final int LINESHIFT    = 10;
    public static final int MAXCOLUMN    = (1<<LINESHIFT) - 1;
    public static final int MAXLINE      = (1<<(Integer.SIZE-LINESHIFT)) - 1;

    public static final int MAXPOS       = Integer.MAX_VALUE;

    /**
     * This is class is not supposed to be instantiated.
     */
    private Position() {}

    /** A two-way map between line/column numbers and positions,
     *  derived from a scan done at creation time.  Tab expansion is
     *  optionally supported via a character map.  Text content
     *  is not retained.
     *<p>
     *  Notes:  The first character position FIRSTPOS is at
     *  (FIRSTLINE,FIRSTCOLUMN).  No account is taken of Unicode escapes.
     *
     * @param   src         Source characters
     * @param   max         Number of characters to read
     * @param   expandTabs  If true, expand tabs when calculating columns
     */
    public static LineMap makeLineMap(char[] src, int max, boolean expandTabs) {
        LineMapImpl lineMap = expandTabs ?
            new LineTabMapImpl(max) : new LineMapImpl();
        lineMap.build(src, max);
        return lineMap;
    }

    /** Encode line and column numbers in an integer as:
     *  line-number << LINESHIFT + column-number
     *  {@link Position.NOPOS represents an undefined position.
     *
     * @param  line  number of line (first is 1)
     * @param  col   number of character on line (first is 1)
     * @return       an encoded position or {@link Position.NOPOS
     *               if the line or column number is too big to
     *               represent in the encoded format
     * @throws IllegalArgumentException if line or col is less than 1
     */
    public static int encodePosition(int line, int col) {
        if (line < 1)
            throw new IllegalArgumentException("line must be greater than 0");
        if (col < 1)
            throw new IllegalArgumentException("column must be greater than 0");

        if (line > MAXLINE || col > MAXCOLUMN) {
            return NOPOS;
        }
        return (line << LINESHIFT) + col;
    }

    public static interface LineMap extends LineMaping {
        /** Find the start position of a line.
         *
         * @param line number of line (first is 1)
         * @return     position of first character in line
         * @throws  ArrayIndexOutOfBoundsException
         *           if <tt>lineNumber < 1</tt>
         *           if <tt>lineNumber > no. of lines</tt>
         */
        int getStartPosition(int line);

        /** Find the position corresponding to a (line,column).
         *
         * @param   line    number of line (first is 1)
         * @param   column  number of character on line (first is 1)
         *
         * @return  position of character
         * @throws  ArrayIndexOutOfBoundsException
         *           if <tt>line < 1</tt>
         *           if <tt>line > no. of lines</tt>
         */
        int getPosition(int line, int column);

        /** Find the line containing a position; a line termination
         * character is on the line it terminates.
         *
         * @param   pos  character offset of the position
         * @return the line number on which pos occurs (first line is 1)
         */
        int getLineNumber(int pos);

        /** Find the column for a character position.
         *  Note:  this method does not handle tab expansion.
         *  If tab expansion is needed, use a LineTabMap instead.
         *
         * @param  pos   character offset of the position
         * @return       the column number at which pos occurs
         */
        int getColumnNumber(int pos);
    }

    static class LineMapImpl implements LineMap {
        protected int[] startPosition; // start position of each line

        protected LineMapImpl() {}

        protected void build(char[] src, int max) {
            int c = 0;
            int i = 0;
            int[] linebuf = new int[max];
            while (i < max) {
                linebuf[c++] = i;
                do {
                    char ch = src[i];
                    if (ch == '\r' || ch == '\n') {
                        if (ch == '\r' && (i+1) < max && src[i+1] == '\n')
                            i += 2;
                        else
                            ++i;
                        break;
                    }
                    else if (ch == '\t')
                        setTabPosition(i);
                } while (++i < max);
            }
            this.startPosition = new int[c];
            System.arraycopy(linebuf, 0, startPosition, 0, c);
        }

        public int getStartPosition(int line) {
            return startPosition[line - FIRSTLINE];
        }

        public long getStartPosition(long line) {
            return getStartPosition(longToInt(line));
        }

        public int getPosition(int line, int column) {
            return startPosition[line - FIRSTLINE] + column - FIRSTCOLUMN;
        }

        public long getPosition(long line, long column) {
            return getPosition(longToInt(line), longToInt(column));
        }

        // Cache of last line number lookup
        private int lastPosition = Position.FIRSTPOS;
        private int lastLine = Position.FIRSTLINE;

        public int getLineNumber(int pos) {
            if (pos == lastPosition) {
                return lastLine;
            }
            lastPosition = pos;

            int low = 0;
            int high = startPosition.length-1;
            while (low <= high) {
                int mid = (low + high) >> 1;
                int midVal = startPosition[mid];

                if (midVal < pos)
                    low = mid + 1;
                else if (midVal > pos)
                    high = mid - 1;
                else {
                    lastLine = mid + 1; // pos is at beginning of this line
                    return lastLine;
                }
            }
            lastLine = low;
            return lastLine;  // pos is on this line
        }

        public long getLineNumber(long pos) {
            return getLineNumber(longToInt(pos));
        }

        public int getColumnNumber(int pos) {
            return pos - startPosition[getLineNumber(pos) - FIRSTLINE] + FIRSTCOLUMN;
        }

        public long getColumnNumber(long pos) {
            return getColumnNumber(longToInt(pos));
        }

        private static int longToInt(long longValue) {
            int intValue = (int)longValue;
            if (intValue != longValue)
                throw new IndexOutOfBoundsException();
            return intValue;
        }

        protected void setTabPosition(int offset) {}
    }

    /**
     * A LineMap that handles tab expansion correctly.  The cost is
     * an additional bit per character in the source array.
     */
    public static class LineTabMapImpl extends LineMapImpl {
        private BitSet tabMap;       // bits set for tab positions.

        public LineTabMapImpl(int max) {
            super();
            tabMap = new BitSet(max);
        }

        protected void setTabPosition(int offset) {
            tabMap.set(offset);
        }

        public int getColumnNumber(int pos) {
            int lineStart = startPosition[getLineNumber(pos) - FIRSTLINE];
            int column = 0;
            for (int bp = lineStart; bp < pos; bp++) {
                if (tabMap.get(bp))
                    column = (column / TabInc * TabInc) + TabInc;
                else
                    column++;
            }
            return column + FIRSTCOLUMN;
        }

        public int getPosition(int line, int column) {
            int pos = startPosition[line - FIRSTLINE];
            column -= FIRSTCOLUMN;
            int col = 0;
            while (col < column) {
                pos++;
                if (tabMap.get(pos))
                    col = (col / TabInc * TabInc) + TabInc;
                else
                    col++;
            }
            return pos;
        }
    }
}
