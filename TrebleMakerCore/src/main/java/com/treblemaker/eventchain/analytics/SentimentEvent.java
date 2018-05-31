package com.treblemaker.eventchain.analytics;

import com.treblemaker.dal.interfaces.ISentimentCompositionDal;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.sentiment.SentimentComposition;
import com.treblemaker.model.sentiment.SentimentSection;
import com.treblemaker.model.sentiment.SentimentSections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SentimentEvent {

    private ISentimentCompositionDal sentimentCompositionDal;

    @Autowired
    public SentimentEvent(ISentimentCompositionDal sentimentCompositionDal) {
        this.sentimentCompositionDal = sentimentCompositionDal;
    }

    public void saveSenimentLabels(QueueState queueState){
        SentimentComposition sentimentComposition = createSentimentComposition(queueState);
        this.sentimentCompositionDal.save(sentimentComposition);
    }

    public SentimentComposition createSentimentComposition(QueueState queueState){

        SentimentSections sentimentSections = new SentimentSections();

        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            for(int i=0; i<progressionUnit.getProgressionUnitBars().size(); i++){

                if(i % 2 == 0) {
                    ProgressionUnitBar progressionUnitBar = progressionUnit.getProgressionUnitBars().get(i);

                    SentimentSection sentimentSection = new SentimentSection();
                    sentimentSection.setSentimentLabels(progressionUnitBar.getSentimentLabels());

                    sentimentSections.addSentimentSection(sentimentSection);
                }
            }
        }

        SentimentComposition sentimentComposition = new SentimentComposition();
        sentimentComposition.setCompositionId(queueState.getQueueItem().getQueueItemId());
        sentimentComposition.setSentimentJson(sentimentSections);

        return sentimentComposition;
    }
}
