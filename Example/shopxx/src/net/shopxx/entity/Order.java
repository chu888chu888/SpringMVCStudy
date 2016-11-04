/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

import net.shopxx.BaseAttributeConverter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 订单
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Entity
@Table(name = "xx_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_order")
public class Order extends BaseEntity<Long> {

	private static final long serialVersionUID = 4099014220188098119L;

	/** 锁定过期时间 */
	public static final int LOCK_EXPIRE = 60;

	/**
	 * 配送验证组
	 */
	public interface Delivery extends Default {

	}

	/**
	 * 类型
	 */
	public enum Type {

		/** 普通订单 */
		general,

		/** 兑换订单 */
		exchange
	}

	/**
	 * 状态
	 */
	public enum Status {

		/** 等待付款 */
		pendingPayment,

		/** 等待审核 */
		pendingReview,

		/** 等待发货 */
		pendingShipment,

		/** 已发货 */
		shipped,

		/** 已收货 */
		received,

		/** 已完成 */
		completed,

		/** 已失败 */
		failed,

		/** 已取消 */
		canceled,

		/** 已拒绝 */
		denied
	}

	/** 编号 */
	private String sn;

	/** 类型 */
	private Order.Type type;

	/** 状态 */
	private Order.Status status;

	/** 商品价格 */
	private BigDecimal price;

	/** 支付手续费 */
	private BigDecimal fee;

	/** 运费 */
	private BigDecimal freight;

	/** 税金 */
	private BigDecimal tax;

	/** 促销折扣 */
	private BigDecimal promotionDiscount;

	/** 优惠券折扣 */
	private BigDecimal couponDiscount;

	/** 调整金额 */
	private BigDecimal offsetAmount;

	/** 订单金额 */
	private BigDecimal amount;

	/** 已付金额 */
	private BigDecimal amountPaid;

	/** 退款金额 */
	private BigDecimal refundAmount;

	/** 赠送积分 */
	private Long rewardPoint;

	/** 兑换积分 */
	private Long exchangePoint;

	/** 商品重量 */
	private Integer weight;

	/** 商品数量 */
	private Integer quantity;

	/** 已发货数量 */
	private Integer shippedQuantity;

	/** 已退货数量 */
	private Integer returnedQuantity;

	/** 收货人 */
	private String consignee;

	/** 地区名称 */
	private String areaName;

	/** 地址 */
	private String address;

	/** 邮编 */
	private String zipCode;

	/** 电话 */
	private String phone;

	/** 附言 */
	private String memo;

	/** 过期时间 */
	private Date expire;

	/** 是否已使用优惠码 */
	private Boolean isUseCouponCode;

	/** 是否已兑换积分 */
	private Boolean isExchangePoint;

	/** 是否已分配库存 */
	private Boolean isAllocatedStock;

	/** 支付方式名称 */
	private String paymentMethodName;

	/** 支付方式类型 */
	private PaymentMethod.Type paymentMethodType;

	/** 配送方式名称 */
	private String shippingMethodName;

	/** 锁定KEY */
	private String lockKey;

	/** 锁定过期时间 */
	private Date lockExpire;

	/** 完成日期 */
	private Date completeDate;

	/** 发票 */
	private Invoice invoice;

	/** 地区 */
	private Area area;

	/** 支付方式 */
	private PaymentMethod paymentMethod;

	/** 配送方式 */
	private ShippingMethod shippingMethod;

	/** 会员 */
	private Member member;

	/** 优惠码 */
	private CouponCode couponCode;

	/** 促销名称 */
	private List<String> promotionNames = new ArrayList<String>();

	/** 赠送优惠券 */
	private List<Coupon> coupons = new ArrayList<Coupon>();

	/** 订单项 */
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	/** 支付记录 */
	private Set<PaymentLog> paymentLogs = new HashSet<PaymentLog>();

	/** 收款单 */
	private Set<Payment> payments = new HashSet<Payment>();

	/** 退款单 */
	private Set<Refunds> refunds = new HashSet<Refunds>();

	/** 发货单 */
	private Set<Shipping> shippings = new HashSet<Shipping>();

	/** 退货单 */
	private Set<Returns> returns = new HashSet<Returns>();

	/** 订单记录 */
	private Set<OrderLog> orderLogs = new HashSet<OrderLog>();

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
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@Column(nullable = false, updatable = false)
	public Order.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Order.Type type) {
		this.type = type;
	}

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	@Column(nullable = false)
	public Order.Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setStatus(Order.Status status) {
		this.status = status;
	}

	/**
	 * 获取商品价格
	 * 
	 * @return 商品价格
	 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置商品价格
	 * 
	 * @param price
	 *            商品价格
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取支付手续费
	 * 
	 * @return 支付手续费
	 */
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFee() {
		return fee;
	}

	/**
	 * 设置支付手续费
	 * 
	 * @param fee
	 *            支付手续费
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	/**
	 * 获取运费
	 * 
	 * @return 运费
	 */
	@NotNull(groups = Delivery.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * 设置运费
	 * 
	 * @param freight
	 *            运费
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	/**
	 * 获取税金
	 * 
	 * @return 税金
	 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getTax() {
		return tax;
	}

	/**
	 * 设置税金
	 * 
	 * @param tax
	 *            税金
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * 获取促销折扣
	 * 
	 * @return 促销折扣
	 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getPromotionDiscount() {
		return promotionDiscount;
	}

	/**
	 * 设置促销折扣
	 * 
	 * @param promotionDiscount
	 *            促销折扣
	 */
	public void setPromotionDiscount(BigDecimal promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}

	/**
	 * 获取优惠券折扣
	 * 
	 * @return 优惠券折扣
	 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	/**
	 * 设置优惠券折扣
	 * 
	 * @param couponDiscount
	 *            优惠券折扣
	 */
	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	/**
	 * 获取调整金额
	 * 
	 * @return 调整金额
	 */
	@NotNull
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getOffsetAmount() {
		return offsetAmount;
	}

	/**
	 * 设置调整金额
	 * 
	 * @param offsetAmount
	 *            调整金额
	 */
	public void setOffsetAmount(BigDecimal offsetAmount) {
		this.offsetAmount = offsetAmount;
	}

	/**
	 * 获取订单金额
	 * 
	 * @return 订单金额
	 */
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置订单金额
	 * 
	 * @param amount
	 *            订单金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取已付金额
	 * 
	 * @return 已付金额
	 */
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	/**
	 * 设置已付金额
	 * 
	 * @param amountPaid
	 *            已付金额
	 */
	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	/**
	 * 获取退款金额
	 * 
	 * @return 退款金额
	 */
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	/**
	 * 设置退款金额
	 * 
	 * @param refundAmount
	 *            退款金额
	 */
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
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
	@Column(nullable = false, updatable = false)
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
	 * 获取商品重量
	 * 
	 * @return 商品重量
	 */
	@Column(nullable = false, updatable = false)
	public Integer getWeight() {
		return weight;
	}

	/**
	 * 设置商品重量
	 * 
	 * @param weight
	 *            商品重量
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * 获取商品数量
	 * 
	 * @return 商品数量
	 */
	@Column(nullable = false, updatable = false)
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 设置商品数量
	 * 
	 * @param quantity
	 *            商品数量
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 获取已发货数量
	 * 
	 * @return 已发货数量
	 */
	@Column(nullable = false)
	public Integer getShippedQuantity() {
		return shippedQuantity;
	}

	/**
	 * 设置已发货数量
	 * 
	 * @param shippedQuantity
	 *            已发货数量
	 */
	public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	/**
	 * 获取已退货数量
	 * 
	 * @return 已退货数量
	 */
	@Column(nullable = false)
	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}

	/**
	 * 设置已退货数量
	 * 
	 * @param returnedQuantity
	 *            已退货数量
	 */
	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	/**
	 * 获取收货人
	 * 
	 * @return 收货人
	 */
	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	public String getConsignee() {
		return consignee;
	}

	/**
	 * 设置收货人
	 * 
	 * @param consignee
	 *            收货人
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * 获取地区名称
	 * 
	 * @return 地区名称
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置地区名称
	 * 
	 * @param areaName
	 *            地区名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 获取地址
	 * 
	 * @return 地址
	 */
	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * 
	 * @param address
	 *            地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取邮编
	 * 
	 * @return 邮编
	 */
	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	@Pattern(regexp = "^\\d{6}$")
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置邮编
	 * 
	 * @param zipCode
	 *            邮编
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取电话
	 * 
	 * @return 电话
	 */
	@NotEmpty(groups = Delivery.class)
	@Length(max = 200)
	@Pattern(regexp = "^\\d{3,4}-?\\d{7,9}$")
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 * 
	 * @param phone
	 *            电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取附言
	 * 
	 * @return 附言
	 */
	@Length(max = 200)
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置附言
	 * 
	 * @param memo
	 *            附言
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取过期时间
	 * 
	 * @return 过期时间
	 */
	public Date getExpire() {
		return expire;
	}

	/**
	 * 设置过期时间
	 * 
	 * @param expire
	 *            过期时间
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}

	/**
	 * 获取是否已使用优惠码
	 * 
	 * @return 是否已使用优惠码
	 */
	@Column(nullable = false)
	public Boolean getIsUseCouponCode() {
		return isUseCouponCode;
	}

	/**
	 * 设置是否已使用优惠码
	 * 
	 * @param isUseCouponCode
	 *            是否已使用优惠码
	 */
	public void setIsUseCouponCode(Boolean isUseCouponCode) {
		this.isUseCouponCode = isUseCouponCode;
	}

	/**
	 * 获取是否已兑换积分
	 * 
	 * @return 是否已兑换积分
	 */
	@Column(nullable = false)
	public Boolean getIsExchangePoint() {
		return isExchangePoint;
	}

	/**
	 * 设置是否已兑换积分
	 * 
	 * @param isExchangePoint
	 *            是否已兑换积分
	 */
	public void setIsExchangePoint(Boolean isExchangePoint) {
		this.isExchangePoint = isExchangePoint;
	}

	/**
	 * 获取是否已分配库存
	 * 
	 * @return 是否已分配库存
	 */
	@Column(nullable = false)
	public Boolean getIsAllocatedStock() {
		return isAllocatedStock;
	}

	/**
	 * 设置是否已分配库存
	 * 
	 * @param isAllocatedStock
	 *            是否已分配库存
	 */
	public void setIsAllocatedStock(Boolean isAllocatedStock) {
		this.isAllocatedStock = isAllocatedStock;
	}

	/**
	 * 获取支付方式名称
	 * 
	 * @return 支付方式名称
	 */
	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	/**
	 * 设置支付方式名称
	 * 
	 * @param paymentMethodName
	 *            支付方式名称
	 */
	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	/**
	 * 获取支付方式类型
	 * 
	 * @return 支付方式类型
	 */
	public PaymentMethod.Type getPaymentMethodType() {
		return paymentMethodType;
	}

	/**
	 * 设置支付方式类型
	 * 
	 * @param paymentMethodType
	 *            支付方式类型
	 */
	public void setPaymentMethodType(PaymentMethod.Type paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}

	/**
	 * 获取配送方式名称
	 * 
	 * @return 配送方式名称
	 */
	public String getShippingMethodName() {
		return shippingMethodName;
	}

	/**
	 * 设置配送方式名称
	 * 
	 * @param shippingMethodName
	 *            配送方式名称
	 */
	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}

	/**
	 * 获取锁定KEY
	 * 
	 * @return 锁定KEY
	 */
	public String getLockKey() {
		return lockKey;
	}

	/**
	 * 设置锁定KEY
	 * 
	 * @param lockKey
	 *            锁定KEY
	 */
	public void setLockKey(String lockKey) {
		this.lockKey = lockKey;
	}

	/**
	 * 获取锁定过期时间
	 * 
	 * @return 锁定过期时间
	 */
	public Date getLockExpire() {
		return lockExpire;
	}

	/**
	 * 设置锁定过期时间
	 * 
	 * @param lockExpire
	 *            锁定过期时间
	 */
	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}

	/**
	 * 获取完成日期
	 * 
	 * @return 完成日期
	 */
	public Date getCompleteDate() {
		return completeDate;
	}

	/**
	 * 设置完成日期
	 * 
	 * @param completeDate
	 *            完成日期
	 */
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	/**
	 * 获取发票
	 * 
	 * @return 发票
	 */
	@Valid
	@Embedded
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * 设置发票
	 * 
	 * @param invoice
	 *            发票
	 */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	/**
	 * 获取地区
	 * 
	 * @return 地区
	 */
	@NotNull(groups = Delivery.class)
	@ManyToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * 
	 * @param area
	 *            地区
	 */
	public void setArea(Area area) {
		this.area = area;
	}

	/**
	 * 获取支付方式
	 * 
	 * @return 支付方式
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * 设置支付方式
	 * 
	 * @param paymentMethod
	 *            支付方式
	 */
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * 获取配送方式
	 * 
	 * @return 配送方式
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * 设置配送方式
	 * 
	 * @param shippingMethod
	 *            配送方式
	 */
	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 获取优惠码
	 * 
	 * @return 优惠码
	 */
	@OneToOne(fetch = FetchType.LAZY)
	public CouponCode getCouponCode() {
		return couponCode;
	}

	/**
	 * 设置优惠码
	 * 
	 * @param couponCode
	 *            优惠码
	 */
	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	/**
	 * 获取促销名称
	 * 
	 * @return 促销名称
	 */
	@Column(updatable = false, length = 4000)
	@Convert(converter = PromotionNameConverter.class)
	public List<String> getPromotionNames() {
		return promotionNames;
	}

	/**
	 * 设置促销名称
	 * 
	 * @param promotionNames
	 *            促销名称
	 */
	public void setPromotionNames(List<String> promotionNames) {
		this.promotionNames = promotionNames;
	}

	/**
	 * 获取赠送优惠券
	 * 
	 * @return 赠送优惠券
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_order_coupon")
	public List<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * 设置赠送优惠券
	 * 
	 * @param coupons
	 *            赠送优惠券
	 */
	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	/**
	 * 获取订单项
	 * 
	 * @return 订单项
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("type asc")
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	/**
	 * 设置订单项
	 * 
	 * @param orderItems
	 *            订单项
	 */
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	/**
	 * 获取支付记录
	 * 
	 * @return 支付记录
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<PaymentLog> getPaymentLogs() {
		return paymentLogs;
	}

	/**
	 * 设置支付记录
	 * 
	 * @param paymentLogs
	 *            支付记录
	 */
	public void setPaymentLogs(Set<PaymentLog> paymentLogs) {
		this.paymentLogs = paymentLogs;
	}

	/**
	 * 获取收款单
	 * 
	 * @return 收款单
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Payment> getPayments() {
		return payments;
	}

	/**
	 * 设置收款单
	 * 
	 * @param payments
	 *            收款单
	 */
	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	/**
	 * 获取退款单
	 * 
	 * @return 退款单
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Refunds> getRefunds() {
		return refunds;
	}

	/**
	 * 设置退款单
	 * 
	 * @param refunds
	 *            退款单
	 */
	public void setRefunds(Set<Refunds> refunds) {
		this.refunds = refunds;
	}

	/**
	 * 获取发货单
	 * 
	 * @return 发货单
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Shipping> getShippings() {
		return shippings;
	}

	/**
	 * 设置发货单
	 * 
	 * @param shippings
	 *            发货单
	 */
	public void setShippings(Set<Shipping> shippings) {
		this.shippings = shippings;
	}

	/**
	 * 获取退货单
	 * 
	 * @return 退货单
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<Returns> getReturns() {
		return returns;
	}

	/**
	 * 设置退货单
	 * 
	 * @param returns
	 *            退货单
	 */
	public void setReturns(Set<Returns> returns) {
		this.returns = returns;
	}

	/**
	 * 获取订单记录
	 * 
	 * @return 订单记录
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	public Set<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	/**
	 * 设置订单记录
	 * 
	 * @param orderLogs
	 *            订单记录
	 */
	public void setOrderLogs(Set<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	/**
	 * 获取是否需要物流
	 * 
	 * @return 是否需要物流
	 */
	@Transient
	public boolean getIsDelivery() {
		return CollectionUtils.exists(getOrderItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				OrderItem orderItem = (OrderItem) object;
				return orderItem != null && BooleanUtils.isTrue(orderItem.getIsDelivery());
			}
		});
	}

	/**
	 * 获取应付金额
	 * 
	 * @return 应付金额
	 */
	@Transient
	public BigDecimal getAmountPayable() {
		if (!hasExpired() && !Order.Status.completed.equals(getStatus()) && !Order.Status.failed.equals(getStatus()) && !Order.Status.canceled.equals(getStatus()) && !Order.Status.denied.equals(getStatus())) {
			BigDecimal amountPayable = getAmount().subtract(getAmountPaid());
			return amountPayable.compareTo(BigDecimal.ZERO) >= 0 ? amountPayable : BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 获取应收金额
	 * 
	 * @return 应收金额
	 */
	@Transient
	public BigDecimal getAmountReceivable() {
		if (!hasExpired() && PaymentMethod.Type.cashOnDelivery.equals(getPaymentMethodType()) && !Order.Status.completed.equals(getStatus()) && !Order.Status.failed.equals(getStatus()) && !Order.Status.canceled.equals(getStatus()) && !Order.Status.denied.equals(getStatus())) {
			BigDecimal amountReceivable = getAmount().subtract(getAmountPaid());
			return amountReceivable.compareTo(BigDecimal.ZERO) >= 0 ? amountReceivable : BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 获取应退金额
	 * 
	 * @return 应退金额
	 */
	@Transient
	public BigDecimal getRefundableAmount() {
		if (hasExpired() || Order.Status.failed.equals(getStatus()) || Order.Status.canceled.equals(getStatus()) || Order.Status.denied.equals(getStatus())) {
			BigDecimal refundableAmount = getAmountPaid();
			return refundableAmount.compareTo(BigDecimal.ZERO) >= 0 ? refundableAmount : BigDecimal.ZERO;
		}
		if (Order.Status.completed.equals(getStatus())) {
			BigDecimal refundableAmount = getAmountPaid().subtract(getAmount());
			return refundableAmount.compareTo(BigDecimal.ZERO) >= 0 ? refundableAmount : BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 获取可发货数
	 * 
	 * @return 可发货数
	 */
	@Transient
	public int getShippableQuantity() {
		if (!hasExpired() && Order.Status.pendingShipment.equals(getStatus())) {
			int shippableQuantity = getQuantity() - getShippedQuantity();
			return shippableQuantity >= 0 ? shippableQuantity : 0;
		}
		return 0;
	}

	/**
	 * 获取可退货数
	 * 
	 * @return 可退货数
	 */
	@Transient
	public int getReturnableQuantity() {
		if (!hasExpired() && Order.Status.failed.equals(getStatus())) {
			int returnableQuantity = getShippedQuantity() - getReturnedQuantity();
			return returnableQuantity >= 0 ? returnableQuantity : 0;
		}
		return 0;
	}

	/**
	 * 判断是否已过期
	 * 
	 * @return 是否已过期
	 */
	@Transient
	public boolean hasExpired() {
		return getExpire() != null && !getExpire().after(new Date());
	}

	/**
	 * 获取订单项
	 * 
	 * @param sn
	 *            商品编号
	 * @return 订单项
	 */
	@Transient
	public OrderItem getOrderItem(String sn) {
		if (StringUtils.isEmpty(sn) || CollectionUtils.isEmpty(getOrderItems())) {
			return null;
		}
		for (OrderItem orderItem : getOrderItems()) {
			if (orderItem != null && StringUtils.equalsIgnoreCase(orderItem.getSn(), sn)) {
				return orderItem;
			}
		}
		return null;
	}

	/**
	 * 持久化前处理
	 */
	@PrePersist
	public void prePersist() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentMethod() != null) {
			setPaymentMethodName(getPaymentMethod().getName());
			setPaymentMethodType(getPaymentMethod().getType());
		}
		if (getShippingMethod() != null) {
			setShippingMethodName(getShippingMethod().getName());
		}
	}

	/**
	 * 更新前处理
	 */
	@PreUpdate
	public void preUpdate() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentMethod() != null) {
			setPaymentMethodName(getPaymentMethod().getName());
			setPaymentMethodType(getPaymentMethod().getType());
		}
		if (getShippingMethod() != null) {
			setShippingMethodName(getShippingMethod().getName());
		}
	}

	/**
	 * 类型转换 - 促销名称
	 * 
	 * @author SHOP++ Team
	 * @version 4.0
	 */
	@Converter
	public static class PromotionNameConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}

}
