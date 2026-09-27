package com.japs.frontend.bodyfitgym.models;

import java.time.LocalDateTime;

public class Attendance {
    private Long id;
    private Long affiliateId;
    private Long affiliateMembershipId;
    private LocalDateTime attendanceDate;

    public Attendance() {
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

    public Long getAffiliateMembershipId() {
        return affiliateMembershipId;
    }

    public void setAffiliateMembershipId(Long affiliateMembershipId) {
        this.affiliateMembershipId = affiliateMembershipId;
    }

    public LocalDateTime getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDateTime attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", affiliateId=" + affiliateId +
                ", affiliateMembershipId=" + affiliateMembershipId +
                ", attendanceDate=" + attendanceDate +
                '}';
    }
}
