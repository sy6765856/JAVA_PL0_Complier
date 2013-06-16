import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

public class Parsing
{
	public LexiAnalyse Lexi;
	public token word;
	public String id = null;
	public int lineNum;
	public int error_number = 0;
	int cx;
	int cx0;
	int dx;
	int lev = -1;
	int errorNum = 0;
	public int t = -1;
	final public int stacksize = 500;
	public int p = 0;
	public int b = 0;
	public pcode i;
	public int[] s = new int[501];
	Vector pcodeArray = new Vector();;
	Vector nameArray = new Vector();

	public Parsing(LexiAnalyse CF)
	{
		Lexi = CF;
		word = Lexi.GetToken();
		analyse();
	}

	public int getErroNumber()
    {
		return error_number;
	}

	public void printCode()
	{
		for (int n = 0; n < pcodeArray.size(); n++) {
			pcode code = (pcode) pcodeArray.get(n);
			System.out.println(n + "  " + code.getF() + "   " + code.getL()+"   " + code.getA());
		}
	}
	public void analyse() {
		int tx0;
		lev++;
		dx = 3;
		tx0 = nameArray.size();
		nameArray.addElement(new nameClass(" ", " ", 0, 0, 0));
		((nameClass) nameArray.get(tx0)).setAdr(pcodeArray.size());
		pcodeArray.addElement(new pcode("jmp", 0, 0));
		while (word.getSym().equals("constsym")
				|| word.getSym().equals("varsym")
				|| word.getSym().equals("procsym"))
        {
			if (word.getSym().equals("constsym"))
			{
				word = Lexi.GetToken();
				delcaration();
				while (word.getSym().equals("comma"))
				{
					word = Lexi.GetToken();
					delcaration();
				}
				if (word.getSym().equals("semicolon"))
				{
					word = Lexi.GetToken();
				}
                else
                {
					error_number++;
					new error(word.getLineNum(), 5);
				}
			}
			else if (word.getSym().equals("varsym"))
            {
				word = Lexi.GetToken();
				vardeclaration();
				while (word.getSym().equals("comma"))
                {
					word = Lexi.GetToken();
					vardeclaration();
				}
				if (word.getSym().equals("semicolon"))
                {
					word = Lexi.GetToken();
				}
                else
                {
					error_number++;
					new error(word.getLineNum(), 5);
				}
			}
			while (word.getSym().equals("procsym")) {
				word = Lexi.GetToken();
				if (word.getSym().equals("ident")) {
					id = word.getNam();
					enter("procedure");
					word = Lexi.GetToken();
				} else {
					error_number++;
					new error(word.getLineNum(), 4);
				}
				if (word.getSym().equals("semicolon")) {
					word = Lexi.GetToken();
				} else {
					error_number++;
					new error(word.getLineNum(), 5);
				}
				int dxxxxx = dx;
				int levv = lev;
				analyse();
				dx = dxxxxx;
				lev = levv;
                if (word.getSym().equals("semicolon")) {
					word = Lexi.GetToken();
                    String sym = word.getSym();
					if (!(sym.equals("ident") || sym.equals("procsym")
							|| sym.equals("beginsym") || sym.equals("callsym")
							|| sym.equals("ifsym") || sym.equals("while"))) {
						error_number++;
						new error(word.getLineNum(), 6);
					}
				} else {
					error_number++;
					new error(word.getLineNum(), 5);
				}
            }
		}
		((pcode) pcodeArray.get(((nameClass) nameArray.get(tx0)).getAdr()))
				.setA(pcodeArray.size());
		((nameClass) nameArray.get(tx0)).setAdr(pcodeArray.size());
		cx0 = pcodeArray.size();
		pcodeArray.addElement(new pcode("int", 0, dx));
		statement1();
		pcodeArray.addElement(new pcode("opr", 0, 0));

	}

	public void delcaration()
	{
		if (word.getSym().equals("ident"))
		{
			id = word.getNam();
			word = Lexi.GetToken();
			if (word.getSym().equals("becomes"))
            {
				error_number++;
				new error(word.getLineNum(), 1);
			}
            else if (word.getSym().equals("eql"))
            {
				word = Lexi.GetToken();
				if (word.getSym().equals("number")) {
					enter("constant");
					word = Lexi.GetToken();
				}
                else
				{
					error_number++;
					new error(word.getLineNum(), 2);
				}
			}
            else
			{
				error_number++;
				new error(word.getLineNum(), 3);
			}
		}
        else
		{
			error_number++;
			new error(word.getLineNum(), 4);
		}
	}

	public void vardeclaration()
	{
		if (word.getSym().equals("ident")) {
			id = word.getNam();
			enter("variable");
			word = Lexi.GetToken();
		}
        else
		{
			error_number++;
			new error(word.getLineNum(), 4);
		}
	}

	public void enter(String k)
	{
		if (k.equals("constant"))
        {
			int num = Integer.parseInt(word.getNam());
			if (num > 2047)
            {
				nameArray.addElement(new nameClass(id, k, 0, 0, 0));
				new error(word.getLineNum(), 31);
			}
            else
				nameArray.addElement(new nameClass(id, k, num, 0, 0));
		}
        else if (k.equals("variable"))
        {
			nameArray.addElement(new nameClass(id, k, 0, lev, dx));
			dx = dx + 1;
		}
        else if (k.equals("procedure"))
        {
			nameArray.addElement(new nameClass(id, k, 0, lev, 0));
		}
	}

	public int position(String id)
	{
		int i = 0;
		while (i < nameArray.size()
				&& (!((nameClass) nameArray.get(i)).getNam().equals(id))) {
			i++;
		}
		if (i >= nameArray.size())
        {
			return -1;
		}
		else
        {
			return i;
		}
	}

	public void statement1()
	{
		int i;
		int cx1;
		int cx2;
		if (word.getSym().equals("ident"))
		{
			i = position(word.getNam());
			if (i == -1)
			{
				error_number++;
				new error(word.getLineNum(), 11);
			}
            else if (!((nameClass) nameArray.get(i)).getKind().equals("variable"))
			{
				error_number++;
				new error(word.getLineNum(), 12);
				i = -1;
			}
			word = Lexi.GetToken();
			if (word.getSym().equals("becomes")) {
				word = Lexi.GetToken();
			} else {
				error_number++;
				new error(word.getLineNum(), 13);
			}
			expression();
			if (i != -1)
			{
				pcodeArray.addElement(new pcode("sto", lev
						- ((nameClass) nameArray.get(i)).getLev(),
						((nameClass) nameArray.get(i)).getAdr()));
			}
		}
        else if (word.getSym().equals("callsym"))
		{
			word = Lexi.GetToken();
			if (!word.getSym().equals("ident"))
			{
				error_number++;
				new error(word.getLineNum(), 14);
			}
            else
            {
				i = position(word.getNam());
				if (i == -1)
                {
					error_number++;
					new error(word.getLineNum(), 11);
				}
				else if (((nameClass) nameArray.get(i)).getKind().equals(
						"procedure"))
                {
					pcodeArray.addElement(new pcode("cal", lev
							- ((nameClass) nameArray.get(i)).getLev(),
							((nameClass) nameArray.get(i + 1)).getAdr()));
				}
                else
                {
					error_number++;
					new error(word.getLineNum(), 15);
				}
				word = Lexi.GetToken();
			}
		}
        else if (word.getSym().equals("ifsym"))
		{
			word = Lexi.GetToken();
			condition();
			if (word.getSym().equals("thensym"))
            {
				word = Lexi.GetToken();
			}
            else
            {
				error_number++;
				new error(word.getLineNum(), 16);
			}
			cx = pcodeArray.size();
			cx1 = cx;
			pcodeArray.addElement(new pcode("jpc", 0, 0));
			statement1();
			((pcode) pcodeArray.get(cx1)).setA(pcodeArray.size());
		}
        else if (word.getSym().equals("beginsym"))
        {
			word = Lexi.GetToken();
			statement1();
			while (word.getSym().equals("semicolon")
					|| word.getSym().equals("beginsym")
					|| word.getSym().equals("callsym")
					|| word.getSym().equals("ifsym")
					|| word.getSym().equals("whilesym"))
            {
				if (word.getSym().equals("semicolon"))
                {
					word = Lexi.GetToken();
				}
                else
                {
					error_number++;
					new error(word.getLineNum(), 10);
				}
				statement1();
			}
			if (word.getSym().equals("endsym"))
            {
				word = Lexi.GetToken();
			}
            else
            {
				error_number++;
				new error(word.getLineNum(), 17);
			}
		}
        else if (word.getSym().equals("whilesym"))
        {
			cx = pcodeArray.size();
			cx1 = cx;
			word = Lexi.GetToken();
			condition();
			cx = pcodeArray.size();
			cx2 = cx;
			pcodeArray.addElement(new pcode("jpc", 0, 0));
			if (word.getSym().equals("dosym"))
			{
				word = Lexi.GetToken();
			}
            else
            {
				error_number++;
				new error(word.getLineNum(), 18);
			}
			statement1();
			pcodeArray.addElement(new pcode("jmp", 0, cx1));
			((pcode) pcodeArray.get(cx2)).setA(pcodeArray.size());
		}
        else if (word.getSym().equals("readsym"))
        {
			word = Lexi.GetToken();
			if (word.getSym().equals("lparen"))
            {
				word = Lexi.GetToken();
				if (word.getSym().equals("ident"))
                {
					i = position(word.getNam());
					if (i == -1)
                    {
						error_number++;
						new error(word.getLineNum(), 11);
					}
                    else
                    {
						pcodeArray.addElement(new pcode("opr", 0, 16));
						pcodeArray.addElement(new pcode("sto", lev-((nameClass) nameArray.get(i)).getLev(),((nameClass) nameArray.get(i)).getAdr()));
					}
				}
				word = Lexi.GetToken();
				while (word.getSym().equals("comma"))
				{
					word = Lexi.GetToken();
					if (word.getSym().equals("ident"))
                    {
						i = position(word.getNam());
						if (i == -1)
                        {
							error_number++;
							new error(word.getLineNum(), 11);
						}
                        else
                        {
							pcodeArray.addElement(new pcode("opr", 0, 16));
							pcodeArray.addElement(new pcode("sto", lev
									- ((nameClass) nameArray.get(i)).getLev(),
									((nameClass) nameArray.get(i)).getAdr()));
						}
					}
					word = Lexi.GetToken();
				}
				if (!word.getSym().equals("rparen"))
                {
					error_number++;
					new error(word.getLineNum(), 9);
				}
			}
            else
            {
				error_number++;
				new error(word.getLineNum(), 0);
			}
			word = Lexi.GetToken();
		}
        else if (word.getSym().equals("writesym"))
        {
			word = Lexi.GetToken();
			if (word.getSym().equals("lparen"))
            {
				word = Lexi.GetToken();
				expression();
				pcodeArray.addElement(new pcode("opr", 0, 14));
				while (word.getSym().equals("comma"))
                {
					word = Lexi.GetToken();
					expression();
					pcodeArray.addElement(new pcode("opr", 0, 14));
				}

				if (!word.getSym().equals("rparen"))
                {
					error_number++;
					new error(word.getLineNum(), 9);
				}
                else
                {
					word = Lexi.GetToken();
				}
			}
			pcodeArray.addElement(new pcode("opr", 0, 15));
		}
	}

	public void condition()
	{
		String relop = null;
		if (word.getSym().equals("oddsym"))
		{
			word = Lexi.GetToken();
			expression();
			pcodeArray.addElement(new pcode("opr", 0, 6));
		}
        else
		{
			expression();
			relop = word.getSym();
			word = Lexi.GetToken();
			expression();
			if (relop.equals("eql"))
            {
				pcodeArray.addElement(new pcode("opr", 0, 8));
			}
            else if (relop.equals("neq"))
            {
				pcodeArray.addElement(new pcode("opr", 0, 9));
			}
            else if (relop.equals("lss"))
            {
				pcodeArray.addElement(new pcode("opr", 0, 10));
			}
            else if (relop.equals("geq"))
            {
				pcodeArray.addElement(new pcode("opr", 0, 11));
			}
            else if (relop.equals("gtr"))
            {
				pcodeArray.addElement(new pcode("opr", 0, 12));
			}
            else if (relop.equals("leg"))
            {
				pcodeArray.addElement(new pcode("opr", 0, 13));
			}
            else
            {
				System.out.println(word.getNam() + "   " + relop);
				error_number++;
				new error(word.getLineNum(), 20);
			}
		}
	}

	public void expression()
	{
		String addop = null;
		if (word.getSym().equals("plus") || word.getSym().equals("minus")) {
			addop = word.getSym();
			word = Lexi.GetToken();
			term();
			if (addop.equals("minus"))
			{
				pcodeArray.addElement(new pcode("opr", 0, 1));
			}
		}
        else
		{
			term();
		}
		while (word.getSym().equals("plus") || word.getSym().equals("minus"))
		{
			addop = word.getSym();
			word = Lexi.GetToken();
			term();
			if (addop.equals("plus"))
			{
				pcodeArray.addElement(new pcode("opr", 0, 2));
			}
            else
			{
				pcodeArray.addElement(new pcode("opr", 0, 3));
			}
		}
	}

	public void term()
	{
		String mulop = null;
		factor();
		while (word.getSym().equals("times") || word.getSym().equals("slash"))
		{
			mulop = word.getSym();
			word = Lexi.GetToken();
			factor();
			if (mulop.equals("times"))
			{
				pcodeArray.addElement(new pcode("opr", 0, 4));
			}
            else
			{
				pcodeArray.addElement(new pcode("opr", 0, 5));
			}
		}
	}
	public void factor()
	{
		int i;
		while (word.getSym().equals("ident") || word.getSym().equals("number")
				|| word.getSym().equals("lqaren"))
		{
			if (word.getSym().equals("ident"))
			{
				i = position(word.getNam());
				if (i == -1)
				{
					error_number++;
					new error(word.getLineNum(), 11);
				}
  				else if (((nameClass) nameArray.get(i)).getKind().equals(
						"constant"))
				{
					pcodeArray.addElement(new pcode("lit", 0,
							((nameClass) nameArray.get(i)).getVal()));
					word = Lexi.GetToken();
				}
                else if (((nameClass) nameArray.get(i)).getKind().equals(
						"variable"))
                {
					pcodeArray.addElement(new pcode("lod", lev
							- ((nameClass) nameArray.get(i)).getLev(),
							((nameClass) nameArray.get(i)).getAdr()));
					word = Lexi.GetToken();
				}
                else if (((nameClass) nameArray.get(i)).getKind().equals(
						"procedure"))
                {
					error_number++;
					new error(word.getLineNum(), 21);
					word = Lexi.GetToken();
				}
			}
            else if (word.getSym().equals("number"))
			{
				int num = Integer.parseInt(word.getNam());
				if (num > 2047)
				{
					error_number++;
					new error(word.getLineNum(), 7);
					num = 0;
				}
				pcodeArray.addElement(new pcode("lit", 0, num));
				word = Lexi.GetToken();
			} else if (word.getSym().equals("lqaren"))
			{
				word = Lexi.GetToken();
				expression();
				if (word.getSym().equals("rqaren"))
				{
					word = Lexi.GetToken();
				}
                else
                {
					error_number++;
					new error(word.getLineNum(), 22);
				}
			}
		}
	}
	public int base(int l)
	{
		int b1;
		b1 = b;
		while (l > 0) {
			b1 = s[b1];
			l--;
		}
		return b1;
	}

	public void interpret()
    {
		s[1] = 0;
		s[2] = 0;
		// s[3]=0;
		System.out.println("start pl/0");
		do {
			i = (pcode) pcodeArray.get(p);
			p++;
			if (i.getF().equals("lit"))
            {
				t++;
				s[t] = i.getA();
			}
            else if (i.getF().equals("opr"))
            {
				if (i.getA() == 0)
                {
					t = b - 1;
					p = s[t + 3];
					b = s[t + 2];
				}
                else if (i.getA() == 1)
				{
					s[t] = -s[t];
				}
                else if (i.getA() == 2)
				{
					t--;
					s[t] = s[t] + s[t + 1];
				}
                else if (i.getA() == 3)
				{
					t--;
					s[t] = s[t] - s[t + 1];
				}
                else if (i.getA() == 4)
				{
					t--;
					s[t] = s[t] * s[t + 1];
				}
                else if (i.getA() == 5)
				{
					t--;
					s[t] = s[t] / s[t + 1];
				}
                else if (i.getA() == 6)
				{
					if (s[t] % 2 == 0)
						s[t] = 0;
					else
						s[t] = 1;
				}
                else if (i.getA() == 8)
				{
					t--;
					if (s[t] == s[t + 1])
						s[t] = 1;
					else
						s[t] = 0;
				}
                else if (i.getA() == 9)
				{
					t--;
					if (s[t] == s[t + 1])
						s[t] = 0;
					else
						s[t] = 1;
				}
                else if (i.getA() == 10)
				{
					t--;
					if (s[t] < s[t + 1])
						s[t] = 1;
					else
						s[t] = 0;
				}
                else if (i.getA() == 11)
				{
					t--;
					if (s[t] >= s[t + 1])
						s[t] = 1;
					else
						s[t] = 0;
				}
                else if (i.getA() == 12)
				{
					t--;
					if (s[t] > s[t + 1])
						s[t] = 1;
					else
						s[t] = 0;
				}
                else if (i.getA() == 13)
				{
					t--;
					if (s[t] <= s[t + 1])
						s[t] = 1;
					else
						s[t] = 0;
				}
                else if (i.getA() == 14)
				{
					System.out.println(s[t]);
					t--;
				}
                else if (i.getA() == 15)
				{
					System.out.println();
				}
                else if (i.getA() == 16)
				{
					System.out.println("Please enter a number (0 exit):");
					int age;
					try {
						InputStreamReader fin = new InputStreamReader(System.in);
						BufferedReader br = new BufferedReader(fin);
						String name = br.readLine();
						age = Integer.parseInt(name);
					}
                    catch (Exception e)
                    {
						age = 0;
					}
					t++;
					s[t] = age;
				}
			}
            else if (i.getF().equals("lod"))
            {
				t++;
				s[t] = s[base(i.getL()) + i.getA()];
			}
            else if (i.getF().equals("sto"))
            {
				s[base(i.getL()) + i.getA()] = s[t];
				t--;
			}
            else if (i.getF().equals("cal"))
            {
				s[t + 1] = base(i.getL());
				s[t + 2] = b;
				s[t + 3] = p;
				b = t + 1;
				p = i.getA();
			}
            else if (i.getF().equals("int"))
            {
				t = t + i.getA();
			}
            else if (i.getF().equals("jmp"))
            {
				p = i.getA();
			}
            else if (i.getF().equals("jpc"))
            {
				if (s[t] == 0)
					p = i.getA();
				t--;
			}
            else if (i.getF().equals("wrt"))
            {
				System.out.println(s[t]);
				t--;
			}
		}while (p != 0);
		System.out.println("end pl/0");
	}
}