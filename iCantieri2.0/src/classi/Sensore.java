package classi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sensore {
	
	private int idSensore;
	private int idLav;
	private int idArea;
	
	
	public int getIdSensore() {
		return idSensore;
	}
	public void setIdSensore(int idSensore) {
		this.idSensore = idSensore;
	}
	public int getIdLav() {
		return idLav;
	}
	public void setIdLav(int idLav) {
		this.idLav = idLav;
	}
	public int getIdArea() {
		return idArea;
	}
	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}
	
	
	// Funzione che restituisce un oggetto area contenuto in una tabella nel
	// database e con l'id corrispondente all'id passato come parametro
	public Sensore getSensore(int idArea) throws IOException, SQLException {

		Sensore sensore = new Sensore();
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;

		String query = "SELECT * FROM SENSORE WHERE IDAREA=?"; // Query in SQL
		if (conn != null) {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idArea);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				sensore.setIdLav(rs.getInt(2));
				sensore.setIdArea(rs.getInt(3));
			}
			pstmt.close();
		}
		return sensore;
	}
	
	

}
