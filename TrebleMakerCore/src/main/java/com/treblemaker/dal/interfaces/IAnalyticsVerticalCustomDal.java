package com.treblemaker.dal.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IAnalyticsVerticalCustomDal {

    ResultSet getAnalyticsVertical() throws SQLException, ClassNotFoundException;
}
