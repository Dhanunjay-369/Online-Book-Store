//Server
import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.Math;
public class Server
{
   public static void main(String args[])throws Exception
   {
	ServerSocket ss=new ServerSocket(10000);
	Socket cs;
	int count=0;
	Child c;
	System.out.println("Online BookStore Server Started");
	while(true)
	{
		try{
		cs=ss.accept();
		++count;
		c=new Child(cs,count);
		Thread t=new Thread(c);
		t.start();
		System.out.println("Client"+count+" connected succesfully");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
   }
}

class User
{
    HashMap<String ,String > users=new HashMap<>();
    User(){
    users.put("21BCE5524","Dhanunjay");
    users.put("21BLC5357","YeduKrishnan");
    users.put("21BME6078","Bipin Shah");
    users.put("21BRS1054","SamGodSon");
    users.put("21BCE2095","Pardheev");
    users.put("21BPS3456","Mukesh");
    users.put("21BRS7568","Pavan");
    users.put("21BME1234","Anirudh");
    users.put("21BLC9848","Anvith");
    }
    boolean match(String id,String pass)
    {
        boolean exists=users.containsKey(id);
        if(exists)
        {
            if(pass.equals(users.get(id)))
               return true;
            return false;
        }
        return false;
    }
}
class Book 
{
    HashMap<String ,String[] >book=new HashMap<>();
    int book_count[]=new int[10];
    String temp="";
    Book()
    {
        book.put("B001", new String[]{"Organic Chemistry", "Jagadamba Singh","2019","1000"});
        book.put("B002", new String[]{"Cognitive Psychology", "Nick Braisby","2014","500"});
        book.put("B003", new String[]{"Forces of Nature", "Brian Cox","2008","300"});
        book.put("B004", new String[]{"Concept of Physics", "HC Verma","2015","400"});
        book.put("B005", new String[]{"Cormen Algorithms", "Cormen","2021","1200"});
        book.put("B006", new String[]{"Operating System Concepts", "Albert Silberschatz","2020","900"});
        book.put("B007", new String[]{"Fundamentals of Database Systems", "Navate","2022","600"});
        book.put("B008", new String[]{"Data Communication and Networking", "Forouzan","2023","350"});
        book.put("B009", new String[]{"Discrete Math and its Applications", "Kenneth Rosen","2018","700"});
        book.put("B010", new String[]{"Cyber Security", "Nina Godbole","2020","800"});
    }
    
    void  initialise_stock()
    {
        for(int i=0;i<10;i++)
        {
            book_count[i]=10;  //10 Copies of each book available initially
        }
    }
    
    boolean stock_available()
    {
        for(int i=0;i<10;i++)
        {
            if(book_count[i]<0)
               return false;
        }
        return true;
    }

    void stock_refill()
    {
        for(int i=0;i<10;i++)
        {
            if(book_count[i]<=0)
            {
                book_count[i]=10;
            }
        }
    }

    String display_catalog() {
	String temp="";
        System.out.println("Book Catalog:");
        for (Map.Entry<String, String[]> entry : book.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            temp+="Book ID: " + key+"  -  Book Name: " + value[0]+"\n";
        }
	return temp;
    }


    String fetch_book(String book_id)
    {
	String temp="";
        if(book.containsKey(book_id))
        {
            String buffer[]=new String[4];
            buffer=book.get(book_id);
            String column[]=new String[]{"Book Name:","Author:","Version:","Price:"};
            int i=0;
            for(String str:buffer)
            {
                temp+=column[i++]+str;
                if(i<=3)
                  temp+="\n";
            }
            return temp;//System.out.println("Book Details are:\n"+temp);
        }
        else
        {
            temp="Book Not Availabe";
	    return temp;
        }
    }
    
    boolean check_availability(int index)
    {
        if(book_count[index]>0)
          return true;
        return false;
    }

    void add_to_cart(String book_id)
    {
	temp+=book_id+" ";
    }

    String cart_items()
    {
	return temp;
    }
    
    int purchase_update(String book_id)
    {
        String ind=book_id.substring(book_id.indexOf('B')+1);
        int index=Integer.parseInt(ind);
        index=index-1;
	String buffer[]=new String[4];
        buffer=book.get(book_id);
	int price=Integer.parseInt(buffer[3]);
        if(!(check_availability(index)))
         stock_refill(); 
	book_count[index]--;
	return price;
    }
}


class Child implements Runnable 
{
	Socket s;
	int ct;
	Child(Socket cs,int count)
	{
		s=cs;ct=count;
	}
	@Override public void run()
	{
		try{
		Scanner sc=new Scanner(System.in);
		DataInputStream dis=new DataInputStream(s.getInputStream());
		DataOutputStream dos=new DataOutputStream(s.getOutputStream());
		User user1=new User();
	    	Book books=new Book();	
		String str;
		while(true){
	    	String id="21BCE5524",pass="Dhanunjay";
		str="Enter the ID:";
		dos.writeUTF(str);
		id=dis.readUTF();
		str="Enter the Password:";
		dos.writeUTF(str);
		pass=dis.readUTF();
		boolean is_valid=user1.match(id,pass);
		if(is_valid)
		{
		  System.out.println("User "+id+" logged in Succesfully");
		  if(!books.stock_available())
		  {
		      books.stock_refill();
		  }
		  str="";
		  str+="Here is the list of Catalog of Books Available in the Store:\n";
		  str+=books.display_catalog();
		  dos.writeUTF(str);
		  str=dis.readUTF();
		  System.out.println("Client Says:"+str);
		  String b_id;
		  String reply="";
		  boolean purchased=false;
		  int no_of_copies;
		  int sum=0;
		  while(!(reply.equals("End")))
		  {
		  str="Dear Customer "+id+" Enter the Book ID you want to purchase/view:";
		  dos.writeUTF(str);
		  b_id=dis.readUTF();
		  str="";
		  str+=books.fetch_book(b_id);
		  dos.writeUTF(str);
		  str=dis.readUTF();
		  System.out.println("Client Says:"+str);
		  str="Click Yes to Purchase (or) Click Ok to Add to Cart (or) Click Ok Ok to Shop items in Cart";
		  dos.writeUTF(str);
		  String Click=dis.readUTF();
		  if(Click.equals("Yes"))
		  {
		       str="Enter the number of copies of book you want:";	
		       dos.writeUTF(str);
		       no_of_copies=Integer.parseInt(dis.readUTF());
		       str="Fetching in Process.Hold a Second!";
		       
		       System.out.println("Dispensing the Book Succesfully");
		       while(no_of_copies>0)
		       {
		       	   sum+=books.purchase_update(b_id);
			   no_of_copies--;
		       }
		       System.out.println("Succesfully Dispensed the Book.");
		       purchased=true;
		  }
		  else if(Click.equals("Ok"))
		  {
			books.add_to_cart(b_id);
		  }
		  else if(Click.equals("Ok Ok"))
		  {
			System.out.println("Dispensing the Books Succesfully");
			String temp=books.cart_items();
			String buffer[]=temp.split(" ");
			//String item="";
			for(String item:buffer)
			{
			    sum+=books.purchase_update(item);
			}
			System.out.println("Succesfully Dispensed the Book.");
			purchased=true;
		  }
		  str="Still want to buy/View more books?";
		  dos.writeUTF(str);
		  reply=dis.readUTF();
		  }
		  if(purchased){
			str="\n-----------------\n";
			str+="|Customer Name:"+id+"|\n"+"|Bill Amount:"+sum+"|\n";
			str+="------------------\n";
		        dos.writeUTF(str);
		        str=dis.readUTF();
		        //System.out.println("Client Says:"+str);
			if(str.equals("Paid")){
				str="Payment Received\nDear Customer "+id+" Thanks For Shopping with us and Have a Good Study!";
				dos.writeUTF(str);
				str=dis.readUTF();
				System.out.println("Client Says:"+str);

			}
                   }
		   else
		   {
			str="Thanks for Visiting the store.Have a Nice Day!";
			dos.writeUTF(str); 
		   }
		}
		else
		{
		    str="User id-"+id+" and Password-"+pass+" didn't match";
		    dos.writeUTF(str);
		}
		}
		}
		catch(Exception e){
		e.printStackTrace();
		}
	}
}