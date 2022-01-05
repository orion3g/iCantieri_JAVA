package classi;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;

public class Area {

	private int idArea;
	private String Nome;
	private int idLav;
	private int idCant;

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

	public int getIdLav() {
		return idLav;
	}

	public void setIdLav(int idLav) {
		this.idLav = idLav;
	}

	public int getIdCant() {
		return idCant;
	}

	public void setIdCant(int idCant) {
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
				area.setNome(rs.getString(2));
				area.setIdCant(rs.getInt(4));

				Listaree.add(area);
			}
			pstmt.close();
		}
		return Listaree;
	}

	// Funzione che restituisce un oggetto area contenuto in una tabella nel
	// database e con l'id corrispondente all'id passato come parametro

	public List<Area> getAreePerCantiere(int idCant) throws IOException, SQLException {
		List<Area> Listaree = new ArrayList<Area>();
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;
		

		String query = "SELECT * FROM AREA WHERE IDCANT=?"; // Query in SQL
		if (conn != null) {

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idCant);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Area area = new Area();
				area.setIdArea(rs.getInt(1));
				area.setNome(rs.getString(2));
				
				
				Listaree.add(area);
			}
			pstmt.close();
		}
		return Listaree;
	}

	// Funzione utilizzata per salvare un'area. Se è presente l'id nell'oggetto
	// area allora aggiorna un'area esistente, altrimenti ne crea una nuova

	protected Boolean saveArea(Area area, Lavoratore responsabile, Lavoratore operatore) throws IOException, SQLException {
		// TODO Auto-generated method stub
		Connection conn = new Database().getDefaultConnection();
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		int rows = 0;
		String query;
		String query1;
		if (conn != null) {
			if (area.getIdArea() == 0) { // Se l'area non esiste
				cstmt = conn.prepareCall("{call INSERTAREA(?,?,?,?,?,?,?,?)}");
				cstmt.setString(1, operatore.getNome());
				cstmt.setString(2, operatore.getCognome());

				cstmt.setTimestamp(3, java.sql.Timestamp.valueOf(operatore.getDataNascita()));
				
				cstmt.setString(4, responsabile.getNome());
				cstmt.setString(5, responsabile.getCognome());

				cstmt.setTimestamp(6, java.sql.Timestamp.valueOf(responsabile.getDataNascita()));
				cstmt.setInt(7, responsabile.getIdCant());
				cstmt.setString(8, area.getNome());

				rows = cstmt.executeUpdate();
			} else { // Se l'area esiste

				cstmt = conn.prepareCall("{call UPDATEAREA(?,?,?,?,?,?,?,?,?,?)}");

				cstmt.setString(1, operatore.getNome());
				cstmt.setString(2, operatore.getCognome());
				cstmt.setTimestamp(3, java.sql.Timestamp.valueOf(operatore.getDataNascita()));
				cstmt.setInt(4, operatore.getIdLav());
				
				cstmt.setString(5, responsabile.getNome());
				cstmt.setString(6, responsabile.getCognome());
				cstmt.setTimestamp(7, java.sql.Timestamp.valueOf(responsabile.getDataNascita()));
				cstmt.setInt(8, responsabile.getIdLav());
				cstmt.setInt(9, area.getIdArea());
				cstmt.setString(10, area.getNome());

				rows = cstmt.executeUpdate();
			}

		}

		if (rows == 0) {
			return false;
		}

		return true;
	}

	// Funzione che restituisce un oggetto area contenuto in una tabella nel
	// database e con l'id corrispondente all'id passato come parametro
	public Area getArea(int idArea) throws IOException, SQLException {

		Area area = new Area();
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;

		String query = "SELECT * FROM AREA WHERE IDAREA=?"; // Query in SQL
		if (conn != null) {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idArea);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				area.setIdArea(rs.getInt(1));
				area.setNome(rs.getString(2));
				area.setIdLav(rs.getInt(3));
				area.setIdCant(rs.getInt(4));
			}
			pstmt.close();
		}
		return area;
	}

	// Funzione per eliminare un'area
	public boolean deleteArea(int idArea, int idLav) throws IOException, SQLException {
		// TODO Auto-generated method stub
		Connection conn = new Database().getDefaultConnection();
		CallableStatement cstmt = null;
		int rows = 0;
		if (conn != null) {

			cstmt = conn.prepareCall("{call DELETEAREA(?, ?)}");
			cstmt.setInt (1, idArea);
			cstmt.setInt (2, idLav);
		

			rows = cstmt.executeUpdate();

		}

		if (rows == 0) {
			return false;
		}

		return true;
	}

}
