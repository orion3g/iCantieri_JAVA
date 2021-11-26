package classi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class Database {
	private String url = "jdbc:oracle:thin:@";
	private String host = "";
	private String port = "";
	private String sid = "";
	private String username = "";
	private String password = "";
	
	public Database() throws IOException {
		Properties prop = getPropertiesFromFile("dbconfig.properties");
		host = prop.getProperty("host");
		port = prop.getProperty("port");
		sid = prop.getProperty("sid");
		username = prop.getProperty("username");
		password = prop.getProperty("password");
	}
	
	private Properties getPropertiesFromFile(String filename) throws IOException {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
		
		if(is != null) {
			prop.load(is);
		}else {
			throw new FileNotFoundException("File " + filename + " non trovato.");
		}
		
		return prop;
	}

	public Connection getDefaultConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			String dbUrl = url + host + ":" + port + ":" + sid;
			conn = DriverManager.getConnection(dbUrl, username, password);
		}catch(SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
}
