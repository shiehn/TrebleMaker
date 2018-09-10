package com.treblemaker.dal;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IQueueItemCustomDal;
import com.treblemaker.dal.interfaces.IQueueItemsDal;
import com.treblemaker.dal.interfaces.ISqlManager;
import com.treblemaker.model.queues.QueueItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class QueueItemCustomDal implements IQueueItemCustomDal {

    private ISqlManager sqlManager;
    private IQueueItemsDal queueItemsDal;

    @Autowired
    public QueueItemCustomDal(ISqlManager sqlManager, IQueueItemsDal queueItemsDal) {
        this.sqlManager = sqlManager;
        this.queueItemsDal = queueItemsDal;
    }

    public QueueItem getQueueItem() throws Exception {

        Iterable<QueueItem> queueItems = queueItemsDal.findAll();

        for(QueueItem queueItem : queueItems){
            if(queueItem.getJobStatus() == 0 && queueItem.isRefactor() == false){
                return queueItem;
            }
        }

        return null;
    }

    public QueueItem getQueueItemById(String queueItemId) throws SQLException, ClassNotFoundException {

        QueueItem queueItem = null;

        Statement statement = sqlManager.getConnection().createStatement();

        String sqlString = "SELECT * FROM queue_items WHERE id = " + queueItemId;
        ResultSet resultSet = statement.executeQuery(sqlString);

        while (resultSet.next()) {

            queueItem = new QueueItem();

            queueItem.setId(resultSet.getInt("id"));
            queueItem.setQueueItemId(resultSet.getString("queue_item_id"));
            queueItem.setQueueItem(resultSet.getString("queue_item_id"));
            queueItem.setJobStatus(resultSet.getInt("status"));
            queueItem.setIsRefactor(resultSet.getBoolean("is_refactor"));
        }


        return queueItem;
    }

    public QueueItem getRefactoredQueueItem(String refactoredQueueId) throws SQLException, ClassNotFoundException {

        QueueItem queueItem = null;

            Statement statement = sqlManager.getConnection().createStatement();

            String sqlString = "SELECT TOP 1 * FROM queue_items WHERE QueueItemId = '" + refactoredQueueId + "' AND IsRefactor = 1";
            ResultSet resultSet = statement.executeQuery(sqlString);

            while (resultSet.next()) {

                queueItem = new QueueItem();

                queueItem.setId(resultSet.getInt("id"));
                queueItem.setQueueItemId(resultSet.getString("queue_item_id"));
                queueItem.setQueueItem(resultSet.getString("queue_item_id"));
                queueItem.setJobStatus(resultSet.getInt("status"));
                queueItem.setIsRefactor(resultSet.getBoolean("is_refactor"));
            }


        return queueItem;
    }

    public void setQueueItemProcessing(String queueItemId) throws ClassNotFoundException {

        try {

            String sqlString = "UPDATE queue_items SET status = 1 WHERE queue_item_id = '" + queueItemId + "'";

            // Use the connection to create the SQL statement.
            Statement statement = sqlManager.getConnection().createStatement();

            // Execute the statement.
            statement.executeUpdate(sqlString);

        } catch (SQLException e) {
            Application.logger.debug("LOG:", e);
        }
    }

    public void setQueueItemComplete(String queueItemId, String outputPath){

        //String jsonQueueItem = new ObjectMapper().writeValueAsString(queueItem.getQueueItem());

        String sqlString = "UPDATE queue_items SET status = 2, is_refactor = 1, output_path = '" + outputPath + "' WHERE id = " + queueItemId;

        // Use the connection to create the SQL statement.
        Statement statement = null;
        try {
            statement = sqlManager.getConnection().createStatement();
        } catch (SQLException e) {
            Application.logger.debug("LOG:", e);
        } catch (ClassNotFoundException e) {
            Application.logger.debug("LOG:", e);
        }

        // Execute the statement.
        try {
            statement.executeUpdate(sqlString);
        } catch (SQLException e) {
            Application.logger.debug("LOG:", e);
        }
    }
}
