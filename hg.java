import java.io.*;   
public class hg {   
    public static void main(String[] args) throws IOException   
    {   
        String command="javac test.java";
        String cmd="rm test.java";
        Runtime r=Runtime.getRuntime();   
        Process p=r.exec(command);
        //r.exec(cmd);
        BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));   
        StringBuffer sb=new StringBuffer();   
        String inline;   
        while(null!=(inline=br.readLine())){   
            sb.append(inline).append("/n");   
        }   
        System.out.println(sb.toString());   
    }   
}  
