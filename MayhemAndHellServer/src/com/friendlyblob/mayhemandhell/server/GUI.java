package com.friendlyblob.mayhemandhell.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

public class GUI extends SwingWorker<String, Object> {

	public static class GuiView extends JFrame{
		public GuiView() {
			setTitle("Simple example");
			setSize(300, 200);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE); 
			this.setVisible(true);
			
			JButton button = new JButton("On");
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Shutdown.getInstance().run();
				}
			});
			
			this.getContentPane().add(button);
		}
	}

	@Override
	protected String doInBackground() throws Exception {
		new GuiView();
		return "yup";
	}
	
}
