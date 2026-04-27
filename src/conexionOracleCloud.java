import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class conexionOracleCloud {

	/**
	 * Clase para la conexión con una base de datos Oracle Autonomous Database
	 * (Oracle Cloud)
	 */
	public class ConexionOracleCloud {

		private String USUARIO;
		private String PASS;
		private String TNS_ALIAS; // Ej: dbname_high
		private String WALLET_PATH; // Ruta donde descomprimiste el wallet
		private Connection connection;

		public ConexionOracleCloud(String usuario, String pass, String tnsAlias, String walletPath) {
			USUARIO = usuario;
			PASS = pass;
			TNS_ALIAS = tnsAlias;
			WALLET_PATH = walletPath;
			connection = null;
		}

		private void registrarDriver() throws SQLException {
			try {
				Class.forName("oracle.jdbc.OracleDriver");
			} catch (ClassNotFoundException e) {
				throw new SQLException("Error al cargar el driver de Oracle: " + e.getMessage());
			}
		}

		public void conectar() throws SQLException {
			if (connection == null || connection.isClosed()) {
				registrarDriver();

				String url = "jdbc:oracle:thin:@" + TNS_ALIAS + "?TNS_ADMIN=" + WALLET_PATH;

				Properties props = new Properties();
				props.put("user", USUARIO);
				props.put("password", PASS);
				props.put("oracle.net.wallet_location",
						"(SOURCE=(METHOD=FILE)(METHOD_DATA=(DIRECTORY=" + WALLET_PATH + ")))");
				props.put("oracle.net.tns_admin", WALLET_PATH);

				connection = DriverManager.getConnection(url, props);
			}
		}

		public void desconectar() throws SQLException {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}

		public ResultSet ejecutarSelect(String consulta) throws SQLException {
			Statement stmt = connection.createStatement();
			return stmt.executeQuery(consulta);
		}

		public int ejecutarInsertDeleteUpdate(String consulta) throws SQLException {
			Statement stmt = connection.createStatement();
			return stmt.executeUpdate(consulta);
		}
	}
}
