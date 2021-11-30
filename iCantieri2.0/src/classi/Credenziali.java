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
	
	
	//funzione che restituisce restituisce tipolav se il login è andato bene
	
	Lavoratore verificaLogin(String username, char[] password) throws SQLException, IOException {
		// TODO Auto-generated method stub
		Connection conn = new Database().getDefaultConnection();
		String query = "SELECT * FROM CREDENZIALI WHERE utente=? and password=?";
		PreparedStatement pstmt=conn.prepareStatement(query);
		
		boolean i=false;
		
		String s=String.copyValueOf(password);
		
	
		
		pstmt.setString(1, username);
		pstmt.setString(2, s);
		ResultSet rs=pstmt.executeQuery();
		
		
		
		Credenziali credenziali = new Credenziali();
		
				
		while (rs.next()) {
				
				credenziali.setNomeUtente(rs.getString(1));
				credenziali.setPassword(rs.getString(2));
				credenziali.setIdLav(rs.getInt(3));
				
				i=true;
			}
			pstmt.close();
		
	   String errore="ERRORE";
		
	   /*Se i è true quindi significa che ha trovato utente con user e password inseriti mi controlla di che tipo di utente
	   si tratta, se non trova niente ritorna null */
	   
	   
	    
	   Lavoratore lavoratore=new Lavoratore().verificaTipoUtente(credenziali.getIdLav());
		
		
	    
		
		return lavoratore;
		
	}
	
		

}
