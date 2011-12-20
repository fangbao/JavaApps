package edu.stevens.dns.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadDNSfromFile {

	public static String read(){
		String dnsServer="";
		try {
			FileInputStream fis=new FileInputStream("/etc/resolv.conf") ;
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr); 
			String line;
			
			while ((line = br.readLine()) != null) {
				if(line.startsWith("nameserver")){
					dnsServer=line.substring(11);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dnsServer;
	}
}
