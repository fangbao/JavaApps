package edu.stevens.dns.resolver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Set;

import edu.stevens.dns.dnsParsing.DNSParsing;
import edu.stevens.dns.pojo.Domain;
import edu.stevens.dns.util.CharacterUtil;
import edu.stevens.dns.util.XMLUtil;



public class ResolverMessageThread extends Thread
{
	private ResolverGUI resolverGUI;
	
	private InputStream is;
	
	private OutputStream os;
	
	public ResolverMessageThread(ResolverGUI resolverGUI, Socket socket)
	{
		try
		{
			this.resolverGUI = resolverGUI;
			
			this.is = socket.getInputStream();
			
			this.os = socket.getOutputStream();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	public ResolverMessageThread(ResolverGUI resolverGUI, Socket receiveSocket,Socket sendSocket)
	{
		try
		{
			this.resolverGUI = resolverGUI;
			
			this.is = receiveSocket.getInputStream();
			
			this.os = sendSocket.getOutputStream();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	//updateUserList
	public void updateUserList()
	{
		//get set of users
		Set<String> users = this.resolverGUI.getMap().keySet();
	
		
		String str = "";
		
		for(String user : users)
		{
			str += user + "\n";
		}
		
		//update 
		this.resolverGUI.getJTextArea().setText(str);
		
	}
	
	public void updateCacheList(){
		Domain[] cache=this.resolverGUI.getCache();
		String str="";
		
		for(Domain d:cache){
			if(d!=null)
				str+=d.getName()+"    "+d.getIP_Address();
		}
		
		//update 
		this.resolverGUI.getJTextArea2().setText(str);
	}
	
	public void updateCacheList2(){
		Domain[] cache=this.resolverGUI.getCache();
		String str="";
		
		for(Domain d:cache){
			if(d!=null)
				str+="<inqueryTimes:"+d.getInquireTimes()+">  "+d.getName()+"    "+d.getIP_Address();
		}
		
		//update 
		this.resolverGUI.getJTextArea2().setText(str);
	}
	
	//send message to client
	public void sendMessage(String message)
	{
		try
		{
			os.write(message.getBytes());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	//process Query with LRU algorithm
	public String processQuery(String domain){
		Domain[] cache=resolverGUI.getCache();
		
System.out.println("******the domains in cache(before the inqury)**********");
		for(Domain d:cache){
				if(d==null)
System.out.println("null");
				else
System.out.println(d.getName());
		}
		
		for(Domain d:cache){
			if(d!=null&&domain.equals(d.getName())){
				sort(d,cache);
System.out.println("Domain is in the cache");
				return d.getIP_Address();
			}
		}
		
System.out.println("Domain is not in the cache and send request to DNS SERVER");
	
		//PLEASE PAY ATTENTION HERE
		//SECOND PART OF THIS ASSIGNMENT, SEND UDP REQUEST TO DNS SERVER AND PARSE RESPONE 
		DNSParsing parse = new DNSParsing();   
		String result = parse.getIPRecords(domain, resolverGUI.getDNSServerIP());
		if((!result.equals(""))&&(!result.equals("time_out"))&&(!result.equals("207.223.0.140\n"))){
			Domain newDomain=new Domain();
			newDomain.setName(domain);
			newDomain.setIP_Address(result);
			sort2(newDomain, cache);
		}
		return result;
	}
	
	
	//LRU
	public void sort(Domain d,Domain[] domains){
		for(int i=0;i<4;i++){
			if(domains[i]!=null&&domains[i].getName().equals(d.getName())){
				for(int j=i;j>0;j--){
					domains[j]=domains[j-1];
				}
				domains[0]=d;
				break;
			}
		}
	}
	
	public void sort2(Object newObject,Object[] objects){
		for(int i=3;i>0;i--){
			objects[i]=objects[i-1];
		}
		objects[0]=newObject;
	}
	
	
	//process Query with LFU algorithm
	public String processQuery2(String domain){
		Domain[] cache=resolverGUI.getCache();
		
System.out.println("******the domains in cache(before the inqury)**********");
		for(Domain d:cache){
				if(d==null)
System.out.println("null");
				else
System.out.println(d.getName()+"   inquiringTimes:"+d.getInquireTimes());
		}
		
		for(Domain d:cache){
			if(d!=null&&domain.equals(d.getName())){
				sort(d,cache);
				d.setInquireTimes(d.getInquireTimes()+1);
System.out.println("Domain is in the cache");
				return d.getIP_Address();
			}
		}
		
System.out.println("Domain is not in the cache and send request to DNS SERVER");
		DNSParsing parse = new DNSParsing();   
		String result = parse.getIPRecords(domain, resolverGUI.getDNSServerIP());
		if((!result.equals(""))&&(!result.equals("time_out"))&&(!result.equals("207.223.0.140\n"))){
			Domain newDomain=new Domain();
			newDomain.setName(domain);
			newDomain.setIP_Address(result);
			newDomain.setInquireTimes(1);
			for(int i=0;i<4;i++){
				if(cache[i]==null){
					sort2(newDomain,cache);
					return result;
				}			
			}
			sort3(newDomain, cache);
		}
		return result;
	}
	
	//LFU
	public void sort3(Domain newDomain,Domain[] domains){
				
		int temps[]=new int[4];
		for(int i=0;i<4;i++){
			if(domains[i]!=null)
				temps[i]=domains[i].getInquireTimes();
		}
		
		
		int[] y = new int[temps.length];
		System.arraycopy(temps, 0, y, 0, temps.length);
		Arrays.sort(y);
		
		int pos=0;  //the position of smallest number and least recently used for the substitute
		for (int i = 3; i >=0; i--) {
			if (temps[i] == y[0]) {
				pos=i;
				break;
			}
		}
		
		for(int j=pos;j>0;j--){
				domains[j]=domains[j-1];
		}
		domains[0]=newDomain;
	}
	
	
	
	
	
	
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				byte[] buf = new byte[5000];
				
				int length = this.is.read(buf);
				
				//message from client
				String xml = new String(buf,0,length);
				
				int type = Integer.parseInt(XMLUtil.extractType(xml));
				
				// querying data
				if(CharacterUtil.CLIENT_MESSAGE == type)
				{
					//Username
					String username = XMLUtil.extractUsername(xml);
					//querying data
					String content = XMLUtil.extractContent(xml);
					
					String result="";
					if(resolverGUI.getLRUBtn().isSelected()){
						//query catch or send UDPpakcet to DNS server
						 result = processQuery(content);
						
						//show new cache list on server
						updateCacheList();
					}else{
						 result = processQuery2(content);
						 updateCacheList2();
					}
					
					//construct XML for message
					String messageXML = XMLUtil.constructServerMessageXML(result);
					
					this.sendMessage(messageXML);
					/*Map<String, ServerMessageThread> map = this.resolverGUI.getMap();
					
					Collection<ServerMessageThread> cols = map.values();
					
					for(ServerMessageThread smt : cols)
					{
						
						smt.sendMessage(messageXML);
					}*/
				}
				// close client window
				else if(CharacterUtil.CLOSE_CLIENT_WINDOW == type)
				{
					String username = XMLUtil.extractUsername(xml);
					//Get users to delete objects corresponding to the thread
					ResolverMessageThread smt = this.resolverGUI.getMap().get(username);
					//Constructed close to the client to confirm the information in XML
					String confirmationXML = XMLUtil.constructCloseClientWindowConfirmationXML();
					//Sent  a confirmation message to the client
					smt.sendMessage(confirmationXML);
					
					// remove user from map
					this.resolverGUI.getMap().remove(username);
					//update
					this.updateUserList();
					
					this.is.close();
					this.os.close();
					
					break; // end this thread
				}				
			}
			catch(Exception ex)
			{
				
			}
		}
	}
}
