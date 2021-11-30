package classi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Lavoratore {
	
	private int idLav;
	private String nome;
	private String cognome;
	private String dataNascita;
	private String tipoLav;
	private int idCant;
	public int getIdLav() {
		return idLav;
	}
	public void setIdLav(int idLav) {
		this.idLav = idLav;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getTipoLav() {
		return tipoLav;
	}
	public void setTipoLav(String tipoLav) {
		this.tipoLav = tipoLav;
	}
	public int getIdCant() {
		return idCant;
	}
	public void setIdCant(int idCant) {
		this.idCant = idCant;
	}
	
	
	
	//funzione che crea un menu diverso a seconda della tipologia di amministratore o capocantiere
	
	protected String verificaTipoUtente(int idLav) throws SQLException, IOException {
		// TODO Auto-generated method stub
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;
		String query = "select * from VIEW_TIPO_LAV where IDLAV=?";

		Lavoratore lavoratore = new Lavoratore();
		
		
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idLav);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				
				lavoratore.setTipoLav(rs.getString(1));
				
			}
			pstmt.close();
		
		
		
		
		return lavoratore.getTipoLav();
		
	}
	
	

}
