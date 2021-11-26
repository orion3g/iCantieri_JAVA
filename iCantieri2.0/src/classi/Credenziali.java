package classi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Credenziali {
	
	private String nomeUtente;
	private String password;
	private int idLav;
	
	
	public String getNomeUtente() {
		return nomeUtente;
	}
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getIdLav() {
		return idLav;
	}
	public void setIdLav(int idLav) {
		this.idLav = idLav;
	}
	
	
	//funzione che restituisce restituisce idlav se il login è andato bene
	
	boolean  verificaLogin(String username, char[] password) throws SQLException, IOException {
		// TODO Auto-generated method stub
		Connection conn = new Database().getDefaultConnection();
		PreparedStatement pstmt;
		ResultSet rs;
		String query = "SELECT * FROM CREDENZIALI WHERE utente=? and password=?";
		boolean i=false;
		
		Credenziali credenziali = new Credenziali();
		if(conn != null) {
			
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				
				credenziali.setNomeUtente(rs.getString("nomeUtente"));
				credenziali.setPassword(rs.getString("password"));
				credenziali.setIdLav(rs.getInt("idLav"));
				
				i=true;
			}
			pstmt.close();
		}
		
		if (i) {
		new Lavoratore().verificaTipoUtente(credenziali.getIdLav());
		}
		
		
		return i;
		
	}
	

	
	
	
	
	
	
	

}
