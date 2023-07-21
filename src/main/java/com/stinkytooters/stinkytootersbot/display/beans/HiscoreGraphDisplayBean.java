package com.stinkytooters.stinkytootersbot.display.beans;

import java.util.ArrayList;
import java.util.List;

public class HiscoreGraphDisplayBean {

    private String id;
    private List<String> labels = new ArrayList<>();
    private List<Integer> data = new ArrayList<>();
    private String dataLabel = "NO LABEL";

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
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

    public int getDelta() {
        return getMax() - getMin();
    }

    public int getMin() {
        return data.get(0);
    }

    public int getMax() {
        return data.get(data.size() - 1);
    }
}
