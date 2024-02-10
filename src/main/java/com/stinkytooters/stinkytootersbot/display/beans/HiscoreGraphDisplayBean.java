package com.stinkytooters.stinkytootersbot.display.beans;

import java.util.ArrayList;
import java.util.List;

public class HiscoreGraphDisplayBean {

    private String id;
    private List<String> labels = new ArrayList<>();
    private List<Long> data = new ArrayList<Long>();
    private String dataLabel = "NO LABEL";

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Long> getData() {
        return data;
    }

    public void setData(List<Long> data) {
        this.data = data;
    }

    public String getDataLabel() {
        return dataLabel;
    }

    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDelta() {
        return getMax() - getMin();
    }

    public Long getMin() {
        return data.get(0);
    }

    public Long getMax() {
        return data.get(data.size() - 1);
    }
}
