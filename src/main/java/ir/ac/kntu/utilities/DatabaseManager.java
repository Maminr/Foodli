package ir.ac.kntu.utilities;

import ir.ac.kntu.helper.Logger;

import java.sql.*;

/**
 * DatabaseManager - Manages JDBC database connections
 * <p>
 * Uses SQLite for simplicity and portability
 * Can be easily switched to other databases (PostgreSQL, MySQL) by changing connection string
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private static final String DB_URL = "jdbc:sqlite:foodli.db";
    private static final String DRIVER = "org.sqlite.JDBC";

    private Connection connection;
    private final Logger logger;

    private DatabaseManager() {
        this.logger = Logger.getInstance();
        initializeDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Initialize database connection and create schema if needed
     */
    private void initializeDatabase() {
        try {
            // Load SQLite JDBC driver
            Class.forName(DRIVER);

            // Create connection
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);

            // Create schema if tables don't exist
            createSchema();

            logger.debug("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            logger.error("SQLite JDBC driver not found. Please add sqlite-jdbc dependency.");
            logger.error("For Gradle, add: implementation 'org.xerial:sqlite-jdbc:3.42.0.0'");
        } catch (SQLException e) {
            logger.error("Failed to initialize database: " + e.getMessage());
        }
    }

    /**
     * Create database schema from SQL file
     */
    private void createSchema() {
        try {
            // Read schema from resources
            String schema = readSchemaFromFile();

            if (schema != null && !schema.trim().isEmpty()) {
                // Execute schema creation
                String[] statements = schema.split(";");
                Statement stmt = connection.createStatement();

                for (String statement : statements) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                        try {
                            stmt.execute(trimmed);
                        } catch (SQLException e) {
                            // Ignore "table already exists" errors
                            if (!e.getMessage().contains("already exists")) {
                                logger.debug("Schema execution warning: " + e.getMessage());
                            }
                        }
                    }
                }

                connection.commit();
                stmt.close();
                logger.debug("Database schema initialized successfully.");
            }
        } catch (SQLException e) {
            logger.error("Failed to create database schema: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Failed to rollback: " + rollbackEx.getMessage());
            }
        }
    }

    /**
     * Read schema SQL from file
     */
    private String readSchemaFromFile() {
        try {
            java.io.InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("database/schema.sql");

            if (inputStream == null) {
                logger.debug("Schema file not found, using default schema creation.");
                return getDefaultSchema();
            }

            try (java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A")) {
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (Exception e) {
            logger.debug("Could not read schema file, using default: " + e.getMessage());
            return getDefaultSchema();
        }
    }

    /**
     * Get default schema if file not found
     */
    private String getDefaultSchema() {
        // Return minimal schema - full schema should be in schema.sql file
        return "-- Default schema placeholder";
    }

    /**
     * Get database connection
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            initializeDatabase();
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.debug("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Execute a query and return ResultSet
     */
    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        return stmt.executeQuery();
    }

    /**
     * Execute an update (INSERT, UPDATE, DELETE)
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        int result = stmt.executeUpdate();
        conn.commit();
        return result;
    }

    /**
     * Execute batch updates
     */
    public int[] executeBatch(String sql, java.util.List<Object[]> paramsList) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        for (Object[] params : paramsList) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.addBatch();
        }

        int[] results = stmt.executeBatch();
        conn.commit();
        return results;
    }

    /**
     * Begin transaction
     */
    public void beginTransaction() throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
    }

    /**
     * Commit transaction
     */
    public void commit() throws SQLException {
        Connection conn = getConnection();
        conn.commit();
        conn.setAutoCommit(true);
    }

    /**
     * Rollback transaction
     */
    public void rollback() throws SQLException {
        Connection conn = getConnection();
        conn.rollback();
        conn.setAutoCommit(true);
    }
}

