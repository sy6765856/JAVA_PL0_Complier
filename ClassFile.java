public class ClassFile {

    public final static int JAVA_MAGIC = 0xCAFEBABE;

    // see Target
    public final static int CONSTANT_Utf8 = 1;
    public final static int CONSTANT_Unicode = 2;
    public final static int CONSTANT_Integer = 3;
    public final static int CONSTANT_Float = 4;
    public final static int CONSTANT_Long = 5;
    public final static int CONSTANT_Double = 6;
    public final static int CONSTANT_Class = 7;
    public final static int CONSTANT_String = 8;
    public final static int CONSTANT_Fieldref = 9;
    public final static int CONSTANT_Methodref = 10;
    public final static int CONSTANT_InterfaceMethodref = 11;
    public final static int CONSTANT_NameandType = 12;
    public final static int CONSTANT_MethodHandle = 15;
    public final static int CONSTANT_MethodType = 16;
    public final static int CONSTANT_InvokeDynamic = 18;

    public final static int MAX_PARAMETERS = 0xff;
    public final static int MAX_DIMENSIONS = 0xff;
    public final static int MAX_CODE = 0xffff;
    public final static int MAX_LOCALS = 0xffff;
    public final static int MAX_STACK = 0xffff;

    public enum Version {
        V45_3(45, 3), // base level for all attributes
        V49(49, 0),   // JDK 1.5: enum, generics, annotations
        V50(50, 0),   // JDK 1.6: stackmaps
        V51(51, 0);   // JDK 1.7
        Version(int major, int minor) {
            this.major = major;
            this.minor = minor;
        }
        public final int major, minor;
    }


/************************************************************************
 * String Translation Routines
 ***********************************************************************/

    /** Return internal representation of buf[offset..offset+len-1],
     *  converting '/' to '.'.
     */
    public static byte[] internalize(byte[] buf, int offset, int len) {
        byte[] translated = new byte[len];
        for (int j = 0; j < len; j++) {
            byte b = buf[offset + j];
            if (b == '/') translated[j] = (byte) '.';
            else translated[j] = b;
        }
        return translated;
    }
}