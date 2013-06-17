import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Pl0
{
    public static void main(String[] args)
    {
        String file_name="";
        System.out.println("Please input PL FileName:");
        try
        {
            InputStreamReader fin =new InputStreamReader(System.in);
            BufferedReader buf=new BufferedReader(fin);
            file_name=buf.readLine();
        }
        catch (IOException e)
        {
            System.out.println("error!Please input file name:");   
        }
        LexiAnalyse Lexi=new LexiAnalyse(file_name);
        Parsing Pars=new Parsing(Lexi);
        Pars.print_code();
        //Pars.printPode();
        Pars.interpret();        
    }
}