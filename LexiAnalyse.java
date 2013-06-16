import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
public class LexiAnalyse
{
    String line;
    int lineLength,lineNum=0,word_cnt=13;
    public char Cha;
    int index,al=10,nmax=4;
    public int error_number=0;
    BufferedReader in;
    static token word[];

    public LexiAnalyse(String file_name)
    {
        word = new token[13];
		word[0] = new token("begin", "beginsym");
		word[1] = new token("call", "callsym");
		word[2] = new token("const", "constsym");
		word[3] = new token("do", "dosym");
		word[4] = new token("end", "endsym");
		word[5] = new token("if", "ifsym");
		word[6] = new token("odd", "oddsym");
		word[7] = new token("procedure", "procsym");
		word[8] = new token("read", "readsym");
		word[9] = new token("then", "thensym");
		word[10] = new token("var", "varsym");
		word[11] = new token("while", "whilesym");
		word[12] = new token("write", "writesym");
        try
        {
            FileInputStream fis = new FileInputStream(file_name);
            in=new BufferedReader(new InputStreamReader(fis));
            line = in.readLine().trim();
			lineLength = line.length();
			lineNum = 1;
			while (lineLength == 0)
            {
				line = in.readLine().trim();
				lineLength = line.length();
				lineNum++;
			}
			index = 0;
			GetCh();
        }
        catch (FileNotFoundException a)
        {
            System.out.println("No Such File");
        }
        catch (IOException b) {
			System.out.println("Read error!");
		}
    }
 
    public void GetCh()
    {
        try
        {
            if (index == lineLength)
            {
                line = in.readLine().trim();
                lineLength = line.length();
                lineNum++;
                while (lineLength == 0) {
                    line = in.readLine().trim();
                    lineLength = line.length();
                    lineNum++;
                }
                index = 0;
                Cha = line.charAt(index);
                index++;
            }
            else
            {
                Cha = line.charAt(index);
                index++;
            }
        } catch (NullPointerException e) {
            System.out.println("****************************Read FInished!*******************************");
        } catch (IOException c) {}
    }

    public token GetToken()
	{

		int charNum1;
		int charNum2;
		String tokenName;
		int n = 0;
		while (Cha == ' ') {
			GetCh();
		}
		if (Cha >= 'a' && Cha <= 'z')
        {
			charNum1 = index - 1;
			while ((Character.isLetter(Cha) || Character.isDigit(Cha))&& index <lineLength)
            {
				GetCh();
			}
			charNum2 = index;
            //System.out.println(charNum1);
            //System.out.println(charNum2);
			if ((charNum2 - charNum1) > al)
            {
				new error(lineNum, 19);
				error_number++;
				tokenName = line.substring(charNum1, charNum1 + al);
			}
			else
            {
				if (Character.isLetter(Cha) || Character.isDigit(Cha))
                {
					tokenName = line.substring(charNum1);
					GetCh();
				}
                else
                {
					tokenName = line.substring(charNum1, charNum2 - 1);
				}
			}
			while (n < 13 && (!word[n].getNam().equals(tokenName)))
            {
				n++;
			}
			if (n == 13)
			{
				return new token(tokenName, "ident", lineNum);
			}
            else
            {
				word[n].setLineNum(lineNum);
				return word[n];
			}
		}
		else if (Character.isDigit(Cha))
        {
			charNum1 = index - 1;
			charNum2 = index;
			while (Character.isDigit(Cha) && index < lineLength)
            {
				charNum2 = index;
				GetCh();
			}
			if (Character.isDigit(Cha))
			{
				tokenName = line.substring(charNum1);
				GetCh();
			}
            else
				tokenName = line.substring(charNum1, charNum2);
			if ((charNum2 - charNum1) > nmax)
            {
				new error(lineNum, 8);
				error_number++;
				tokenName = tokenName.substring(0, nmax);
			}
			return new token(tokenName, "number", lineNum);
		}
        else if (Cha == ':')
        {
			GetCh();
			if (Cha == '=')
            {
				GetCh();
				return new token(":=", "becomes", lineNum);

			} else
				return new token(":", "null", lineNum);
		}
        else if (Cha == '<')
        {
			GetCh();
			if (Cha == '=')
            {
				GetCh();
				return new token("<=", "leg", lineNum);

			} else
				return new token("<", "lss", lineNum);
		}
        else if (Cha == '>')
        {
			GetCh();
			if (Cha == '=')
            {
				GetCh();
				return new token(">=", "geq", lineNum);
			}
            else return new token(">", "gtr", lineNum);
		}
        else
        {
			String sym;
			String sr = Character.toString(Cha);
			switch (Cha) {
			case '+':
				sym = "plus";
				break;
			case '-':
				sym = "minus";
				break;
			case '*':
				sym = "times";
				break;
			case '/':
				sym = "slash";
				break;
			case '(':
				sym = "lparen";
				break;
			case ')':
				sym = "rparen";
				break;
			case '=':
				sym = "eql";
				break;
			case '#':
				sym = "neq";
				break;
			case ',':
				sym = "comma";
				break;
			case '.':
				sym = "period";
				break;
			case ';':
				sym = "semicolon";
				break;
			default:
				sym = "nul";
			}
			GetCh();
			return new token(sr, sym, lineNum);
		}
	}
}