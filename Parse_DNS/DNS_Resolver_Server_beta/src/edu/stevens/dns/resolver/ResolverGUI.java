package edu.stevens.dns.resolver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.stevens.dns.pojo.Domain;
import edu.stevens.dns.util.ReadDNSfromFile;




public class ResolverGUI extends JFrame
{
	private JLabel jLabel1;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JButton jButton;

	private JRadioButton LRUBtn,LFUBtn;
	
	private JPanel jPanel0;
	
	private JPanel jPanel1;

	private JPanel jPanel2;
	
	private JPanel jPanel3;
	
	private JPanel jPanel4;

	private JScrollPane jScrollPane;

	private JTextArea jTextArea;
	
	private JTextArea jTextArea2;

	private JTextField jTextField;
	
	private  String DNSServerIP;
	
	public String getDNSServerIP() {
		return DNSServerIP;
	}



	public void setDNSServerIP(String dNSServerIP) {
		DNSServerIP = dNSServerIP;
	}



	private Map<String, ResolverMessageThread> map = new HashMap<String, ResolverMessageThread>();
		
	//cache for domains in resolver(LRU  algorithm)
	private Domain[] cache=new Domain[4];
	
	
	public ResolverGUI(String name)
	{
		super(name);

		this.initComponents(); //initialize UI
	}
	
	

	public JLabel getJLabel2()
	{
		return jLabel2;
	}

	public JButton getJButton()
	{
		return jButton;
	}

	public JTextArea getJTextArea()
	{
		return jTextArea;
	}

	public JTextField getJTextField()
	{
		return jTextField;
	}

	public void setJTextField(JTextField textField)
	{
		jTextField = textField;
	}

	private void initComponents()
	{
		jPanel0 = new JPanel();
		jPanel0.setLayout(new BorderLayout());
		jPanel1 = new JPanel();
		jPanel2 = new JPanel();
		jPanel3 = new JPanel();
		jPanel4 = new JPanel();
		
		
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		
		jTextField = new JTextField(4);
		jButton = new JButton();
		jScrollPane = new JScrollPane();
		jTextArea = new JTextArea();
		jTextArea2 = new JTextArea();
		
		LRUBtn=new JRadioButton("LRU");
		LRUBtn.setSelected(true);
		LFUBtn=new JRadioButton("LFU");
		ButtonGroup bg=new ButtonGroup();
		bg.add(LRUBtn);
		bg.add(LFUBtn);
		jPanel4.add(new JLabel("please choose cache algorithm: "));
		jPanel4.add(LRUBtn);
		jPanel4.add(LFUBtn);
		

		jPanel0.setBorder(BorderFactory.createTitledBorder("Resolver infomation"));
		jPanel2.setBorder(BorderFactory.createTitledBorder("users online"));
		jPanel3.setBorder(BorderFactory.createTitledBorder("domains in resolver cache"));

		jTextField.setText("5000");
		
		jLabel1.setText("Server Status:");
		jLabel2.setText("Stop");
		jLabel2.setForeground(new Color(204, 0, 51));
		jLabel3.setText("Port");

		jButton.setText("Start");
		
		
		jButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				ResolverGUI.this.execute(event);
			}
		});
		
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				
		

					System.exit(0);

			}
		});
		

		jPanel1.add(jLabel1);
		jPanel1.add(jLabel2);
		jPanel1.add(jLabel3);
		jPanel1.add(jTextField);
		jPanel1.add(jButton);

		jTextArea.setEditable(false); //Does not allow the user to manually modify the online users list
		jTextArea.setRows(12);
		jTextArea.setColumns(30);
		jTextArea.setForeground(new Color(0, 51, 204));
		
		jTextArea2.setEditable(false); //Does not allow the user to manually modify the online users list
		jTextArea2.setRows(6);
		jTextArea2.setColumns(30);
		jTextArea2.setForeground(new Color(0,100,0));

		jScrollPane.setViewportView(jTextArea); 

		jPanel2.add(jScrollPane);
		
		jPanel3.add(jTextArea2);
		jPanel0.add(jPanel1,BorderLayout.NORTH);
		jPanel0.add(jPanel4,BorderLayout.CENTER);
		this.getContentPane().add(jPanel0, BorderLayout.NORTH);
		this.getContentPane().add(jPanel2, BorderLayout.CENTER);
		this.getContentPane().add(jPanel3, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}

	private void execute(ActionEvent evt)
	{
		int port = Integer.parseInt(this.getJTextField().getText());
		
		new ResolverConnection(this, port).start();
		
		this.LRUBtn.setEnabled(false);
		this.LFUBtn.setEnabled(false);
		
		this.setDNSServerIP(ReadDNSfromFile.read());
	}

	public static void main(String[] args)
	{
		new ResolverGUI("Resolv");
	}



	public Map<String, ResolverMessageThread> getMap() {
		return map;
	}



	public void setMap(Map<String, ResolverMessageThread> map) {
		this.map = map;
	}



	public Domain[] getCache() {
		return cache;
	}



	public void setCache(Domain[] cache) {
		this.cache = cache;
	}



	public JTextArea getJTextArea2() {
		return jTextArea2;
	}



	public void setJTextArea2(JTextArea textArea2) {
		jTextArea2 = textArea2;
	}



	public JRadioButton getLRUBtn() {
		return LRUBtn;
	}



	public void setLRUBtn(JRadioButton btn) {
		LRUBtn = btn;
	}



	public JRadioButton getLFUBtn() {
		return LFUBtn;
	}



	public void setLFUBtn(JRadioButton btn) {
		LFUBtn = btn;
	}

		
	
}
