package edu.stevens.dns.util;


import java.util.ArrayList;
import java.net.*;
public class ByteArray
{
	private ArrayList<Short> Header=new ArrayList<Short>();
	public ByteArray()
	{
			
	}
	public void add(short e)
	{
		byte [] b=new byte[2];
		b=short2bytes(e);
		Header.add(Short.valueOf(b[0]));
		Header.add(Short.valueOf(b[1]));
	}
	public void add(byte[] b)
	{
		int i=0;
		while(i<b.length)
		{
			Header.add(Short.valueOf(b[i]));
			i++;
		}
	}		
	public byte[] getBytes()
	{
		byte[] b=new byte[Header.size()];
		for(int i=0;i<Header.size();i++)
		{
			b[i]=Header.get(i).byteValue();
		}
		return b;
	}
	public void clear()
	{
		Header.clear();
	}
	public static byte[] short2bytes(short x)
	{ 
		byte[] b = new byte[2]; 
		for (int i = b.length; i > 0; i--)
		{ 
			b[i - 1] = (byte) x; 
  			x >>= 8; 
  		} 
		return b; 
	} 

	public static short bytes2short(byte[] b, int startIndex)
	{ 
		short re = 0; 
		for (int i = startIndex; i < startIndex + 2; i++)
		{ 
			re <<= 8; 
			re += b[i]; 
		} 
  	return re; 
	}
	public static int bytestoint(byte[] b, int startIndex)
	{ 
		int re = 0; 
		for (int i = startIndex; i < startIndex + 4; i++)
		{ 
			re <<= 8; 
			re += b[i]; 
		} 
		return re; 
	} 
	public static short unsignedByte(byte b)
	{
		return (short)((b>>7&1)==1?b+256:b);
	}
	public static InetAddress strtourl(String url)
	{
		byte [] Url =new byte[4];
		String [] url_arr=url.split("\\.");
		if(url_arr.length==4)
		{
			Url[0]=(byte)Integer.parseInt(url_arr[0]);
			Url[0]=(byte)Integer.parseInt(url_arr[0]);
			Url[0]=(byte)Integer.parseInt(url_arr[0]);
			Url[0]=(byte)Integer.parseInt(url_arr[0]);
			try
			{
				return InetAddress.getByAddress(Url);
			}catch(java.net.UnknownHostException e)
			{
				System.out.println(e);
			}
		}
			return null;
	}
}