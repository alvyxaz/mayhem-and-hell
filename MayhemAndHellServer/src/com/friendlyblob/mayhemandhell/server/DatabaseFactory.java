package com.friendlyblob.mayhemandhell.server;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;
import com.mchange.v2.c3p0.ComboPooledDataSource;


/*
 * SQL database connection handler
 */
public class DatabaseFactory {
		private static final Logger log = Logger.getLogger(DatabaseFactory.class.getName());
		
		private static DatabaseFactory instance;
		private static volatile ScheduledExecutorService executor;
		private ComboPooledDataSource source;
		
		/**
		 * Instantiates a new l2 database factory.
		 * @throws SQLException the SQL exception
		 */
		public DatabaseFactory() throws SQLException {
			try {
				
				
				if (Config.DATABASE_MAX_CONNECTIONS < 2) {
					Config.DATABASE_MAX_CONNECTIONS = 2;
					log.warning("A minimum of " + Config.DATABASE_MAX_CONNECTIONS + " db connections are required.");
				}
				
				source = new ComboPooledDataSource();
				source.setAutoCommitOnClose(true);
				
				source.setInitialPoolSize(10);
				source.setMinPoolSize(10);
				source.setMaxPoolSize(Math.max(10, Config.DATABASE_MAX_CONNECTIONS));
				
				source.setAcquireRetryAttempts(0); // try to obtain connections indefinitely (0 = never quit)
				source.setAcquireRetryDelay(500); // 500 milliseconds wait before try to acquire connection again
				source.setCheckoutTimeout(0); // 0 = wait indefinitely for new connection
				// if pool is exhausted
				source.setAcquireIncrement(5); // if pool is exhausted, get 5 more connections at a time
				// cause there is a "long" delay on acquire connection
				// so taking more than one connection at once will make connection pooling
				// more effective.
				
				// this "connection_test_table" is automatically created if not already there
				source.setAutomaticTestTable("connection_test_table");
				source.setTestConnectionOnCheckin(false);
				
				// testing OnCheckin used with IdleConnectionTestPeriod is faster than testing on checkout
				
				source.setIdleConnectionTestPeriod(3600); // test idle connection every 60 sec
				source.setMaxIdleTime(Config.DATABASE_MAX_IDLE_TIME); // 0 = idle connections never expire
				// *THANKS* to connection testing configured above
				// but I prefer to disconnect all connections not used
				// for more than 1 hour
				
				// enables statement caching, there is a "semi-bug" in c3p0 0.9.0 but in 0.9.0.2 and later it's fixed
				source.setMaxStatementsPerConnection(100);
				
				source.setBreakAfterAcquireFailure(false); // never fail if any way possible
				// setting this to true will make
				// c3p0 "crash" and refuse to work
				// till restart thus making acquire
				// errors "FATAL" ... we don't want that
				// it should be possible to recover
				source.setDriverClass(Config.DATABASE_DRIVER);
				source.setJdbcUrl(Config.DATABASE_URL);
				source.setUser(Config.DATABASE_LOGIN);
				source.setPassword(Config.DATABASE_PASSWORD);
				
				/* Test the connection */
				source.getConnection().close();
				
			} catch (SQLException x) {

				throw x;
			} catch (Exception e) {

				throw new SQLException("Could not init DB connection:" + e.getMessage());
			}
		}
		
		/**
		 * Prepared query select.
		 * @param fields the fields
		 * @param tableName the table name
		 * @param whereClause the where clause
		 * @param returnOnlyTopRecord the return only top record
		 * @return the string
		 */
		public final String prepQuerySelect(String[] fields, String tableName, String whereClause, boolean returnOnlyTopRecord)
		{
			String msSqlTop1 = "";
			String mySqlTop1 = "";
			if (returnOnlyTopRecord) {
				mySqlTop1 = " Limit 1 ";
			}
			String query = "SELECT " + msSqlTop1 + safetyString(fields) + " FROM " + tableName + " WHERE " + whereClause + mySqlTop1;
			return query;
		}
		
		/**
		 * Shutdown.
		 */
		public void shutdown()
		{
			try
			{
				source.close();
			}
			catch (Exception e)
			{
				log.log(Level.INFO, "", e);
			}
			try
			{
				source = null;
			}
			catch (Exception e)
			{
				log.log(Level.INFO, "", e);
			}
		}
		
		/**
		 * Safety string.
		 * @param whatToCheck the what to check
		 * @return the string
		 */
		public final String safetyString(String... whatToCheck)
		{
			// NOTE: Use brace as a safety precaution just in case name is a reserved word
			final char braceLeft;
			final char braceRight;

			braceLeft = '`';
			braceRight = '`';
			
			int length = 0;
			
			for (String word : whatToCheck)
			{
				length += word.length() + 4;
			}
			
			final StringBuilder sbResult = new StringBuilder(length);
			
			for (String word : whatToCheck)
			{
				if (sbResult.length() > 0)
				{
					sbResult.append(", ");
				}
				
				sbResult.append(braceLeft);
				sbResult.append(word);
				sbResult.append(braceRight);
			}
			
			return sbResult.toString();
		}
		
		/**
		 * Gets the single instance of L2DatabaseFactory.
		 * @return single instance of L2DatabaseFactory
		 * @throws SQLException the SQL exception
		 */
		public static DatabaseFactory getInstance() throws SQLException
		{
			synchronized (DatabaseFactory.class)
			{
				if (instance == null)
				{
					instance = new DatabaseFactory();
				}
			}
			return instance;
		}
		
		/**
		 * Gets the connection.
		 * @return the connection
		 */
		public Connection getConnection()
		{
			Connection con = null;
			while (con == null)
			{
				try
				{
					con = source.getConnection();

					ThreadPoolManager.getInstance().scheduleGeneral(new ConnectionCloser(con, new RuntimeException()), Config.CONNECTION_CLOSE_TIME);

				}
				catch (SQLException e)
				{
					log.log(Level.WARNING, "L2DatabaseFactory: getConnection() failed, trying again " + e.getMessage(), e);
				}
			}
			return con;
		}
		
		/**
		 * The Class ConnectionCloser.
		 */
		private static class ConnectionCloser implements Runnable
		{
			private static final Logger _log = Logger.getLogger(ConnectionCloser.class.getName());
			
			/** The connection. */
			private final Connection c;
			
			/** The exception. */
			private final RuntimeException exp;
			
			/**
			 * Instantiates a new connection closer.
			 * @param con the con
			 * @param e the e
			 */
			public ConnectionCloser(Connection con, RuntimeException e)
			{
				c = con;
				exp = e;
			}
			
			@Override
			public void run()
			{
				try
				{
					if (!c.isClosed())
					{
						_log.log(Level.WARNING, "Unclosed connection! Trace: " + exp.getStackTrace()[1], exp);
					}
				}
				catch (SQLException e)
				{
					_log.log(Level.WARNING, "", e);
				}
			}
		}
		
		/**
		 * Close the connection.
		 * @param con the con the connection
		 * @deprecated now database connections are closed using try-with-resource.
		 */
		@Deprecated
		public static void close(Connection con)
		{
			if (con == null)
			{
				return;
			}
			
			try
			{
				con.close();
			}
			catch (SQLException e)
			{
				log.log(Level.WARNING, "Failed to close database connection!", e);
			}
		}
		
		/**
		 * Gets the executor.
		 * @return the executor
		 */
		private static ScheduledExecutorService getExecutor()
		{
			if (executor == null)
			{
				synchronized (DatabaseFactory.class)
				{
					if (executor == null)
					{
						executor = Executors.newSingleThreadScheduledExecutor();
					}
				}
			}
			return executor;
		}
		
		/**
		 * Gets the busy connection count.
		 * @return the busy connection count
		 * @throws SQLException the SQL exception
		 */
		public int getBusyConnectionCount() throws SQLException {
			return source.getNumBusyConnectionsDefaultUser();
		}
		
		/**
		 * Gets the idle connection count.
		 * @return the idle connection count
		 * @throws SQLException the SQL exception
		 */
		public int getIdleConnectionCount() throws SQLException {
			return source.getNumIdleConnectionsDefaultUser();
		}
		
		
		/**
		 * Designed to execute simple db operations like INSERT, UPDATE, DELETE.
		 * @param query
		 * @param params
		 * @throws SQLException
		 */
		public void executeQuery(String query, Object... params) throws SQLException
		{
			try (Connection con = getConnection();
				PreparedStatement st = con.prepareStatement(query))
			{
				for (int i = 0; i < params.length; i++)
				{
					st.setObject(i + 1, params[i]);
				}
				st.execute();
			}
		}
}
