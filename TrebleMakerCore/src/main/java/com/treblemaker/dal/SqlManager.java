package com.treblemaker.dal;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.ISqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class SqlManager implements ISqlManager {

	private static Connection connection = null;

	private String springDatasourceUrl;
	private String springDatasourceUsername;
	private String springDatasourcePassword;

    @Autowired
	public SqlManager(@Value("${spring.datasource.url}") String springDatasourceUrl,
					  @Value("${spring.datasource.username}") String springDatasourceUsername,
					  @Value("${spring.datasource.password}") String springDatasourcePassword) {
		this.springDatasourceUrl = springDatasourceUrl;
		this.springDatasourceUsername = springDatasourceUsername;
		this.springDatasourcePassword = springDatasourcePassword;
	}

	public static void setConnection(Connection connection) {
		SqlManager.connection = connection;
	}

	public Connection getConnection() throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver");

		if(connection == null || connection.isClosed() || !connection.isValid(1000)){
			try {
				connection = DriverManager.getConnection(springDatasourceUrl,springDatasourceUsername, springDatasourcePassword);
			} catch (SQLException e) {
				Application.logger.debug("LOG:", e);
			}
		}

		return connection;
	}
}
