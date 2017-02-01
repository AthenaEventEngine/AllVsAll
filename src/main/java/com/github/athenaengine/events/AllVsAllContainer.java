package com.github.athenaengine.events;

import com.github.athenaengine.core.interfaces.IEventConfig;
import com.github.athenaengine.core.model.base.BaseEvent;
import com.github.athenaengine.core.model.base.BaseEventContainer;
import com.github.athenaengine.events.config.AllVsAllEventConfig;

public class AllVsAllContainer extends BaseEventContainer {

    @Override
    protected Class<? extends IEventConfig> getConfigClass() {
        return AllVsAllEventConfig.class;
    }

    public Class<? extends BaseEvent> getEventClass() {
        return AllVsAll.class;
    }

    public String getEventName() {
        return "All vs All";
    }

    public String getDescription() {
        return "Kill whatever you want, who kills more, wins the event";
    }
}
