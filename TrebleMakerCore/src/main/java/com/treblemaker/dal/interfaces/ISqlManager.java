package com.treblemaker.dal.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface ISqlManager {

	Connection getConnection() throws ClassNotFoundException, SQLException;
}
