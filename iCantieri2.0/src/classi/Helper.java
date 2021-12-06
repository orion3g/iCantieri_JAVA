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






public class Helper {

	public final static String dateFormatApp = "dd/MM/yyyy"; //Formato data nell'applicazione
	public final static String timeFormatApp = "hh:mm:ss"; //Formato time nell'applicazione
	public final static String dateTimeFormatApp = "dd/MM/yyyy HH:mm:ss";	//Formato datatime nell'applicazione
	public final static String dateTimeFormatDb = "yyyy-MM-dd HH:mm:ss";	//Formato datatime nel database
	
	
	
	//Funzione che converte una List<Lavoratore> in un Object[][] necessario per la costruzione di una JTable
	public static Object[][] ConvertOperaioListToObject(List<Lavoratore> list){
		Object[][] result = new Object[list.size()][4];
		for(int i = 0; i < list.size(); i++){
		    result[i][0] = list.get(i).getIdLav();
		    result[i][1] = list.get(i).getNome();
		    result[i][2] = list.get(i).getCognome();
		   
		    
		    String dataConvertita = convertDate(dateTimeFormatDb, dateFormatApp, list.get(i).getDataNascita());	
		    //Converto la data nel formato adeguato
			result[i][3] = dataConvertita;
			
		}
		return result;
	}
	
	
	//Funzione che converte una List<Lavoratore> in un Object[][] necessario per la costruzione di una JTable per Amministratore
		public static Object[][] ConvertOperaioListToObjectAmministratore(List<Lavoratore> list){
			Object[][] result = new Object[list.size()][5];
			for(int i = 0; i < list.size(); i++){
			    result[i][0] = list.get(i).getIdLav();
			    result[i][1] = list.get(i).getNome();
			    result[i][2] = list.get(i).getCognome();
			   
			    
			    String dataConvertita = convertDate(dateTimeFormatDb, dateFormatApp, list.get(i).getDataNascita());	
			    //Converto la data nel formato adeguato
				result[i][3] = dataConvertita;
				
				result[i][4]=list.get(i).getIdCant();
				
			}
			return result;
		}
		
	
	//Funzione che converte una data da un formato ad un altro
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
	
	
	//Funzione che controlla che la stringa sia un numero intero positivo
		public static boolean isIntegerPositive(String strNum) {
		    if (strNum == null) {
		        return false;
		    }
		    try {
		    	if(strNum.contains("f") || strNum.contains("F") || strNum.contains("d") || strNum.contains("D") || strNum.contains("l") || strNum.contains("L")) {
		    		return false;
		    	}
		        int d = Integer.parseInt(strNum);
		        if(d <= 0) return false;
		    } catch (NumberFormatException nfe) {
		    	System.out.println("Errore: stringa non numerica.");
		        return false;
		    }
		    return true;
		}
		
		//Funzione che controlla che l'operaio sia maggiorenne
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
				    	 
				    	  if ((diff1.getYears())>=18) {
				    		  
				    		  return true;
				    	  }
				    
				    	  else
				    
				    return false;
				}
		
		//Funzione che controlla che la stringa sia convertibile in una data
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
		

				
		
		//Funzione che mostra un messaggio d'errore
		public static void showErrorMessage(JFrame frame, String message) {
			JOptionPane.showMessageDialog(frame, message, "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
		
		//Funzione che mostra un messaggio che informa l'utente che l'operazione è avvenuta con successo
		public static void showSuccessMessage(JFrame frame, String message) {
			JOptionPane.showMessageDialog(frame, message, "OPERAZIONE COMPLETATA", JOptionPane.INFORMATION_MESSAGE);
		}
		
		
		
	
	
}
