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
		table.getColumnModel().getColumn(1).setPreferredWidth(500);
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

    //Quando questa funzione viene richiamata mostra la pagina che visualizza la lista dei Capo Cantieri 
	private void showCapoCantieriView(JFrame frame) throws IOException, SQLException {

		Component contents = createListaCapoCantieriComponents(frame);

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che contiene la lista dei CapoCantieri

	private Component createListaCapoCantieriComponents(JFrame frame) throws IOException, SQLException {
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		List<Lavoratore> ListCapoCantiere = new ArrayList<Lavoratore>();
		ListCapoCantiere = lavoratore.getAllCapoCantieri();

		String[] columnNames = { "ID", "NOME", "COGNOME", "DATA NASCITA" }; // Lista degli header della tabella
		Object[][] data = Helper.ConvertOperaioListToObject(ListCapoCantiere); // Elementi della tabella

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
						showModificaCapoCantieriView(frame, idLav);
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
		
		
		JButton addCapoCantiereButton = new JButton("Aggiungi Capo Cantiere"); // Bottone per aggiungere un capocantiere
		addCapoCantiereButton.setPreferredSize(new Dimension(100, 50));
		addCapoCantiereButton.setFont(new Font("Arial", Font.PLAIN, 20));
		addCapoCantiereButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				try {
					showCreaCapocantieriView(frame);
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

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout
				.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel).addComponent(scrollPanel)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(addCapoCantiereButton))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(mainMenuButton)))));
		
		
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(pageLabel).addComponent(scrollPanel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(mainMenuButton).addComponent(addCapoCantiereButton)));

		return panelContainer;
	}

	// Quando questa funzione viene richiamata mostra la pagina che consente la
	// modifica dei cantieri
	private void showModificaCantiereView(JFrame frame, Integer idCant) throws IOException, SQLException {

		cantiere = cantiere.getCantiere(idCant);

		Component contents = createModificaCantiereComponents(frame, cantiere); // Utilizzo la funzione che mi
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
	private Component createModificaCantiereComponents(JFrame frame, Cantiere oldCantiere) {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);

		JLabel pageLabel = new JLabel("Modifica Cantiere"); // Titolo
		pageLabel.setFont(headerFont);

		JLabel nomeLabel = new JLabel("NOME"); // Label dei vari campi da modificare
		nomeLabel.setFont(labelFont);
		JLabel DescrizioneLabel = new JLabel("DESCRIZIONE");
		DescrizioneLabel.setFont(labelFont);

		JTextField nomeTF = new JTextField(oldCantiere.getNome()); // Campi da modificare
		nomeTF.setFont(normalFont);
		JTextField descrizioneTF = new JTextField(String.valueOf(oldCantiere.getDescrizione()));
		descrizioneTF.setFont(normalFont);

		nomeLabel.setLabelFor(nomeTF); // Associo le label ai vari campi
		DescrizioneLabel.setLabelFor(descrizioneTF);

		JButton salvaButton = new JButton("Salva"); // Bottone per aggiornare il cantiere
		salvaButton.setPreferredSize(new Dimension(100, 50));
		salvaButton.setBackground(Color.GREEN);
		salvaButton.setForeground(Color.BLACK);
		salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));

		salvaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { // Funzione di salvataggio sul click
				String nome = nomeTF.getText(); // Leggo i valori nei campi
				String descrizione = descrizioneTF.getText();

				Boolean result = false;

				Cantiere cantiere = new Cantiere();

				if (nome.isEmpty() || descrizione.isEmpty()) {
					// Controllo che non ci siano campi vuoti
					setMessage("Tutti i campi devono essere compilati");
				} else {

					cantiere.setIdCantiere(oldCantiere.getIdCantiere());
					cantiere.setNome(nome);
					cantiere.setDescrizione(descrizione);

					try {
						result = cantiere.saveCantiere(cantiere);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (result) { // Se il salvataggio del cantiere avviene con successo
						Helper.showSuccessMessage(frame, "Cantiere salvato correttamente");
						try {
							showListaCantieriView(frame); // Torna alla lista dei cantieri
						} catch (IOException | SQLException e) {
							// TODO Auto-generated catch block
							Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire
																								// qualche
																								// errore durante il
																								// cambio
																								// pagina(connessione
																								// fallita per esempio)
							e.printStackTrace();
						}
					} else { // Altrimenti se il salvataggio del cantiere non avviene con successo
						Helper.showErrorMessage(frame, getMessage());
					}
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
//				int risposta = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare l'operaio?",
//						"ELIMINA OPERAIO", JOptionPane.YES_NO_OPTION);
//				if (risposta == 0) {
//					Boolean result = false;
//					try {
//						result = deleteOperaio(oldCantiere.getIdCantiere());
//					} catch (IOException | SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//					if (result) { // Se l'eliminazione avviene con successo
//						Helper.showSuccessMessage(frame, "Operaio  eliminato correttamente");
//						try {
//							showListaOperaiView(frame, idCant); // Torna alla lista degli operai
//						} catch (IOException | SQLException e) {
//							// TODO Auto-generated catch block
//							Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire
//																								// qualche errore
//																								// durante il cambio
//																								// pagina(connessione
//																								// mancante per esempio)
//							e.printStackTrace();
//						}
//					} else { // Altrimenti se l'eliminazione dei dati non avviene con successo
//						Helper.showErrorMessage(frame, "Errore durante l'eliminazione dell'operaio");
//					}
				// }

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
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(nomeLabel).addComponent(DescrizioneLabel))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(nomeTF).addComponent(descrizioneTF)))
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(salvaButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(tornaAllaListaButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(eliminaButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup() // Definisco il verticalGroup
				.addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeLabel)
						.addComponent(nomeTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(DescrizioneLabel)
						.addComponent(descrizioneTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
						.addComponent(tornaAllaListaButton).addComponent(eliminaButton)));

		return panelContainer;
	}

	// Quando questa funzione viene richiamata mostra la pagina che consente la
	// modifica dei capocantieri
	private void showModificaCapoCantieriView(JFrame frame, Integer idLav) throws IOException, SQLException {

		lavoratore = lavoratore.getLavoratore(idLav);

		Component contents = createModificaCapocantieriComponents(frame, lavoratore); // Utilizzo la funzione che mi
		// restituisce
		// un
		// panel da inserire nel frame

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che consente la modifica del
	// CapoCantiere
	private Component createModificaCapocantieriComponents(JFrame frame, Lavoratore oldCapoCantiere) {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);

		JLabel pageLabel = new JLabel("Modifica Capo Cantiere"); // Titolo
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

		JTextField nomeTF = new JTextField(oldCapoCantiere.getNome()); // Campi da modificare
		nomeTF.setFont(normalFont);
		JTextField cognomeTF = new JTextField(String.valueOf(oldCapoCantiere.getCognome()));
		cognomeTF.setFont(normalFont);

		String oldDataNascita = oldCapoCantiere.getDataNascita();
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

		nomeLabel.setLabelFor(nomeTF); // Associo le label ai vari campi
		cognomeLabel.setLabelFor(cognomeTF);
		dataNascitaGiornoLabel.setLabelFor(dataNascitaGiornoTF);
		dataNascitaMeseLabel.setLabelFor(dataNascitaGiornoTF);
		dataNascitaAnnoLabel.setLabelFor(dataNascitaGiornoTF);

		JButton salvaButton = new JButton("Salva"); // Bottone per aggiornare il cantiere
		salvaButton.setPreferredSize(new Dimension(100, 50));
		salvaButton.setBackground(Color.GREEN);
		salvaButton.setForeground(Color.BLACK);
		salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));

		salvaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { // Funzione di salvataggio sul click
				String nome = nomeTF.getText(); // Leggo i valori nei campi
				String cognome = cognomeTF.getText();
				String dataNGiorno = dataNascitaGiornoTF.getText();
				String dataNMese = dataNascitaMeseTF.getText();
				String dataNAnno = dataNascitaAnnoTF.getText();
				String dataNascita = dataNGiorno + "/" + dataNMese + "/" + dataNAnno;
				Boolean result = false;

				Lavoratore capoCantiere = new Lavoratore();

				if (nome.isEmpty() || cognome.isEmpty() || dataNGiorno.isEmpty() || dataNMese.isEmpty()
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

							capoCantiere.setIdLav(oldCapoCantiere.getIdLav());
							capoCantiere.setNome(nome);
							capoCantiere.setCognome(cognome);
							capoCantiere.setDataNascita(dataNascitaRiconvertita);

							try {
								result = lavoratore.saveCapocantiere(capoCantiere);
							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								Helper.showErrorMessage(frame, getMessage());
								e.printStackTrace();
							}
						} else {
							setMessage(
									"Il campo data nascita deve avere un formato dd/mm/yyyy e l'operaio deve essere maggiorenne"); // I
							// campi
							// non
							// sono
							// stati
							// compilati
							// correttamente
						}
				}

				if (result) { // Se il salvataggio dell'operaio avviene con successo
					Helper.showSuccessMessage(frame, "Capo Cantiere salvato correttamente");
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
//					int risposta = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare l'operaio?",
//							"ELIMINA OPERAIO", JOptionPane.YES_NO_OPTION);
//					if (risposta == 0) {
//						Boolean result = false;
//						try {
//							result = deleteOperaio(oldCantiere.getIdCantiere());
//						} catch (IOException | SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
				//
//						if (result) { // Se l'eliminazione avviene con successo
//							Helper.showSuccessMessage(frame, "Operaio  eliminato correttamente");
//							try {
//								showListaOperaiView(frame, idCant); // Torna alla lista degli operai
//							} catch (IOException | SQLException e) {
//								// TODO Auto-generated catch block
//								Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire
//																									// qualche errore
//																									// durante il cambio
//																									// pagina(connessione
//																									// mancante per esempio)
//								e.printStackTrace();
//							}
//						} else { // Altrimenti se l'eliminazione dei dati non avviene con successo
//							Helper.showErrorMessage(frame, "Errore durante l'eliminazione dell'operaio");
//						}
				// }

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
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
								.addGroup(layout.createSequentialGroup().addGroup(layout
										.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nomeLabel)
										.addComponent(cognomeLabel).addComponent(dataNascitaLabel))
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(nomeTF).addComponent(cognomeTF)
												.addGroup(layout.createSequentialGroup()
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaGiornoLabel))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaGiornoTF))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaMeseLabel))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaMeseTF))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaAnnoLabel))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
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


	// Funzione utilizzata per creare la pagina che consente la creazione dei cantieri
		private Component createCantiereComponents(JFrame frame) throws IOException, SQLException {
			// TODO Auto-generated method stub
			Font headerFont = new Font("SansSerif", Font.BOLD, 20);
			Font labelFont = new Font("SansSerif", Font.BOLD, 18);
			Font normalFont = new Font("SansSerif", Font.PLAIN, 18);
			Font tableFont = new Font("Arial", Font.PLAIN, 18);

			JLabel pageLabel = new JLabel("Crea cantiere"); // Titolo
			pageLabel.setFont(headerFont);

			JLabel nomeLabel = new JLabel("NOME"); // Label dei vari campi da modificare
			nomeLabel.setFont(labelFont);
			JLabel descrizioneLabel = new JLabel("DESCRIZIONE");
			descrizioneLabel.setFont(labelFont);
		
			JTextField nomeTF = new JTextField(); // Campi da compilare
			nomeTF.setFont(normalFont);
			JTextField descrizioneTF = new JTextField();
			descrizioneTF.setFont(normalFont);
			

			nomeLabel.setLabelFor(nomeTF); // Associo le label ai vari campi
			descrizioneLabel.setLabelFor(descrizioneTF);
	
			JButton salvaButton = new JButton("Salva"); // Bottone per salvare il nuovo operaio
			salvaButton.setPreferredSize(new Dimension(100, 50));
			salvaButton.setBackground(Color.GREEN);
			salvaButton.setForeground(Color.BLACK);
			salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));
			salvaButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) { // Funzione di salvataggio sul click
					String nome = nomeTF.getText(); // Leggo i valori nei campi
					String cognome = descrizioneTF.getText();
					Boolean result = false;

                    

					if (nome.isEmpty() || cognome.isEmpty() ) { // Controllo che non ci siano campi
																						// vuoti
						setMessage("Tutti i campi devono essere compilati");
					} else {
						cantiere.setNome(nome);
						cantiere.setDescrizione(cognome);
						
					
								try {

									result = cantiere.saveCantiere(cantiere);

								} catch (IOException | SQLException e) {
									// TODO Auto-generated catch block
									

						}

					}

					if (result) { // Se il salvataggio del cantiere avviene con successo
						Helper.showSuccessMessage(frame, "Cantiere salvato correttamente");
						try {
							showListaCantieriView(frame); // Torna alla lista dei cantieri
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
							.addGroup(layout.createSequentialGroup()
									.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addComponent(nomeLabel).addComponent(descrizioneLabel))
									.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
											.addComponent(nomeTF).addComponent(descrizioneTF)))
							.addGroup(layout.createSequentialGroup()
									.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addComponent(salvaButton))
									.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
											.addComponent(tornaAllaListaButton))
									)));

			layout.setVerticalGroup(layout.createSequentialGroup() // Definisco il verticalGroup
					.addComponent(pageLabel)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeLabel)
							.addComponent(nomeTF))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(descrizioneLabel)
							.addComponent(descrizioneTF))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
							.addComponent(tornaAllaListaButton)));

			return panelContainer;

		}
		
		
		// Quando questa funzione viene richiamata mostra la pagina che consente la
		// creazione dei cantieri
		private void showCreaCapocantieriView(JFrame frame) throws IOException, SQLException {

			Component contents = createCapoCantieriComponents(frame);

			frame.getContentPane().removeAll(); // Pulisco il frame esistente
			frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
			frame.pack();
			frame.setVisible(true);
		}
		
		// Funzione utilizzata per creare la pagina che consente la creazione dei capocantieri
		private Component createCapoCantieriComponents(JFrame frame) throws IOException, SQLException {
			// TODO Auto-generated method stub
			Font headerFont = new Font("SansSerif", Font.BOLD, 20);
			Font labelFont = new Font("SansSerif", Font.BOLD, 18);
			Font normalFont = new Font("SansSerif", Font.PLAIN, 18);
			Font tableFont = new Font("Arial", Font.PLAIN, 18);

			JLabel pageLabel = new JLabel("Crea Capo Cantiere"); // Titolo
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

			JLabel cantieriLabel = new JLabel("SELEZIONARE IL CANTIERE:");
			dataNascitaAnnoLabel.setFont(labelFont);
			Cantiere cantiere = new Cantiere();

			String[] columnNames = { "ID", "NOME CANTIERE", "DESCRIZIONE" }; // Lista degli header della tabella
			Object[][] data = Helper.ConvertCantiereListToObject(cantiere.getAllCantieri()); // Elementi della
				
			// tabella

			
			JTable table = new JTable(data, columnNames); // Creo la tabella riempendola con i dati
			table.getTableHeader().setFont(headerFont);
			table.setDefaultEditor(Object.class, null); // Rendo la tabella non editabile
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {

					JTable target = (JTable) me.getSource();
					int row = target.getSelectedRow(); // Seleziono la riga
					idCant = (Integer) target.getValueAt(row, 0); // Seleziono l'id corrispondente a quella riga

				}

			});
			table.setFont(tableFont);
			table.setRowHeight(30);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(500);
			table.getColumnModel().getColumn(2).setPreferredWidth(200);

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
					String nome = nomeTF.getText(); // Leggo i valori nei campi
					String cognome = cognomeTF.getText();
					String dataNascitaGiorno = dataNascitaGiornoTF.getText();
					String dataNascitaMese = dataNascitaMeseTF.getText();
					String dataNascitaAnno = dataNascitaAnnoTF.getText();
					String dataNascita = dataNascitaGiorno + "/" + dataNascitaMese + "/" + dataNascitaAnno;
					Boolean result = false;

					Lavoratore capocantiere = new Lavoratore();

					if (nome.isEmpty() || cognome.isEmpty() || dataNascita.isEmpty()) { // Controllo che non ci siano campi
																						// vuoti
						setMessage("Tutti i campi devono essere compilati");
					} else {
						if (Helper.isDate(dataNascita, Helper.dateFormatApp))
							if (Helper.isMaggiorenne(dataNascita)) // Controllo che i valori nei campi non
							// stringa siano validi

							{

								String dataNascitaConvertita = Helper.convertDate(Helper.dateFormatApp,
										Helper.dateTimeFormatDb, dataNascita);
								// Funzione che consente di convertire una data

								capocantiere.setNome(nome);
								capocantiere.setCognome(cognome);
								capocantiere.setDataNascita(dataNascitaConvertita);
								capocantiere.setIdCant(idCant);

								try {

									result = lavoratore.saveCapocantiere(capocantiere);

								} catch (IOException | SQLException e) {
									// TODO Auto-generated catch block
									setMessage(

											"ATTENZIONE: "
													+ "1) Nel DB non ci possono essere due operai con nome, cognome e data di nascita uguali;"
													+ "2) Selezionare il cantiere;"
													+ "3) Ogni cantiere deve avere un solo capocantiere;");
								}
							} else {
								setMessage("L'operaio deve essere maggiorenne");
								// i campi non sono stati compilati correttamente
							}

						else {
							setMessage("Il campo data nascita deve avere un formato dd/mm/yyyy");
							// i campi non sono stati compilati correttamente
						}

					}

					if (result) { // Se il salvataggio dell'operaio avviene con successo
						Helper.showSuccessMessage(frame, "Capo Cantiere salvato correttamente");
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
						showCapoCantieriView(frame); // Torna alla lista degli operai
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
					.addGroup(
							layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(pageLabel)
									.addGroup(layout.createSequentialGroup().addGroup(layout
											.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nomeLabel)
											.addComponent(cognomeLabel).addComponent(dataNascitaLabel))
											.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
													.addComponent(nomeTF).addComponent(cognomeTF)
													.addGroup(layout.createSequentialGroup()
															.addGroup(layout
																	.createParallelGroup(GroupLayout.Alignment.LEADING)
																	.addComponent(dataNascitaGiornoLabel))
															.addGroup(layout
																	.createParallelGroup(GroupLayout.Alignment.LEADING)
																	.addComponent(dataNascitaGiornoTF))
															.addGroup(layout
																	.createParallelGroup(GroupLayout.Alignment.LEADING)
																	.addComponent(dataNascitaMeseLabel))
															.addGroup(layout
																	.createParallelGroup(GroupLayout.Alignment.LEADING)
																	.addComponent(dataNascitaMeseTF))
															.addGroup(layout
																	.createParallelGroup(GroupLayout.Alignment.LEADING)
																	.addComponent(dataNascitaAnnoLabel))
															.addGroup(layout
																	.createParallelGroup(GroupLayout.Alignment.LEADING)
																	.addComponent(dataNascitaAnnoTF)))))

									.addGroup(layout.createSequentialGroup()
											.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
													.addComponent(cantieriLabel)))

									.addGroup(layout.createSequentialGroup().addGroup(
											layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(table)))

									.addGroup(layout.createSequentialGroup()
											.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
													.addComponent(salvaButton))
											.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
													.addComponent(tornaAllaListaButton)))));

			layout.setVerticalGroup(layout.createSequentialGroup() // Definisco il verticalGroup
					.addComponent(pageLabel)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeLabel)
							.addComponent(nomeTF))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cognomeLabel)
							.addComponent(cognomeTF))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(dataNascitaLabel)
							.addComponent(dataNascitaGiornoLabel).addComponent(dataNascitaGiornoTF)
							.addComponent(dataNascitaMeseLabel).addComponent(dataNascitaMeseTF)
							.addComponent(dataNascitaAnnoLabel).addComponent(dataNascitaAnnoTF))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cantieriLabel))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(table))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
							.addComponent(tornaAllaListaButton)));

			return panelContainer;

		}
		
}