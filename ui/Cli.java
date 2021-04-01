package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class Cli {

	Parser myParser=new Parser();
	
	public void showCli()
	{
		final JFrame frame = new JFrame("HTMLdb");
		frame.setBounds(10,10,800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		
		JLabel lblCmd = new JLabel("Command");
		lblCmd.setBounds(10,10,70,25);
		frame.add(lblCmd);

		final JTextField txtCmd = new JTextField();
		txtCmd.setBounds(80,10, 500, 25);
		txtCmd.setColumns(100);
		frame.add(txtCmd);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(600, 10, 91, 23);
		frame.add(btnOk);


		final JEditorPane jep = new JEditorPane();
		jep.setBounds(10,50,760,500);
		jep.setContentType("text/html");
		Border border = new LineBorder(Color.black, 1);
		jep.setBorder(border);
		jep.setEditable(false);
        frame.add(jep);
        frame.setVisible(true);
        

        JRootPane rootPane = SwingUtilities.getRootPane(btnOk); 
        rootPane.setDefaultButton(btnOk);


		btnOk.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{	
				jep.setText("");
				String result=new String();
				String command=new String();
				command=txtCmd.getText();
				//System.out.println("command"+command);
				if(!command.isEmpty())
				{
					result=myParser.parse(command);
					jep.setText(result);
					txtCmd.setText("");
					txtCmd.requestFocus();

				}
			}

		});


	}
}
