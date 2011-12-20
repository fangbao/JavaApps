package edu.stevens.dns.app;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class AppGUI extends JFrame
{
	private JButton jButton1; 

	
	private JLabel jLabel1;


	private JPanel jPanel;
	private JPanel jPanel2;
	
	private JTextField jTextField;
	
	private JTextArea jTextArea1;
	
	private AppConnection clientConnection;

	

	public AppGUI(AppConnection clientConnection)
	{
		super("App");
		
		this.clientConnection=clientConnection;

		initComponents(); // initialize UI
	}

	private void initComponents()
	{
		jPanel = new JPanel();
		jPanel2 = new JPanel();
		jLabel1 = new JLabel();
		

		jTextField = new JTextField(15);
		

		jButton1 = new JButton();
		jTextArea1 = new javax.swing.JTextArea();
	
		this.setLocation(500, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);

		jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("input:"));
		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("result:"));
		
		jLabel1.setText("domain:");
		

		jButton1.setText("query");
		jTextArea1.setColumns(20);
		jTextArea1.setRows(8);
		this.jTextArea1.setEditable(false);
		
		jButton1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AppGUI.this.sendMessage(e);
			}
		});
		
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					AppGUI.this.clientConnection.sendMessage("client closed", "5");
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		

		
		
		jPanel.add(jLabel1);
		jPanel.add(jTextField);
		

		jPanel.add(jButton1);
		jPanel2.add(jTextArea1);

		this.setLayout(new FlowLayout());
		this.getContentPane().add(jPanel);
		this.getContentPane().add(jPanel2);

		this.setSize(350, 300);
		this.setVisible(true);
	}

	public JTextArea getJTextArea1() {
		return jTextArea1;
	}

	public void setJTextArea1(JTextArea textArea1) {
		jTextArea1 = textArea1;
	}
	
	private void sendMessage(ActionEvent event)
	{
		// querying data
		String message = this.jTextField.getText();
		// clear
		//this.jTextField.setText("");
		// send data to the server
		if(!message.contains(".")){
			JOptionPane.showMessageDialog(this, "Please input correctly!", "Warnning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		this.clientConnection.sendMessage(message, "2");
	}
	
}
