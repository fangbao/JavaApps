package edu.stevens.dns.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class LoginGUI extends JFrame
{
	private JButton jButton1; 

	private JButton jButton2;

	private JLabel jLabel1;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JPanel jPanel;

	private JTextField username;

	private JTextField hostAddress;

	private JTextField port;

	private JRadioButton one2Btn,two1Btn;
	
	public JRadioButton getOne2Btn() {
		return one2Btn;
	}

	public void setOne2Btn(JRadioButton one2Btn) {
		this.one2Btn = one2Btn;
	}

	public LoginGUI(String name)
	{
		super(name);

		initComponents(); // initialize UI
	}

	private void initComponents()
	{
		jPanel = new JPanel();

		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();

		username = new JTextField(12);
		hostAddress = new JTextField(12);
		port = new JTextField(12);

		jButton1 = new JButton();
		jButton2 = new JButton();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setLocation(500, 250);

		jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("User Login"));

		jLabel1.setText("UserName:");
		jLabel2.setText("Server:       ");
		jLabel3.setText("Port:           ");

		jButton1.setText("Login");
		jButton2.setText("Reset");
		
		
		
		jButton1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				LoginGUI.this.login(e);
			}
		});

		username.setText("matt");
		hostAddress.setText("localhost");
		port.setText("5000");
		
		jPanel.add(jLabel1);
		jPanel.add(username);
		jPanel.add(jLabel2);
		jPanel.add(hostAddress);
		jPanel.add(jLabel3);
		jPanel.add(port);

		jPanel.add(jButton1);
		jPanel.add(jButton2);
		
		one2Btn=new JRadioButton("One Two-way");
		one2Btn.setSelected(true);
		two1Btn=new JRadioButton("Two One-ways");
		ButtonGroup bg=new ButtonGroup();
		bg.add(one2Btn);
		bg.add(two1Btn);
		jPanel.add(new JLabel("TCP communication mode:"));
		jPanel.add(one2Btn);
		jPanel.add(two1Btn);

		this.getContentPane().add(jPanel);

		this.setSize(250, 300);
		this.setVisible(true);
	}
	
	private void login(ActionEvent event)
	{
		String username = this.username.getText();
		String hostAddress = this.hostAddress.getText();
		String port = this.port.getText();
		
		AppConnection clientConnection = new AppConnection(this, hostAddress, Integer.parseInt(port), username,this.one2Btn.isSelected());
		
		if(clientConnection.login())
		{
			clientConnection.start();
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Username repeat��", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	
	

	public static void main(String[] args)
	{
		new LoginGUI("App Login");
	}

}
