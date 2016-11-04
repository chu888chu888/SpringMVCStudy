/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service;

import java.math.BigDecimal;
import java.util.List;

import net.shopxx.Filter;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Cart;
import net.shopxx.entity.CouponCode;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Invoice;
import net.shopxx.entity.Member;
import net.shopxx.entity.Order;
import net.shopxx.entity.Payment;
import net.shopxx.entity.PaymentMethod;
import net.shopxx.entity.Receiver;
import net.shopxx.entity.Refunds;
import net.shopxx.entity.Returns;
import net.shopxx.entity.Shipping;
import net.shopxx.entity.ShippingMethod;

/**
 * Service - 订单
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public interface OrderService extends BaseService<Order, Long> {

	/**
	 * 根据编号查找订单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	Order findBySn(String sn);

	/**
	 * 查找订单
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isUseCouponCode
	 *            是否已使用优惠码
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 订单
	 */
	List<Order> findList(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
			List<net.shopxx.Order> orders);

	/**
	 * 查找订单分页
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isUseCouponCode
	 *            是否已使用优惠码
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 订单分页
	 */
	Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable);

	/**
	 * 查询订单数量
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isUseCouponCode
	 *            是否已使用优惠码
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @return 订单数量
	 */
	Long count(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired);

	/**
	 * 计算税金
	 * 
	 * @param price
	 *            商品价格
	 * @param promotionDiscount
	 *            促销折扣
	 * @param couponDiscount
	 *            优惠券折扣
	 * @param offsetAmount
	 *            调整金额
	 * @return 税金
	 */
	BigDecimal calculateTax(BigDecimal price, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount);

	/**
	 * 计算税金
	 * 
	 * @param order
	 *            订单
	 * @return 税金
	 */
	BigDecimal calculateTax(Order order);

	/**
	 * 计算订单金额
	 * 
	 * @param price
	 *            商品价格
	 * @param fee
	 *            支付手续费
	 * @param freight
	 *            运费
	 * @param tax
	 *            税金
	 * @param promotionDiscount
	 *            促销折扣
	 * @param couponDiscount
	 *            优惠券折扣
	 * @param offsetAmount
	 *            调整金额
	 * @return 订单金额
	 */
	BigDecimal calculateAmount(BigDecimal price, BigDecimal fee, BigDecimal freight, BigDecimal tax, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount);

	/**
	 * 计算订单金额
	 * 
	 * @param order
	 *            订单
	 * @return 订单金额
	 */
	BigDecimal calculateAmount(Order order);

	/**
	 * 判断订单是否锁定
	 * 
	 * @param order
	 *            订单
	 * @param admin
	 *            管理员
	 * @param autoLock
	 *            是否自动加锁
	 * @return 订单是否锁定
	 */
	boolean isLocked(Order order, Admin admin, boolean autoLock);

	/**
	 * 判断订单是否锁定
	 * 
	 * @param order
	 *            订单
	 * @param member
	 *            会员
	 * @param autoLock
	 *            是否自动加锁
	 * @return 订单是否锁定
	 */
	boolean isLocked(Order order, Member member, boolean autoLock);

	/**
	 * 订单锁定
	 * 
	 * @param order
	 *            订单
	 * @param admin
	 *            管理员
	 */
	void lock(Order order, Admin admin);

	/**
	 * 订单锁定
	 * 
	 * @param order
	 *            订单
	 * @param member
	 *            会员
	 */
	void lock(Order order, Member member);

	/**
	 * 过期订单优惠码使用撤销
	 */
	void undoExpiredUseCouponCode();

	/**
	 * 过期订单积分兑换撤销
	 */
	void undoExpiredExchangePoint();

	/**
	 * 释放过期订单已分配库存
	 */
	void releaseExpiredAllocatedStock();

	/**
	 * 订单生成
	 * 
	 * @param type
	 *            类型
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param paymentMethod
	 *            支付方式
	 * @param shippingMethod
	 *            配送方式
	 * @param couponCode
	 *            优惠码
	 * @param invoice
	 *            发票
	 * @param balance
	 *            使用余额
	 * @param memo
	 *            附言
	 * @return 订单
	 */
	Order generate(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo);

	/**
	 * 订单创建
	 * 
	 * @param type
	 *            类型
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param paymentMethod
	 *            支付方式
	 * @param shippingMethod
	 *            配送方式
	 * @param couponCode
	 *            优惠码
	 * @param invoice
	 *            发票
	 * @param balance
	 *            使用余额
	 * @param memo
	 *            附言
	 * @return 订单
	 */
	Order create(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo);

	/**
	 * 订单更新
	 * 
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	void update(Order order, Admin operator);

	/**
	 * 订单取消
	 * 
	 * @param order
	 *            订单
	 */
	void cancel(Order order);

	/**
	 * 订单审核
	 * 
	 * @param order
	 *            订单
	 * @param passed
	 *            是否审核通过
	 * @param operator
	 *            操作员
	 */
	void review(Order order, boolean passed, Admin operator);

	/**
	 * 订单收款
	 * 
	 * @param order
	 *            订单
	 * @param payment
	 *            收款单
	 * @param operator
	 *            操作员
	 */
	void payment(Order order, Payment payment, Admin operator);

	/**
	 * 订单退款
	 * 
	 * @param order
	 *            订单
	 * @param refunds
	 *            退款单
	 * @param operator
	 *            操作员
	 */
	void refunds(Order order, Refunds refunds, Admin operator);

	/**
	 * 订单发货
	 * 
	 * @param order
	 *            订单
	 * @param shipping
	 *            发货单
	 * @param operator
	 *            操作员
	 */
	void shipping(Order order, Shipping shipping, Admin operator);

	/**
	 * 订单退货
	 * 
	 * @param order
	 *            订单
	 * @param returns
	 *            退货单
	 * @param operator
	 *            操作员
	 */
	void returns(Order order, Returns returns, Admin operator);

	/**
	 * 订单收货
	 * 
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	void receive(Order order, Admin operator);

	/**
	 * 订单完成
	 * 
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	void complete(Order order, Admin operator);

	/**
	 * 订单失败
	 * 
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	void fail(Order order, Admin operator);

}