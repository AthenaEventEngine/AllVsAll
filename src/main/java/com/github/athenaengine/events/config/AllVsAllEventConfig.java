package com.github.athenaengine.events.config;

import com.github.athenaengine.core.interfaces.IEventConfig;
import com.github.athenaengine.core.model.holder.EItemHolder;
import com.github.athenaengine.core.model.holder.LocationHolder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllVsAllEventConfig implements IEventConfig {

    @SerializedName("enabled") private boolean mEnabled;
    @SerializedName("instanceFile") private String mInstanceFile;
    @SerializedName("reward") private List<EItemHolder> mReward;
    @SerializedName("rewardKillEnabled") private boolean mRewardKillEnabled;
    @SerializedName("rewardKill") private List<EItemHolder> mRewardKill;
    @SerializedName("rewardPvPKillEnabled") private boolean mRewardPvPKillEnabled;
    @SerializedName("rewardPvPKill") private int mRewardPvPKill;
    @SerializedName("rewardFameKillEnabled") private boolean mRewardFameKillEnabled;
    @SerializedName("rewardFameKill") private int mRewardFameKill;
    @SerializedName("coordinates") private List<LocationHolder> mCoordinates;

    public boolean isEnabled() {
        return mEnabled;
    }

    public String getInstanceFile() {
        return mInstanceFile;
    }

    public List<EItemHolder> getReward() {
        return mReward;
    }

    public boolean isRewardKillEnabled() {
        return mRewardKillEnabled;
    }

    public List<EItemHolder> getRewardKill() {
        return mRewardKill;
    }

    public boolean isRewardPvPKillEnabled() {
        return mRewardPvPKillEnabled;
    }

    public int getRewardPvPKill() {
        return mRewardPvPKill;
    }

    public boolean isRewardFameKillEnabled() {
        return mRewardFameKillEnabled;
    }

    public int getRewardFameKill() {
        return mRewardFameKill;
    }

    public List<LocationHolder> getCoordinates() {
        return mCoordinates;
    }
}

