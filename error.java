public class error
{
	String[] err={"missing lparen"
	 ,"illegal: ':='"
   ,"illegal character behind '=' "
   ,"missing '=' "
   ,"illegal character "
   ,"missing ';' "
   ,"illegal prase "
   ,"integer overflow "
   ,"integer "
   ,"missing rparen"   
   ,"missing ';'"
   ,"ident not exist"
   ,"ident is not a variable"
   ,"missing '='"
   ,"missing ident after call"
   ,"missing proc after call"
   ,"missing then"
   ,"missing end"
   ,"missing do"
   ,"indent too long"
   ,"missing operator"
   ,"indent is a proc name"
   ,"missing rparen"
               };
	error(int linenumber,int t)
	{
		System.out.println("row:"+linenumber+"  error number"+t+"  "+err[t]);
	}
}