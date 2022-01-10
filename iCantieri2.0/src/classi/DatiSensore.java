package classi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatiSensore {

	private float datoRumore;
	private float datoGas;
	private String dataGiorno;
	private int allarme;
	private int idSens;

	public float getDatoRumore() {
		return datoRumore;
	}

	public void setDatoRumore(float datoRumore) {
		this.datoRumore = datoRumore;
	}

	public float getDatoGas() {
		return datoGas;
	}

	public void setDatoGas(float datoGas) {
		this.datoGas = datoGas;
	}

	public String getDataGiorno() {
		return dataGiorno;
	}

	public void setDataGiorno(String dataGiorno) {
		this.dataGiorno = dataGiorno;
	}

	public int getAllarme() {
		return allarme;
	}

	public void setAllarme(int allarme) {
		this.allarme = allarme;
	}

	public int getIdSens() {
		return idSens;
	}

	public void setIdSens(int idSens) {
		this.idSens = idSens;
	}

	// Funzione che restituisce tutti i Dati Sensore

	public List<DatiSensore> getAllDati() throws IOException, SQLException {

		List<DatiSensore> listDatiSensore = new ArrayList<DatiSensore>();
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;

		String query = "SELECT * FROM DATISENSORE"; // Query in SQL
		if (conn != null) {
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				DatiSensore datiSensore = new DatiSensore();

				datiSensore.setDatoRumore(rs.getFloat(1));
				datiSensore.setDatoGas(rs.getFloat(2));
				datiSensore.setDataGiorno(rs.getString(3));
				datiSensore.setAllarme(rs.getInt(4));
				datiSensore.setIdSens(rs.getInt(5));

				listDatiSensore.add(datiSensore);
			}
			pstmt.close();
		}
		return listDatiSensore;
	}

	// Funzione che restituisce tutti i Dati Sensore per cantiere

	public List<DatiSensore> getDatiPerCantiere(int idCant) throws IOException, SQLException {

		List<DatiSensore> listDatiSensore = new ArrayList<DatiSensore>();
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;

		String query = "select sensore.idsensore, datisensore.datorumore, datisensore.datogas, datisensore.datag, datisensore.allarme from sensore join datisensore on sensore.idsensore=datisensore.idsens join area on area.idarea=sensore.idarea where  area.idcant=?"; // Query
																																																																			// in
																																																																			// SQL
		if (conn != null) {
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, idCant);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DatiSensore datiSensore = new DatiSensore();

				datiSensore.setIdSens(rs.getInt(1));
				datiSensore.setDatoRumore(rs.getFloat(2));
				datiSensore.setDatoGas(rs.getFloat(3));
				datiSensore.setDataGiorno(rs.getString(4));
				datiSensore.setAllarme(rs.getInt(5));

				listDatiSensore.add(datiSensore);
			}
			pstmt.close();
		}
		return listDatiSensore;
	}

	// Funzione che restituisce tutti i Dati Sensore per cantiere e area

	public List<DatiSensore> getDatiPerArea(int idArea) throws IOException, SQLException {

		List<DatiSensore> listDatiSensore = new ArrayList<DatiSensore>();
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;

		String query = "select sensore.idsensore, datisensore.datorumore, datisensore.datogas, "
				+ "datisensore.datag, datisensore.allarme from sensore join datisensore on sensore.idsensore=datisensore.idsens "
				+ "where sensore.idarea=?"; // Query in SQL
		if (conn != null) {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idArea);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DatiSensore datiSensore = new DatiSensore();

				datiSensore.setIdSens(rs.getInt(1));
				datiSensore.setDatoRumore(rs.getFloat(2));
				datiSensore.setDatoGas(rs.getFloat(3));
				datiSensore.setDataGiorno(rs.getString(4));
				datiSensore.setAllarme(rs.getInt(5));

				listDatiSensore.add(datiSensore);
			}
			pstmt.close();
		}
		return listDatiSensore;
	}

	// Funzione utilizzata per salvare un Dato Sansore.
	protected Boolean saveDatoSensore(DatiSensore datiSensore) throws IOException, SQLException {
		// TODO Auto-generated method stub
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		Integer rows = 0;
		String query;

		query = "INSERT INTO DATISENSORE(datorumore, datogas, datag, idsens) values(?,?,?,?)";
		pstmt = conn.prepareStatement(query);
		pstmt.setFloat(1, datiSensore.getDatoRumore());
		pstmt.setFloat(2, datiSensore.getDatoGas());
		pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(datiSensore.getDataGiorno()));
		pstmt.setInt(4, datiSensore.getIdSens());

		rows = pstmt.executeUpdate();
		pstmt.close();

		if (rows > 0)

		{
			return true;
		}
		return false;
	}

}
