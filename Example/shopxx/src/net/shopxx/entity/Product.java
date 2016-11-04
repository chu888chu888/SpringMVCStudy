/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import net.shopxx.BaseAttributeConverter;
import net.shopxx.Setting;
import net.shopxx.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

/**
 * Entity - 商品
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Entity
@Table(name = "xx_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_product")
public class Product extends BaseEntity<Long> {

	private static final long serialVersionUID = 5410116769251786656L;

	/**
	 * 普通商品验证组
	 */
	public interface General extends Default {

	}

	/**
	 * 兑换商品验证组
	 */
	public interface Exchange extends Default {

	}

	/**
	 * 赠品验证组
	 */
	public interface Gift extends Default {

	}

	/** 编号 */
	private String sn;

	/** 销售价 */
	private BigDecimal price;

	/** 成本价 */
	private BigDecimal cost;

	/** 市场价 */
	private BigDecimal marketPrice;

	/** 赠送积分 */
	private Long rewardPoint;

	/** 兑换积分 */
	private Long exchangePoint;

	/** 库存 */
	private Integer stock;

	/** 已分配库存 */
	private Integer allocatedStock;

	/** 是否默认 */
	private Boolean isDefault;

	/** 货品 */
	private Goods goods;

	/** 规格值 */
	private List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>();

	/** 购物车项 */
	private Set<CartItem> cartItems = new HashSet<CartItem>();

	/** 订单项 */
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();

	/** 发货项 */
	private Set<ShippingItem> shippingItems = new HashSet<ShippingItem>();

	/** 到货通知 */
	private Set<ProductNotify> productNotifies = new HashSet<ProductNotify>();

	/** 库存记录 */
	private Set<StockLog> stockLogs = new HashSet<StockLog>();

	/** 赠品促销 */
	private Set<Promotion> giftPromotions = new HashSet<Promotion>();

	/**
	 * 获取编号
	 * 
	 * @return 编号
	 */
	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return sn;
	}

	/**
	 * 设置编号
	 * 
	 * @param sn
	 *            编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取销售价
	 * 
	 * @return 销售价
	 */
	@NotNull(groups = General.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置销售价
	 * 
	 * @param price
	 *            销售价
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取成本价
	 * 
	 * @return 成本价
	 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	public BigDecimal getCost() {
		return cost;
	}

	/**
	 * 设置成本价
	 * 
	 * @param cost
	 *            成本价
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	/**
	 * 获取市场价
	 * 
	 * @return 市场价
	 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	/**
	 * 设置市场价
	 * 
	 * @param marketPrice
	 *            市场价
	 */
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	/**
	 * 获取赠送积分
	 * 
	 * @return 赠送积分
	 */
	@Min(0)
	@Column(nullable = false)
	public Long getRewardPoint() {
		return rewardPoint;
	}

	/**
	 * 设置赠送积分
	 * 
	 * @param rewardPoint
	 *            赠送积分
	 */
	public void setRewardPoint(Long rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	/**
	 * 获取兑换积分
	 * 
	 * @return 兑换积分
	 */
	@NotNull(groups = Exchange.class)
	@Min(0)
	@Column(nullable = false)
	public Long getExchangePoint() {
		return exchangePoint;
	}

	/**
	 * 设置兑换积分
	 * 
	 * @param exchangePoint
	 *            兑换积分
	 */
	public void setExchangePoint(Long exchangePoint) {
		this.exchangePoint = exchangePoint;
	}

	/**
	 * 获取库存
	 * 
	 * @return 库存
	 */
	@NotNull(groups = Save.class)
	@Min(0)
	@Column(nullable = false)
	public Integer getStock() {
		return stock;
	}

	/**
	 * 设置库存
	 * 
	 * @param stock
	 *            库存
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/**
	 * 获取已分配库存
	 * 
	 * @return 已分配库存
	 */
	@Column(nullable = false)
	public Integer getAllocatedStock() {
		return allocatedStock;
	}

	/**
	 * 设置已分配库存
	 * 
	 * @param allocatedStock
	 *            已分配库存
	 */
	public void setAllocatedStock(Integer allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

	/**
	 * 获取是否默认
	 * 
	 * @return 是否默认
	 */
	@NotNull
	@Column(nullable = false)
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * 设置是否默认
	 * 
	 * @param isDefault
	 *            是否默认
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 获取货品
	 * 
	 * @return 货品
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, updatable = false)
	public Goods getGoods() {
		return goods;
	}

	/**
	 * 设置货品
	 * 
	 * @param goods
	 *            货品
	 */
	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	/**
	 * 获取规格值
	 * 
	 * @return 规格值
	 */
	@Valid
	@Column(length = 4000)
	@Convert(converter = SpecificationValueConverter.class)
	public List<SpecificationValue> getSpecificationValues() {
		return specificationValues;
	}

	/**
	 * 设置规格值
	 * 
	 * @param specificationValues
	 *            规格值
	 */
	public void setSpecificationValues(List<SpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}

	/**
	 * 获取购物车项
	 * 
	 * @return 购物车项
	 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	/**
	 * 设置购物车项
	 * 
	 * @param cartItems
	 *            购物车项
	 */
	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	/**
	 * 获取订单项
	 * 
	 * @return 订单项
	 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	/**
	 * 设置订单项
	 * 
	 * @param orderItems
	 *            订单项
	 */
	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	/**
	 * 获取发货项
	 * 
	 * @return 发货项
	 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	public Set<ShippingItem> getShippingItems() {
		return shippingItems;
	}

	/**
	 * 设置发货项
	 * 
	 * @param shippingItems
	 *            发货项
	 */
	public void setShippingItems(Set<ShippingItem> shippingItems) {
		this.shippingItems = shippingItems;
	}

	/**
	 * 获取到货通知
	 * 
	 * @return 到货通知
	 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<ProductNotify> getProductNotifies() {
		return productNotifies;
	}

	/**
	 * 设置到货通知
	 * 
	 * @param productNotifies
	 *            到货通知
	 */
	public void setProductNotifies(Set<ProductNotify> productNotifies) {
		this.productNotifies = productNotifies;
	}

	/**
	 * 获取库存记录
	 * 
	 * @return 库存记录
	 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	public Set<StockLog> getStockLogs() {
		return stockLogs;
	}

	/**
	 * 设置库存记录
	 * 
	 * @param stockLogs
	 *            库存记录
	 */
	public void setStockLogs(Set<StockLog> stockLogs) {
		this.stockLogs = stockLogs;
	}

	/**
	 * 获取赠品促销
	 * 
	 * @return 赠品促销
	 */
	@ManyToMany(mappedBy = "gifts", fetch = FetchType.LAZY)
	public Set<Promotion> getGiftPromotions() {
		return giftPromotions;
	}

	/**
	 * 设置赠品促销
	 * 
	 * @param giftPromotions
	 *            赠品促销
	 */
	public void setGiftPromotions(Set<Promotion> giftPromotions) {
		this.giftPromotions = giftPromotions;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	@Transient
	public String getName() {
		return getGoods() != null ? getGoods().getName() : null;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@Transient
	public Goods.Type getType() {
		return getGoods() != null ? getGoods().getType() : null;
	}

	/**
	 * 获取展示图片
	 * 
	 * @return 展示图片
	 */
	@Transient
	public String getImage() {
		return getGoods() != null ? getGoods().getImage() : null;
	}

	/**
	 * 获取单位
	 * 
	 * @return 单位
	 */
	@Transient
	public String getUnit() {
		return getGoods() != null ? getGoods().getUnit() : null;
	}

	/**
	 * 获取重量
	 * 
	 * @return 重量
	 */
	@Transient
	public Integer getWeight() {
		return getGoods() != null ? getGoods().getWeight() : null;
	}

	/**
	 * 获取是否上架
	 * 
	 * @return 是否上架
	 */
	@Transient
	public boolean getIsMarketable() {
		return getGoods() != null && BooleanUtils.isTrue(getGoods().getIsMarketable());
	}

	/**
	 * 获取是否列出
	 * 
	 * @return 是否列出
	 */
	@Transient
	public boolean getIsList() {
		return getGoods() != null && BooleanUtils.isTrue(getGoods().getIsList());
	}

	/**
	 * 获取是否置顶
	 * 
	 * @return 是否置顶
	 */
	@Transient
	public boolean getIsTop() {
		return getGoods() != null && BooleanUtils.isTrue(getGoods().getIsTop());
	}

	/**
	 * 获取是否需要物流
	 * 
	 * @return 是否需要物流
	 */
	@Transient
	public boolean getIsDelivery() {
		return getGoods() != null && BooleanUtils.isTrue(getGoods().getIsDelivery());
	}

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */
	@Transient
	public String getPath() {
		return getGoods() != null ? getGoods().getPath() : null;
	}

	/**
	 * 获取URL
	 * 
	 * @return URL
	 */
	@Transient
	public String getUrl() {
		return getGoods() != null ? getGoods().getUrl() : null;
	}

	/**
	 * 获取缩略图
	 * 
	 * @return 缩略图
	 */
	@Transient
	public String getThumbnail() {
		return getGoods() != null ? getGoods().getThumbnail() : null;
	}

	/**
	 * 获取可用库存
	 * 
	 * @return 可用库存
	 */
	@Transient
	public int getAvailableStock() {
		int availableStock = getStock() - getAllocatedStock();
		return availableStock >= 0 ? availableStock : 0;
	}

	/**
	 * 获取是否库存警告
	 * 
	 * @return 是否库存警告
	 */
	@Transient
	public boolean getIsStockAlert() {
		Setting setting = SystemUtils.getSetting();
		return setting.getStockAlertCount() != null && getAvailableStock() <= setting.getStockAlertCount();
	}

	/**
	 * 获取是否缺货
	 * 
	 * @return 是否缺货
	 */
	@Transient
	public boolean getIsOutOfStock() {
		return getAvailableStock() <= 0;
	}

	/**
	 * 获取规格值ID
	 * 
	 * @return 规格值ID
	 */
	@Transient
	public List<Integer> getSpecificationValueIds() {
		List<Integer> specificationValueIds = new ArrayList<Integer>();
		if (CollectionUtils.isNotEmpty(getSpecificationValues())) {
			for (SpecificationValue specificationValue : getSpecificationValues()) {
				specificationValueIds.add(specificationValue.getId());
			}
		}
		return specificationValueIds;
	}

	/**
	 * 获取规格
	 * 
	 * @return 规格
	 */
	@Transient
	public List<String> getSpecifications() {
		List<String> specifications = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(getSpecificationValues())) {
			for (SpecificationValue specificationValue : getSpecificationValues()) {
				specifications.add(specificationValue.getValue());
			}
		}
		return specifications;
	}

	/**
	 * 获取有效促销
	 * 
	 * @return 有效促销
	 */
	@Transient
	public Set<Promotion> getValidPromotions() {
		return getGoods() != null ? getGoods().getValidPromotions() : Collections.<Promotion> emptySet();
	}

	/**
	 * 是否存在规格
	 * 
	 * @return 是否存在规格
	 */
	@Transient
	public boolean hasSpecification() {
		return CollectionUtils.isNotEmpty(getSpecificationValues());
	}

	/**
	 * 判断促销是否有效
	 * 
	 * @param promotion
	 *            促销
	 * @return 促销是否有效
	 */
	@Transient
	public boolean isValid(Promotion promotion) {
		return getGoods() != null ? getGoods().isValid(promotion) : false;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		Set<OrderItem> orderItems = getOrderItems();
		if (orderItems != null) {
			for (OrderItem orderItem : orderItems) {
				orderItem.setProduct(null);
			}
		}
		Set<ShippingItem> shippingItems = getShippingItems();
		if (shippingItems != null) {
			for (ShippingItem shippingItem : getShippingItems()) {
				shippingItem.setProduct(null);
			}
		}
		Set<Promotion> giftPromotions = getGiftPromotions();
		if (giftPromotions != null) {
			for (Promotion giftPromotion : giftPromotions) {
				giftPromotion.getGifts().remove(this);
			}
		}
	}

	/**
	 * 类型转换 - 规格值
	 * 
	 * @author SHOP++ Team
	 * @version 4.0
	 */
	@Converter
	public static class SpecificationValueConverter extends BaseAttributeConverter<List<SpecificationValue>> implements AttributeConverter<Object, String> {
	}

}
