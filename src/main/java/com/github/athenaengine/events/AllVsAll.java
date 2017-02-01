package com.github.athenaengine.events;

/*
 * Copyright (C) 2015-2016 L2J EventEngine
 *
 * This file is part of L2J EventEngine.
 *
 * L2J EventEngine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * L2J EventEngine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import com.github.athenaengine.core.builders.TeamsBuilder;
import com.github.athenaengine.core.config.BaseConfigLoader;
import com.github.athenaengine.core.datatables.MessageData;
import com.github.athenaengine.core.dispatcher.events.OnDeathEvent;
import com.github.athenaengine.core.dispatcher.events.OnKillEvent;
import com.github.athenaengine.core.enums.AnnounceType;
import com.github.athenaengine.core.enums.ListenerType;
import com.github.athenaengine.core.enums.ScoreType;
import com.github.athenaengine.core.helper.RewardHelper;
import com.github.athenaengine.core.interfaces.IParticipant;
import com.github.athenaengine.core.model.base.BaseEvent;
import com.github.athenaengine.core.model.entity.Player;
import com.github.athenaengine.core.model.entity.Summon;
import com.github.athenaengine.core.util.EventUtil;
import com.github.athenaengine.core.util.SortUtils;
import com.github.athenaengine.events.config.AllVsAllEventConfig;

import java.util.List;

public class AllVsAll extends BaseEvent<AllVsAllEventConfig>
{
    // Time for resurrection
    private static final int TIME_RES_PLAYER = 10;

    @Override
    protected String getInstanceFile() {
        return getConfig().getInstanceFile();
    }

    @Override
    protected TeamsBuilder onCreateTeams() {
        return new TeamsBuilder()
                .addTeam(getConfig().getCoordinates())
                .setPlayers(getPlayerEventManager().getAllEventPlayers());
    }

    @Override
    protected void onEventStart() {
        addSuscription(ListenerType.ON_KILL);
        addSuscription(ListenerType.ON_DEATH);
    }

    @Override
    protected void onEventFight() {
        // Nothing
    }

    @Override
    protected void onEventEnd() {
        giveRewardsTeams();
    }

    // LISTENERS -----------------------------------------------------------------------
    @Override
    public void onKill(OnKillEvent event) {
        if (event.getTarget() instanceof Summon) return;

        Player player = getPlayerEventManager().getEventPlayer(event.getAttacker());

        // Increase the amount of one character kills
        player.increasePoints(ScoreType.KILL, 1);
        updateTitle(player);

        // Reward for kills
        if (getConfig().isRewardKillEnabled()) {
            player.giveItems(getConfig().getRewardKill());
        }
        // Reward PvP for kills
        if (getConfig().isRewardPvPKillEnabled()) {
            player.setPvpKills(player.getPvpKills() + getConfig().getRewardPvPKill());
            EventUtil.sendEventMessage(player, MessageData.getInstance().getMsgByLang(player, "reward_text_pvp", true).replace("%count%", getConfig().getRewardPvPKill() + ""));
        }
        // Reward fame for kills
        if (getConfig().isRewardFameKillEnabled()) {
            player.setFame(player.getFame() + getConfig().getRewardFameKill());
            EventUtil.sendEventMessage(player, MessageData.getInstance().getMsgByLang(player, "reward_text_fame", true).replace("%count%", getConfig().getRewardFameKill() + ""));
        }
        // Message Kill
        if (BaseConfigLoader.getInstance().getMainConfig().isKillerMessageEnabled()) {
            EventUtil.messageKill(player, event.getTarget());
        }
    }

    @Override
    public void onDeath(OnDeathEvent event) {
        scheduleRevivePlayer(event.getTarget(), TIME_RES_PLAYER, _radius);
        event.getTarget().increasePoints(ScoreType.DEATH, 1);
        updateTitle(event.getTarget());
    }

    // VARIOUS METHODS ------------------------------------------------------------------
    private void updateTitle(Player player) {
        // Adjust the title character
        player.setTitle("Kills " + player.getPoints(ScoreType.KILL) + " | " + player.getPoints(ScoreType.DEATH) + " Death");
    }

    private void giveRewardsTeams() {
        if (getPlayerEventManager().getAllEventPlayers().isEmpty()) return;

        List<IParticipant> listOrdered = SortUtils.getOrdered(getPlayerEventManager().getAllEventParticipants(), ScoreType.KILL).get(0);

        RewardHelper.newInstance()
                .addReward(1, getConfig().getReward())
                .setScoreType(ScoreType.DEATH)
                .setParticipants(listOrdered)
                .distribute(AnnounceType.WINNER);
    }
}