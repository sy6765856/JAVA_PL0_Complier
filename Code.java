public class Code
{
    public final boolean debugCode;
    public final boolean needStackMap;
    public byte[] code = new byte[64];
    public boolean alive = true;
    public int cp = 0;
    //State state;
    //Chain pendingJumps = null;
    boolean pendingStackMap = false;
    // public int curPc() {
    //     if (pendingJumps != null) resolvePending();
    //     if (pendingStatPos != Position.NOPOS) markStatBegin();
    //     fixedPc = true;
    //     return cp;
    // }

    /** Emit a byte of code.
     */
    public  void emit1(int od) {
        if (!alive) return;
        if (cp == code.length) {
            byte[] newcode = new byte[cp * 2];
            System.arraycopy(code, 0, newcode, 0, cp);
            code = newcode;
        }
        code[cp++] = (byte)od;
    }

    // /** Emit two bytes of code.
    //  */
    // public void emit2(int od) {
    //     if (!alive) return;
    //     if (cp + 2 > code.length) {
    //         emit1(od >> 8);
    //         emit1(od);
    //     } else {
    //         code[cp++] = (byte)(od >> 8);
    //         code[cp++] = (byte)od;
    //     }
    // }

    // /** Emit four bytes of code.
    //  */
    // public void emit4(int od) {
    //     if (!alive) return;
    //     if (cp + 4 > code.length) {
    //         emit1(od >> 24);
    //         emit1(od >> 16);
    //         emit1(od >> 8);
    //         emit1(od);
    //     } else {
    //         code[cp++] = (byte)(od >> 24);
    //         code[cp++] = (byte)(od >> 16);
    //         code[cp++] = (byte)(od >> 8);
    //         code[cp++] = (byte)od;
    //     }
    // }

    // /** Emit an opcode.
    //  */
    // public void emitop(int op) {
    //     if (pendingJumps != null) resolvePending();
    //     if (alive) {
    //         if (pendingStatPos != Position.NOPOS)
    //             markStatBegin();
    //         if (pendingStackMap) {
    //             pendingStackMap = false;
    //             emitStackMap();
    //         }
    //         if (debugCode)
    //             System.err.println("emit@" + cp + " stack=" +
    //                                state.stacksize + ": " +
    //                                mnem(op));
    //         emit1(op);
    //     }
    // }

    // void postop() {
    //     Assert.check(alive || state.stacksize == 0);
    // }

}