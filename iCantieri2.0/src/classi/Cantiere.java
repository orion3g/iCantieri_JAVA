package classi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cantiere {
	
	private int idCantiere;
	private String nome;
	private String descrizione;
	
	
	
	public int getIdCantiere() {
		return idCantiere;
	}
	public void setIdCantiere(int idCantiere) {
		this.idCantiere = idCantiere;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	
	//Funzione che mi restituisce tutti i cantieri nel DB  
	
	public List<Cantiere> getAllCantieri() throws IOException, SQLException {
		List<Cantiere> ListaCantieri = new ArrayList<Cantiere>();
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;

		String query = "SELECT * FROM CANTIERE"; // Query in SQL
		if (conn != null) {
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Cantiere cantiere = new Cantiere();
				cantiere.setIdCantiere(rs.getInt(1));
				cantiere.setNome(rs.getString(2));
				cantiere.setDescrizione(rs.getString(3));
		

				ListaCantieri.add(cantiere);
			}
			pstmt.close();
		}
		return ListaCantieri;
	}
	

	// Funzione che restituisce un oggetto cantiere contenuto in una tabella nel
	// database e con l'id corrispondente all'id passato come parametro
	public Cantiere getCantiere(int idCantiere) throws IOException, SQLException {
		Cantiere cantiere = new Cantiere();
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;

		String query = "SELECT * FROM CANTIERE WHERE IDCANTIERE=?"; // Query in SQL
		if (conn != null) {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idCantiere);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				cantiere.setIdCantiere(rs.getInt(1));
				cantiere.setNome(rs.getString(2));
				cantiere.setDescrizione(rs.getString(3));
			
			}
			pstmt.close();
		}
		return cantiere;
	}
	
	// Funzione utilizzata per salvare un cantiere. Se è presente l'id nell'oggetto
		// cantiere allora aggiorna un operaio esistente, altrimenti ne crea uno nuovo
		protected Boolean saveCantiere(Cantiere cantiere) throws IOException, SQLException {
			// TODO Auto-generated method stub
			Connection conn = new Database().getDefaultConnection();
			PreparedStatement pstmt;
			Integer rows = 0;
			String query;
			if (conn != null) {
				if (cantiere.getIdCantiere() == 0) { // Se il cantiere non esiste
					query = "INSERT INTO CANTIERE(nome, descrizione) values(?, ?)";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, cantiere.getNome());
					pstmt.setString(2, cantiere.getDescrizione());

				} else { // Se il cantiere esiste
					query = "UPDATE CANTIERE SET nome = ?, descrizione = ? WHERE idcantiere = ?";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, cantiere.getNome());
					pstmt.setString(2, cantiere.getDescrizione());
					pstmt.setInt(3, cantiere.getIdCantiere());
				}

				rows = pstmt.executeUpdate();
				pstmt.close();
			}

			if (rows > 0) {
				return true;
			}
			return false;
		}
	
	
	

}
