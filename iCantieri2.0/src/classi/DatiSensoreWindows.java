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
import javax.swing.JTextField;
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
	Sensore sensore = new Sensore();

	// Funzione che mostra la pagina che contiene la lista dei dati Sensore
	public void showListaDatiSensoreView(JFrame frame, int idCantiere) throws IOException, SQLException {

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

	private Component createListaDatiSensoreCapoCComponents(List<DatiSensore> ListDati, JFrame frame)
			throws IOException, SQLException {
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		List<Area> ListArea = new ArrayList<Area>();
		ListArea = area.getAreePerCantiere(idCant);

		String[] columnNames = { "IDSENSORE", "RUMORE", "GAS", "ALLARME", "GIORNO" }; // Lista degli header della

		// tabella
		JTable table = new JTable(); // creo tabella
		DefaultTableModel model = new DefaultTableModel(); // creo modello tabella
		model.setColumnIdentifiers(columnNames); // setto il modello tabella con gli header prima dichiarati
		table.setModel(model); // mi imposto la tabella secondo il modello di cui prima

		String[] idAreeStrings = new String[area.getAreePerCantiere(idCant).size()];// Mi recupero tutti gli id area da
		// mettere nella combobox

		// table per visualizzare tutti i dati per tutte le aree del cantiere
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
				List<DatiSensore> listDatiSensore = null;
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

		JButton createOperaioButton = new JButton("AGGIUNGI DATO"); // Bottone per aggiungere un dato alla lista
		createOperaioButton.setPreferredSize(new Dimension(100, 50));
		createOperaioButton.setBackground(Color.GREEN);
		createOperaioButton.setForeground(Color.BLACK);
		createOperaioButton.setFont(new Font("Arial", Font.PLAIN, 20));
		createOperaioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					showListaSensoriView(frame, idCant);
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

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout
				.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pageArea))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(comboBoxAree))

						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(showAllDati)))
				.addComponent(scrollPanel)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(createOperaioButton))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(mainMenuButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageArea)
						.addComponent(comboBoxAree).addComponent(showAllDati))
				.addComponent(scrollPanel).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(createOperaioButton).addComponent(mainMenuButton)));

		return panelContainer;

	}

	// Funzione utilizzata per creare la pagina che contiene la lista dei dati sensori
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
				try {
					showListaSensoriView(frame, idCant);
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

	// Quando questa funzione viene richiamata mostra la pagina che consente
	// l'inserimento di un nuovo dato
	private void showCreaDatiView(JFrame frame, int idSens) throws IOException, SQLException {

		Component contents = createDatiSensoreComponents(frame, idSens);

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	private Component createDatiSensoreComponents(JFrame frame, int idSens) throws IOException, SQLException {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		JLabel pageLabel = new JLabel("Dati Sensore"); // Titolo
		pageLabel.setFont(headerFont);

		JLabel rumoreLabel = new JLabel("RUMORE:"); // Label dei vari campi da modificare
		rumoreLabel.setFont(labelFont);

		JLabel gasLabel = new JLabel("GAS:");
		gasLabel.setFont(labelFont);

		JLabel dataLabel = new JLabel("DATA:");
		dataLabel.setFont(labelFont);

		JTextField rumoreTF = new JTextField(); // Campi da compilare
		rumoreTF.setFont(normalFont);

		JTextField gasTF = new JTextField();
		gasTF.setFont(normalFont);

		JTextField giornoTF = new JTextField();
		giornoTF.setFont(normalFont);

		rumoreLabel.setLabelFor(rumoreTF); // Associo le label ai vari campi
		gasLabel.setLabelFor(gasTF);
		dataLabel.setLabelFor(giornoTF);

		JLabel dataGiornoLabel = new JLabel("Giorno(dd)");
		dataGiornoLabel.setFont(labelFont);
		JLabel dataMeseLabel = new JLabel("Mese(MM)");
		dataMeseLabel.setFont(labelFont);
		JLabel dataAnnoLabel = new JLabel("Anno(yyyy)");
		dataAnnoLabel.setFont(labelFont);

		JTextField dataGiornoTF = new JTextField();
		dataGiornoTF.setFont(normalFont);
		JTextField dataMeseTF = new JTextField();
		dataMeseTF.setFont(normalFont);
		JTextField dataAnnoTF = new JTextField();
		dataAnnoTF.setFont(normalFont);

		dataGiornoLabel.setLabelFor(dataGiornoTF);
		dataMeseLabel.setLabelFor(dataMeseTF);
		dataAnnoLabel.setLabelFor(dataAnnoTF);


		JButton salvaButton = new JButton("AGGIUNGI!"); // Bottone per salvare il nuovo dato
		salvaButton.setPreferredSize(new Dimension(100, 50));
		salvaButton.setBackground(Color.GREEN);
		salvaButton.setForeground(Color.BLACK);
		salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));
		salvaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { // Funzione di salvataggio sul click
				String r1 = rumoreTF.getText();
				String g1 = gasTF.getText();
				String dataGiorno = dataGiornoTF.getText();
				String dataMese = dataMeseTF.getText();
				String dataAnno = dataAnnoTF.getText();
				String data = dataGiorno + "/" + dataMese + "/" + dataAnno;
				Boolean result = false;
				
				DatiSensore datiSensore= new DatiSensore();
				

				if (r1.isEmpty() || g1.isEmpty()|| dataGiorno.isEmpty() || dataMese.isEmpty()
						|| dataAnno.isEmpty()) {
					// Controllo che non ci siano campi vuoti
					setMessage("Tutti i campi devono essere compilati");
				} else {
					if (Helper.isDate(data, Helper.dateFormatApp))
						

						{

						Float rumore = Float.parseFloat(rumoreTF.getText()); // Leggo i valori nei campi
						Float gas = Float.parseFloat(gasTF.getText());
						
							// Controllo che i valori nei campi non stringa siano validi
							String dataRiconvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, data);
							// Funzione che consente di converte il formato della data

							datiSensore.setDatoRumore(rumore);
							datiSensore.setDatoGas(gas);
							datiSensore.setDataGiorno(dataRiconvertita);
							datiSensore.setIdSens(idSens);
							
						

							try {
								result =  datiSensore.saveDatoSensore(datiSensore);
							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								Helper.showErrorMessage(frame, getMessage());
								e.printStackTrace();
							}
						} 
				}

				if (result) { // Se il salvataggio del dato avviene con successo
					Helper.showSuccessMessage(frame, "Dato salvato correttamente");
				
					
					try {
					
						showListaDatiSensoreView(frame, idCant); // Torna alla lista dei dati
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
																							// errore durante il cambio
																							// pagina(connessione
																							// fallita per esempio)
						e.printStackTrace();
					}
				} else { // Altrimenti se il salvataggio del dato non avviene con successo
					Helper.showErrorMessage(frame, getMessage());
				}

				

			}
		});

		JButton tornaAllaListaButton = new JButton("Torna alla lista"); // Bottone per tornare alla lista dei dati
		tornaAllaListaButton.setPreferredSize(new Dimension(100, 50));
		tornaAllaListaButton.setFont(new Font("Arial", Font.PLAIN, 20));
		tornaAllaListaButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				try {
					showListaDatiSensoreView(frame, idCant); // Torna alla lista dei dati
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

		JPanel panelContainer = new JPanel();
		panelContainer.add(pageLabel);
	
		panelContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		GroupLayout layout = new GroupLayout(panelContainer);
		panelContainer.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup() // Definisco l'horizontalGroup
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(rumoreLabel).addComponent(gasLabel).addComponent(dataLabel))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(rumoreTF).addComponent(gasTF)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataGiornoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataGiornoTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataMeseLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataMeseTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataAnnoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataAnnoTF)))))



						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(salvaButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(tornaAllaListaButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup() // Definisco il verticalGroup
				.addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(rumoreLabel)
						.addComponent(rumoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(gasLabel)
						.addComponent(gasTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(dataLabel)
						.addComponent(dataGiornoLabel).addComponent(dataGiornoTF).addComponent(dataMeseLabel)
						.addComponent(dataMeseTF).addComponent(dataAnnoLabel).addComponent(dataAnnoTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
						.addComponent(tornaAllaListaButton)));

		return panelContainer;

	}

	// Funzione che mostra la pagina che contiene la lista dei sensori
	public void showListaSensoriView(JFrame frame, int idCantiere) throws IOException, SQLException {

		idCant = idCantiere;

		Component contents = null;

		// se idCant è uguale a 0 nella lista mi elenco tutte le aree , mentre se è
		// diverso da zero mi elenco solo le aree di
		// di un particolare cantiere

		if (idCant != 0) {
			List<Sensore> ListSensore = sensore.getSensoriPerCantiere(idCant);
			// Utilizzo la funzione che mi restituisce un panel da inserire nel frame

			contents = createListaSensoreCapoCComponents(ListSensore, frame);
		}

		else {

			List<Sensore> ListSensore = sensore.getAllSensori();
			contents = createListaSensoreAmministratoreComponents(ListSensore, frame);
		}

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che contiene la lista dei sensori
	// per gli Amministratori

	@SuppressWarnings("null")
	private Component createListaSensoreAmministratoreComponents(List<Sensore> listSensore, JFrame frame)

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
		String[] columnNames = { "IDAREA", "IDSENSORE"}; // Lista degli header della

		// tabella
		JTable table = new JTable(); // creo tabella
		DefaultTableModel model = new DefaultTableModel(); // creo modello tabella
		model.setColumnIdentifiers(columnNames); // setto il modello tabella con gli header prima dichiarati
		table.setModel(model); // mi imposto la tabella secondo il modello di cui prima

		// se non si filtra niente si recuperano tutti i dati
		Helper.addElementsTableSensore(listSensore, model);

		comboBoxCantieri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String s = (String) comboBoxCantieri.getSelectedItem();
				int scelta = Integer.parseInt(s);

				List<Sensore> ListSensore = null;
				try {
					ListSensore = sensore.getSensoriPerCantiere(scelta);
					// inserisco una alla volta le righe nella tabella in base al numero di righe
					// recuperate per cantiere

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Helper.addElementsTableSensore(ListSensore, model);

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

				List<Sensore> ListSensore = null;
				try {
					ListSensore = sensore.getSensoriPerArea(scelta);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// inserisco una alla volta le righe nella tabella in base al numero di righe
				// recuperate per cantiere
				Helper.addElementsTableSensore(ListSensore, model);

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
//						try {
//							showModificaOperaioView(frame, idLav);
//						} catch (IOException | SQLException e) {
//							// TODO Auto-generated catch block
//							Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
//																								// errore durante il cambio
//																								// pagina(connessione
//																								// fallita per esempio)
					// e.printStackTrace();
					// }
				}
			}
		});
		table.setFont(tableFont);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);

		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setPreferredSize(new Dimension(1500, 1000));

		JLabel pageLabel = new JLabel("SELEZIONARE IL SENSORE:");
		pageLabel.setFont(headerFont);

		JLabel pageCantieri = new JLabel("Filtra per cantiere:");
		pageLabel.setFont(headerFont);

		JLabel pageArea = new JLabel("Filtra per area:");
		pageLabel.setFont(headerFont);

//			// con questo bottone mi recupero tutti gli operai
//			JButton showAllOperai = new JButton("Visualizza tutti gli operai");
//			showAllOperai.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent evt) {
//					Helper.addElementsTable(operai, model);
//				}
//			});

		JButton createOperaioButton = new JButton("AVANTI"); // Bottone per aggiungere un dato alla lista
		createOperaioButton.setPreferredSize(new Dimension(100, 50));
		createOperaioButton.setBackground(Color.BLUE);
		createOperaioButton.setForeground(Color.BLACK);
		createOperaioButton.setFont(new Font("Arial", Font.PLAIN, 20));
		createOperaioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
				
					int idSens  = (int) table.getValueAt(table.getSelectedRow(), 1);
					showCreaDatiView(frame, idSens);
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

	// Funzione utilizzata per creare la pagina che contiene la lista dei sensori
		// per i capocantieri

		@SuppressWarnings("null")
	private Component createListaSensoreCapoCComponents(List<Sensore> listSensore, JFrame frame)

				throws IOException, SQLException {

			Font headerFont = new Font("SansSerif", Font.BOLD, 20);
			Font tableFont = new Font("Arial", Font.PLAIN, 18);

			// recupera Arraylist cantiere per vedere tutti i cantieri e metterli nella
			// combobox
	
			String[] columnNames = { "IDAREA", "IDSENSORE"}; // Lista degli header della

			// tabella
			JTable table = new JTable(); // creo tabella
			DefaultTableModel model = new DefaultTableModel(); // creo modello tabella
			model.setColumnIdentifiers(columnNames); // setto il modello tabella con gli header prima dichiarati
			table.setModel(model); // mi imposto la tabella secondo il modello di cui prima

			// se non si filtra niente si recuperano tutti i dati
			Helper.addElementsTableSensore(listSensore, model);


			List<Area> ListAree = new ArrayList<Area>();
			ListAree = area.getAreePerCantiere(idCant);

			String[] idAreeStrings = new String[area.getAreePerCantiere(idCant).size()];
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

					List<Sensore> ListSensore = null;
					try {
						ListSensore = sensore.getSensoriPerArea(scelta);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// inserisco una alla volta le righe nella tabella in base al numero di righe
					// recuperate per cantiere
					Helper.addElementsTableSensore(ListSensore, model);

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
//							try {
//								showModificaOperaioView(frame, idLav);
//							} catch (IOException | SQLException e) {
//								// TODO Auto-generated catch block
//								Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
//																									// errore durante il cambio
//																									// pagina(connessione
//																									// fallita per esempio)
						// e.printStackTrace();
						// }
					}
				}
			});
			table.setFont(tableFont);
			table.setRowHeight(30);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);

			JScrollPane scrollPanel = new JScrollPane(table);
			scrollPanel.setPreferredSize(new Dimension(1500, 1000));

			JLabel pageLabel = new JLabel("SELEZIONARE IL SENSORE:");
			pageLabel.setFont(headerFont);


			JLabel pageArea = new JLabel("Filtra per area:");
			pageLabel.setFont(headerFont);

//				// con questo bottone mi recupero tutti gli operai
//				JButton showAllOperai = new JButton("Visualizza tutti gli operai");
//				showAllOperai.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent evt) {
//						Helper.addElementsTable(operai, model);
//					}
//				});

			JButton createOperaioButton = new JButton("AVANTI"); // Bottone per aggiungere un dato alla lista
			createOperaioButton.setPreferredSize(new Dimension(100, 50));
			createOperaioButton.setBackground(Color.BLUE);
			createOperaioButton.setForeground(Color.BLACK);
			createOperaioButton.setFont(new Font("Arial", Font.PLAIN, 20));
			createOperaioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						int idSens  = (int) table.getValueAt(table.getSelectedRow(), 1);
						showCreaDatiView(frame, idSens);
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

			JButton mainMenuButton = new JButton("Torna indietro"); // Bottone per tornare al menu principale
			mainMenuButton.setPreferredSize(new Dimension(100, 50));
			mainMenuButton.setFont(new Font("Arial", Font.PLAIN, 20));
			mainMenuButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

					try {
						showListaDatiSensoreView(frame, idCant);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
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
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
									)
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
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(pageArea).addComponent(comboBoxAree))
					.addComponent(scrollPanel).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(createOperaioButton).addComponent(mainMenuButton)));

			return panelContainer;
		}
	
}
