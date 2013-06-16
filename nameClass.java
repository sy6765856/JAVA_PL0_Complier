public class nameClass
{
	String name = null;
	String kind = null;
	int val;
	int level;
	int adr;

	public nameClass(String a, String b, int c, int d, int e)
    {
		name = a;
		kind = b;
		val = c;
		level = d;
		adr = e;
	}

	public void setAdr(int cx0)
    {
		adr = cx0;
	}

	public int getAdr()
    {
		return adr;
	}

	public String getKind()
    {
		return kind;
	}

	public int getVal()
    {
		return val;
	}

	public String getNam()
    {
		return name;
	}

	public int getLev()
    {
		return level;
	}
}