public class token
{
    private String sym="";
    private String name="";
    public int lineNum;

    token(String name,String sym)
    {
        this.name=name;
        this.sym=sym;
    }

    token(String name,String sym,int l)
    {
        this.name=name;
        this.sym=sym;
        this.lineNum=l;
    }

    public void setLineNum(int l)
    {
        lineNum=l;
    }

    public int getLineNum()
    {
        return lineNum;
    }

    public String getNam()
    {
        return name;
    }

    public String getSym()
    {
        return sym;
    }
}