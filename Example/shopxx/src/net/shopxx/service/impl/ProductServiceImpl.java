/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.shopxx.dao.ProductDao;
import net.shopxx.dao.StockLogDao;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Product;
import net.shopxx.entity.StockLog;
import net.shopxx.service.ProductService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 商品
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("productServiceImpl")
public class ProductServiceImpl extends BaseServiceImpl<Product, Long> implements ProductService {

	@Resource(name = "productDaoImpl")
	private ProductDao productDao;
	@Resource(name = "stockLogDaoImpl")
	private StockLogDao stockLogDao;

	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return productDao.snExists(sn);
	}

	@Transactional(readOnly = true)
	public Product findBySn(String sn) {
		return productDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public List<Product> search(Goods.Type type, String keyword, Set<Product> excludes, Integer count) {
		return productDao.search(type, keyword, excludes, count);
	}

	public void addStock(Product product, int amount, StockLog.Type type, Admin operator, String memo) {
		Assert.notNull(product);
		Assert.notNull(type);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(productDao.getLockMode(product))) {
			productDao.refresh(product, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(product.getStock());
		Assert.state(product.getStock() + amount >= 0);

		boolean previousOutOfStock = product.getIsOutOfStock();

		product.setStock(product.getStock() + amount);
		productDao.flush();

		Goods goods = product.getGoods();
		if (goods != null) {
			if (product.getIsOutOfStock() != previousOutOfStock) {
				goods.setGenerateMethod(Goods.GenerateMethod.eager);
			} else {
				goods.setGenerateMethod(Goods.GenerateMethod.lazy);
			}
		}

		StockLog stockLog = new StockLog();
		stockLog.setType(type);
		stockLog.setInQuantity(amount > 0 ? amount : 0);
		stockLog.setOutQuantity(amount < 0 ? Math.abs(amount) : 0);
		stockLog.setStock(product.getStock());
		stockLog.setOperator(operator);
		stockLog.setMemo(memo);
		stockLog.setProduct(product);
		stockLogDao.persist(stockLog);
	}

	public void addAllocatedStock(Product product, int amount) {
		Assert.notNull(product);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(productDao.getLockMode(product))) {
			productDao.refresh(product, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(product.getAllocatedStock());
		Assert.state(product.getAllocatedStock() + amount >= 0);

		boolean previousOutOfStock = product.getIsOutOfStock();

		product.setAllocatedStock(product.getAllocatedStock() + amount);
		productDao.flush();

		Goods goods = product.getGoods();
		if (goods != null) {
			if (product.getIsOutOfStock() != previousOutOfStock) {
				goods.setGenerateMethod(Goods.GenerateMethod.eager);
			} else {
				goods.setGenerateMethod(Goods.GenerateMethod.lazy);
			}
		}
	}

	@Transactional(readOnly = true)
	public void filter(List<Product> products) {
		CollectionUtils.filter(products, new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null && product.getStock() != null;
			}
		});
	}

}