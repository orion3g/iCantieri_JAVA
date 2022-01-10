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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CantiereWindows {

	private String message = "Errore durante il salvataggio, verificare che i campi siano compilati correttamente.";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	Cantiere cantiere = new Cantiere();
	Lavoratore lavoratore = new Lavoratore();
	int idCant;
	int idCantSelezionato;
	Lavoratore CapoCantiere = new Lavoratore();

	Component contents = null;

	// Funzione che mostra la pagina che contiene la lista dei cantieri
	public void showListaCantieriView(JFrame frame) throws IOException, SQLException {

		List<Cantiere> ListaCantieri = cantiere.getAllCantieri();
		// Utilizzo la funzione che mi restituisce un panel da inserire nel frame

		contents = createListaCantieriComponents(ListaCantieri, frame);

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che contiene la lista dei cantieri

	private Component createListaCantieriComponents(List<Cantiere> cantiere, JFrame frame) {
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		String[] columnNames = { "ID", "NOME", "DESCRIZIONE" }; // Lista degli header della tabella
		Object[][] data = Helper.ConvertCantiereListToObject(cantiere); // Elementi della tabella

		JTable table = new JTable(data, columnNames); // Creo la tabella riempendola con i dati
		table.getTableHeader().setFont(headerFont);
		table.setDefaultEditor(Object.class, null); // Rendo la tabella non editabile
		table.addMouseListener(new MouseAdapter() { // Creo una funzione che consente di aprire la scheda dell'oggetto
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) { // Sul doppio click parte la funzione
					JTable target = (JTable) me.getSource();
					int row = target.getSelectedRow(); // Seleziono la riga
					Integer idCant = (Integer) target.getValueAt(row, 0); // Seleziono l'id corrispondente a quella riga
					try {
						showModificaCantiereView(frame, idCant);
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
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		// table.getColumnModel().getColumn(3).setPreferredWidth(200);

		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setPreferredSize(new Dimension(1500, 1000));

		JLabel pageLabel = new JLabel("LISTA CANTIERI:");
		pageLabel.setFont(headerFont);

		JButton createOperaioButton = new JButton("Aggiungi Cantiere"); // Bottone per aggiungere un cantiere alla lista
		createOperaioButton.setPreferredSize(new Dimension(100, 50));
		createOperaioButton.setBackground(Color.GREEN);
		createOperaioButton.setForeground(Color.BLACK);
		createOperaioButton.setFont(new Font("Arial", Font.PLAIN, 20));
		createOperaioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					showshowCreaCantieriView(frame);
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

		JButton capoCantiereButton = new JButton("Visualizza Capo Cantieri"); // Bottone per visualizzare tutti i
																				// capocantieri
		capoCantiereButton.setPreferredSize(new Dimension(100, 50));
		capoCantiereButton.setFont(new Font("Arial", Font.PLAIN, 20));
		capoCantiereButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				try {
					showCapoCantieriView(frame);
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
				.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel).addComponent(scrollPanel)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(capoCantiereButton))
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(mainMenuButton))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(createOperaioButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pageLabel).addComponent(scrollPanel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(capoCantiereButton)
						.addComponent(createOperaioButton).addComponent(mainMenuButton)));

		return panelContainer;
	}

	// Quando questa funzione viene richiamata mostra la pagina che visualizza la
	// lista dei Capo Cantieri
	private void showCapoCantieriView(JFrame frame) throws IOException, SQLException {

		Component contents = createListaCapoCantieriComponents(frame);

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che contiene la lista dei
	// CapoCantieri

	private Component createListaCapoCantieriComponents(JFrame frame) throws IOException, SQLException {
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		List<Lavoratore> ListCapoCantiere = new ArrayList<Lavoratore>();
		ListCapoCantiere = lavoratore.getAllCapoCantieri();

		String[] columnNames = { "ID", "NOME", "COGNOME", "DATA NASCITA", "IDCANTIERE" }; // Lista degli header della
																							// tabella
		Object[][] data = Helper.ConvertOperaioListToObject(ListCapoCantiere); // Elementi della tabella

		JTable table = new JTable(data, columnNames); // Creo la tabella riempendola con i dati
		table.getTableHeader().setFont(headerFont);
		table.setDefaultEditor(Object.class, null); // Rendo la tabella non editabile
		
		table.setFont(tableFont);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);

		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setPreferredSize(new Dimension(1500, 1000));

		JLabel pageLabel = new JLabel("Lista Capo Cantieri");
		pageLabel.setFont(headerFont);

		JButton mainMenuButton = new JButton("Torna al menu"); // Bottone per tornare al menu principale
		mainMenuButton.setPreferredSize(new Dimension(100, 50));
		mainMenuButton.setFont(new Font("Arial", Font.PLAIN, 20));
		mainMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				try {
					showListaCantieriView(frame);
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
		panelContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		GroupLayout layout = new GroupLayout(panelContainer);
		panelContainer.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
						.addComponent(scrollPanel)
						.addGroup(layout.createSequentialGroup()
								
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(mainMenuButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pageLabel).addComponent(scrollPanel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(mainMenuButton)
						));

		return panelContainer;
	}

	// Quando questa funzione viene richiamata mostra la pagina che consente la
	// modifica dei cantieri
	private void showModificaCantiereView(JFrame frame, Integer idCant) throws IOException, SQLException {

		cantiere = cantiere.getCantiere(idCant);
		CapoCantiere= CapoCantiere.getCapocPerCant(idCant);
		
		Component contents = createModificaCantiereComponents(frame, cantiere, CapoCantiere); // Utilizzo la funzione che mi
																				// restituisce
																				// un
																				// panel da inserire nel frame

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che consente la modifica del
	// Cantiere
	private Component createModificaCantiereComponents(JFrame frame, Cantiere oldCantiere, Lavoratore oldCapoC) {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);
		
		JLabel pageLabel = new JLabel("MODIFICA CANTIERE"); // Titolo
		pageLabel.setFont(headerFont);

		JLabel pageLabelCapoc = new JLabel("MODIFICA CAPOCANTIERE"); // Titolo
		pageLabelCapoc.setFont(headerFont);

		JLabel nomeCantiereLabel = new JLabel("NOME"); // Label dei vari campi da modificare
		nomeCantiereLabel.setFont(labelFont);
		JLabel descrizioneLabel = new JLabel("DESCRIZIONE");
		descrizioneLabel.setFont(labelFont);


		JLabel nomeLabel = new JLabel("NOME"); // Label dei vari campi da modificare
		nomeLabel.setFont(labelFont);
		JLabel cognomeLabel = new JLabel("COGNOME");
		cognomeLabel.setFont(labelFont);
		JLabel dataNascitaLabel = new JLabel("DATA NASCITA");
		dataNascitaLabel.setFont(labelFont);
		JLabel dataNascitaGiornoLabel = new JLabel("Giorno(dd)");
		dataNascitaGiornoLabel.setFont(labelFont);
		JLabel dataNascitaMeseLabel = new JLabel("Mese(MM)");
		dataNascitaMeseLabel.setFont(labelFont);
		JLabel dataNascitaAnnoLabel = new JLabel("Anno(yyyy)");
		dataNascitaAnnoLabel.setFont(labelFont);


		
		JTextField nomeTF = new JTextField(oldCapoC.getNome()); // Campi da compilare
		nomeTF.setFont(normalFont);
		JTextField cognomeTF = new JTextField(oldCapoC.getCognome());
		cognomeTF.setFont(normalFont);
		String oldDataNascita = oldCapoC.getDataNascita();
		String dataNascitaConvertita = Helper.convertDate(Helper.dateTimeFormatDb, Helper.dateFormatApp,
				oldDataNascita); // Funzione
									// che
									// consente
									// di
									// convertire
									// il
									// formato
									// della
									// data

		JTextField dataNascitaGiornoTF = new JTextField(dataNascitaConvertita.substring(0, 2));
		dataNascitaGiornoTF.setFont(normalFont);
		JTextField dataNascitaMeseTF = new JTextField(dataNascitaConvertita.substring(3, 5));
		dataNascitaMeseTF.setFont(normalFont);
		JTextField dataNascitaAnnoTF = new JTextField(dataNascitaConvertita.substring(6, 10));
		dataNascitaAnnoTF.setFont(normalFont);

		
		
		JTextField nomeCantiereTF = new JTextField(oldCantiere.getNome()); // Campi da modificare
		nomeCantiereTF.setFont(normalFont);
		JTextField descrizioneTF = new JTextField(String.valueOf(oldCantiere.getDescrizione()));
		descrizioneTF.setFont(normalFont);
		

		nomeLabel.setLabelFor(nomeTF); // Associo le label ai vari campi
		descrizioneLabel.setLabelFor(descrizioneTF);
		
		nomeLabel.setLabelFor(nomeTF); // Associo le label ai vari campi
		cognomeLabel.setLabelFor(cognomeTF);
		dataNascitaGiornoLabel.setLabelFor(dataNascitaGiornoTF);
		dataNascitaMeseLabel.setLabelFor(dataNascitaMeseTF);
		dataNascitaAnnoLabel.setLabelFor(dataNascitaAnnoTF);

		JButton salvaButton = new JButton("Salva"); // Bottone per aggiornare il cantiere
		salvaButton.setPreferredSize(new Dimension(100, 50));
		salvaButton.setBackground(Color.GREEN);
		salvaButton.setForeground(Color.BLACK);
		salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));

		salvaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { // Funzione di salvataggio sul click
				String nomeCantiere = nomeCantiereTF.getText(); // Leggo i valori nei campi
				String descrizione = descrizioneTF.getText();
				String nome = nomeTF.getText(); // Leggo i valori nei campi
				String cognome = cognomeTF.getText();
				String dataNGiorno = dataNascitaGiornoTF.getText();
				String dataNMese = dataNascitaMeseTF.getText();
				String dataNAnno = dataNascitaAnnoTF.getText();
				String dataNascita = dataNGiorno + "/" + dataNMese + "/" + dataNAnno;

				Boolean result = false;

				Cantiere cantiere = new Cantiere();
				
				

				if (nomeCantiere.isEmpty() || descrizione.isEmpty() ||nome.isEmpty() || cognome.isEmpty() || dataNGiorno.isEmpty() || dataNMese.isEmpty()
						|| dataNAnno.isEmpty()) {
					// Controllo che non ci siano campi vuoti
					setMessage("Tutti i campi devono essere compilati");
				} else {
					if (Helper.isDate(dataNascita, Helper.dateFormatApp))
						if (Helper.isMaggiorenne(dataNascita))

						{

							// Controllo che i valori nei campi non stringa siano validi
							String dataNascitaRiconvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, dataNascita);
							// Funzione che consente di converte il formato della data

							cantiere.setIdCantiere(oldCantiere.getIdCantiere());
							cantiere.setNome(nomeCantiere);
							cantiere.setDescrizione(descrizione);
							
							CapoCantiere.setIdLav(oldCapoC.getIdLav());
							CapoCantiere.setNome(nome);
							CapoCantiere.setCognome(cognome);
							CapoCantiere.setDataNascita(dataNascitaRiconvertita);

							try {
								result = cantiere.saveCantiere(cantiere, CapoCantiere);
							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								Helper.showErrorMessage(frame, getMessage());
								e.printStackTrace();
							}
						} else {
							setMessage(
									"1) Il campo data nascita deve avere un formato dd/mm/yyyy \n"
									+ "2) il capocantiere deve essere maggiorenne"); // I
							// campi
							// non
							// sono
							// stati
							// compilati
							// correttamente
						}
				}

				if (result) { // Se il salvataggio dell'operaio avviene con successo
					Helper.showSuccessMessage(frame, "Cantiere salvato correttamente");
					try {
						showListaCantieriView(frame); // Torna alla lista degli operai
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
																							// errore durante il cambio
																							// pagina(connessione
																							// fallita per esempio)
						e.printStackTrace();
					}
				} else { // Altrimenti se il salvataggio dell'operaio non avviene con successo
					Helper.showErrorMessage(frame, getMessage());
				}
			}
		});

		JButton eliminaButton = new JButton("Elimina"); // Bottone per eliminare l'operaio
		eliminaButton.setPreferredSize(new Dimension(100, 50));
		eliminaButton.setBackground(Color.RED);
		eliminaButton.setForeground(Color.BLACK);
		eliminaButton.setFont(new Font("Arial", Font.PLAIN, 20));
		eliminaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int risposta = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare il cantiere?",
						"ELIMINA CANTIERE", JOptionPane.YES_NO_OPTION);
				if (risposta == 0) {
					Boolean result = false;
					try {
						result = cantiere.deleteCantiere(oldCantiere.getIdCantiere(), oldCapoC.getIdLav());
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (result) { // Se l'eliminazione avviene con successo
						Helper.showSuccessMessage(frame, "Cantiere  eliminato correttamente");
						try {
							showListaCantieriView(frame); // Torna alla lista degli operai
						} catch (IOException | SQLException e) {
							// TODO Auto-generated catch block
							Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire
																								// qualche errore
																								// durante il cambio
																								// pagina(connessione
																								// mancante per esempio)
							e.printStackTrace();
						}
					} else { // Altrimenti se l'eliminazione dei dati non avviene con successo
						Helper.showErrorMessage(frame, "Errore durante l'eliminazione del cantiere");
					}
				 }

			}
		});

		JButton tornaAllaListaButton = new JButton("Torna alla lista"); // Bottone per tornare alla lista dei film
		tornaAllaListaButton.setPreferredSize(new Dimension(100, 50));
		tornaAllaListaButton.setFont(new Font("Arial", Font.PLAIN, 20));
		tornaAllaListaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					showListaCantieriView(frame); // Torna alla lista degli operai
				} catch (IOException | SQLException e) {
					// TODO Auto-generated catch block
					Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
																						// errore durante il cambio
																						// pagina(connessione mancante
																						// per esempio)
					e.printStackTrace();
				}
			}
		});

		JPanel panelContainer = new JPanel(); // Panel da inviare alla funzione chiamante
		panelContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		GroupLayout layout = new GroupLayout(panelContainer); // Definisco un group layout
		panelContainer.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup() // Definisco l'horizontalGroup
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
						.addComponent(pageLabelCapoc)
						.addGroup(layout.createSequentialGroup().addGroup(layout
								.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nomeCantiereLabel)
								.addComponent(descrizioneLabel).addComponent(nomeLabel).addComponent(cognomeLabel)
								.addComponent(dataNascitaLabel))

								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(nomeCantiereTF).addComponent(descrizioneTF).addComponent(nomeTF)
										.addComponent(cognomeTF)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaGiornoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaGiornoTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaMeseLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaMeseTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaAnnoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaAnnoTF)))))					
						.addGroup(layout.createSequentialGroup()	
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(salvaButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(tornaAllaListaButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(eliminaButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup() // Definisco il verticalGroup
				.addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeCantiereLabel)
						.addComponent(nomeCantiereTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(descrizioneLabel)
						.addComponent(descrizioneTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelCapoc))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeLabel)
						.addComponent(nomeTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cognomeLabel)
						.addComponent(cognomeTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(dataNascitaLabel)
						.addComponent(dataNascitaGiornoLabel).addComponent(dataNascitaGiornoTF)
						.addComponent(dataNascitaMeseLabel).addComponent(dataNascitaMeseTF)
						.addComponent(dataNascitaAnnoLabel).addComponent(dataNascitaAnnoTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
						.addComponent(tornaAllaListaButton).addComponent(eliminaButton)));

		return panelContainer;
	}


	// Quando questa funzione viene richiamata mostra la pagina che consente la
	// creazione dei cantieri
	private void showshowCreaCantieriView(JFrame frame) throws IOException, SQLException {

		Component contents = createCantiereComponents(frame);

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che consente la creazione dei
	// cantieri
	private Component createCantiereComponents(JFrame frame) throws IOException, SQLException {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		JLabel pageLabel = new JLabel("Crea cantiere"); // Titolo
		pageLabel.setFont(headerFont);

		JLabel pageLabelCapoc = new JLabel("Crea Capocantiere"); // Titolo
		pageLabelCapoc.setFont(headerFont);

		JLabel nomeCantiereLabel = new JLabel("NOME"); // Label dei vari campi da modificare
		nomeCantiereLabel.setFont(labelFont);
		JLabel descrizioneLabel = new JLabel("DESCRIZIONE");
		descrizioneLabel.setFont(labelFont);

		JLabel pageLabelCantiere = new JLabel("Capo Cantiere"); // Titolo
		pageLabel.setFont(headerFont);

		JLabel nomeLabel = new JLabel("NOME"); // Label dei vari campi da modificare
		nomeLabel.setFont(labelFont);
		JLabel cognomeLabel = new JLabel("COGNOME");
		cognomeLabel.setFont(labelFont);
		JLabel dataNascitaLabel = new JLabel("DATA NASCITA");
		dataNascitaLabel.setFont(labelFont);
		JLabel dataNascitaGiornoLabel = new JLabel("Giorno(dd)");
		dataNascitaGiornoLabel.setFont(labelFont);
		JLabel dataNascitaMeseLabel = new JLabel("Mese(MM)");
		dataNascitaMeseLabel.setFont(labelFont);
		JLabel dataNascitaAnnoLabel = new JLabel("Anno(yyyy)");
		dataNascitaAnnoLabel.setFont(labelFont);

		JTextField nomeTF = new JTextField(); // Campi da compilare
		nomeTF.setFont(normalFont);
		JTextField cognomeTF = new JTextField();
		cognomeTF.setFont(normalFont);
		JTextField dataNascitaGiornoTF = new JTextField();
		dataNascitaGiornoTF.setFont(normalFont);
		JTextField dataNascitaMeseTF = new JTextField();
		dataNascitaMeseTF.setFont(normalFont);
		JTextField dataNascitaAnnoTF = new JTextField();
		dataNascitaAnnoTF.setFont(normalFont);

		JTextField nomeCantiereTF = new JTextField(); // Campi da compilare
		nomeCantiereTF.setFont(normalFont);
		JTextField descrizioneTF = new JTextField();
		descrizioneTF.setFont(normalFont);

		nomeCantiereLabel.setLabelFor(nomeCantiereTF); // Associo le label ai vari campi
		descrizioneLabel.setLabelFor(descrizioneTF);

		nomeLabel.setLabelFor(nomeTF); // Associo le label ai vari campi
		cognomeLabel.setLabelFor(cognomeTF);
		dataNascitaGiornoLabel.setLabelFor(dataNascitaGiornoTF);
		dataNascitaMeseLabel.setLabelFor(dataNascitaMeseTF);
		dataNascitaAnnoLabel.setLabelFor(dataNascitaAnnoTF);

		JButton salvaButton = new JButton("Salva"); // Bottone per salvare il nuovo operaio
		salvaButton.setPreferredSize(new Dimension(100, 50));
		salvaButton.setBackground(Color.GREEN);
		salvaButton.setForeground(Color.BLACK);
		salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));
		salvaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { // Funzione di salvataggio sul click
				String nome = nomeCantiereTF.getText(); // Leggo i valori nei campi
				String descrizione = descrizioneTF.getText();

				String nomeCapoC = nomeTF.getText();
				String cognomeCapoC = cognomeTF.getText();
				String dataNascitaGiorno = dataNascitaGiornoTF.getText();
				String dataNascitaMese = dataNascitaMeseTF.getText();
				String dataNascitaAnno = dataNascitaAnnoTF.getText();
				String dataNascita = dataNascitaGiorno + "/" + dataNascitaMese + "/" + dataNascitaAnno;

				Boolean result = false;

				if (nome.isEmpty() || descrizione.isEmpty() || nomeCapoC.isEmpty() || cognomeCapoC.isEmpty()) {

					// Controllo che non ci siano campi
					setMessage("Tutti i campi devono essere compilati");
				} else {
					if (Helper.isDate(dataNascita, Helper.dateFormatApp))
						if (Helper.isMaggiorenne(dataNascita)) // Controllo che i valori nei campi non
						// stringa siano validi

						{

							String dataNascitaConvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, dataNascita);
							// Funzione che consente di convertire una data

							cantiere.setNome(nome);
							cantiere.setDescrizione(descrizione);

							CapoCantiere.setNome(nomeCapoC);
							CapoCantiere.setCognome(cognomeCapoC);
							CapoCantiere.setDataNascita(dataNascitaConvertita);
							

							try {

								result = cantiere.saveCantiere(cantiere, CapoCantiere);

							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								setMessage(

										"ATTENZIONE: "
												+ "1) Nel DB non ci possono essere due lavoratori con nome, cognome e data di nascita uguali; \n"
												+ "2) Ogni cantiere deve avere un solo capocantiere;");
							}
						} else {
							setMessage("Il capocantiere deve essere maggiorenne");
							// i campi non sono stati compilati correttamente
						}

					else {
						setMessage("Il campo data nascita deve avere un formato dd/mm/yyyy");
						// i campi non sono stati compilati correttamente
					}

				}

				if (result) { // Se il salvataggio dell'operaio avviene con successo
					Helper.showSuccessMessage(frame, "Cantiere salvato correttamente");

					try {
						showCapoCantieriView(frame); // Torna alla lista degli operai
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
																							// errore durante il cambio
																							// pagina(connessione
																							// fallita per esempio)
						e.printStackTrace();
					}
				} else { // Altrimenti se il salvataggio dell'operaio non avviene con successo
					Helper.showErrorMessage(frame, getMessage());
				}
			}
		});

		JButton tornaAllaListaButton = new JButton("Torna alla lista"); // Bottone per tornare alla lista degli operai
		tornaAllaListaButton.setPreferredSize(new Dimension(100, 50));
		tornaAllaListaButton.setFont(new Font("Arial", Font.PLAIN, 20));
		tornaAllaListaButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				try {
					showListaCantieriView(frame); // Torna alla lista dei cantieri
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

		JPanel panelContainer = new JPanel(); // Panel da inviare alla funzione chiamante
		panelContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		GroupLayout layout = new GroupLayout(panelContainer); // Definisco un group layout
		panelContainer.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup() // Definisco l'horizontalGroup
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
						.addComponent(pageLabelCapoc)
						.addGroup(layout.createSequentialGroup().addGroup(layout
								.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nomeCantiereLabel)
								.addComponent(descrizioneLabel).addComponent(nomeLabel).addComponent(cognomeLabel)
								.addComponent(dataNascitaLabel))

								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(nomeCantiereTF).addComponent(descrizioneTF).addComponent(nomeTF)
										.addComponent(cognomeTF)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaGiornoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaGiornoTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaMeseLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaMeseTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaAnnoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaAnnoTF)))))

						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(salvaButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(tornaAllaListaButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup() // Definisco il verticalGroup
				.addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeCantiereLabel)
						.addComponent(nomeCantiereTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(descrizioneLabel)
						.addComponent(descrizioneTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelCapoc))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeLabel)
						.addComponent(nomeTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cognomeLabel)
						.addComponent(cognomeTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(dataNascitaLabel)
						.addComponent(dataNascitaGiornoLabel).addComponent(dataNascitaGiornoTF)
						.addComponent(dataNascitaMeseLabel).addComponent(dataNascitaMeseTF)
						.addComponent(dataNascitaAnnoLabel).addComponent(dataNascitaAnnoTF))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
						.addComponent(tornaAllaListaButton)));

		return panelContainer;

	}




}