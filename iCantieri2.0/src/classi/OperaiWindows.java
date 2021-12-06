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
import java.sql.ResultSet;
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

public class OperaiWindows {

	private String message = "Errore durante il salvataggio, verificare che i campi siano compilati correttamente.";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private static int idCant;
	
	Lavoratore lavoratore=new Lavoratore();
	

	
	// Funzione che restituisce una MAP contenente tutti gli operai del
		// contenuti in una tabella nel database


	// Funzione che mostra la pagina che contiene la lista degli operai
	public void showListaOperaiView(JFrame frame, int idCantiere) throws IOException, SQLException {

		idCant = idCantiere;

		Component contents = null;

		// se idCant è uguale a 0 nella lista mi elenco tutti gli operai, mentre se è
		// diverso da zero mi elenco solo gli operai
		// di un particolare cantiere

		if (idCant != 0) {
			List<Lavoratore> operai = lavoratore.getOperaiPerCant(idCant);
			// Utilizzo la funzione che mi restituisce un panel da inserire nel frame

			contents = createListaOperaiCapoCComponents(operai, frame);
		}

		else {
			
			List<Lavoratore> operai = lavoratore.getAllOperai();
			contents = createListaOperaiAmministratorrComponents(operai, frame);
		}

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che contiene la lista degli operai per i CapoCantieri

	private Component createListaOperaiCapoCComponents(List<Lavoratore> operai, JFrame frame) {
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		String[] columnNames = { "ID", "NOME", "COGNOME", "DATA NASCITA" }; // Lista degli header della tabella
		Object[][] data = Helper.ConvertOperaioListToObject(operai); // Elementi della tabella

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
	
	// Funzione utilizzata per creare la pagina che contiene la lista degli operai per gli Amministratori
	
	private Component createListaOperaiAmministratorrComponents(List<Lavoratore> operai, JFrame frame) {
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		String[] columnNames = { "ID", "NOME", "COGNOME", "DATA NASCITA", "IDCANTIERE" }; // Lista degli header della tabella
		Object[][] data = Helper.ConvertOperaioListToObjectAmministratore(operai); // Elementi della tabella

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

	// Quando questa funzione viene richiamata mostra la pagina che consente la
	// modifica degli operai
	private void showModificaOperaioView(JFrame frame, Integer idLav) throws IOException, SQLException {

		
		lavoratore=lavoratore.getOperaio(idLav);
		
		Component contents = createModificaOperaioComponents(frame, lavoratore); // Utilizzo la funzione che mi restituisce
																				// un
																				// panel da inserire nel frame

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Quando questa funzione viene richiamata mostra la pagina che consente la
	// creazione degli operai
	private void showCreaOperaioView(JFrame frame) throws IOException, SQLException {
		Component contents = createCreaOperaioComponents(frame); // Utilizzo la funzione che mi restituisce un panel da
																	// inserire nel frame

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che consente la modifica degli
	// operai
	private Component createModificaOperaioComponents(JFrame frame, Lavoratore oldLavoratore) {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);

		JLabel pageLabel = new JLabel("Modifica Operaio"); // Titolo
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

		JTextField nomeTF = new JTextField(oldLavoratore.getNome()); // Campi da modificare
		nomeTF.setFont(normalFont);
		JTextField cognomeTF = new JTextField(String.valueOf(oldLavoratore.getCognome()));
		cognomeTF.setFont(normalFont);

		String oldDataNascita = oldLavoratore.getDataNascita();
		String dataNascitaConvertita = Helper.convertDate(Helper.dateTimeFormatDb, Helper.dateFormatApp,
				oldDataNascita); // Funzione
									// che
									// consente
									// di
									// converte
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

		JButton salvaButton = new JButton("Salva"); // Bottone per aggiornare l'operaio
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

				Lavoratore operaio = new Lavoratore();

				if (nome.isEmpty() || cognome.isEmpty() || dataNGiorno.isEmpty() || dataNMese.isEmpty()
						|| dataNAnno.isEmpty()) {
					// Controllo che non ci siano campi vuoti
					setMessage("Tutti i campi devono essere compilati");
				} else {
					if (Helper.isDate(dataNascita, Helper.dateFormatApp))
						if (Helper.isIntegerPositive(dataNascita))

						{

							// Controllo che i valori nei campi non stringa siano validi
							String dataNascitaRiconvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, dataNascita);
							// Funzione che consente di converte il formato della data

							operaio.setIdLav(oldLavoratore.getIdLav());
							operaio.setNome(nome);
							operaio.setCognome(cognome);
							operaio.setDataNascita(dataNascitaRiconvertita);

							try {
								result = saveOperaio(operaio);
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
					Helper.showSuccessMessage(frame, "Operaio salvato correttamente");
					try {
						showListaOperaiView(frame, idCant); // Torna alla lista degli operai
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
				int risposta = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare l'operaio?",
						"ELIMINA OPERAIO", JOptionPane.YES_NO_OPTION);
				if (risposta == 0) {
					Boolean result = false;
					try {
						result = deleteOperaio(oldLavoratore.getIdLav());
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (result) { // Se l'eliminazione avviene con successo
						Helper.showSuccessMessage(frame, "Operaio  eliminato correttamente");
						try {
							showListaOperaiView(frame, idCant); // Torna alla lista degli operai
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
						Helper.showErrorMessage(frame, "Errore durante l'eliminazione dell'operaio");
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
					showListaOperaiView(frame, idCant); // Torna alla lista degli operai
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

	// Funzione utilizzata per salvare un operaio. Se è presente l'id nell'oggetto
	// operaio allora aggiorna un operaio esistente, altrimenti ne crea uno nuovo
	protected Boolean saveOperaio(Lavoratore operaio) throws IOException, SQLException {
		// TODO Auto-generated method stub
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		Integer rows = 0;
		String query;
		if (conn != null) {
			if (operaio.getIdLav() == 0) { // Se il film non esiste
				query = "INSERT INTO LAVORATORE(nome, cognome, datan, idcant, tipolav) values(?, ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, operaio.getNome());
				pstmt.setString(2, operaio.getCognome());

				pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(operaio.getDataNascita()));
				pstmt.setInt(4, idCant);
				pstmt.setString(5, "OPERAIO SEMPLICE");
			} else { // Se il film esiste
				query = "UPDATE LAVORATORE SET nome = ?, cognome = ?, datan = ? WHERE idlav = ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, operaio.getNome());
				pstmt.setString(2, operaio.getCognome());
				pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(operaio.getDataNascita()));
				pstmt.setInt(4, operaio.getIdLav());
			}

			rows = pstmt.executeUpdate();
			pstmt.close();
		}

		if (rows > 0) {
			return true;
		}
		return false;
	}

	// Funzione utilizzata per eliminare un operaio tramite il suo id
	protected Boolean deleteOperaio(int idLav) throws IOException, SQLException {
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		Integer rows = 0;
		String query;
		if (conn != null) {
			query = "DELETE FROM LAVORATORE WHERE idlav = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idLav);

			rows = pstmt.executeUpdate();
			pstmt.close();
		}

		if (rows > 0) {
			return true;
		}
		return false;
	}

	// Funzione utilizzata per creare la pagina che consente la creazione degli
	// operai
	private Component createCreaOperaioComponents(JFrame frame) {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);

		JLabel pageLabel = new JLabel("Crea Operaio"); // Titolo
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
		
		String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
		JComboBox comboBoxCantiere = new JComboBox(petStrings);
	
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

				
				Lavoratore operaio = new Lavoratore();

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

							operaio.setNome(nome);
							operaio.setCognome(cognome);
							operaio.setDataNascita(dataNascitaConvertita);

							try {
								result = saveOperaio(operaio);
							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								setMessage("Nel DB non ci possono essere due operai con nome, cognome e data di nascita uguali");
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
					Helper.showSuccessMessage(frame, "Operaio salvato correttamente");
					try {
						showListaOperaiView(frame, idCant); // Torna alla lista degli operai
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
					showListaOperaiView(frame, idCant); // Torna alla lista dei film
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
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
						.addComponent(tornaAllaListaButton)));
		
		

		return panelContainer;
	}


	
}
