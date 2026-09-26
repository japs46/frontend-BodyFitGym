package com.japs.frontend.bodyfitgym.models;

import java.time.LocalDate;

public class AffiliateMembership {
    private Long id;
    private Long affiliateId;
    private Long membershipId;
    private LocalDate startDate;
    private LocalDate endDate;
    private TrackingMode trackingMode;
    private Integer remainingUnits;
    private SubscriptionStatus status;
    private Boolean frozen;
    private LocalDate frozenAt;
    private LocalDate createdAt;

    public AffiliateMembership() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(Long affiliateId) {
        this.affiliateId = affiliateId;
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public TrackingMode getTrackingMode() {
        return trackingMode;
    }

    public void setTrackingMode(TrackingMode trackingMode) {
        this.trackingMode = trackingMode;
    }

    public Integer getRemainingUnits() {
        return remainingUnits;
    }

    public void setRemainingUnits(Integer remainingUnits) {
        this.remainingUnits = remainingUnits;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public LocalDate getFrozenAt() {
        return frozenAt;
    }

    public void setFrozenAt(LocalDate frozenAt) {
        this.frozenAt = frozenAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AffiliateMembership{" +
                "id=" + id +
                ", affiliateId=" + affiliateId +
                ", membershipId=" + membershipId +
                ", status=" + status +
                ", frozen=" + frozen +
                '}';
    }
}
