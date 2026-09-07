package com.japs.frontend.bodyfitgym.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Membership {
    private Long id;

    private String name;

    private String description;

    private DurationUnit durationUnit;

    private Integer durationQuantity;

    private TrackingMode trackingMode;

    private BigDecimal price;

    private MembershipStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Membership() {
    }

    public Membership(Long id, String name, String description, DurationUnit durationUnit, Integer durationQuantity,
                       TrackingMode trackingMode, BigDecimal price, MembershipStatus status,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationUnit = durationUnit;
        this.durationQuantity = durationQuantity;
        this.trackingMode = trackingMode;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DurationUnit getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(DurationUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Integer getDurationQuantity() {
        return durationQuantity;
    }

    public void setDurationQuantity(Integer durationQuantity) {
        this.durationQuantity = durationQuantity;
    }

    public TrackingMode getTrackingMode() {
        return trackingMode;
    }

    public void setTrackingMode(TrackingMode trackingMode) {
        this.trackingMode = trackingMode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", durationUnit=" + durationUnit +
                ", durationQuantity=" + durationQuantity +
                ", trackingMode=" + trackingMode +
                ", price=" + price +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
