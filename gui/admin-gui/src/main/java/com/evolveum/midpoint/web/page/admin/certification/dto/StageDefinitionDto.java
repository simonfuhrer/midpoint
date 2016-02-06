package com.evolveum.midpoint.web.page.admin.certification.dto;

import com.evolveum.midpoint.xml.ns._public.common.common_3.AccessCertificationCaseOutcomeStrategyType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.AccessCertificationResponseType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kate on 15.12.2015.
 */
public class StageDefinitionDto implements Serializable {
    public final static String F_NUMBER = "number";
    public final static String F_NAME = "name";
    public final static String F_DESCRIPTION = "description";
    public final static String F_DAYS = "days";
    public final static String F_NOTIFY_BEFORE_DEADLINE = "notifyBeforeDeadline";
    public final static String F_NOTIFY_ONLY_WHEN_NO_DECISION = "notifyOnlyWhenNoDecision";
    public final static String F_REVIEWER_SPECIFICATION = "reviewerSpecification";
    public final static String F_REVIEWER_DTO = "reviewerDto";
    public final static String F_OUTCOME_STRATEGY = "outcomeStrategy";
    public final static String F_OUTCOME_IF_NO_REVIEWERS = "outcomeIfNoReviewers";

    private int number;
    private String name;
    private String description;
    private Integer days;
    private String notifyBeforeDeadline;
    private boolean notifyOnlyWhenNoDecision;
    private AccessCertificationReviewerDto reviewerDto;
    private AccessCertificationCaseOutcomeStrategyType outcomeStrategy;
    private AccessCertificationResponseType outcomeIfNoReviewers;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getNotifyBeforeDeadline() {
        return notifyBeforeDeadline;
    }

    public void setNotifyBeforeDeadline(String notifyBeforeDeadline) {
        this.notifyBeforeDeadline = notifyBeforeDeadline;
    }

    public boolean isNotifyOnlyWhenNoDecision() {
        return notifyOnlyWhenNoDecision;
    }

    public void setNotifyOnlyWhenNoDecision(boolean notifyOnlyWhenNoDecision) {
        this.notifyOnlyWhenNoDecision = notifyOnlyWhenNoDecision;
    }

    public AccessCertificationReviewerDto getReviewerDto() {
        return reviewerDto;
    }

    public void setReviewerDto(AccessCertificationReviewerDto reviewerDto) {
        this.reviewerDto = reviewerDto;
    }

    public AccessCertificationCaseOutcomeStrategyType getOutcomeStrategy() {
        return outcomeStrategy;
    }

    public void setOutcomeStrategy(AccessCertificationCaseOutcomeStrategyType outcomeStrategy) {
        this.outcomeStrategy = outcomeStrategy;
    }

    public AccessCertificationResponseType getOutcomeIfNoReviewers() {
        return outcomeIfNoReviewers;
    }

    public void setOutcomeIfNoReviewers(AccessCertificationResponseType outcomeIfNoReviewers) {
        this.outcomeIfNoReviewers = outcomeIfNoReviewers;
    }
}