import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Clase para conectar a Oracle Autonomous Database usando un wallet
 * incluido dentro del proyecto (src\main\resources\Wallet_BBDDMarianaMarina).
 */
public class ConexionOracleCloud {

    private String USUARIO;
    private String PASS;
    private String TNS_ALIAS;     // Ej: dbname_high
    private Connection connection;
    private String walletTempPath; // Ruta temporal donde se copia el wallet

    public ConexionOracleCloud(String usuario, String pass, String tnsAlias) {
        USUARIO = usuario;
        PASS = pass;
        TNS_ALIAS = tnsAlias;
        connection = null;
    }

    /**
     * Carga el wallet desde resources y lo copia a una carpeta temporal.
     */
    private void cargarWallet() throws IOException, URISyntaxException {
        if (walletTempPath != null) return; // Ya cargado

        // Carpeta temporal
        Path tempDir = Files.createTempDirectory("wallet");
        walletTempPath = tempDir.toString();

        // Carpeta dentro de resources
        URL walletURL = getClass().getResource("\Wallet_BBDDMarianaMarina");
        if (walletURL == null) {
            throw new IOException("No se encontró la carpeta /wallet en resources.");
        }

        Path walletPath = Paths.get(walletURL.toURI());

        // Copiar todos los archivos del wallet
        Files.walk(walletPath).forEach(source -> {
            try {
                Path destino = tempDir.resolve(walletPath.relativize(source).toString());
                if (Files.isDirectory(source)) {
                    Files.createDirectories(destino);
                } else {
                    Files.copy(source, destino, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error copiando el wallet", e);
            }
        });
    }

    /**
     * Registra el driver JDBC de Oracle.
     */
    private void registrarDriver() throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se pudo cargar el driver de Oracle: " + e.getMessage());
        }
    }

    /**
     * Conecta con Oracle Autonomous Database usando el wallet.
     */
    public void conectar() throws SQLException {
        if (connection != null && !connection.isClosed()) return;

        try {
            cargarWallet();
        } catch (Exception e) {
            throw new SQLException("Error cargando el wallet: " + e.getMessage());
        }

        registrarDriver();

        // URL JDBC usando el alias del tnsnames.ora
        String url = "jdbc:oracle:thin:@" + TNS_ALIAS + "?TNS_ADMIN=" + walletTempPath;

        Properties props = new Properties();
        props.put("user", USUARIO);
        props.put("password", PASS);
        props.put("oracle.net.wallet_location",
                "(SOURCE=(METHOD=FILE)(METHOD_DATA=(DIRECTORY=" + walletTempPath + ")))");
        props.put("oracle.net.tns_admin", walletTempPath);

        connection = DriverManager.getConnection(url, props);
    }

    /**
     * Cierra la conexión.
     */
    public void desconectar() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Ejecuta SELECT.
     */
    public ResultSet ejecutarSelect(String consulta) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(consulta);
    }

    /**
     * Ejecuta INSERT, UPDATE o DELETE.
     */
    public int ejecutarInsertDeleteUpdate(String consulta) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(consulta);
    }
}
