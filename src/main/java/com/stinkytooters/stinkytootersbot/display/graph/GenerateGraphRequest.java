package com.stinkytooters.stinkytootersbot.display.graph;

import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Boss;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Skill;

public class GenerateGraphRequest {

    private int daysBack;
    private int minimumXpGain;
    private String filterUser;
    private GraphType graphType;
    private boolean stOnly;
    private Skill skill;
    private Boss boss;


    public int getDaysBack() {
        return daysBack;
    }

    public void setDaysBack(int daysBack) {
        this.daysBack = daysBack;
    }

    public int getMinimumXpGain() {
        return minimumXpGain;
    }

    public void setMinimumXpGain(int minimumXpGain) {
        this.minimumXpGain = minimumXpGain;
    }

    public String getFilterUser() {
        return filterUser;
    }

    public void setFilterUser(String filterUser) {
        this.filterUser = filterUser;
    }

    public boolean isStOnly() {
        return stOnly;
    }

    public void setStOnly(boolean stOnly) {
        this.stOnly = stOnly;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public GraphType getGraphType() {
        return graphType;
    }

    public void setGraphType(GraphType graphType) {
        this.graphType = graphType;
    }

    public static final class Builder {
        private GenerateGraphRequest generateGraphRequest;

        public Builder() {
            generateGraphRequest = new GenerateGraphRequest();
        }

        public static Builder aGenerateGraphRequest() {
            return new Builder();
        }

        public Builder withDaysBack(int daysBack) {
            generateGraphRequest.setDaysBack(daysBack);
            return this;
        }

        public Builder withMinimumXpGain(int minimumXpGain) {
            generateGraphRequest.setMinimumXpGain(minimumXpGain);
            return this;
        }

        public Builder withFilterUser(String filterUser) {
            generateGraphRequest.setFilterUser(filterUser);
            return this;
        }

        public Builder withStOnly(boolean stOnly) {
            generateGraphRequest.setStOnly(stOnly);
            return this;
        }

        public Builder withSkil(Skill skil) {
            generateGraphRequest.setSkill(skil);
            return this;
        }

        public Builder withBoss(Boss boss) {
            generateGraphRequest.setBoss(boss);
            return this;
        }

        public Builder withGraphType(GraphType graphType) {
            generateGraphRequest.setGraphType(graphType);
            return this;
        }

        public GenerateGraphRequest build() {
            return generateGraphRequest;
        }
    }
}
