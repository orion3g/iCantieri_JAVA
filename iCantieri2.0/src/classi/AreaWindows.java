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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class AreaWindows {

	private String message = "Errore durante il salvataggio, verificare che i campi siano compilati correttamente.";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private static int idCant;
	private static int idCantSelezionato;

	Area area = new Area();
	Sensore sensore = new Sensore();

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
			contents = createListaAreeAmministratoreComponents(listArea, frame);
		}

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che contiene la lista delle aree
	// per i CapoCantieri

	private Component createListaAreeCapoCComponents(List<Area> listArea, JFrame frame) {
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		String[] columnNames = { "ID AREA", "NOME AREA" }; // Lista degli header della tabella
		Object[][] data = Helper.ConvertAreaListToObject(listArea); // Elementi della tabella

		JTable table = new JTable(data, columnNames); // Creo la tabella riempendola con i dati
		table.getTableHeader().setFont(headerFont);
		table.setDefaultEditor(Object.class, null); // Rendo la tabella non editabile
		table.addMouseListener(new MouseAdapter() { // Creo una funzione che consente di aprire la scheda dell'oggetto
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) { // Sul doppio click parte la funzione
					JTable target = (JTable) me.getSource();
					int row = target.getSelectedRow(); // Seleziono la riga
					Integer idArea = (Integer) target.getValueAt(row, 0); // Seleziono l'id corrispondente a quella riga
					try {
						showModificaAreaView(frame, idArea);
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

		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setPreferredSize(new Dimension(1500, 1000));

		JLabel pageLabel = new JLabel("Lista Aree");
		pageLabel.setFont(headerFont);

		JButton createOperaioButton = new JButton("Aggiungi Area"); // Bottone per aggiungere un operaio alla lista
		createOperaioButton.setPreferredSize(new Dimension(100, 50));
		createOperaioButton.setBackground(Color.GREEN);
		createOperaioButton.setForeground(Color.BLACK);
		createOperaioButton.setFont(new Font("Arial", Font.PLAIN, 20));
		createOperaioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					showCreaAreaView(frame);
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

	// Funzione utilizzata per creare la pagina che contiene la lista delle aree
	// per gli Amministratori

	private Component createListaAreeAmministratoreComponents(List<Area> listArea, JFrame frame) {

		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		String[] columnNames = { "ID AREA", "NOME AREA", "IDCANT" }; // Lista degli header della tabella
		Object[][] data = Helper.ConvertAreaListToObject(listArea); // Elementi della tabella

		JTable table = new JTable(data, columnNames); // Creo la tabella riempendola con i dati
		table.getTableHeader().setFont(headerFont);
		table.setDefaultEditor(Object.class, null); // Rendo la tabella non editabile
		table.addMouseListener(new MouseAdapter() { // Creo una funzione che consente di aprire la scheda dell'oggetto
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) { // Sul doppio click parte la funzione
					JTable target = (JTable) me.getSource();
					int row = target.getSelectedRow(); // Seleziono la riga
					Integer idArea = (Integer) target.getValueAt(row, 0); // Seleziono l'id corrispondente a quella riga
					try {
						showModificaAreaView(frame, idArea);
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
		table.getColumnModel().getColumn(2).setPreferredWidth(100);

		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setPreferredSize(new Dimension(1500, 1000));

		JLabel pageLabel = new JLabel("Lista Aree");
		pageLabel.setFont(headerFont);

		JButton createOperaioButton = new JButton("Aggiungi Area"); // Bottone per aggiungere un'area alla lista
		createOperaioButton.setPreferredSize(new Dimension(100, 50));
		createOperaioButton.setBackground(Color.GREEN);
		createOperaioButton.setForeground(Color.BLACK);
		createOperaioButton.setFont(new Font("Arial", Font.PLAIN, 20));
		createOperaioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					showCreaAreaView(frame);
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
	// creazione delle aree
	private void showCreaAreaView(JFrame frame) throws IOException, SQLException {
		Component contents = null;

		// se idCant è uguale a zero crea il frame con la tabella per selezionare anche
		// il cantiere, se è diverso
		// da zero crea il frame senza la possibilità di selezionare il cantiere

		if (idCant != 0) {

			contents = createCreaAreaCapoCComponents(frame); // Utilizzo la funzione che mi restituisce un panel da
																// inserire nel frame
		}

		else
			contents = createCreaAreaAmministratoreComponents(frame);

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che consente la creazione delle
	// aree per i capoCantieri
	private Component createCreaAreaCapoCComponents(JFrame frame) throws IOException, SQLException {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);

		JLabel pageLabel = new JLabel("Crea AREA "); // Titolo
		pageLabel.setFont(headerFont);

		JLabel nomeAreaLabel = new JLabel("NOME AREA:"); // Label dei vari campi da modificare
		nomeAreaLabel.setFont(labelFont);

		JLabel pageLabelResponsabile = new JLabel("RESPONSABILE"); // Titolo
		pageLabelResponsabile.setFont(headerFont);

		JLabel nomeResponsabileLabel = new JLabel("NOME:"); // Label dei vari campi da modificare
		nomeResponsabileLabel.setFont(labelFont);

		JLabel cognomeResponsabileLabel = new JLabel("COGNOME:");
		cognomeResponsabileLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileLabel = new JLabel("DATA NASCITA:");
		dataNascitaResponsabileLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileGiornoLabel = new JLabel("Giorno(dd):");
		dataNascitaResponsabileGiornoLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileMeseLabel = new JLabel("Mese(MM):");
		dataNascitaResponsabileMeseLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileAnnoLabel = new JLabel("Anno(yyyy):");
		dataNascitaResponsabileAnnoLabel.setFont(labelFont);

		JLabel pageLabelOperatore = new JLabel("OPERATORE SENSORI"); // Titolo
		pageLabelOperatore.setFont(headerFont);

		JLabel nomeOperatoreLabel = new JLabel("NOME"); // Label dei vari campi da modificare
		nomeOperatoreLabel.setFont(labelFont);
		JLabel cognomeOperatoreLabel = new JLabel("COGNOME");
		cognomeOperatoreLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreLabel = new JLabel("DATA NASCITA");
		dataNascitaOperatoreLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreGiornoLabel = new JLabel("Giorno(dd)");
		dataNascitaOperatoreGiornoLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreMeseLabel = new JLabel("Mese(MM)");
		dataNascitaOperatoreMeseLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreAnnoLabel = new JLabel("Anno(yyyy)");
		dataNascitaOperatoreAnnoLabel.setFont(labelFont);

		JLabel pageLabelSoglie = new JLabel("IMPOSTA SOGLIE "); // Titolo
		pageLabelSoglie.setFont(headerFont);

		JLabel sogliaRumoreLabel = new JLabel("SOGLIA RUMORE");
		sogliaRumoreLabel.setFont(labelFont);

		JLabel sogliaGasLabel = new JLabel("SOGLIA GAS");
		sogliaGasLabel.setFont(labelFont);

		JTextField sogliaRumoreTF = new JTextField();
		sogliaRumoreTF.setFont(normalFont);
		JTextField sogliaGasTF = new JTextField();
		sogliaGasTF.setFont(normalFont);

		JTextField nomeAreaTF = new JTextField(); // Campi da compilare
		nomeAreaTF.setFont(normalFont);

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

		JTextField nomeOperatoreTF = new JTextField(); // Campi da compilare
		nomeOperatoreTF.setFont(normalFont);
		JTextField cognomeOperatoreTF = new JTextField();
		cognomeOperatoreTF.setFont(normalFont);
		JTextField dataNascitaOperatoreGiornoTF = new JTextField();
		dataNascitaOperatoreGiornoTF.setFont(normalFont);
		JTextField dataNascitaOperatoreMeseTF = new JTextField();
		dataNascitaOperatoreMeseTF.setFont(normalFont);
		JTextField dataNascitaOperatoreAnnoTF = new JTextField();
		dataNascitaOperatoreAnnoTF.setFont(normalFont);

		nomeAreaLabel.setLabelFor(nomeAreaTF);

		nomeResponsabileLabel.setLabelFor(nomeTF); // Associo le label ai vari campi
		cognomeResponsabileLabel.setLabelFor(cognomeTF);
		dataNascitaResponsabileGiornoLabel.setLabelFor(dataNascitaGiornoTF);
		dataNascitaResponsabileMeseLabel.setLabelFor(dataNascitaMeseTF);
		dataNascitaResponsabileAnnoLabel.setLabelFor(dataNascitaAnnoTF);

		nomeOperatoreLabel.setLabelFor(pageLabelOperatore);
		nomeOperatoreLabel.setLabelFor(nomeOperatoreTF);
		cognomeOperatoreLabel.setLabelFor(cognomeOperatoreTF);
		dataNascitaOperatoreGiornoLabel.setLabelFor(dataNascitaOperatoreGiornoTF);
		dataNascitaOperatoreMeseLabel.setLabelFor(dataNascitaOperatoreMeseTF);
		dataNascitaOperatoreAnnoLabel.setLabelFor(dataNascitaOperatoreAnnoTF);

		sogliaRumoreLabel.setLabelFor(sogliaRumoreTF);
		sogliaGasLabel.setLabelFor(sogliaGasTF);

		JButton salvaButton = new JButton("Salva"); // Bottone per salvare la nuova area
		salvaButton.setPreferredSize(new Dimension(100, 50));
		salvaButton.setBackground(Color.GREEN);
		salvaButton.setForeground(Color.BLACK);
		salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));
		salvaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { // Funzione di salvataggio sul click
				// Funzione di salvataggio sul click
				String nome = nomeTF.getText(); // Leggo i valori nei campi
				String cognome = cognomeTF.getText();
				String dataNascitaGiorno = dataNascitaGiornoTF.getText();
				String dataNascitaMese = dataNascitaMeseTF.getText();
				String dataNascitaAnno = dataNascitaAnnoTF.getText();
				String dataNascita = dataNascitaGiorno + "/" + dataNascitaMese + "/" + dataNascitaAnno;
				String nomeOperatore = nomeOperatoreTF.getText();
				String cognomeOperatore = cognomeOperatoreTF.getText();
				String dataNascitaOperatoreGiorno = dataNascitaOperatoreGiornoTF.getText();
				String dataNascitaOperatoreMese = dataNascitaOperatoreMeseTF.getText();
				String dataNascitaOperatoreAnno = dataNascitaOperatoreAnnoTF.getText();
				String dataNascitaOperatore = dataNascitaOperatoreGiorno + "/" + dataNascitaOperatoreMese + "/"
						+ dataNascitaOperatoreAnno;

				String r1 = sogliaRumoreTF.getText();
				String g1 = sogliaGasTF.getText();

				Boolean result = false;

				Lavoratore responsabile = new Lavoratore();
				Lavoratore operatore = new Lavoratore();

				String nomeArea = nomeAreaTF.getText();
				Area area = new Area();

				if (nomeArea.isEmpty() || nome.isEmpty() || cognome.isEmpty() || dataNascitaGiorno.isEmpty()
						|| dataNascitaMese.isEmpty() || dataNascitaAnno.isEmpty() || nomeOperatore.isEmpty()
						|| cognomeOperatore.isEmpty() || dataNascitaOperatoreGiorno.isEmpty()
						|| dataNascitaOperatoreMese.isEmpty() || dataNascitaOperatoreAnno.isEmpty()
						|| r1.isEmpty() || g1.isEmpty()) { // Controllo
					// che
					// non
					// ci
					// siano
					// campi
					// vuoti
					setMessage("Tutti i campi devono essere compilati");
				} else {
					if (Helper.isDate(dataNascita, Helper.dateFormatApp)
							&& (Helper.isDate(dataNascitaOperatore, Helper.dateFormatApp)))
						if (Helper.isMaggiorenne(dataNascita) && Helper.isMaggiorenne(dataNascitaOperatore)) // Controllo
																												// che i
																												// valori
																												// nei
																												// campi
																												// non
						// stringa siano validi

						{
							
							Float rumore = Float.parseFloat(sogliaRumoreTF.getText()); // Leggo i valori nei campi
							Float gas = Float.parseFloat(sogliaGasTF.getText());
							String dataNascitaConvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, dataNascita);
							// Funzione che consente di convertire una data

							String dataNascitaOperatoreConvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, dataNascitaOperatore);

							area.setNome(nomeArea);
							responsabile.setNome(nome);
							responsabile.setCognome(cognome);
							responsabile.setDataNascita(dataNascitaConvertita);
							responsabile.setIdCant(idCant);
							operatore.setNome(nomeOperatore);
							operatore.setCognome(cognomeOperatore);
							operatore.setDataNascita(dataNascitaOperatoreConvertita);
							area.setSogliaGas(gas);
							area.setSogliaRumore(rumore);

							try {

								result = area.saveArea(area, responsabile, operatore);

							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block

							}

						} else {
							setMessage("I lavoratori devono essere maggiorenni");
							// i campi non sono stati compilati correttamente
						}

					else {
						setMessage("Il campo data nascita deve avere un formato dd/mm/yyyy");
						// i campi non sono stati compilati correttamente
					}

				}

				if (result) { // Se il salvataggio dell'area avviene con successo

					Helper.showSuccessMessage(frame, "Area salvata correttamente");
					try {
						showListaAreeView(frame, idCant); // Torna alla lista degli operai
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
					showListaAreeView(frame, idCant); // Torna alla lista degli operai
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
						.addComponent(pageLabelResponsabile).addComponent(pageLabelOperatore)
						.addComponent(pageLabelSoglie)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(nomeAreaLabel).addComponent(nomeResponsabileLabel).addComponent(
												cognomeResponsabileLabel)
										.addComponent(dataNascitaResponsabileLabel))

								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(nomeAreaTF).addComponent(nomeTF).addComponent(cognomeTF)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaResponsabileGiornoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaGiornoTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaResponsabileMeseLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaMeseTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaResponsabileAnnoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaAnnoTF)))))

						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(nomeOperatoreLabel).addComponent(cognomeOperatoreLabel)
										.addComponent(dataNascitaOperatoreLabel))

								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(nomeOperatoreTF).addComponent(cognomeOperatoreTF)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreGiornoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreGiornoTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreMeseLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreMeseTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreAnnoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreAnnoTF)))))

						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(sogliaRumoreLabel).addComponent(sogliaGasLabel))

								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(sogliaRumoreTF).addComponent(sogliaGasTF)))

						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(salvaButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(tornaAllaListaButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup() // Definisco il verticalGroup
				.addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeAreaLabel)
						.addComponent(nomeAreaTF))
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelResponsabile))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeResponsabileLabel)
						.addComponent(nomeTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(cognomeResponsabileLabel).addComponent(cognomeTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(dataNascitaResponsabileLabel).addComponent(dataNascitaResponsabileGiornoLabel)
						.addComponent(dataNascitaGiornoTF).addComponent(dataNascitaResponsabileMeseLabel)
						.addComponent(dataNascitaMeseTF).addComponent(dataNascitaResponsabileAnnoLabel)
						.addComponent(dataNascitaAnnoTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelOperatore))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeOperatoreLabel)
						.addComponent(nomeOperatoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cognomeOperatoreLabel)
						.addComponent(cognomeOperatoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(dataNascitaOperatoreLabel).addComponent(dataNascitaOperatoreGiornoLabel)
						.addComponent(dataNascitaOperatoreGiornoTF).addComponent(dataNascitaOperatoreMeseLabel)
						.addComponent(dataNascitaOperatoreMeseTF).addComponent(dataNascitaOperatoreAnnoLabel)
						.addComponent(dataNascitaOperatoreAnnoTF))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelSoglie))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(sogliaRumoreLabel)
						.addComponent(sogliaRumoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(sogliaGasLabel)
						.addComponent(sogliaGasTF))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
						.addComponent(tornaAllaListaButton)));

		return panelContainer;

	}

	// Funzione utilizzata per creare la pagina che consente la creazione delle
	// aree per gli Amministratori
	private Component createCreaAreaAmministratoreComponents(JFrame frame) throws IOException, SQLException {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);
		Font tableFont = new Font("Arial", Font.PLAIN, 18);

		JLabel pageLabel = new JLabel("Crea AREA "); // Titolo
		pageLabel.setFont(headerFont);

		JLabel nomeAreaLabel = new JLabel("NOME AREA:"); // Label dei vari campi da modificare
		nomeAreaLabel.setFont(labelFont);

		JLabel pageLabelResponsabile = new JLabel("RESPONSABILE"); // Titolo
		pageLabelResponsabile.setFont(headerFont);

		JLabel nomeResponsabileLabel = new JLabel("NOME:"); // Label dei vari campi da modificare
		nomeResponsabileLabel.setFont(labelFont);
		JLabel cognomeResponsabileLabel = new JLabel("COGNOME:");
		cognomeResponsabileLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileLabel = new JLabel("DATA NASCITA:");
		dataNascitaResponsabileLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileGiornoLabel = new JLabel("Giorno(dd):");
		dataNascitaResponsabileGiornoLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileMeseLabel = new JLabel("Mese(MM):");
		dataNascitaResponsabileMeseLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileAnnoLabel = new JLabel("Anno(yyyy):");
		dataNascitaResponsabileAnnoLabel.setFont(labelFont);

		JLabel pageLabelOperatore = new JLabel("OPERATORE SENSORI"); // Titolo
		pageLabelOperatore.setFont(headerFont);

		JLabel nomeOperatoreLabel = new JLabel("NOME"); // Label dei vari campi da modificare
		nomeOperatoreLabel.setFont(labelFont);
		JLabel cognomeOperatoreLabel = new JLabel("COGNOME");
		cognomeOperatoreLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreLabel = new JLabel("DATA NASCITA");
		dataNascitaOperatoreLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreGiornoLabel = new JLabel("Giorno(dd)");
		dataNascitaOperatoreGiornoLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreMeseLabel = new JLabel("Mese(MM)");
		dataNascitaOperatoreMeseLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreAnnoLabel = new JLabel("Anno(yyyy)");
		dataNascitaOperatoreAnnoLabel.setFont(labelFont);

		JLabel pageLabelSoglie = new JLabel("IMPOSTA SOGLIE "); // Titolo
		pageLabelSoglie.setFont(headerFont);

		JLabel sogliaRumoreLabel = new JLabel("SOGLIA RUMORE");
		sogliaRumoreLabel.setFont(labelFont);

		JLabel sogliaGasLabel = new JLabel("SOGLIA GAS");
		sogliaGasLabel.setFont(labelFont);

		JLabel cantieriLabel = new JLabel("SELEZIONARE IL CANTIERE:");
		cantieriLabel.setFont(headerFont);

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
				idCantSelezionato = (Integer) target.getValueAt(row, 0); // Seleziono l'id corrispondente a quella riga

			}

		});
		table.setFont(tableFont);
		table.setRowHeight(30);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);

		JTextField nomeAreaTF = new JTextField(); // Campi da compilare
		nomeAreaTF.setFont(normalFont);

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

		JTextField nomeOperatoreTF = new JTextField(); // Campi da compilare
		nomeOperatoreTF.setFont(normalFont);
		JTextField cognomeOperatoreTF = new JTextField();
		cognomeOperatoreTF.setFont(normalFont);
		JTextField dataNascitaOperatoreGiornoTF = new JTextField();
		dataNascitaOperatoreGiornoTF.setFont(normalFont);
		JTextField dataNascitaOperatoreMeseTF = new JTextField();
		dataNascitaOperatoreMeseTF.setFont(normalFont);
		JTextField dataNascitaOperatoreAnnoTF = new JTextField();
		dataNascitaOperatoreAnnoTF.setFont(normalFont);

		JTextField sogliaRumoreTF = new JTextField();
		sogliaRumoreTF.setFont(normalFont);
		JTextField sogliaGasTF = new JTextField();
		sogliaGasTF.setFont(normalFont);

		nomeAreaLabel.setLabelFor(nomeAreaTF);
		nomeResponsabileLabel.setLabelFor(nomeTF); // Associo le label ai vari campi
		cognomeResponsabileLabel.setLabelFor(cognomeTF);
		dataNascitaResponsabileGiornoLabel.setLabelFor(dataNascitaGiornoTF);
		dataNascitaResponsabileMeseLabel.setLabelFor(dataNascitaMeseTF);
		dataNascitaResponsabileAnnoLabel.setLabelFor(dataNascitaAnnoTF);

		nomeOperatoreLabel.setLabelFor(pageLabelOperatore);
		nomeOperatoreLabel.setLabelFor(nomeOperatoreTF);
		cognomeOperatoreLabel.setLabelFor(cognomeOperatoreTF);
		dataNascitaOperatoreGiornoLabel.setLabelFor(dataNascitaOperatoreGiornoTF);
		dataNascitaOperatoreMeseLabel.setLabelFor(dataNascitaOperatoreMeseTF);
		dataNascitaOperatoreAnnoLabel.setLabelFor(dataNascitaOperatoreAnnoTF);

		sogliaRumoreLabel.setLabelFor(sogliaRumoreTF);
		sogliaGasLabel.setLabelFor(sogliaGasTF);

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
				String nomeOperatore = nomeOperatoreTF.getText();
				String cognomeOperatore = cognomeOperatoreTF.getText();
				String dataNascitaOperatoreGiorno = dataNascitaOperatoreGiornoTF.getText();
				String dataNascitaOperatoreMese = dataNascitaOperatoreMeseTF.getText();
				String dataNascitaOperatoreAnno = dataNascitaOperatoreAnnoTF.getText();
				String dataNascitaOperatore = dataNascitaOperatoreGiorno + "/" + dataNascitaOperatoreMese + "/"
						+ dataNascitaOperatoreAnno;

				String nomeArea = nomeAreaTF.getText();

				String r1 = sogliaRumoreTF.getText();
				String g1 = sogliaGasTF.getText();

				

				Boolean result = false;

				Lavoratore responsabile = new Lavoratore();
				Lavoratore operatore = new Lavoratore();

				Area area = new Area();

				if (nomeArea.isEmpty() || nome.isEmpty() || cognome.isEmpty() || dataNascitaGiorno.isEmpty()
						|| dataNascitaMese.isEmpty() || dataNascitaAnno.isEmpty() || nomeOperatore.isEmpty()
						|| cognomeOperatore.isEmpty() || dataNascitaOperatoreGiorno.isEmpty()
						|| dataNascitaOperatoreMese.isEmpty() || dataNascitaOperatoreAnno.isEmpty()
						|| r1.isEmpty() || g1.isEmpty()) { // Controllo
					// che
					// non
					// ci
					// siano
					// campi
					// vuoti
					setMessage("Tutti i campi devono essere compilati");
				} else {
					if (Helper.isDate(dataNascita, Helper.dateFormatApp)
							&& (Helper.isDate(dataNascitaOperatore, Helper.dateFormatApp)))
						if (Helper.isMaggiorenne(dataNascita) && Helper.isMaggiorenne(dataNascitaOperatore)) // Controllo
																												// che i
																												// valori
																												// nei
																												// campi
																												// non
						// stringa siano validi

						{
							Float rumore = Float.parseFloat(sogliaRumoreTF.getText()); // Leggo i valori nei campi
							Float gas = Float.parseFloat(sogliaGasTF.getText());
							String dataNascitaConvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, dataNascita);
							// Funzione che consente di convertire una data

							String dataNascitaOperatoreConvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, dataNascitaOperatore);

							area.setNome(nomeArea);
							responsabile.setNome(nome);
							responsabile.setCognome(cognome);
							responsabile.setDataNascita(dataNascitaConvertita);
							responsabile.setIdCant(idCantSelezionato);
							operatore.setNome(nomeOperatore);
							operatore.setCognome(cognomeOperatore);
							operatore.setDataNascita(dataNascitaOperatoreConvertita);

							area.setSogliaGas(gas);
							area.setSogliaRumore(rumore);

							try {

								result = area.saveArea(area, responsabile, operatore);

							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block

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

				if (result) { // Se il salvataggio dell'area avviene con successo

					Helper.showSuccessMessage(frame, "Area salvata correttamente");
					try {
						showListaAreeView(frame, idCant); // Torna alla lista degli operai
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
					showListaAreeView(frame, idCant); // Torna alla lista degli operai
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
						.addComponent(pageLabelResponsabile).addComponent(pageLabelOperatore)
						.addComponent(pageLabelSoglie)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(nomeAreaLabel)

										.addComponent(nomeResponsabileLabel).addComponent(cognomeResponsabileLabel)
										.addComponent(dataNascitaResponsabileLabel))

								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(nomeAreaTF).addComponent(nomeTF).addComponent(cognomeTF)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaResponsabileGiornoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaGiornoTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaResponsabileMeseLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaMeseTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaResponsabileAnnoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaAnnoTF)))))

						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)

										.addComponent(nomeOperatoreLabel).addComponent(cognomeOperatoreLabel)
										.addComponent(dataNascitaOperatoreLabel))

								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(nomeOperatoreTF).addComponent(cognomeOperatoreTF)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreGiornoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreGiornoTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreMeseLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreMeseTF))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreAnnoLabel))
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(dataNascitaOperatoreAnnoTF)))))

						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(sogliaRumoreLabel).addComponent(sogliaGasLabel))

								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(sogliaRumoreTF).addComponent(sogliaGasTF)))

						.addGroup(layout.createSequentialGroup().addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(cantieriLabel)))

						.addGroup(layout.createSequentialGroup().addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(table)))

						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(salvaButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(tornaAllaListaButton)))));

		layout.setVerticalGroup(layout.createSequentialGroup() // Definisco il verticalGroup
				.addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeAreaLabel)
						.addComponent(nomeAreaTF))
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelResponsabile))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeResponsabileLabel)
						.addComponent(nomeTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(cognomeResponsabileLabel).addComponent(cognomeTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(dataNascitaResponsabileLabel).addComponent(dataNascitaResponsabileGiornoLabel)
						.addComponent(dataNascitaGiornoTF).addComponent(dataNascitaResponsabileMeseLabel)
						.addComponent(dataNascitaMeseTF).addComponent(dataNascitaResponsabileAnnoLabel)
						.addComponent(dataNascitaAnnoTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelOperatore))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeOperatoreLabel)
						.addComponent(nomeOperatoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cognomeOperatoreLabel)
						.addComponent(cognomeOperatoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(dataNascitaOperatoreLabel).addComponent(dataNascitaOperatoreGiornoLabel)
						.addComponent(dataNascitaOperatoreGiornoTF).addComponent(dataNascitaOperatoreMeseLabel)
						.addComponent(dataNascitaOperatoreMeseTF).addComponent(dataNascitaOperatoreAnnoLabel)
						.addComponent(dataNascitaOperatoreAnnoTF))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelSoglie))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(sogliaRumoreLabel)
						.addComponent(sogliaRumoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(sogliaGasLabel)
						.addComponent(sogliaGasTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cantieriLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(table))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
						.addComponent(tornaAllaListaButton)));

		return panelContainer;

	}

	// Quando questa funzione viene richiamata mostra la pagina che consente la
	// modifica degli operai
	private void showModificaAreaView(JFrame frame, Integer idArea) throws IOException, SQLException {

		area = area.getArea(idArea);
		Lavoratore responsabile = new Lavoratore();
		responsabile = responsabile.getLavoratore(area.getIdLav());

		sensore = sensore.getSensore(idArea);

		Lavoratore operatore = new Lavoratore();
		operatore = operatore.getLavoratore(sensore.getIdLav());

		Component contents = createModificaAreaComponents(frame, area, responsabile, operatore);

		// Utilizzo la funzione che mi
		// restituisce
		// un
		// panel da inserire nel frame

		frame.getContentPane().removeAll(); // Pulisco il frame esistente
		frame.getContentPane().add(contents, BorderLayout.CENTER); // Inserisco i nuovi componenti nel frame
		frame.pack();
		frame.setVisible(true);
	}

	// Funzione utilizzata per creare la pagina che consente la modifica delle aree
	private Component createModificaAreaComponents(JFrame frame, Area oldArea, Lavoratore oldResponsabile,
			Lavoratore oldOperatore) {
		// TODO Auto-generated method stub
		Font headerFont = new Font("SansSerif", Font.BOLD, 20);
		Font labelFont = new Font("SansSerif", Font.BOLD, 18);
		Font normalFont = new Font("SansSerif", Font.PLAIN, 18);

		JLabel pageLabel = new JLabel("Modifica Area"); // Titolo
		pageLabel.setFont(headerFont);

		JLabel nomeAreaLabel = new JLabel("NOME AREA:"); // Label dei vari campi da modificare
		nomeAreaLabel.setFont(labelFont);

		JLabel pageLabelResponsabile = new JLabel("MODIFICA RESPONSABILE"); // Titolo
		pageLabelResponsabile.setFont(headerFont);

		JLabel nomeResponsabileLabel = new JLabel("NOME:"); // Label dei vari campi da modificare
		nomeResponsabileLabel.setFont(labelFont);

		JLabel cognomeResponsabileLabel = new JLabel("COGNOME:");
		cognomeResponsabileLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileLabel = new JLabel("DATA NASCITA:");
		dataNascitaResponsabileLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileGiornoLabel = new JLabel("Giorno(dd):");
		dataNascitaResponsabileGiornoLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileMeseLabel = new JLabel("Mese(MM):");
		dataNascitaResponsabileMeseLabel.setFont(labelFont);
		JLabel dataNascitaResponsabileAnnoLabel = new JLabel("Anno(yyyy):");
		dataNascitaResponsabileAnnoLabel.setFont(labelFont);

		JLabel pageLabelOperatore = new JLabel("MODIFICA OPERATORE SENSORI"); // Titolo
		pageLabelOperatore.setFont(headerFont);

		JLabel nomeOperatoreLabel = new JLabel("NOME"); // Label dei vari campi da modificare
		nomeOperatoreLabel.setFont(labelFont);
		JLabel cognomeOperatoreLabel = new JLabel("COGNOME");
		cognomeOperatoreLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreLabel = new JLabel("DATA NASCITA");
		dataNascitaOperatoreLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreGiornoLabel = new JLabel("Giorno(dd)");
		dataNascitaOperatoreGiornoLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreMeseLabel = new JLabel("Mese(MM)");
		dataNascitaOperatoreMeseLabel.setFont(labelFont);
		JLabel dataNascitaOperatoreAnnoLabel = new JLabel("Anno(yyyy)");
		dataNascitaOperatoreAnnoLabel.setFont(labelFont);

		JLabel pageLabelSoglie = new JLabel("MODIFICA SOGLIE"); // Titolo
		pageLabelSoglie.setFont(headerFont);

		JLabel sogliaRumoreLabel = new JLabel("SOGLIA RUMORE");
		sogliaRumoreLabel.setFont(labelFont);

		JLabel sogliaGasLabel = new JLabel("SOGLIA GAS");
		sogliaGasLabel.setFont(labelFont);

		JTextField nomeAreaTF = new JTextField(oldArea.getNome()); // Campi da modificare
		nomeAreaTF.setFont(normalFont);

		JTextField nomeResposabileTF = new JTextField(oldResponsabile.getNome()); // Campi da modificare
		nomeResposabileTF.setFont(normalFont);

		JTextField cognomeResponsabileTF = new JTextField(String.valueOf(oldResponsabile.getCognome()));
		cognomeResponsabileTF.setFont(normalFont);

		String oldDataNascita = oldResponsabile.getDataNascita();
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

		JTextField nomeOperatoreTF = new JTextField(oldOperatore.getNome()); // Campi da compilare
		nomeOperatoreTF.setFont(normalFont);
		JTextField cognomeOperatoreTF = new JTextField(oldOperatore.getCognome());
		cognomeOperatoreTF.setFont(normalFont);

		String oldDataNascitaOperatore = oldOperatore.getDataNascita();
		String dataNascitaOperatoreConvertita = Helper.convertDate(Helper.dateTimeFormatDb, Helper.dateFormatApp,
				oldDataNascitaOperatore);

		JTextField dataNascitaOperatoreGiornoTF = new JTextField(dataNascitaOperatoreConvertita.substring(0, 2));
		dataNascitaGiornoTF.setFont(normalFont);
		JTextField dataNascitaOperatoreMeseTF = new JTextField(dataNascitaOperatoreConvertita.substring(3, 5));
		dataNascitaMeseTF.setFont(normalFont);
		JTextField dataNascitaOperatoreAnnoTF = new JTextField(dataNascitaOperatoreConvertita.substring(6, 10));
		dataNascitaAnnoTF.setFont(normalFont);

		float rumore = oldArea.getSogliaRumore();
		String s = Float.toString(rumore);

		float gas = oldArea.getSogliaGas();
		String s1 = Float.toString(gas);

		JTextField sogliaRumoreTF = new JTextField(s);
		sogliaRumoreTF.setFont(normalFont);
		JTextField sogliaGasTF = new JTextField(s1);
		sogliaGasTF.setFont(normalFont);

		nomeAreaLabel.setLabelFor(nomeAreaTF); // Associo le label ai vari campi
		nomeResponsabileLabel.setLabelFor(nomeResposabileTF);
		cognomeResponsabileLabel.setLabelFor(cognomeResponsabileTF);
		dataNascitaResponsabileGiornoLabel.setLabelFor(dataNascitaGiornoTF);
		dataNascitaResponsabileMeseLabel.setLabelFor(dataNascitaMeseTF);
		dataNascitaResponsabileAnnoLabel.setLabelFor(dataNascitaAnnoTF);

		nomeOperatoreLabel.setLabelFor(pageLabelOperatore);
		nomeOperatoreLabel.setLabelFor(nomeOperatoreTF);
		cognomeOperatoreLabel.setLabelFor(cognomeOperatoreTF);
		dataNascitaOperatoreGiornoLabel.setLabelFor(dataNascitaOperatoreGiornoTF);
		dataNascitaOperatoreMeseLabel.setLabelFor(dataNascitaOperatoreMeseTF);
		dataNascitaOperatoreAnnoLabel.setLabelFor(dataNascitaOperatoreAnnoTF);

		JButton salvaButton = new JButton("Salva"); // Bottone per aggiornare l'area
		salvaButton.setPreferredSize(new Dimension(100, 50));
		salvaButton.setBackground(Color.GREEN);
		salvaButton.setForeground(Color.BLACK);
		salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));
		salvaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { // Funzione di salvataggio sul click
				String nomeArea = nomeAreaTF.getText();
				String nome = nomeResposabileTF.getText(); // Leggo i valori nei campi
				String cognome = cognomeResponsabileTF.getText();
				String dataNGiorno = dataNascitaGiornoTF.getText();
				String dataNMese = dataNascitaMeseTF.getText();
				String dataNAnno = dataNascitaAnnoTF.getText();
				String dataNascita = dataNGiorno + "/" + dataNMese + "/" + dataNAnno;

				String nomeOperatore = nomeOperatoreTF.getText();
				String cognomeOperatore = cognomeOperatoreTF.getText();
				String dataNascitaOperatoreGiorno = dataNascitaOperatoreGiornoTF.getText();
				String dataNascitaOperatoreMese = dataNascitaOperatoreMeseTF.getText();
				String dataNascitaOperatoreAnno = dataNascitaOperatoreAnnoTF.getText();
				String dataNascitaOperatore = dataNascitaOperatoreGiorno + "/" + dataNascitaOperatoreMese + "/"
						+ dataNascitaOperatoreAnno;
				Boolean result = false;

				Float rumore = Float.parseFloat(sogliaRumoreTF.getText()); // Leggo i valori nei campi
				Float gas = Float.parseFloat(sogliaGasTF.getText());

				Lavoratore responsabile = new Lavoratore();
				Lavoratore operatore = new Lavoratore();
				Area area = new Area();

				if (nomeArea.isEmpty() || nome.isEmpty() || cognome.isEmpty() || dataNGiorno.isEmpty()
						|| dataNMese.isEmpty() || dataNAnno.isEmpty() || nomeOperatore.isEmpty()
						|| cognomeOperatore.isEmpty() || dataNascitaOperatoreGiorno.isEmpty()
						|| dataNascitaOperatoreMese.isEmpty() || dataNascitaOperatoreAnno.isEmpty() || rumore == null
						|| gas == null) {
					// Controllo che non ci siano campi vuoti
					setMessage("Tutti i campi devono essere compilati");
				} else {
					if (Helper.isDate(dataNascita, Helper.dateFormatApp))
						if (Helper.isMaggiorenne(dataNascita))

						{

							// Controllo che i valori nei campi non stringa siano validi
							String dataNascitaRiconvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, dataNascita);
							String dataNascitaOperatoreRiconvertita = Helper.convertDate(Helper.dateFormatApp,
									Helper.dateTimeFormatDb, dataNascitaOperatore);
							// Funzione che consente di converte il formato della data

							area.setIdArea(oldArea.getIdArea());
							area.setNome(nomeArea);
							area.setSogliaGas(gas);
							area.setSogliaRumore(rumore);

							responsabile.setIdLav(oldResponsabile.getIdLav());
							responsabile.setNome(nome);
							responsabile.setCognome(cognome);
							responsabile.setDataNascita(dataNascitaRiconvertita);
							operatore.setIdLav(oldOperatore.getIdLav());
							operatore.setNome(nomeOperatore);
							operatore.setCognome(cognomeOperatore);
							operatore.setDataNascita(dataNascitaOperatoreRiconvertita);

							try {
								result = area.saveArea(area, responsabile, operatore);
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

				if (result) { // Se il salvataggio dell'area avviene con successo
					Helper.showSuccessMessage(frame, "Area salvata correttamente");
					try {
						showListaAreeView(frame, idCant); // Torna alla lista delle aree
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); // Potrebbe avvenire qualche
																							// errore durante il cambio
																							// pagina(connessione
																							// fallita per esempio)
						e.printStackTrace();
					}
				} else { // Altrimenti se il salvataggio dell'area non avviene con successo
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
				int risposta = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare l'area?",
						"ELIMINA AREA", JOptionPane.YES_NO_OPTION);
				if (risposta == 0) {
					Boolean result = false;
					try {
						result = area.deleteArea(oldArea.getIdArea(), oldResponsabile.getIdLav(),
								oldOperatore.getIdLav());
					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (result) { // Se l'eliminazione avviene con successo
						Helper.showSuccessMessage(frame, "Area  eliminata correttamente");
						try {
							showListaAreeView(frame, idCant); // Torna alla lista delle aree
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
						Helper.showErrorMessage(frame, "Errore durante l'eliminazione dell'area");
					}
				}

			}
		});

		JButton tornaAllaListaButton = new JButton("Torna alla lista"); // Bottone per tornare alla lista delle aree
		tornaAllaListaButton.setPreferredSize(new Dimension(100, 50));
		tornaAllaListaButton.setFont(new Font("Arial", Font.PLAIN, 20));
		tornaAllaListaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					showListaAreeView(frame, idCant); // Torna alla lista degli operai
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
								.addComponent(pageLabelResponsabile)

								.addComponent(pageLabelOperatore).addComponent(pageLabelSoglie)
								.addGroup(layout.createSequentialGroup().addGroup(layout
										.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nomeAreaLabel)

										.addComponent(
												nomeResponsabileLabel)
										.addComponent(cognomeResponsabileLabel)
										.addComponent(dataNascitaResponsabileLabel))

										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(nomeAreaTF).addComponent(nomeResposabileTF)
												.addComponent(cognomeResponsabileTF)
												.addGroup(layout.createSequentialGroup()
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaResponsabileGiornoLabel))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaGiornoTF))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaResponsabileMeseLabel))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaMeseTF))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaResponsabileAnnoLabel))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaAnnoTF)))))

								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)

												.addComponent(
														nomeOperatoreLabel)
												.addComponent(cognomeOperatoreLabel)
												.addComponent(dataNascitaOperatoreLabel))

										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(nomeOperatoreTF).addComponent(cognomeOperatoreTF)
												.addGroup(layout.createSequentialGroup()
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaOperatoreGiornoLabel))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaOperatoreGiornoTF))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaOperatoreMeseLabel))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaOperatoreMeseTF))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaOperatoreAnnoLabel))
														.addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataNascitaOperatoreAnnoTF)))))

								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(sogliaRumoreLabel).addComponent(sogliaGasLabel))

										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(sogliaRumoreTF).addComponent(sogliaGasTF)))

								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(salvaButton))
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(tornaAllaListaButton))
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(eliminaButton)))

				));

		layout.setVerticalGroup(layout.createSequentialGroup() // Definisco il verticalGroup
				.addComponent(pageLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeAreaLabel)
						.addComponent(nomeAreaTF))
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelResponsabile))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeResponsabileLabel)
						.addComponent(nomeResposabileTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(cognomeResponsabileLabel).addComponent(cognomeResponsabileTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(dataNascitaResponsabileLabel).addComponent(dataNascitaResponsabileGiornoLabel)
						.addComponent(dataNascitaGiornoTF).addComponent(dataNascitaResponsabileMeseLabel)
						.addComponent(dataNascitaMeseTF).addComponent(dataNascitaResponsabileAnnoLabel)
						.addComponent(dataNascitaAnnoTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelOperatore))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nomeOperatoreLabel)
						.addComponent(nomeOperatoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cognomeOperatoreLabel)
						.addComponent(cognomeOperatoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(dataNascitaOperatoreLabel).addComponent(dataNascitaOperatoreGiornoLabel)
						.addComponent(dataNascitaOperatoreGiornoTF).addComponent(dataNascitaOperatoreMeseLabel)
						.addComponent(dataNascitaOperatoreMeseTF).addComponent(dataNascitaOperatoreAnnoLabel)
						.addComponent(dataNascitaOperatoreAnnoTF))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabelSoglie))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(sogliaRumoreLabel)
						.addComponent(sogliaRumoreTF))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(sogliaGasLabel)
						.addComponent(sogliaGasTF))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(salvaButton)
						.addComponent(tornaAllaListaButton).addComponent(eliminaButton))

		);

		return panelContainer;
	}

}
