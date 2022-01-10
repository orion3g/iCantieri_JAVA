package classi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Helper {

	public final static String dateFormatApp = "dd/MM/yyyy"; // Formato data nell'applicazione
	public final static String timeFormatApp = "hh:mm:ss"; // Formato time nell'applicazione
	public final static String dateTimeFormatApp = "dd/MM/yyyy HH:mm:ss"; // Formato datatime nell'applicazione
	public final static String dateTimeFormatDb = "yyyy-MM-dd HH:mm:ss"; // Formato datatime nel database

	// Funzione che converte una List<Lavoratore> in un Object[][] necessario per la
	// costruzione di una JTable
	public static Object[][] ConvertOperaioListToObject(List<Lavoratore> list) {
		Object[][] result = new Object[list.size()][5];
		for (int i = 0; i < list.size(); i++) {
			result[i][0] = list.get(i).getIdLav();
			result[i][1] = list.get(i).getNome();
			result[i][2] = list.get(i).getCognome();

			String dataConvertita = convertDate(dateTimeFormatDb, dateFormatApp, list.get(i).getDataNascita());
			// Converto la data nel formato adeguato
			result[i][3] = dataConvertita;
			result[i][4] = list.get(i).getIdCant();

		}
		return result;
	}

	// Funzione che converte una List<Lavoratore> in un Object[][] necessario per la
	// costruzione di una JTable per Amministratore
	public static Object[][] ConvertOperaioListToObjectAmministratore(List<Lavoratore> list) {
		Object[][] result = new Object[list.size()][5];
		for (int i = 0; i < list.size(); i++) {
			result[i][0] = list.get(i).getIdLav();
			result[i][1] = list.get(i).getNome();
			result[i][2] = list.get(i).getCognome();

			String dataConvertita = convertDate(dateTimeFormatDb, dateFormatApp, list.get(i).getDataNascita());
			// Converto la data nel formato adeguato
			result[i][3] = dataConvertita;

			result[i][4] = list.get(i).getIdCant();

		}
		return result;
	}

	// Funzione che converte una List<Cantiere> in un Object[][]
	public static Object[][] ConvertCantiereListToObject(List<Cantiere> list) {
		Object[][] result = new Object[list.size()][3];
		for (int i = 0; i < list.size(); i++) {
			result[i][0] = list.get(i).getIdCantiere();
			result[i][1] = list.get(i).getNome();
			result[i][2] = list.get(i).getDescrizione();

		}
		return result;
	}

	// Funzione che converte una List<DatiSensore> in un Object[][] necessario per
	// la
	// costruzione di una JTable Amministratore
	public static Object[][] ConvertDatiSensoreListToObject(List<DatiSensore> list) {
		Object[][] result = new Object[list.size()][5];
		for (int i = 0; i < list.size(); i++) {
			result[i][0] = list.get(i).getIdSens();
			result[i][1] = list.get(i).getDatoRumore();
			result[i][2] = list.get(i).getDatoGas();
			result[i][3] = list.get(i).getAllarme();

			String dataConvertita = convertDate(dateTimeFormatDb, dateFormatApp, list.get(i).getDataGiorno());
			// Converto la data nel formato adeguato
			result[i][4] = dataConvertita;

		}
		return result;
	}

	// Funzione che converte una List<Area> in un Object[][] necessario per la
	// costruzione di una JTable Amministratore
	public static Object[][] ConvertAreaListToObject(List<Area> list) {
		Object[][] result = new Object[list.size()][4];
		for (int i = 0; i < list.size(); i++) {
			result[i][0] = list.get(i).getIdArea();
			result[i][1] = list.get(i).getNome();
			result[i][2] = list.get(i).getIdCant();

		}
		return result;
	}

	// Funzione che converte una List<Sensore> in un Object[][] necessario per la
	// costruzione di una JTable Amministratore
	public static Object[][] ConvertSensoreListToObject(List<Sensore> list) {
		Object[][] result = new Object[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			result[i][0] = list.get(i).getIdArea();
			result[i][1] = list.get(i).getIdSensore();

		}
		return result;
	}

	// Funzione che converte una List<Area> in un Object[][] necessario per la
	// costruzione di una JTable
	public static Object[][] ConvertAreaListToObjectCapoC(List<Area> list) {
		Object[][] result = new Object[list.size()][4];
		for (int i = 0; i < list.size(); i++) {
			result[i][0] = list.get(i).getIdArea();
			result[i][1] = list.get(i).getNome();

		}
		return result;
	}

	// Funzione che converte una data da un formato ad un altro
	public static String convertDate(String parserString, String formatterString, String data) {
		Date convertedDate = null;
		SimpleDateFormat parser = new SimpleDateFormat(parserString);
		SimpleDateFormat formatter = new SimpleDateFormat(formatterString);
		try {
			convertedDate = parser.parse(data);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return formatter.format(convertedDate);
	}

	// Funzione che controlla che l'operaio sia maggiorenne
	public static boolean isMaggiorenne(String strNum) {
		if (strNum == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date d = null;
		try {
			d = sdf.parse(strNum);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		LocalDate l1 = LocalDate.of(year, month, date);
		LocalDate now1 = LocalDate.now();
		Period diff1 = Period.between(l1, now1);

		if ((diff1.getYears()) >= 18) {

			return true;
		}

		else

			return false;
	}

	// Funzione che controlla che la stringa sia convertibile in una data
	public static boolean isDate(String strNDate, String dateFormat) {
		if (strNDate == null) {
			return false;
		}
		try {
			DateFormat sdf = new SimpleDateFormat(dateFormat);
			sdf.setLenient(false);

			sdf.parse(strNDate);
		} catch (ParseException pe) {
			System.out.println("Errore: stringa non è una data.");
			return false;
		}
		return true;
	}

	// Funzione che mostra un messaggio d'errore
	public static void showErrorMessage(JFrame frame, String message) {
		JOptionPane.showMessageDialog(frame, message, "ERRORE", JOptionPane.ERROR_MESSAGE);
	}

	// Funzione che mostra un messaggio che informa l'utente che l'operazione è
	// avvenuta con successo
	public static void showSuccessMessage(JFrame frame, String message) {
		JOptionPane.showMessageDialog(frame, message, "OPERAZIONE COMPLETATA", JOptionPane.INFORMATION_MESSAGE);
	}

	// Funziona utilizzata per riempire tabella in base alla lista degli operai
	// passata

	public static DefaultTableModel addElementsTable(List<Lavoratore> operai, DefaultTableModel model) {
		model.setRowCount(0); // mi resetto tutte le righe della tabella
		for (int i = 0; i < operai.size(); i++) {

			model.insertRow(model.getRowCount(),
					new Object[] { operai.get(i).getIdLav(), operai.get(i).getNome(), operai.get(i).getCognome(), Helper
							.convertDate(Helper.dateTimeFormatDb, Helper.dateFormatApp, operai.get(i).getDataNascita()),
							operai.get(i).getIdCant() });

		}
		return model;
	}

	// Funziona utilizzata per riempire tabella in base alla lista degli operai
	// passata

	public static DefaultTableModel addElementsTableDatiSensore(List<DatiSensore> DatiSensore,
			DefaultTableModel model) {
		model.setRowCount(0); // mi resetto tutte le righe della tabella

		for (int i = 0; i < DatiSensore.size(); i++) {

			model.insertRow(model.getRowCount(),
					new Object[] { DatiSensore.get(i).getIdSens(), DatiSensore.get(i).getDatoRumore(),
							DatiSensore.get(i).getDatoGas(), DatiSensore.get(i).getAllarme(),
							Helper.convertDate(Helper.dateTimeFormatDb, Helper.dateFormatApp,
									DatiSensore.get(i).getDataGiorno()) });

		}
		return model;
	}

	// Funziona utilizzata per riempire tabella in base alla lista dei sensori

	public static DefaultTableModel addElementsTableSensore(List<Sensore> sensore, DefaultTableModel model) {
		model.setRowCount(0); // mi resetto tutte le righe della tabella

		for (int i = 0; i < sensore.size(); i++) {

			model.insertRow(model.getRowCount(),
					new Object[] { sensore.get(i).getIdArea(), sensore.get(i).getIdSensore()});

		}
		return model;
	}

}
