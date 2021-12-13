package classi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;

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

	Lavoratore lavoratore = new Lavoratore();
	
	
	// Funzione che mostra la pagina che contiene la lista delle aree
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

	
	

}
