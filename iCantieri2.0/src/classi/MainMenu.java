package classi;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class MainMenu {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Component createLoginComponents(JFrame frame)  {
		
		Font btnFont = new Font("Arial", Font.PLAIN, 40);
		
		
		JLabel labelUser = new JLabel("USERNAME:");
		JTextField textUser = new JTextField ();
		JPasswordField textPass=new JPasswordField();
		JLabel labelPass = new JLabel("PASSWORD:");
		JButton loginButton = new JButton("LOGIN!");
		loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                
            	
            	try {
					if (new Credenziali().verificaLogin(textUser.getText(), textPass.getPassword())==false) 
							
							{
						System.out.print("ERRORE");
						
							}
			
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	
            }
        });
		
		
		GridLayout layout = new GridLayout(3, 2);	//Definisco un group layout
		layout.setVgap(20);
		
		JPanel bodyPanel = new JPanel(layout);	//Panel da inviare alla funzione chiamante
		
		bodyPanel.add(labelUser);	//Aggiungo i vari elementi al panel
		bodyPanel.add(textUser);
		bodyPanel.add(labelPass);
		bodyPanel.add(textPass);
		
		bodyPanel.add(loginButton);
		
		bodyPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		
		
		return bodyPanel;
		
	
	
	
		
	}	
		protected static void createAndShowGUI() {	
			
			//Funzione che crea e mostra o il menu principale o il messaggio d'errore per mancata connessione al database
			// TODO Auto-generated method stub
			JFrame.setDefaultLookAndFeelDecorated(true);
			JFrame frame = new JFrame("iCantieri");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setPreferredSize(new Dimension(1366, 768));
			
			try {
				if(new Database().getDefaultConnection() != null) {
					showLogin(frame);
				}else {
					showNoConnection(frame);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showNoConnection(frame);
			}

			
		}

		private static void showNoConnection(JFrame frame) {	
			
			//Funzione che viene richiamata quando non � avvenuta una connessione con il database, mostrer� la relativa View
			// TODO Auto-generated method stub
			MainMenu app = new MainMenu();
			Component contents = app.createNoConnectionComponents();
			
			frame.getContentPane().removeAll();
			frame.setPreferredSize(new Dimension(800, 300));
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		}
		
		
		public static void showLogin(JFrame frame) {	
			//Funzione che viene richiamata quando la connessione con il database � avvenuta, mostrer� la relativa View
			// TODO Auto-generated method stub
			MainMenu app = new MainMenu();
			Component contents = app.createLoginComponents(frame);
			
			
			frame.getContentPane().removeAll();
			frame.setPreferredSize(new Dimension(400, 200));
			frame.setResizable(false);
			frame.getContentPane().add(contents, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		}
		
		
		private Component createNoConnectionComponents() {	
			//Funzione utilizzata per creare la pagina che contiene il messaggio d'errore di connessione
			// TODO Auto-generated method stub
			JLabel errorLabel = new JLabel("Non sono riuscito a connettermi al database, controllare il file di configurazione e riprovare.");
			errorLabel.setFont(new Font("Serif", Font.PLAIN, 20));
			
			JButton closeButton = new JButton("CHIUDI");
			closeButton.setPreferredSize(new Dimension(100, 30));
			closeButton.setBackground(Color.RED);
			closeButton.setForeground(Color.WHITE);
			closeButton.setFont(new Font("Arial", Font.PLAIN, 20));
			closeButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	                closeApplication();
	            }
	        });
			
			GridLayout layout = new GridLayout(2, 1);
			layout.setVgap(20);
			
			JPanel panelContainer = new JPanel(layout);
			panelContainer.add(errorLabel);
			panelContainer.add(closeButton);
			
			panelContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
			
			return panelContainer;
		}
		
		protected void closeApplication() {	//Funzione utilizzata per chiudere l'applicazione
			// TODO Auto-generated method stub
			System.exit(0);
		}
		
		
}
