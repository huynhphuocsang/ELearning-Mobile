package com.example.elearningptit.model;

import java.util.List;

public class TimelineDTOList {
    private List<TimelineDTO> timelineDTOS;

    public TimelineDTOList() {
    }

    public TimelineDTOList(List<TimelineDTO> timelineDTOS) {
        this.timelineDTOS = timelineDTOS;
    }

    public List<TimelineDTO> getTimelineDTOS() {
        return timelineDTOS;
    }

    public void setTimelineDTOS(List<TimelineDTO> timelineDTOS) {
        this.timelineDTOS = timelineDTOS;
    }
}
