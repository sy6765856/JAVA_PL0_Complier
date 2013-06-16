import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
public class LexiAnalyse
{
    String line;
    int lineLength,lineNum=0;
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
            line=in.readLine().trim();
            lineLength=line.length();
            lineNum=1;
            while(lineLength==0)
            {
                line=in.readLine().trim();
                lineLength=line.length();
                lineNum++;
            }
            index=0;
            System.out.println(line);
        }
        catch (FileNotFoundException a)
        {
            System.out.println("No Such File");
        }
        catch (IOException b)
        {
            System.out.println("File read error!");
        }
    }
    
}