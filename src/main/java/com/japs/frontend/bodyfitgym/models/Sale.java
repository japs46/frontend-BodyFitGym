package com.japs.frontend.bodyfitgym.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sale {
    private Long id;
    private Long affiliateId;
    private SaleType type;
    private Long productId;
    private Long affiliateMembershipId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private String observation;
    private SaleStatus status;
    private LocalDateTime saleDate;
    private LocalDateTime createdAt;

    public Sale() {
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

    public SaleType getType() {
        return type;
    }

    public void setType(SaleType type) {
        this.type = type;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getAffiliateMembershipId() {
        return affiliateMembershipId;
    }

    public void setAffiliateMembershipId(Long affiliateMembershipId) {
        this.affiliateMembershipId = affiliateMembershipId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", affiliateId=" + affiliateId +
                ", type=" + type +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}
