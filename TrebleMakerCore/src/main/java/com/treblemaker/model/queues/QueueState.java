package com.treblemaker.model.queues;

import com.treblemaker.model.ProcessingState;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.progressions.ProgressionUnit;

import java.util.ArrayList;
import java.util.List;

public class QueueState {

    private String appRoot;

    private QueueItem queueItem;

    private SourceData dataSource;

    private ProcessingState processingState;

    private boolean proceed;

    private boolean breakThreads;

    public QueueItem getQueueItem() {
        return queueItem;
    }

    public void setQueueItem(QueueItem queueItem) {
        this.queueItem = queueItem;
    }

    public SourceData getDataSource() {
        return dataSource;
    }

    public void setDataSource(SourceData dataSource) {
        this.dataSource = dataSource;
    }

    public ProcessingState getProcessingState() {
        return processingState;
    }

    public void setProcessingState(ProcessingState processingState) {
        this.processingState = processingState;
    }

    public boolean shouldProceed() {
        return proceed;
    }

    public void setProceed(boolean proceed) {
        this.proceed = proceed;
    }

    public boolean shouldBreakThreads() {
        return breakThreads;
    }

    public void breakThreads() {
        this.breakThreads = true;
    }

    public List<ProgressionUnit> getStructure(){

        if(this.getQueueItem() != null && this.getQueueItem().getProgression() != null && this.getQueueItem().getProgression().getStructure() != null){
            return this.getQueueItem().getProgression().getStructure();
        }

        return new ArrayList<>();
    }

    public String getAppRoot() {
        return appRoot;
    }

    public void setAppRoot(String appRoot) {
        this.appRoot = appRoot;
    }
}
