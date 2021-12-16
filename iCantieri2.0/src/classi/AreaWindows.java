package classi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class AreaWindows {
	
	
	private String message = "Errore durante il salvataggio, verificare che i campi siano compilati correttamente.";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private static int idCant;
	static int idCantSelezionato;

	Area area = new Area();
	
	
	// Funzione che mostra la pagina che contiene la lista delle aree
		public void showListaAreeView(JFrame frame, int idCantiere) throws IOException, SQLException {

			idCant = idCantiere;

			Component contents = null;

			// se idCant è uguale a 0 nella lista mi elenco tutte le aree , mentre se è
			// diverso da zero mi elenco solo le aree di
			// di un particolare cantiere

			if (idCant != 0) {
				List<Area> listArea = area.getAreePerCantiere(idCant);
				// Utilizzo la funzione che mi restituisce un panel da inserire nel frame

				contents = createListaAreeCapoCComponents(listArea, frame);
			}

			else {

				List<Area> listArea = area.getAllAree();
				contents = createListaOperaiAmministratorrComponents(operai, frame);
			}

			frame.getContentPane().removeAll(); // Pulisco il frame esistente
			frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
			frame.pack();
			frame.setVisible(true);
		}
		
		
		// Funzione utilizzata per creare la pagina che contiene la lista degli operai
		// per i CapoCantieri

		private Component createListaAreeCapoCComponents(List<Area> listArea, JFrame frame) {
			Font headerFont = new Font("SansSerif", Font.BOLD, 20);
			Font tableFont = new Font("Arial", Font.PLAIN, 18);

			String[] columnNames = { "ID", "NOME", "IDCANT"}; // Lista degli header della tabella
			Object[][] data = Helper.ConvertAreaListToObject(listArea); // Elementi della tabella

			JTable table = new JTable(data, columnNames); // Creo la tabella riempendola con i dati
			table.getTableHeader().setFont(headerFont);
			table.setDefaultEditor(Object.class, null); // Rendo la tabella non editabile
			table.addMouseListener(new MouseAdapter() { // Creo una funzione che consente di aprire la scheda dell'oggetto
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() == 2) { // Sul doppio click parte la funzione
						JTable target = (JTable) me.getSource();
						int row = target.getSelectedRow(); // Seleziono la riga
						Integer idLav = (Integer) target.getValueAt(row, 0); // Seleziono l'id corrispondente a quella riga
						try {
							showModificaOperaioView(frame, idLav);
						} catch (IOException | SQLException e) {
							// TODO Auto-generated catch block
							Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
																								// errore durante il cambio
																								// pagina(connessione
																								// fallita per esempio)
							e.printStackTrace();
						}
					}
				}
			});
			table.setFont(tableFont);
			table.setRowHeight(30);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(500);
			table.getColumnModel().getColumn(2).setPreferredWidth(200);
			table.getColumnModel().getColumn(3).setPreferredWidth(200);

			JScrollPane scrollPanel = new JScrollPane(table);
			scrollPanel.setPreferredSize(new Dimension(1500, 1000));

			JLabel pageLabel = new JLabel("Lista Operai");
			pageLabel.setFont(headerFont);

			JButton createOperaioButton = new JButton("Aggiungi Operaio"); // Bottone per aggiungere un operaio alla lista
			createOperaioButton.setPreferredSize(new Dimension(100, 50));
			createOperaioButton.setBackground(Color.GREEN);
			createOperaioButton.setForeground(Color.BLACK);
			createOperaioButton.setFont(new Font("Arial", Font.PLAIN, 20));
			createOperaioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						showCreaOperaioView(frame);
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
																							// errore durante il cambio
																							// pagina(connessione fallita
																							// per esempio)
						e.printStackTrace();
					}
				}
			});

			JButton mainMenuButton = new JButton("Torna al menu"); // Bottone per tornare al menu principale
			mainMenuButton.setPreferredSize(new Dimension(100, 50));
			mainMenuButton.setFont(new Font("Arial", Font.PLAIN, 20));
			mainMenuButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

					MainMenu.openCapocWindow(frame, idCant);
				}
			});

			JPanel panelContainer = new JPanel();
			panelContainer.add(pageLabel);
			panelContainer.add(scrollPanel);
			panelContainer.add(createOperaioButton);
			panelContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

			GroupLayout layout = new GroupLayout(panelContainer);
			panelContainer.setLayout(layout);

			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);

			layout.setHorizontalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
							.addComponent(scrollPanel)
							.addGroup(layout.createSequentialGroup()
									.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addComponent(createOperaioButton))
									.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
											.addComponent(mainMenuButton)))));
			layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pageLabel).addComponent(scrollPanel)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(createOperaioButton)
							.addComponent(mainMenuButton)));

			return panelContainer;
		}

	
	

}
