package edu.stevens.dns.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * login : 1
 * login(two one-way mode):0
 * client message : 2
 * server message : 3
 * user list: 4
 * close client window: 5
 * close server window: 6
 * close client window confirmation: 7
 * login result: 8
 *
 */


public class XMLUtil
{
	private static Document constructDocument()
	{
		Document document = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("message");
		
		document.setRootElement(root);
		
		return document;
	}
	
	
	/**
	 * When a client logs, sent to the server-side XML data
	 * @return
	 */
	public static String constructLoginXML(String username)
	{
		Document document = constructDocument();
		
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("1");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		return document.asXML();
	}
	
	/**
	 * When a client logs, sent to the server-side XML data
	 * @return
	 */
	public static String constructTwoWayLoginXML(String username)
	{
		Document document = constructDocument();
		
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("0");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		return document.asXML();
	}
	
	
	/**
	 * Sent from the client log data in the XML parsing out the user name (username)
	 */
	public static String extractUsername(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			
			Document document = saxReader.read(new StringReader(xml));
			
			Element user = document.getRootElement().element("user");
			
			return user.getText();
		}
		catch(Exception ex)
		{
			
		}
		
		return null;
	}
	
	/**
	 * Structure xml data which sent to the client a list of online users 
	 */
	
	public static String constructUserList(Set<String> users)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("4");
		
		for(String user : users)
		{
			Element e = root.addElement("user");
			e.setText(user);
		}
		
		return document.asXML();
	}
	
	/**
	 * Extract information from XML all the information of online users list
	 */
	public static List<String> extractUserList(String xml)
	{
		List<String> list = new ArrayList<String>();
		
		try
		{
			SAXReader saxReader = new SAXReader();
			
			Document document = saxReader.read(new StringReader(xml));
			
			for(Iterator iter = document.getRootElement().elementIterator("user"); iter.hasNext();)
			{
				Element e = (Element)iter.next();
				
				list.add(e.getText());
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 *  Xml parse out the message from the type of value
	 */
	public static String extractType(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			
			Document document = saxReader.read(new StringReader(xml));
			
			Element typeElement = document.getRootElement().element("type");
			
			return typeElement.getText();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * Construct data xml that the client sends to the server 
	 * 
	 * @param username
	 * @param message
	 * @return
	 */
	public static String constructMessageXML(String username, String message)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("2");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		Element content = root.addElement("content");
		content.setText(message);
		
		return document.asXML();
	}
	
	/**
	 * Construct server-side clients to send all data in XML 
	 * 
	 */
	public static String constructServerMessageXML(String message)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("3");
		
		Element content = root.addElement("content");
		content.setText(message);
		
		return document.asXML();
	}
	
	
	
	
	/**
	 * From the client to the chat server sends the data in the XML parse 
	 */
	public static String extractContent(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			
			Document document = saxReader.read(new StringReader(xml));
			
			Element contentElement = document.getRootElement().element("content");
			
			return contentElement.getText();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Construct server-side XML data when window is closed
	 * 
	 */
	public static String constructCloseServerWindowXML()
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("6");
		
		return document.asXML();
	}
	
	/**
	 * Construct client-side XML data when window is closed
	 */
	public static String constructCloseClientWindowXML(String username)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("5");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		return document.asXML();
	}
	
	/**
	 * Constructed to confirm the client closes the server-side XML information
	 */
	
	public static String constructCloseClientWindowConfirmationXML()
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("7");
		
		return document.asXML();
	}
	
	/**
	 * Structure returned to the client resulting XML log
	 */
	public static String constructLoginResultXML(String result)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("8");
		
		Element resultElement = root.addElement("result");
		resultElement.setText(result);
		
		return document.asXML();
	}
	
	/**
	 * Parse out the data from the xml log results
	 */
	
	public static String extractLoginResult(String xml)
	{
		String result = null;
		
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			Element root = document.getRootElement();
			
			result = root.element("result").getText();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return result;
	}
	
}



















