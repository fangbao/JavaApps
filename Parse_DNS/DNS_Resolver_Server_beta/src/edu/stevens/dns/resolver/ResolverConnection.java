package edu.stevens.dns.resolver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import edu.stevens.dns.util.XMLUtil;



public class ResolverConnection extends Thread
{
	private ServerSocket serverSocket;
	
	private ResolverGUI resolverGUI;
	
	
	public ResolverConnection(ResolverGUI resolverGUI, int port)
	{
		try
		{
			this.resolverGUI = resolverGUI;
			
			this.serverSocket = new ServerSocket(port);
			
			this.resolverGUI.getJLabel2().setText("running...");
			this.resolverGUI.getJButton().setEnabled(false);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
			JOptionPane.showMessageDialog(this.resolverGUI, "Port is occupied��", "Warnning", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				Socket socket = this.serverSocket.accept();
				
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
	
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				
				//Client sent connection information (including the user name)
				String loginXML = new String(buf, 0, length); 
				
				// extract the user name information from Log data from the client
				String username = XMLUtil.extractUsername(loginXML); 
				
				String type=XMLUtil.extractType(loginXML);
				
				String loginResult = null;
				
				//Determine whether the user login is successful
				boolean isLogin = false;
				
				// Determine whether to repeat the user name
				// User name repeated
				if(this.resolverGUI.getMap().containsKey(username))
				{
					loginResult = "failure";
				}
				// User name does not repeated
				else
				{
					loginResult = "success";
					
					isLogin = true;
				}
				
				String xml = XMLUtil.constructLoginResultXML(loginResult);
				
				os.write(xml.getBytes());
				
				
				ResolverMessageThread serverMessageThread=null;
				// If the user login is successful, start the thread
				if(isLogin)
				{
					//one two-way
					if("1".equals(type))
					{
						// Ready to create a new thread to handle the user's querying data, each user will be connected to the corresponding thread 
						serverMessageThread = new ResolverMessageThread(this.resolverGUI, socket);
					}
					//two one-ways
					if("0".equals(type))
					{
						Socket sendSocket=new Socket(socket.getInetAddress(),5001);
						serverMessageThread = new ResolverMessageThread(this.resolverGUI, socket,sendSocket);
					}
					//put the user name and the corresponding thread object into the Map
					this.resolverGUI.getMap().put(username, serverMessageThread);
					
					//update user list on server-side
					serverMessageThread.updateUserList();
					
					serverMessageThread.start();
				}				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
