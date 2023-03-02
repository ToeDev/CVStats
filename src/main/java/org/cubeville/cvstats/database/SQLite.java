package org.cubeville.cvstats.database;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubeville.cvstats.CVStats;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class SQLite {
	private Connection connection;
	private Statement statement;
	String fileName;

	public SQLite(String fileName) {
		this.fileName = fileName;
	}

	public void connect() {
		connection = null;

		try {
			if (!CVStats.getInstance().getDataFolder().exists()) {
				CVStats.getInstance().saveDefaultConfig();
			}
			File dbFile = new File(CVStats.getInstance().getDataFolder(), fileName + ".db");

			if (!dbFile.exists()) {
				dbFile.createNewFile();
			}
			String url = "jdbc:sqlite:" + dbFile.getPath();

			connection = DriverManager.getConnection(url);
			statement = connection.createStatement();

		} catch (IOException | SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void update(String sql) {
		try {
			statement.execute(sql);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public ResultSet getResult(String sql) {
		try {
			return statement.executeQuery(sql);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public void createBackup() throws IOException {
		File dbFile = new File(CVStats.getInstance().getDataFolder(), fileName + ".db");
		if (dbFile.exists()) {
			Path source = dbFile.toPath();
			Path destination = CVStats.getInstance().getDataFolder().toPath();
			Files.copy(source, destination.resolve(fileName + "-backup.db"), StandardCopyOption.REPLACE_EXISTING);
		}
	}
}