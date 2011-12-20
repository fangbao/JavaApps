package edu.stevens.dns.app;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import edu.stevens.dns.util.CharacterUtil;
import edu.stevens.dns.util.XMLUtil;

public class AppConnection extends Thread
{
	private String hostAddress;
	
	private int port;
	
	private String username;
	
	private LoginGUI client;
	
	private Socket socket;
	
	private InputStream is;
	
	private OutputStream os;
	
	private AppGUI appGUI;
	
	private boolean one2way;
	
	public AppConnection(LoginGUI client, String hostAddress, int port, String username,boolean mode)
	{
		this.client = client;
		this.hostAddress = hostAddress;
		this.port = port;
		this.username = username;
		this.one2way=mode;
		
		//connect to resolver(local server)
		this.connect2Server();	
	}

	// Connect to the server, called by the constructor
	private void connect2Server()
	{
		try
		{
			this.socket = new Socket(this.hostAddress, this.port);
			
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	// User login, user name sent to the server
	// Return true that the login is successful
	// Return false to login failure
	public boolean login()
	{
		try
		{
			ServerSocket ss=null;
			Socket inSocket=null;
			String xml="";
		
			if(this.one2way)
				xml = XMLUtil.constructLoginXML(this.username);	
			else{		
				xml= XMLUtil.constructTwoWayLoginXML(this.username);
				ss=new ServerSocket(5001);
			}	
			
			os.write(xml.getBytes()); //  send the user's login information to the server(which contains the user name)
			
			byte[] buf = new byte[5000];
			int length = is.read(buf); // Read the response from the server to determine whether the user login is successful
			
			String loginResultXML = new String(buf, 0, length);
			
			String loginResult = XMLUtil.extractLoginResult(loginResultXML);
			
			// Login successful
			if("success".equals(loginResult))
			{
				//Open AppGUI for searching
				this.appGUI = new AppGUI(this);
				
				this.client.setVisible(false);
				
				if(!this.one2way){
					inSocket=ss.accept();
					System.out.println("second way has connected!");
					is=inSocket.getInputStream();
				}
				
				return true;
			}
			// Login failed
			else
			{
				return false;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return false;
	}
	
	public Socket getSocket()
	{
		return socket;
	}

	public void sendMessage(String message, String type)
	{
		try
		{
			int t = Integer.parseInt(type);
			
			String xml = null;
			
			//Clients send queries to the server
			if(CharacterUtil.CLIENT_MESSAGE == t)
			{
				xml = XMLUtil.constructMessageXML(this.username, message);
			}
			//client send specific data to the server when window is closing
			else if(CharacterUtil.CLOSE_CLIENT_WINDOW == t)
			{
				xml = XMLUtil.constructCloseClientWindowXML(this.username);
			}
			
			//send data to server
			this.os.write(xml.getBytes());
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				
				String xml = new String(buf, 0, length);
				
				int type = Integer.parseInt(XMLUtil.extractType(xml));
				
				// data from server
				if(type == CharacterUtil.SERVER_MESSAGE)
				{
					String content = XMLUtil.extractContent(xml);
					if("time_out".equals(content))
						JOptionPane.showMessageDialog(this.appGUI, "time out! Package maybe lost. Please re-send!", "error", JOptionPane.ERROR_MESSAGE);
					else if("".equals(content)||(content.equals("207.223.0.140\n")))
						JOptionPane.showMessageDialog(this.appGUI, "Do not have correct response. Please check input and re-send!", "error", JOptionPane.ERROR_MESSAGE);
					else{
						this.appGUI.getJTextArea1().setText("");
						this.appGUI.getJTextArea1().append(content);
					}
				}
				// close the window in the server
				else if(type == CharacterUtil.CLOSE_SERVER_WINDOW)
				{
					JOptionPane.showMessageDialog(this.appGUI, "server is closed��the proccess will end��", "infomation", JOptionPane.INFORMATION_MESSAGE);
					
					System.exit(0); //client close
				}
				// server confirm closing the window of client
				else if(type == CharacterUtil.CLOSE_CLIENT_WINDOW_CONFIRMATION)
				{
					try
					{
						this.getSocket().getInputStream().close();
						this.getSocket().getOutputStream().close();
						this.getSocket().close();
					}
					catch(Exception ex)
					{
						//ex.printStackTrace();
					}
					finally
					{
						System.exit(0);//exit
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}	
