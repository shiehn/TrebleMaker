package com.treblemaker.model.queues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.treblemaker.Application;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.utils.json.JsonUtils;

import javax.persistence.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;

@Entity
@Table(name = "queue_items")
@Cacheable(false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueItem {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "queue_item_id")
	private String QueueItemId;

	@Lob
	@Column(name="queue_item", length=100000)
	private String QueueItem;

	@Column(name = "status")
	private int JobStatus;

	@Column(name = "output_path")
	private String outputPath;

	@Column(name = "date")
	private Date date;

	@Column(name = "is_refactor")
	private boolean IsRefactor;

	@Column(name = "users_id")
	private Integer users_id;

	@Column(name = "bpm")
	private Integer bpm;

	@Column(name = "station_id")
    private int stationId;

	@Transient
	private ProgressionDTO progression;

	@Transient
	private String midiFilePath;

	@Transient
	private String audioPartFilePath;

    @Transient
    private String monoPartsFilePath;

    @Transient
    private String stereoPartsFilePath;

	@Transient
	Map<String, Double> selectedVolumeTargets;

	@Transient
	List<Map<String, Double>> finalVolumeLevels;

	@Transient
	private Double finalVolumeTargetMean;

	public String getQueueItemId() {
		return QueueItemId;
	}

	public void setQueueItemId(String queueItemId) {
		QueueItemId = queueItemId;
	}

	public String getQueueItem() {
		return QueueItem;
	}

	public ProgressionDTO getProgression() {

		if(progression != null){
			return progression;
		}

		try {
			this.progression = JsonUtils.mapper.readValue(this.QueueItem, ProgressionDTO.class);

			//TODO this is a major hack because the default constructor what giving me a hard time with jackson serialization . .
			progression.getStructure().forEach(progressionUnit -> {
				//TODO this SHOULD NOT BE HARD CODED TO 4 BARS
				progressionUnit.initBars(FOUR.getValue());
			});

		} catch (IOException e) {
			Application.logger.debug("LOG:",e);
		}

		return this.progression;
	}

	public void setProgression(ProgressionDTO progression) {
		this.progression = progression;
	}

	public void setQueueItem(String queueItem) {
		QueueItem = queueItem;
	}

	public int getJobStatus() {
		return JobStatus;
	}

	public void setJobStatus(int jobStatus) {
		JobStatus = jobStatus;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isRefactor() {
		return IsRefactor;
	}

	public void setIsRefactor(boolean isRefactor) {
		IsRefactor = isRefactor;
	}

	public Integer getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Integer users_id) {
		this.users_id = users_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getBpm() {
		return bpm;
	}

	public void setBpm(Integer bpm) {
		this.bpm = bpm;
	}

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getAudioPartFilePath() {
		return audioPartFilePath;
	}

	public void setAudioPartFilePath(String audioPartFilePath) {
		this.audioPartFilePath = audioPartFilePath;
	}

    public String getMonoPartsFilePath() {
        return monoPartsFilePath;
    }

    public void setMonoPartsFilePath(String monoPartsFilePath) {
        this.monoPartsFilePath = monoPartsFilePath;
    }

    public String getStereoPartsFilePath() {
        return stereoPartsFilePath;
    }

    public void setStereoPartsFilePath(String stereoPartsFilePath) {
        this.stereoPartsFilePath = stereoPartsFilePath;
    }

    public String getMidiFilePath() {
		return midiFilePath;
	}

	public void setMidiFilePath(String midiFilePath) {
		this.midiFilePath = midiFilePath;
	}

	public Map<String, Double> getSelectedVolumeTargets() {
		return selectedVolumeTargets;
	}

	public void setSelectedVolumeTargets(Map<String, Double> selectedVolumeTargets) {
		this.selectedVolumeTargets = selectedVolumeTargets;
	}

	public List<Map<String, Double>> getFinalVolumeLevels() {
		return finalVolumeLevels;
	}

	public void setFinalVolumeLevels(List<Map<String, Double>> finalVolumeLevels) {
		this.finalVolumeLevels = finalVolumeLevels;
	}

	public Double getFinalVolumeTargetMean() {
		return finalVolumeTargetMean;
	}

	public void setFinalVolumeTargetMean(Double finalVolumeTargetMean) {
		this.finalVolumeTargetMean = finalVolumeTargetMean;
	}
}
