package edu.stevens.dns.dnsParsing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import edu.stevens.dns.util.ByteArray;



/**
 * 
 * @author FangBao
 *
 */
public class DNSParsing {
	private String theDnsServer; // The current DNS server address
	private DatagramPacket outPk; // Sending packet
	private DatagramPacket inPk; // Receiving data packets
	private DatagramSocket UDPSocket; // UDPSocket
	private int position, id, length;// variables needed for analysis of the DNS records
	private String DMname; // domain name

	private static int DNS_PORT = 53; // DNS server Port
	private byte[] pkdata = new byte[512]; // 512-byte packet

	public DNSParsing() // Constructor
	{
		id = (new java.util.Date()).getSeconds() * 60
				* (new java.util.Random()).nextInt();
		// Get unique ID
	}

	// The following properties and methods exposed

	public void setDnsServer(String dnsserver) {
		theDnsServer = dnsserver; // Set the current DNS server name or IP
	}

	public String getIPRecords(String dm) // Array of records to get all DNSIP
	{
		return (getIPRecords(dm, theDnsServer));
	}

	public String getIPRecords(String dm, String DNSServerIP) {
		try {
			// gain InetAddress of DNS server
			outPk = new DatagramPacket(pkdata, pkdata.length, new InetSocketAddress(DNSServerIP,DNS_PORT));
			// sending packet
			UDPSocket = new DatagramSocket(); 
			makeDNSQuery(id, dm); // Generate the query message
			
			UDPSocket.setSoTimeout(6000);//set time-out
			
			UDPSocket.send(outPk); // Send data packets
			inPk = new DatagramPacket(pkdata, pkdata.length);
			UDPSocket.receive(inPk); // Receiving the response message 
			pkdata = inPk.getData();//Get bytes
			length = pkdata.length;
		} catch (UnknownHostException ue) {
			ue.printStackTrace();
		}catch(SocketTimeoutException ste){
			ste.printStackTrace();
			return "time_out";
		}catch (SocketException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}finally{
			UDPSocket.close();
		}
		return (getResponse()); // Analysis the returned data packet, record the results obtained
	}

	public void makeDNSQuery(int id, String dm) {// generated the query data in PKdate byte array 
		for (int i = 0; i < 512; i++) {
			pkdata[i] = 0;
		}

		pkdata[0] = (byte) (id >> 8);
		pkdata[1] = (byte) (id & 0xff); // Check the identity of 2 bytes
		pkdata[2] = (byte) 1; // if Qrbit is 1��indicate it is querying message
		pkdata[3] = (byte) 0;
		pkdata[4] = (byte) 0;
		pkdata[5] = (byte) 1; // 1 question
		pkdata[6] = (byte) 0; // Resource records, authorization resource record, the additional resource records are 0, because the query message
		pkdata[7] = (byte) 0;
		pkdata[8] = (byte) 0;
		pkdata[9] = (byte) 0;
		pkdata[10] = (byte) 0;
		pkdata[11] = (byte) 0;
		StringTokenizer st = new StringTokenizer(dm, ".");// .Separate domain
		String label;
		position = 12; // 12 bytes starting from the first generation query problem
		while (st.hasMoreTokens()) {
			label = st.nextToken();
			pkdata[position++] = (byte) (label.length() & 0xFF);// Converted to a byte
			byte[] b = label.getBytes();
			for (int j = 0; j < b.length; j++) {
				pkdata[position++] = b[j];
			}
		}
		pkdata[position++] = (byte) 0;// The end of the domain name is 0
		pkdata[position++] = (byte) 0;
		pkdata[position++] = (byte) 1; // Type 1 indicate it is A-record
		pkdata[position++] = (byte) 0;
		pkdata[position++] = (byte) 1; // on Internet
	}// Construct the query  completed

	private String getResponse() // get response
	{
		String temp = "";
		int qCount = ((pkdata[4] & 0xff) << 8) | (pkdata[5] & 0xFF); // get the number of question
		if (qCount < 0) {
			return ("");
		} // if question is less than 0, return an empty string
		int aCount = ((pkdata[6] & 0xff) << 8) | (pkdata[7] & 0xff);// get answers

		if (aCount < 0) {
			return ("");
		} // if answer is less than 0, return an empty string
		position = 12;// start position after header
		for (int i = 0; i < qCount; i++) {
			DMname = "";
			position = Proc(position);
			position += 4; // add querying type,class 
		}
		for (int i = 0; i < aCount; i++) {
System.out.println("ip is....");
			position+=2;	 //the method of Compression+offset(2 bytes)			
		 	position+=8;     //Type 2 bytes,Class 2 bytes,TTL 4 bytes	
			position+=2;	//Rdatalength 2 bytes
			temp+=ByteArray.unsignedByte(pkdata[position])+"."
				+ByteArray.unsignedByte(pkdata[position+1])+"."
				+ByteArray.unsignedByte(pkdata[position+2])+"."
				+ByteArray.unsignedByte(pkdata[position+3])+"\n";
System.out.println(temp);
			position+=4;
		}
		return (temp);
	}

	private int Proc(int position) {// find domain part from pkdata array
		int len = (pkdata[position++] & 0xff); //  obtain the length of identifiers to be processed 
		if (len == 0)// No other identifiers, return end
		{
			return position;
		}
		int offset;// offset
		do {
			if ((len & 0xc0) == 0xc0) // compress method
			{
				if (position >= length) // error (large than the size of packet)
				{
					return (-1);
				}
				offset = ((len & 0x3f) << 8) | (pkdata[position++] & 0xff); // Get compressed source identifier offset
				Proc(offset);// Recursive call to get the name before compression
				return (position);
			} else // non-compression
			{
				if ((position + len) > length) // Over the length 
				{
					return (-1);
				}
				DMname += new String(pkdata, position, len); //Get the various parts of the domain name identifier
				position += len;
			}
			if (position > length) {
				return (-1);
			}
			len = pkdata[position++] & 0xff;//end with 0
			if (len != 0) {
				DMname += ".";// add "." to construct completed domain
			}
		} while (len != 0);
		return (position);
	}

	//test
	public static void main(String args[]) throws Exception {
		DNSParsing parse = new DNSParsing();
		String s = parse.getIPRecords("stevens.du", "155.246.1.21");
		//System.out.println(s);
	}
}