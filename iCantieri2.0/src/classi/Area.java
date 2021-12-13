package classi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Area {

	private int idArea;
	private String Nome;
	private String idLav;
	private String idCant;

	public int getIdArea() {
		return idArea;
	}

	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public String getIdLav() {
		return idLav;
	}

	public void setIdLav(String idLav) {
		this.idLav = idLav;
	}

	public String getIdCant() {
		return idCant;
	}

	public void setIdCant(String idCant) {
		this.idCant = idCant;
	}

//Funzione che mi restituisce tutte le aree nel DB  

	public List<Area> getAllAree() throws IOException, SQLException {
		List<Area> Listaree = new ArrayList<Area>();
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;

		String query = "SELECT * FROM AREA"; // Query in SQL
		if (conn != null) {
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Area area = new Area();
				area.setIdArea(rs.getInt(1));
				area.setIdCant(rs.getString(2));

				Listaree.add(area);
			}
			pstmt.close();
		}
		return Listaree;
	}

}
