import java.io.*;
public class read
{
    static int len=-1;
    public static int bytesread(int l,FileInputStream is,FileOutputStream os)
    {
        int ans=0;
        try{
            byte[] buf = new byte[1024]; 
            len=is.read(buf,0,l);
            for(int i=0;i<l;i++)
            {
                ans=ans<<8;
                ans+=(int)buf[i];
            }
            os.write(buf,0,l);
        } catch(Exception e){}
        return ans;
    }
    public static void attributes_read(FileInputStream is,FileOutputStream os)
    {
        bytesread(2,is,os);
        int length=bytesread(4,is,os);
        bytesread(length,is,os);
    }
    public static void methods_read(FileInputStream is,FileOutputStream os)
    {
        bytesread(2,is,os);
        bytesread(2,is,os);
        bytesread(2,is,os);
        int cnt=bytesread(2,is,os);
        for(int i=0;i<cnt;i++)
            attributes_read(is,os);
    }
    public static void fields_read(FileInputStream is,FileOutputStream os)
    {
        bytesread(2,is,os);
        bytesread(2,is,os);
        bytesread(2,is,os);
        int cnt=bytesread(2,is,os);
        for(int i=0;i<cnt;i++)
            attributes_read(is,os);
    }
    public static void main(String[] args)
    {
        ClassFile Fl=new ClassFile();
        //System.out.println(Fl.CONSTANT_Utf8);
        try
        {
            FileInputStream is=new FileInputStream(new File("Main.class"));
            FileOutputStream os = new FileOutputStream(new File("out.class"));
            bytesread(8,is,os);//magic and version
            int pool_cnt=bytesread(2,is,os);//pool count
            System.out.println("pool:"+pool_cnt);
            
            for(int i=1;i<pool_cnt;i++)
            {
                int tag=bytesread(1,is,os);
                if(tag==Fl.CONSTANT_Utf8)
                {
                    int length=bytesread(2,is,os);
                    bytesread(length,is,os);
                }
                else if(tag==Fl.CONSTANT_MethodType)
                {
                    bytesread(2,is,os);
                }
                else if(tag==Fl.CONSTANT_Methodref)
                {
                    bytesread(2,is,os);
                    bytesread(2,is,os);
                }
                else if(tag==Fl.CONSTANT_InterfaceMethodref)
                {
                    bytesread(2,is,os);
                    bytesread(2,is,os);
                }
                else if(tag==Fl.CONSTANT_Fieldref)
                {
                    bytesread(2,is,os);
                    bytesread(2,is,os);
                }
                else if(tag==Fl.CONSTANT_Class)
                {
                    bytesread(2,is,os);
                }
                else if(tag==Fl.CONSTANT_NameandType)
                {
                    bytesread(2,is,os);
                    bytesread(2,is,os);
                }
                else if(tag==Fl.CONSTANT_MethodHandle)
                {
                    bytesread(1,is,os);
                    bytesread(2,is,os);
                }
                else if(tag==Fl.CONSTANT_InvokeDynamic)
                {
                    bytesread(2,is,os);
                    bytesread(2,is,os);
                }
                else if(tag==Fl.CONSTANT_Integer)
                {
                    bytesread(4,is,os);
                }
            }
            int access_flags=bytesread(2,is,os);
            System.out.println("access_flags:"+access_flags);
            int this_class=bytesread(2,is,os);
            int super_class=bytesread(2,is,os);
            int interfaces_count=bytesread(2,is,os);
            for(int i=0;i<interfaces_count;i++)
            {
            }
            int fields_count=bytesread(2,is,os);
            System.out.println("fields:"+fields_count);
            for(int i=0;i<fields_count;i++)
            {
                fields_read(is,os);
            }
            int methods_count=bytesread(2,is,os);
            System.out.println("methods:"+methods_count);
            for(int i=0;i<methods_count;i++)
            {
                methods_read(is,os);
            }
            int attributes_count=bytesread(2,is,os);
            System.out.println("attributes:"+attributes_count);
            for(int i=0;i<attributes_count;i++)
            {
                attributes_read(is,os);
            }
        }
        catch(Exception e){}
    }
}
// while ((len = is.read(buf)) != -1)
            // {  
            //     os.write(buf, 0, len);
            // }