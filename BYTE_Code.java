import java.io.*;
public class BYTE_Code
{
    // OutputStream out =new FileOutputStream(file_name+"class");
    byte[] code=new byte[64];
    int length = 0,cp=0;
        // code[0]=(byte)0xca;
        // code[1]=0xa;
        // out.write(code,0,1);
    public void emit1(int od)
    {
        if (cp == code.length)
        {
            byte[] newcode = new byte[cp * 2];
            System.arraycopy(code, 0, newcode, 0, cp);
            code = newcode;
        }
        code[cp++] = (byte)od;
    }
    
    public void emit2(int od)
    {
        if (cp + 2 > code.length) {
            emit1(od >> 8);
            emit1(od);
        }
        else
        {
            code[cp++] = (byte)(od >> 8);
            code[cp++] = (byte)od;
        }
    }
    
    public void emit4(int od)
    {
        if (cp + 4 > code.length)
        {
            emit1(od >> 24);
            emit1(od >> 16);
            emit1(od >> 8);
            emit1(od);
        }
        else
        {
            code[cp++] = (byte)(od >> 24);
            code[cp++] = (byte)(od >> 16);
            code[cp++] = (byte)(od >> 8);
            code[cp++] = (byte)od;
        }
    }

    
}