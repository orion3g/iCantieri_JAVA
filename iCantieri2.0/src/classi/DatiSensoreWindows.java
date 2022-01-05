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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DatiSensoreWindows {

	private String message = "Errore durante il salvataggio, verificare che i campi siano compilati correttamente.";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private static int idCant;
	private static int idCantSelezionato;
	DatiSensore datiSensore = new DatiSensore();
	Area area = new Area();
	

	

	// Funzione che mostra la pagina che contiene la lista delle aree
	public void showListaAreeView(JFrame frame, int idCantiere) throws IOException, SQLException {

		idCant = idCantiere;

		Component contents = null;

		// se idCant è uguale a 0 nella lista mi elenco tutte le aree , mentre se è
		// diverso da zero mi elenco solo le aree di
		// di un particolare cantiere

		if (idCant != 0) {
			List<DatiSensore> ListDati = datiSensore.getDatiPerCantiere(idCant);
			// Utilizzo la funzione che mi restituisce un panel da inserire nel frame

			contents = createListaDatiSensoreCapoCComponents(ListDati, frame);
		}

		else {

			List<DatiSensore> listDatiSensore = datiSensore.getAllDati();
			contents = createListaDatiSensoreAmministratorrComponents(listDatiSensore, frame);
		}

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che contiene la lista delle aree
	// per i CapoCantieri

	private Component createListaDatiSensoreCapoCComponents(List<DatiSensore> ListDati, JFrame frame) throws IOException, SQLException {
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);
		
		List<Area> ListArea = new ArrayList<Area>();
		ListArea=area.getAreePerCantiere(idCant);
	
		String[] columnNames = { "IDSENSORE", "RUMORE", "GAS", "ALLARME", "GIORNO" }; // Lista degli header della

		// tabella
		JTable table = new JTable(); // creo tabella
		DefaultTableModel model = new DefaultTableModel(); // creo modello tabella
		model.setColumnIdentifiers(columnNames); // setto il modello tabella con gli header prima dichiarati
		table.setModel(model); // mi imposto la tabella secondo il modello di cui prima

		
		String[] idAreeStrings = new String[area.getAreePerCantiere(idCant).size()];// Mi recupero tutti gli id area da
		// mettere nella combobox
		
		
		//table per visualizzare tutti i dati per tutte le aree del cantiere
		Helper.addElementsTableDatiSensore(ListDati, model);

		for (int i = 0; i < idAreeStrings.length; i++) {
			int x = (ListArea.get(i).getIdArea());
			String s = String.valueOf(x);
			idAreeStrings[i] = s;			

		}
		
		JComboBox comboBoxAree = new JComboBox(idAreeStrings);
			
			
		comboBoxAree.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent e) {

				model.setRowCount(0);
				

				String y = (String) comboBoxAree.getSelectedItem();
				int sceltaArea = Integer.parseInt(y);
				List<DatiSensore> listDatiSensore=null;
				try {
					listDatiSensore = datiSensore.getDatiPerArea(sceltaArea);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// inserisco una alla volta le righe nella tabella in base al numero di righe
				// recuperate per area
				Helper.addElementsTableDatiSensore(listDatiSensore, model);

			}
		});
		
		
		

		table.getTableHeader().setFont(headerFont);
		table.setDefaultEditor(Object.class, null); // Rendo la tabella non editabile
		table.addMouseListener(new MouseAdapter() { // Creo una funzione che consente di aprire la scheda dell'oggetto
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) { // Sul doppio click parte la funzione
					JTable target = (JTable) me.getSource();
					int row = target.getSelectedRow(); // Seleziono la riga
					Integer idLav = (Integer) target.getValueAt(row, 0); // Seleziono l'id corrispondente a quella riga
//					try {
//						showModificaOperaioView(frame, idLav);
//					} catch (IOException | SQLException e) {
//						// TODO Auto-generated catch block
//						Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
//																							// errore durante il cambio
//																							// pagina(connessione
//																							// fallita per esempio)
					// e.printStackTrace();
					// }
				}
			}
		});
		table.setFont(tableFont);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);

		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setPreferredSize(new Dimension(1500, 1000));

		JLabel pageLabel = new JLabel("LISTA DATI SENSORE");
		pageLabel.setFont(headerFont);

		JLabel pageArea = new JLabel("Filtra per area:");
		pageLabel.setFont(headerFont);

		// con questo bottone mi recupero tutti i dati 
		JButton showAllDati = new JButton("Visualizza tutti i dati");
		showAllDati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Helper.addElementsTableDatiSensore(ListDati, model);
			}
		});

		JButton createOperaioButton = new JButton("Aggiungi Dato"); // Bottone per aggiungere un dato alla lista
		createOperaioButton.setPreferredSize(new Dimension(100, 50));
		createOperaioButton.setBackground(Color.GREEN);
		createOperaioButton.setForeground(Color.BLACK);
		createOperaioButton.setFont(new Font("Arial", Font.PLAIN, 20));
		createOperaioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
//				try {
//					showCreaOperaioView(frame);
//				} catch (IOException | SQLException e) {
//					// TODO Auto-generated catch block
//					Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
//																						// errore durante il cambio
//																						// pagina(connessione fallita
//																						// per esempio)
//					e.printStackTrace();
//				}
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

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout
				.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pageArea))
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(comboBoxAree))
						
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(showAllDati)))
				.addComponent(scrollPanel)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(createOperaioButton))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(mainMenuButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(pageArea).addComponent(comboBoxAree).addComponent(showAllDati))
				.addComponent(scrollPanel).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(createOperaioButton).addComponent(mainMenuButton)));

		return panelContainer;
		
	}

	// Funzione utilizzata per creare la pagina che contiene la lista dei sensori
	// per gli Amministratori

	@SuppressWarnings("null")
	private Component createListaDatiSensoreAmministratorrComponents(List<DatiSensore> listDatiSensore, JFrame frame)
			throws IOException, SQLException {

		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		// recupera Arraylist cantiere per vedere tutti i cantieri e metterli nella
		// combobox

		Cantiere cantiere = new Cantiere();
		String[] idCantieriStrings = new String[cantiere.getAllCantieri().size()];// Mi recupero tutti gli idCantiere da

		// mettere nella combobox

		List<Cantiere> ListCantiere = new ArrayList<Cantiere>();
		ListCantiere = cantiere.getAllCantieri();
		

		idCantieriStrings = new String[cantiere.getAllCantieri().size()];
		for (int i = 0; i < idCantieriStrings.length; i++) {
			int x = (ListCantiere.get(i).getIdCantiere());
			String s = String.valueOf(x);
			idCantieriStrings[i] = s;
			ListCantiere.add(cantiere.getAllCantieri().get(i));

		}

		JComboBox comboBoxCantieri = new JComboBox(idCantieriStrings); // creo combobox
		String[] columnNames = { "IDSENSORE", "RUMORE", "GAS", "ALLARME", "GIORNO" }; // Lista degli header della

		// tabella
		JTable table = new JTable(); // creo tabella
		DefaultTableModel model = new DefaultTableModel(); // creo modello tabella
		model.setColumnIdentifiers(columnNames); // setto il modello tabella con gli header prima dichiarati
		table.setModel(model); // mi imposto la tabella secondo il modello di cui prima
		
		
		// se non si filtra niente si recuperano tutti i dati
		Helper.addElementsTableDatiSensore(listDatiSensore, model);
		
		comboBoxCantieri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			
				String s = (String) comboBoxCantieri.getSelectedItem();
				int scelta = Integer.parseInt(s);
	
				
				List<DatiSensore> ListDatiSensore = null;
				try {
					ListDatiSensore = datiSensore.getDatiPerCantiere(scelta);
					// inserisco una alla volta le righe nella tabella in base al numero di righe
					// recuperate per cantiere
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Helper.addElementsTableDatiSensore(ListDatiSensore, model);
	
				
			}
		});				

			
			
			List<Area> ListAree = new ArrayList<Area>();
			ListAree = area.getAllAree();
			

			String[] idAreeStrings = new String[area.getAllAree().size()];
			for (int i = 0; i < idAreeStrings.length; i++) {
				int x = (ListAree.get(i).getIdArea());
				String s = String.valueOf(x);
				idAreeStrings[i] = s;

			}

			JComboBox comboBoxAree = new JComboBox(idAreeStrings); // creo combobox
			
			
		comboBoxAree.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent e) {

				String s = (String) comboBoxAree.getSelectedItem();
				int scelta = Integer.parseInt(s);
				
				
				List<DatiSensore> ListDatiSensore = null;
				try {
					ListDatiSensore = datiSensore.getDatiPerArea(scelta);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// inserisco una alla volta le righe nella tabella in base al numero di righe
				// recuperate per cantiere
				Helper.addElementsTableDatiSensore(ListDatiSensore, model);

			}
		});
		
		
		

		table.getTableHeader().setFont(headerFont);
		table.setDefaultEditor(Object.class, null); // Rendo la tabella non editabile
		table.addMouseListener(new MouseAdapter() { // Creo una funzione che consente di aprire la scheda dell'oggetto
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) { // Sul doppio click parte la funzione
					JTable target = (JTable) me.getSource();
					int row = target.getSelectedRow(); // Seleziono la riga
					Integer idLav = (Integer) target.getValueAt(row, 0); // Seleziono l'id corrispondente a quella riga
//					try {
//						showModificaOperaioView(frame, idLav);
//					} catch (IOException | SQLException e) {
//						// TODO Auto-generated catch block
//						Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
//																							// errore durante il cambio
//																							// pagina(connessione
//																							// fallita per esempio)
					// e.printStackTrace();
					// }
				}
			}
		});
		table.setFont(tableFont);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);

		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setPreferredSize(new Dimension(1500, 1000));

		JLabel pageLabel = new JLabel("LISTA DATI SENSORE");
		pageLabel.setFont(headerFont);

		JLabel pageCantieri = new JLabel("Filtra per cantiere:");
		pageLabel.setFont(headerFont);

		JLabel pageArea = new JLabel("Filtra per area:");
		pageLabel.setFont(headerFont);

//		// con questo bottone mi recupero tutti gli operai
//		JButton showAllOperai = new JButton("Visualizza tutti gli operai");
//		showAllOperai.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent evt) {
//				Helper.addElementsTable(operai, model);
//			}
//		});

		JButton createOperaioButton = new JButton("Aggiungi Dato"); // Bottone per aggiungere un dato alla lista
		createOperaioButton.setPreferredSize(new Dimension(100, 50));
		createOperaioButton.setBackground(Color.GREEN);
		createOperaioButton.setForeground(Color.BLACK);
		createOperaioButton.setFont(new Font("Arial", Font.PLAIN, 20));
		createOperaioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
//				try {
//					showCreaOperaioView(frame);
//				} catch (IOException | SQLException e) {
//					// TODO Auto-generated catch block
//					Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
//																						// errore durante il cambio
//																						// pagina(connessione fallita
//																						// per esempio)
//					e.printStackTrace();
//				}
			}
		});

		JButton mainMenuButton = new JButton("Torna al menu"); // Bottone per tornare al menu principale
		mainMenuButton.setPreferredSize(new Dimension(100, 50));
		mainMenuButton.setFont(new Font("Arial", Font.PLAIN, 20));
		mainMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				MainMenu.openAmministratoreWindow(frame);
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

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout
				.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pageCantieri))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(comboBoxCantieri))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pageArea))
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(comboBoxAree)))
				.addComponent(scrollPanel)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(createOperaioButton))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(mainMenuButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageCantieri)
						.addComponent(comboBoxCantieri).addComponent(pageArea).addComponent(comboBoxAree))
				.addComponent(scrollPanel).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(createOperaioButton).addComponent(mainMenuButton)));

		return panelContainer;
	}
}
