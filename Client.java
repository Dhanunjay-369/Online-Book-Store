//Client
import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.Math;
public class Client
{
   public static void main(String args[])throws Exception
   {
	Socket cs=new Socket("localhost",10000);
	DataInputStream dis=new DataInputStream(cs.getInputStream());
	DataOutputStream dos=new DataOutputStream(cs.getOutputStream());
	Scanner sc=new Scanner(System.in);
	String str;
	while(true){
	str=dis.readUTF();
	System.out.println("Server Says:"+str);
	System.out.println("Enter the Reply to Server:");
	String client_request=sc.nextLine();
 	dos.writeUTF(client_request);
        System.out.println();
	}
   }
}