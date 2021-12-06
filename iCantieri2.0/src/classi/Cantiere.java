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
	
	
	
	
	
	
	

}
