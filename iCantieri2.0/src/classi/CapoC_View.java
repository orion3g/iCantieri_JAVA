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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class CapoC_View {
	
	private static int idCant;
	
	    //Funzione che restituisce una MAP contenente gli operai del cantiere passato contenuti in una tabella nel database
	
	
		private List<Lavoratore> getOperai() throws IOException, SQLException {
				List<Lavoratore> operai = new ArrayList<Lavoratore>();
				Connection conn = new Database().getDefaultConnection();
				PreparedStatement pstmt;
				ResultSet rs;
				
				String query = "SELECT * FROM LAVORATORE WHERE idcant=? AND TIPOLAV='OPERAIO SEMPLICE'";	//Query in SQL
				if(conn != null) {
					pstmt = conn.prepareStatement(query);
					pstmt.setInt(1, idCant);
					rs = pstmt.executeQuery();
					while(rs.next()) {
						Lavoratore lavoratore = new Lavoratore();
						lavoratore.setIdLav(rs.getInt(1));
						lavoratore.setNome(rs.getString(2));
						lavoratore.setCognome(rs.getString(3));
						lavoratore.setDataNascita(rs.getString(4));
						lavoratore.setTipoLav(rs.getString(5));
						lavoratore.setIdCant(rs.getInt(6));
						
						operai.add(lavoratore);
					}
					pstmt.close();
				}
				return operai;
			}
		
		//Funzione che restituisce un oggetto Lavoratore contenuto in una tabella nel database e con l'id corrispondente all'id passato come parametro
		private Lavoratore getOperaio(int idOperaio, int idCant) throws IOException, SQLException {
			Lavoratore lavoratore = new Lavoratore();
			Connection conn = new Database().getDefaultConnection();
			PreparedStatement pstmt;
			ResultSet rs;
			String query = "SELECT * FROM LAVORATORE WHERE idcant=? AND idlav=?";	//Query in SQL
			if(conn != null) {
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, idCant);
				pstmt.setInt(1, idOperaio);
				
				rs = pstmt.executeQuery();
				while(rs.next()) {
					lavoratore.setIdLav(rs.getInt(1));
					lavoratore.setNome(rs.getString(2));
					lavoratore.setCognome(rs.getString(3));
					lavoratore.setDataNascita(rs.getString(4));
					lavoratore.setTipoLav(rs.getString(5));
					lavoratore.setIdCant(rs.getInt(6));
				}
				pstmt.close();
			}
			return lavoratore;
		}
		
		
		//Funzione che mostra la pagina che contiene la lista degli operai 
			public void showListaOperaiView(JFrame frame, int idCantiere) throws IOException, SQLException {
			
			   idCant=idCantiere;
				List<Lavoratore> operai = getOperai();
				Component contents = createListaOperaiComponents(operai, frame);	
				//Utilizzo la funzione che mi restituisce un panel da inserire nel frame
				
				frame.getContentPane().removeAll();	//Pulisco il frame esistente
				frame.getContentPane().add(contents, BorderLayout.CENTER);	//Inserisco i nuovi componenti nel frame
				frame.pack();
				frame.setVisible(true);
			}
		
			
			
			//Funzione utilizzata per creare la pagina che contiene la lista degli operai 
			
			private Component createListaOperaiComponents(List<Lavoratore> operai, JFrame frame) {
				Font headerFont = new Font("SansSerif", Font.BOLD, 20);
				Font tableFont = new Font("Arial", Font.PLAIN, 18);
				String[] columnNames = {"ID", "NOME", "COGNOME", "DATA NASCITA"};	//Lista degli header della tabella
				Object[][] data = Helper.ConvertOperaioListToObject(operai);	//Elementi della tabella
				
				JTable table = new JTable(data, columnNames);	//Creo la tabella riempendola con i dati
				table.getTableHeader().setFont(headerFont);
				table.setDefaultEditor(Object.class, null);	//Rendo la tabella non editabile
			table.addMouseListener(new MouseAdapter() {	//Creo una funzione che consente di aprire la scheda dell'oggetto
					public void mouseClicked(MouseEvent me) {
						if(me.getClickCount() == 2) {	//Sul doppio click parte la funzione
							JTable target = (JTable)me.getSource();
							int row = target.getSelectedRow();	// Seleziono la riga
							Integer idLav = (Integer) target.getValueAt(row, 0);	//Seleziono l'id corrispondente a quella riga
							try {
								showModificaOperaioView(frame, idLav);
							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								Helper.showErrorMessage(frame, "Errore durante il cambio pagina");	//Potrebbe avvenire qualche errore durante il cambio pagina(connessione fallita per esempio)
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
				
				JButton createFilmButton = new JButton("Aggiungi Operaio");	//Bottone per aggiungere un operaio alla lista
				createFilmButton.setPreferredSize(new Dimension(100, 50));
				createFilmButton.setBackground(Color.GREEN);
				createFilmButton.setForeground(Color.BLACK);
				createFilmButton.setFont(new Font("Arial", Font.PLAIN, 20));
				createFilmButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	try {
		            		showCreaOperaioView(frame);
						} catch (IOException | SQLException e) {
							// TODO Auto-generated catch block
							Helper.showErrorMessage(frame, "Errore durante il cambio pagina");	//Potrebbe avvenire qualche errore durante il cambio pagina(connessione fallita per esempio)
							e.printStackTrace();
						}
		            }
		        });
				
				JButton mainMenuButton = new JButton("Torna al menu");	//Bottone per tornare al menu principale
				mainMenuButton.setPreferredSize(new Dimension(100, 50));
				mainMenuButton.setFont(new Font("Arial", Font.PLAIN, 20));
				mainMenuButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	
		            	MainMenu.createCapocComponents(frame, idCant);
		            }
		        });
				
				JPanel panelContainer = new JPanel();
				panelContainer.add(pageLabel);
				panelContainer.add(scrollPanel);
				panelContainer.add(createFilmButton);
				panelContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

				GroupLayout layout = new GroupLayout(panelContainer);
				panelContainer.setLayout(layout);
				
				layout.setAutoCreateGaps(true);
				layout.setAutoCreateContainerGaps(true);
				
				layout.setHorizontalGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(pageLabel)
								.addComponent(scrollPanel)
								.addGroup(layout.createSequentialGroup()
									.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addComponent(createFilmButton)
											)
									.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
											.addComponent(mainMenuButton)
											)
									)
								)
						);
				layout.setVerticalGroup(layout.createSequentialGroup()
						.addComponent(pageLabel)
						.addComponent(scrollPanel)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(createFilmButton)
								.addComponent(mainMenuButton)
								)
						);
				
				return panelContainer;
			}
			
			
			//Quando questa funzione viene richiamata mostra la pagina che consente la modifica dei film
			private void showModificaOperaioView(JFrame frame, Integer idLav) throws IOException, SQLException {
				Lavoratore operaio = getOperaio(idLav, idCant);
				Component contents = createModificaFilmComponents(frame, operaio);	//Utilizzo la funzione che mi restituisce un panel da inserire nel frame
			
				frame.getContentPane().removeAll();	//Pulisco il frame esistente
				frame.getContentPane().add(contents, BorderLayout.CENTER);	//Inserisco i nuovi componenti nel frame
				frame.pack();
       			frame.setVisible(true);
			}
	
	
	
			//Quando questa funzione viene richiamata mostra la pagina che consente la creazione dei film
			private void showCreaOperaioView(JFrame frame) throws IOException, SQLException {
				Component contents = createCreaOperaioComponents(frame);	//Utilizzo la funzione che mi restituisce un panel da inserire nel frame
				
				frame.getContentPane().removeAll();	//Pulisco il frame esistente
				frame.getContentPane().add(contents, BorderLayout.CENTER);	//Inserisco i nuovi componenti nel frame
				frame.pack();
				frame.setVisible(true);
			}
	
			//Funzione utilizzata per creare la pagina che consente la modifica dei film
			private Component createModificaFilmComponents(JFrame frame, Lavoratore oldLavoratore) {
				// TODO Auto-generated method stub
				Font headerFont = new Font("SansSerif", Font.BOLD, 20);
				Font labelFont = new Font("SansSerif", Font.BOLD, 18);
				Font normalFont = new Font("SansSerif", Font.PLAIN, 18);
				
				JLabel pageLabel = new JLabel("Modifica Operaio");	//Titolo
				pageLabel.setFont(headerFont);
				
				JLabel nomeLabel = new JLabel("NOME");	//Label dei vari campi da modificare
				nomeLabel.setFont(labelFont);
				JLabel durataLabel = new JLabel("COGNOME");
				durataLabel.setFont(labelFont);
				JLabel dataUscitaLabel = new JLabel("DATA USCITA");
				dataUscitaLabel.setFont(labelFont);
				JLabel dataUscitaGiornoLabel = new JLabel("Giorno(dd)");
				dataUscitaGiornoLabel.setFont(labelFont);
				JLabel dataUscitaMeseLabel = new JLabel("Mese(MM)");
				dataUscitaMeseLabel.setFont(labelFont);
				JLabel dataUscitaAnnoLabel = new JLabel("Anno(yyyy)");
				dataUscitaAnnoLabel.setFont(labelFont);

				JTextField nomeTF = new JTextField(oldLavoratore.getNome());	//Campi da modificare
				nomeTF.setFont(normalFont);
				JTextField durataTF = new JTextField(String.valueOf(oldLavoratore.getCognome()));
				durataTF.setFont(normalFont);
				
				String oldDataUscita = oldLavoratore.getDataNascita();
				String dataUscitaConvertita = Helper.convertDate(Helper.dateTimeFormatDb, Helper.dateFormatApp, oldDataUscita);	//Funzione che consente di converte il formato della data
				
				JTextField dataUscitaGiornoTF = new JTextField(dataUscitaConvertita.substring(0, 2));
				dataUscitaGiornoTF.setFont(normalFont);
				JTextField dataUscitaMeseTF = new JTextField(dataUscitaConvertita.substring(3, 5));
				dataUscitaMeseTF.setFont(normalFont);
				JTextField dataUscitaAnnoTF = new JTextField(dataUscitaConvertita.substring(6, 10));
				dataUscitaAnnoTF.setFont(normalFont);
				
				
				nomeLabel.setLabelFor(nomeTF);	//Associo le label ai vari campi
				durataLabel.setLabelFor(durataTF);
				dataUscitaGiornoLabel.setLabelFor(dataUscitaGiornoTF);
				dataUscitaMeseLabel.setLabelFor(dataUscitaMeseTF);
				dataUscitaAnnoLabel.setLabelFor(dataUscitaAnnoTF);
				
				JButton salvaButton = new JButton("Salva");	//Bottone per aggiornare il film
				salvaButton.setPreferredSize(new Dimension(100, 50));
				salvaButton.setBackground(Color.GREEN);
				salvaButton.setForeground(Color.BLACK);
				salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));
				salvaButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {	//Funzione di salvataggio sul click
		            	String nome = nomeTF.getText();	//Leggo i valori nei campi
		            	String cognome = durataTF.getText();
		            	String dataNGiorno = dataUscitaGiornoTF.getText();
		            	String dataNMese = dataUscitaMeseTF.getText();
		            	String dataNAnno = dataUscitaAnnoTF.getText();
		            	String dataNascita = dataNGiorno + "/" + dataNMese + "/" + dataNAnno;
		            	Boolean result = false;
		            	
		            	Lavoratore operaio = new Lavoratore();
		            	
		            	if(nome.isEmpty() || cognome.isEmpty() || dataNGiorno.isEmpty()|| dataNMese.isEmpty() || dataNAnno.isEmpty()) {	
		            		//Controllo che non ci siano campi vuoti
		            		setMessage("Tutti i campi devono essere compilati");
		            	}
		            	else {
		            		if(Helper.isDate(dataNascita, Helper.dateFormatApp)) {	
		            			
		            			//Controllo che i valori nei campi non stringa siano validi
		                		String dataNascitaRiconvertita = Helper.convertDate(Helper.dateFormatApp, Helper.dateTimeFormatDb, dataNascita);
		                		//Funzione che consente di converte il formato della data
		                		
		                		operaio.setIdLav(oldLavoratore.getIdLav());
		                		operaio.setNome(nome);
		                		operaio.setCognome(cognome);
		                		operaio.setDataNascita(dataNascitaRiconvertita);
		                		
		                		try {
		    						result = saveFilm(operaio);
		    					} catch (IOException | SQLException e) {
		    						// TODO Auto-generated catch block
		    						Helper.showErrorMessage(frame, getMessage());
		    						e.printStackTrace();
		    					}
		                	}
		                	else {
		                		setMessage("Il campo durata deve essere un intero positivo ed il campo data uscita deve avere un formato dd/MM/yyyy");	//I campi non sono stati compilati correttamente
		                	}
		            	}
		            	
		            	if(result) {	//Se il salvataggio del film avviene con successo
		            		Helper.showSuccessMessage(frame, "Film salvato correttamente");
		            		try {
		            			showListaOperaiView(frame, idCant); //Torna alla lista dei film
							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								Helper.showErrorMessage(frame, "Errore durante il cambio pagina");	//Potrebbe avvenire qualche errore durante il cambio pagina(connessione fallita per esempio)
								e.printStackTrace();
							}
		            	}
		            	else {	//Altrimenti se il salvataggio del film non avviene con successo
		            		Helper.showErrorMessage(frame, getMessage());
		            	}
		            }
		        });
				
				JButton eliminaButton = new JButton("Elimina");	//Bottone per eliminare il film
				eliminaButton.setPreferredSize(new Dimension(100, 50));
				eliminaButton.setBackground(Color.RED);
				eliminaButton.setForeground(Color.BLACK);
				eliminaButton.setFont(new Font("Arial", Font.PLAIN, 20));
				eliminaButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	int risposta = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare il film?", "ELIMINA FILM", JOptionPane.YES_NO_OPTION);
		            	if(risposta == 0) {
		            		Boolean result = false;
		            		try {
		    					result = deleteOperaio(oldLavoratore.getIdLav());
		    				} catch (IOException | SQLException e) {
		    					// TODO Auto-generated catch block
		    					e.printStackTrace();
		    				}
		                	
		                	if(result) {	//Se l'eliminazione avviene con successo
		                		Helper.showSuccessMessage(frame, "Film eliminato correttamente");
		                		try {
		                			showListaOperaiView(frame, idCant);	//Torna alla lista dei film
		    					} catch (IOException | SQLException e) {
		    						// TODO Auto-generated catch block
		    						Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); //Potrebbe avvenire qualche errore durante il cambio pagina(connessione mancante per esempio)
		    						e.printStackTrace();
		    					}
		                	}
		                	else {	//Altrimenti se l'eliminazione dei dati non avviene con successo
		                		Helper.showErrorMessage(frame, "Errore durante l'eliminazione del film");
		                	}
		            	}
		            	
		            }
		        });
				
				JButton tornaAllaListaButton = new JButton("Torna alla lista");	//Bottone per tornare alla lista dei film
				tornaAllaListaButton.setPreferredSize(new Dimension(100, 50));
				tornaAllaListaButton.setFont(new Font("Arial", Font.PLAIN, 20));
				tornaAllaListaButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	try {
		            		showListaOperaiView(frame, idCant);	//Torna alla lista dei film
						} catch (IOException | SQLException e) {
							// TODO Auto-generated catch block
							Helper.showErrorMessage(frame, "Errore durante il cambio pagina"); //Potrebbe avvenire qualche errore durante il cambio pagina(connessione mancante per esempio)
							e.printStackTrace();
						}
		            }
		        });
				
				JPanel panelContainer = new JPanel();	//Panel da inviare alla funzione chiamante
				panelContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

				GroupLayout layout = new GroupLayout(panelContainer);	//Definisco un group layout
				panelContainer.setLayout(layout);
				
				layout.setAutoCreateGaps(true);
				layout.setAutoCreateContainerGaps(true);
				
				
				layout.setHorizontalGroup(layout.createSequentialGroup()	//Definisco l'horizontalGroup
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(pageLabel)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(nomeLabel)
												.addComponent(durataLabel)
												.addComponent(dataUscitaLabel)
												)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(nomeTF)
												.addComponent(durataTF)
												.addGroup(layout.createSequentialGroup()
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaGiornoLabel)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaGiornoTF)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaMeseLabel)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaMeseTF)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaAnnoLabel)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaAnnoTF)
																)
													)
												)
										)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(salvaButton)
												)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(tornaAllaListaButton)
												)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(eliminaButton)
												)
										)
								)
						);
				
				layout.setVerticalGroup(layout.createSequentialGroup()	//Definisco il verticalGroup
						.addComponent(pageLabel)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(nomeLabel)
								.addComponent(nomeTF)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(durataLabel)
								.addComponent(durataTF)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(dataUscitaLabel)
								.addComponent(dataUscitaGiornoLabel)
								.addComponent(dataUscitaGiornoTF)
								.addComponent(dataUscitaMeseLabel)
								.addComponent(dataUscitaMeseTF)
								.addComponent(dataUscitaAnnoLabel)
								.addComponent(dataUscitaAnnoTF)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(salvaButton)
								.addComponent(tornaAllaListaButton)
								.addComponent(eliminaButton)
								)
						);
				
				return panelContainer;
			}
	
			//Funzione utilizzata per salvare un operaio. Se è presente l'id nell'oggetto film allora aggiorna un film esistente, altrimenti ne crea uno nuovo
			protected Boolean saveFilm(Lavoratore operaio) throws IOException, SQLException {
				// TODO Auto-generated method stub
				Connection conn = new Database().getDefaultConnection();
				PreparedStatement pstmt;
				Integer rows = 0;
				String query;
				if(conn != null) {
					if(operaio.getIdLav() == 0) {	//Se il film non esiste
						query = "INSERT INTO LAVORATORE(nome, cognome, datanascita, idcant) values(?, ?, ?, ?)";
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, operaio.getNome());
						pstmt.setString(2, operaio.getCognome());
						
						pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(operaio.getDataNascita()));
						pstmt.setInt(4, operaio.getIdCant());
					}
					else {	//Se il film esiste
						query = "UPDATE film SET nome = ?, cognome = ?, datanascita = ? WHERE idlav = ?";
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, operaio.getNome());
						pstmt.setString(2, operaio.getCognome());
						pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(operaio.getDataNascita()));
						pstmt.setInt(4, operaio.getIdCant());
					}
					
					rows = pstmt.executeUpdate();
					pstmt.close();
				}
				
				if(rows > 0) {
					return true;
				}
				return false;
			}
	
			//Funzione utilizzata per eliminare un operaio  tramite il suo id
			protected Boolean deleteOperaio(int idLav) throws IOException, SQLException {
				Connection conn = new Database().getDefaultConnection();
				PreparedStatement pstmt;
				Integer rows = 0;
				String query;
				if(conn != null) {
						query = "DELETE FROM LAVORATORE WHERE idlav = ?";
						pstmt = conn.prepareStatement(query);
						pstmt.setInt(1, idLav);
						
						rows = pstmt.executeUpdate();
						pstmt.close();
				}
				
				if(rows > 0) {
					return true;
				}
				return false;
			}
	
			
	
			//Funzione utilizzata per creare la pagina che consente la creazione degli operai 
			private Component createCreaOperaioComponents(JFrame frame) {
				// TODO Auto-generated method stub
				Font headerFont = new Font("SansSerif", Font.BOLD, 20);
				Font labelFont = new Font("SansSerif", Font.BOLD, 18);
				Font normalFont = new Font("SansSerif", Font.PLAIN, 18);
				
				JLabel pageLabel = new JLabel("Crea Film");	//Titolo
				pageLabel.setFont(headerFont);
				
				JLabel nomeLabel = new JLabel("NOME");	//Label dei vari campi da modificare
				nomeLabel.setFont(labelFont);
				JLabel durataLabel = new JLabel("DURATA(minuti)");
				durataLabel.setFont(labelFont);
				JLabel dataUscitaLabel = new JLabel("DATA USCITA");
				dataUscitaLabel.setFont(labelFont);
				JLabel dataUscitaGiornoLabel = new JLabel("Giorno(dd)");
				dataUscitaGiornoLabel.setFont(labelFont);
				JLabel dataUscitaMeseLabel = new JLabel("Mese(MM)");
				dataUscitaMeseLabel.setFont(labelFont);
				JLabel dataUscitaAnnoLabel = new JLabel("Anno(yyyy)");
				dataUscitaAnnoLabel.setFont(labelFont);

				JTextField nomeTF = new JTextField();	//Campi da compilare
				nomeTF.setFont(normalFont);
				JTextField durataTF = new JTextField();
				durataTF.setFont(normalFont);
				JTextField dataUscitaGiornoTF = new JTextField();
				dataUscitaGiornoTF.setFont(normalFont);
				JTextField dataUscitaMeseTF = new JTextField();
				dataUscitaMeseTF.setFont(normalFont);
				JTextField dataUscitaAnnoTF = new JTextField();
				dataUscitaAnnoTF.setFont(normalFont);
				
				nomeLabel.setLabelFor(nomeTF);	//Associo le label ai vari campi
				durataLabel.setLabelFor(durataTF);
				dataUscitaGiornoLabel.setLabelFor(dataUscitaGiornoTF);
				dataUscitaMeseLabel.setLabelFor(dataUscitaMeseTF);
				dataUscitaAnnoLabel.setLabelFor(dataUscitaAnnoTF);
				
				JButton salvaButton = new JButton("Salva");	//Bottone per salvare il nuovo film
				salvaButton.setPreferredSize(new Dimension(100, 50));
				salvaButton.setBackground(Color.GREEN);
				salvaButton.setForeground(Color.BLACK);
				salvaButton.setFont(new Font("Arial", Font.PLAIN, 20));
				salvaButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {	//Funzione di salvataggio sul click
		            	String nome = nomeTF.getText();	//Leggo i valori nei campi
		            	String durata = durataTF.getText();
		            	String dataUscitaGiorno = dataUscitaGiornoTF.getText();
		            	String dataUscitaMese = dataUscitaMeseTF.getText();
		            	String dataUscitaAnno = dataUscitaAnnoTF.getText();
		            	String dataUscita = dataUscitaGiorno + "/" + dataUscitaMese + "/" + dataUscitaAnno;
		            	Boolean result = false;
		            	
		            	Lavoratore operaio = new Lavoratore();
		            	
		            	if(nome.isEmpty() || durata.isEmpty() || dataUscita.isEmpty()) { //Controllo che non ci siano campi vuoti
		            		setMessage("Tutti i campi devono essere compilati");
		            	}
		            	else {
		            		if(Helper.isIntegerPositive(durata) && Helper.isDate(dataUscita, Helper.dateFormatApp)) {	//Controllo che i valori nei campi non stringa siano validi
		                		String dataUscitaConvertita = Helper.convertDate(Helper.dateFormatApp, Helper.dateTimeFormatDb, dataUscita);	//Funzione che consente di convertire una data
		                		
		                		film.setNome(nome);
		                		film.setDurata(Integer.parseInt(durata));
		                		film.setDataUscita(dataUscitaConvertita);
		                		
		                		try {
		    						result = saveFilm(film);
		    					} catch (IOException | SQLException e) {
		    						// TODO Auto-generated catch block
		    						e.printStackTrace();
		    					}
		                	}
		                	else {
		                		setMessage("Il campo durata deve essere un intero positivo ed il campo data uscita deve avere un formato dd/mm/yyyy");	//I campi non sono stati compilati correttamente
		                	}
		            	}
		            	
		            	if(result) {	//Se il salvataggio del film avviene con successo
		            		Helper.showSuccessMessage(frame, "Film salvato correttamente");
		            		try {
								showListaFilmView(frame); //Torna alla lista dei film
							} catch (IOException | SQLException e) {
								// TODO Auto-generated catch block
								Helper.showErrorMessage(frame, "Errore durante il cambio pagina");	//Potrebbe avvenire qualche errore durante il cambio pagina(connessione fallita per esempio)
								e.printStackTrace();
							}
		            	}
		            	else {	//Altrimenti se il salvataggio del film non avviene con successo
		            		Helper.showErrorMessage(frame, getMessage());
		            	}
		            }
		        });
				
				JButton tornaAllaListaButton = new JButton("Torna alla lista");	//Bottone per tornare alla lista dei film
				tornaAllaListaButton.setPreferredSize(new Dimension(100, 50));
				tornaAllaListaButton.setFont(new Font("Arial", Font.PLAIN, 20));
				tornaAllaListaButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		            	try {
							showListaFilmView(frame); //Torna alla lista dei film
						} catch (IOException | SQLException e) {
							// TODO Auto-generated catch block
							Helper.showErrorMessage(frame, "Errore durante il cambio pagina");	//Potrebbe avvenire qualche errore durante il cambio pagina(connessione fallita per esempio)
							e.printStackTrace();
						}
		            }
		        });
						
				
				JPanel panelContainer = new JPanel();	//Panel da inviare alla funzione chiamante		
				panelContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

				GroupLayout layout = new GroupLayout(panelContainer);	//Definisco un group layout
				panelContainer.setLayout(layout);
				
				layout.setAutoCreateGaps(true);
				layout.setAutoCreateContainerGaps(true);
				
				layout.setHorizontalGroup(layout.createSequentialGroup()	//Definisco l'horizontalGroup
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(pageLabel)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(nomeLabel)
												.addComponent(durataLabel)
												.addComponent(dataUscitaLabel)
												)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(nomeTF)
												.addComponent(durataTF)
												.addGroup(layout.createSequentialGroup()
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaGiornoLabel)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaGiornoTF)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaMeseLabel)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaMeseTF)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaAnnoLabel)
																)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(dataUscitaAnnoTF)
																)
													)
												)
										)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addComponent(salvaButton)
												)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(tornaAllaListaButton)
												)
										)
								)
						);
				
				layout.setVerticalGroup(layout.createSequentialGroup()	//Definisco il verticalGroup
						.addComponent(pageLabel)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(nomeLabel)
								.addComponent(nomeTF)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(durataLabel)
								.addComponent(durataTF)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(dataUscitaLabel)
								.addComponent(dataUscitaGiornoLabel)
								.addComponent(dataUscitaGiornoTF)
								.addComponent(dataUscitaMeseLabel)
								.addComponent(dataUscitaMeseTF)
								.addComponent(dataUscitaAnnoLabel)
								.addComponent(dataUscitaAnnoTF)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(salvaButton)
								.addComponent(tornaAllaListaButton)
								)
						);
				
				return panelContainer;
			}
	
	

}
