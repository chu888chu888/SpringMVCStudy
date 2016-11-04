/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopxx.Filter;
import net.shopxx.Order;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.dao.AttributeDao;
import net.shopxx.dao.BrandDao;
import net.shopxx.dao.GoodsDao;
import net.shopxx.dao.ProductCategoryDao;
import net.shopxx.dao.ProductDao;
import net.shopxx.dao.PromotionDao;
import net.shopxx.dao.SnDao;
import net.shopxx.dao.StockLogDao;
import net.shopxx.dao.TagDao;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Attribute;
import net.shopxx.entity.Brand;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Member;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductCategory;
import net.shopxx.entity.Promotion;
import net.shopxx.entity.Sn;
import net.shopxx.entity.SpecificationItem;
import net.shopxx.entity.StockLog;
import net.shopxx.entity.Tag;
import net.shopxx.service.GoodsService;
import net.shopxx.service.ProductImageService;
import net.shopxx.service.SpecificationValueService;
import net.shopxx.service.StaticService;
import net.shopxx.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 货品
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("goodsServiceImpl")
public class GoodsServiceImpl extends BaseServiceImpl<Goods, Long> implements GoodsService {

	@Resource(name = "ehCacheManager")
	private CacheManager cacheManager;
	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;
	@Resource(name = "productDaoImpl")
	private ProductDao productDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;
	@Resource(name = "productCategoryDaoImpl")
	private ProductCategoryDao productCategoryDao;
	@Resource(name = "brandDaoImpl")
	private BrandDao brandDao;
	@Resource(name = "promotionDaoImpl")
	private PromotionDao promotionDao;
	@Resource(name = "tagDaoImpl")
	private TagDao tagDao;
	@Resource(name = "attributeDaoImpl")
	private AttributeDao attributeDao;
	@Resource(name = "stockLogDaoImpl")
	private StockLogDao stockLogDao;
	@Resource(name = "specificationValueServiceImpl")
	private SpecificationValueService specificationValueService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return goodsDao.snExists(sn);
	}

	@Transactional(readOnly = true)
	public Goods findBySn(String sn) {
		return goodsDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public List<Goods> findList(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
		return goodsDao.findList(type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "goods", condition = "#useCache")
	public List<Goods> findList(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Map<Long, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Boolean hasPromotion, Goods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		Brand brand = brandDao.find(brandId);
		if (brandId != null && brand == null) {
			return Collections.emptyList();
		}
		Promotion promotion = promotionDao.find(promotionId);
		if (promotionId != null && promotion == null) {
			return Collections.emptyList();
		}
		Tag tag = tagDao.find(tagId);
		if (tagId != null && tag == null) {
			return Collections.emptyList();
		}
		Map<Attribute, String> map = new HashMap<Attribute, String>();
		if (attributeValueMap != null) {
			for (Map.Entry<Long, String> entry : attributeValueMap.entrySet()) {
				Attribute attribute = attributeDao.find(entry.getKey());
				if (attribute != null) {
					map.put(attribute, entry.getValue());
				}
			}
		}
		if (MapUtils.isNotEmpty(attributeValueMap) && MapUtils.isEmpty(map)) {
			return Collections.emptyList();
		}
		return goodsDao.findList(type, productCategory, brand, promotion, tag, map, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Goods> findList(ProductCategory productCategory, Boolean isMarketable, Goods.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count) {
		return goodsDao.findList(productCategory, isMarketable, generateMethod, beginDate, endDate, first, count);
	}

	@Transactional(readOnly = true)
	public Page<Goods> findPage(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable) {
		return goodsDao.findPage(type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Goods> findPage(Goods.RankingType rankingType, Pageable pageable) {
		return goodsDao.findPage(rankingType, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Goods> findPage(Member member, Pageable pageable) {
		return goodsDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Goods.Type type, Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert) {
		return goodsDao.count(type, favoriteMember, isMarketable, isList, isTop, isOutOfStock, isStockAlert);
	}

	public long viewHits(Long id) {
		Assert.notNull(id);

		Ehcache cache = cacheManager.getEhcache(Goods.HITS_CACHE_NAME);
		Element element = cache.get(id);
		Long hits;
		if (element != null) {
			hits = (Long) element.getObjectValue() + 1;
		} else {
			Goods goods = goodsDao.find(id);
			if (goods == null) {
				return 0L;
			}
			hits = goods.getHits() + 1;
		}
		cache.put(new Element(id, hits));
		return hits;
	}

	public void addHits(Goods goods, long amount) {
		Assert.notNull(goods);
		Assert.state(amount >= 0);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(goodsDao.getLockMode(goods))) {
			goodsDao.refresh(goods, LockModeType.PESSIMISTIC_WRITE);
		}

		Calendar nowCalendar = Calendar.getInstance();
		Calendar weekHitsCalendar = DateUtils.toCalendar(goods.getWeekHitsDate());
		Calendar monthHitsCalendar = DateUtils.toCalendar(goods.getMonthHitsDate());
		if (nowCalendar.get(Calendar.YEAR) > weekHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekHitsCalendar.get(Calendar.WEEK_OF_YEAR)) {
			goods.setWeekHits(amount);
		} else {
			goods.setWeekHits(goods.getWeekHits() + amount);
		}
		if (nowCalendar.get(Calendar.YEAR) > monthHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthHitsCalendar.get(Calendar.MONTH)) {
			goods.setMonthHits(amount);
		} else {
			goods.setMonthHits(goods.getMonthHits() + amount);
		}
		goods.setHits(goods.getHits() + amount);
		goods.setWeekHitsDate(new Date());
		goods.setMonthHitsDate(new Date());
		goodsDao.flush();
	}

	public void addSales(Goods goods, long amount) {
		Assert.notNull(goods);
		Assert.state(amount >= 0);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(goodsDao.getLockMode(goods))) {
			goodsDao.refresh(goods, LockModeType.PESSIMISTIC_WRITE);
		}

		Calendar nowCalendar = Calendar.getInstance();
		Calendar weekSalesCalendar = DateUtils.toCalendar(goods.getWeekSalesDate());
		Calendar monthSalesCalendar = DateUtils.toCalendar(goods.getMonthSalesDate());
		if (nowCalendar.get(Calendar.YEAR) > weekSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekSalesCalendar.get(Calendar.WEEK_OF_YEAR)) {
			goods.setWeekSales(amount);
		} else {
			goods.setWeekSales(goods.getWeekSales() + amount);
		}
		if (nowCalendar.get(Calendar.YEAR) > monthSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthSalesCalendar.get(Calendar.MONTH)) {
			goods.setMonthSales(amount);
		} else {
			goods.setMonthSales(goods.getMonthSales() + amount);
		}
		goods.setSales(goods.getSales() + amount);
		goods.setWeekSalesDate(new Date());
		goods.setMonthSalesDate(new Date());
		goods.setGenerateMethod(Goods.GenerateMethod.lazy);
		goodsDao.flush();
	}

	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods save(Goods goods, Product product, Admin operator) {
		Assert.notNull(goods);
		Assert.isTrue(goods.isNew());
		Assert.notNull(goods.getType());
		Assert.isTrue(!goods.hasSpecification());
		Assert.notNull(product);
		Assert.isTrue(product.isNew());
		Assert.state(!product.hasSpecification());

		switch (goods.getType()) {
		case general:
			product.setExchangePoint(0L);
			break;
		case exchange:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			goods.setPromotions(null);
			break;
		case gift:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			product.setExchangePoint(0L);
			goods.setPromotions(null);
			break;
		}
		if (product.getMarketPrice() == null) {
			product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
		}
		if (product.getRewardPoint() == null) {
			product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
		}
		product.setAllocatedStock(0);
		product.setIsDefault(true);
		product.setGoods(goods);
		product.setSpecificationValues(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setShippingItems(null);
		product.setProductNotifies(null);
		product.setStockLogs(null);
		product.setGiftPromotions(null);

		goods.setPrice(product.getPrice());
		goods.setMarketPrice(product.getMarketPrice());
		goods.setScore(0F);
		goods.setTotalScore(0L);
		goods.setScoreCount(0L);
		goods.setHits(0L);
		goods.setWeekHits(0L);
		goods.setMonthHits(0L);
		goods.setSales(0L);
		goods.setWeekSales(0L);
		goods.setMonthSales(0L);
		goods.setWeekHitsDate(new Date());
		goods.setMonthHitsDate(new Date());
		goods.setWeekSalesDate(new Date());
		goods.setMonthSalesDate(new Date());
		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		goods.setSpecificationItems(null);
		goods.setReviews(null);
		goods.setConsultations(null);
		goods.setFavoriteMembers(null);
		goods.setProducts(null);
		setValue(goods);
		goodsDao.persist(goods);

		setValue(product);
		productDao.persist(product);
		stockIn(product, operator);

		return goods;
	}

	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods save(Goods goods, List<Product> products, Admin operator) {
		Assert.notNull(goods);
		Assert.isTrue(goods.isNew());
		Assert.notNull(goods.getType());
		Assert.isTrue(goods.hasSpecification());
		Assert.notEmpty(products);

		final List<SpecificationItem> specificationItems = goods.getSpecificationItems();
		if (CollectionUtils.exists(products, new Predicate() {
			private Set<List<Integer>> set = new HashSet<List<Integer>>();

			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product == null || !product.isNew() || !product.hasSpecification() || !set.add(product.getSpecificationValueIds()) || !specificationValueService.isValid(specificationItems, product.getSpecificationValues());
			}
		})) {
			throw new IllegalArgumentException();
		}

		Product defaultProduct = (Product) CollectionUtils.find(products, new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null && product.getIsDefault();
			}
		});
		if (defaultProduct == null) {
			defaultProduct = products.get(0);
			defaultProduct.setIsDefault(true);
		}

		for (Product product : products) {
			switch (goods.getType()) {
			case general:
				product.setExchangePoint(0L);
				break;
			case exchange:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				goods.setPromotions(null);
				break;
			case gift:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				product.setExchangePoint(0L);
				goods.setPromotions(null);
				break;
			}
			if (product.getMarketPrice() == null) {
				product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
			}
			if (product.getRewardPoint() == null) {
				product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
			}
			if (product != defaultProduct) {
				product.setIsDefault(false);
			}
			product.setAllocatedStock(0);
			product.setGoods(goods);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setShippingItems(null);
			product.setProductNotifies(null);
			product.setStockLogs(null);
			product.setGiftPromotions(null);
		}

		goods.setPrice(defaultProduct.getPrice());
		goods.setMarketPrice(defaultProduct.getMarketPrice());
		goods.setScore(0F);
		goods.setTotalScore(0L);
		goods.setScoreCount(0L);
		goods.setHits(0L);
		goods.setWeekHits(0L);
		goods.setMonthHits(0L);
		goods.setSales(0L);
		goods.setWeekSales(0L);
		goods.setMonthSales(0L);
		goods.setWeekHitsDate(new Date());
		goods.setMonthHitsDate(new Date());
		goods.setWeekSalesDate(new Date());
		goods.setMonthSalesDate(new Date());
		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		goods.setReviews(null);
		goods.setConsultations(null);
		goods.setFavoriteMembers(null);
		goods.setProducts(null);
		setValue(goods);
		goodsDao.persist(goods);

		for (Product product : products) {
			setValue(product);
			productDao.persist(product);
			stockIn(product, operator);
		}

		return goods;
	}

	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods update(Goods goods, Product product, Admin operator) {
		Assert.notNull(goods);
		Assert.isTrue(!goods.isNew());
		Assert.isTrue(!goods.hasSpecification());
		Assert.notNull(product);
		Assert.isTrue(product.isNew());
		Assert.state(!product.hasSpecification());

		Goods pGoods = goodsDao.find(goods.getId());
		switch (pGoods.getType()) {
		case general:
			product.setExchangePoint(0L);
			break;
		case exchange:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			goods.setPromotions(null);
			break;
		case gift:
			product.setPrice(BigDecimal.ZERO);
			product.setRewardPoint(0L);
			product.setExchangePoint(0L);
			goods.setPromotions(null);
			break;
		}
		if (product.getMarketPrice() == null) {
			product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
		}
		if (product.getRewardPoint() == null) {
			product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
		}
		product.setAllocatedStock(0);
		product.setIsDefault(true);
		product.setGoods(pGoods);
		product.setSpecificationValues(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setShippingItems(null);
		product.setProductNotifies(null);
		product.setStockLogs(null);
		product.setGiftPromotions(null);

		if (pGoods.hasSpecification()) {
			for (Product pProduct : pGoods.getProducts()) {
				productDao.remove(pProduct);
			}
			if (product.getStock() == null) {
				throw new IllegalArgumentException();
			}
			setValue(product);
			productDao.persist(product);
			stockIn(product, operator);
		} else {
			Product defaultProduct = pGoods.getDefaultProduct();
			defaultProduct.setPrice(product.getPrice());
			defaultProduct.setCost(product.getCost());
			defaultProduct.setMarketPrice(product.getMarketPrice());
			defaultProduct.setRewardPoint(product.getRewardPoint());
			defaultProduct.setExchangePoint(product.getExchangePoint());
		}

		goods.setPrice(product.getPrice());
		goods.setMarketPrice(product.getMarketPrice());
		setValue(goods);
		copyProperties(goods, pGoods, "sn", "type", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "generateMethod", "reviews", "consultations", "favoriteMembers",
				"products");
		pGoods.setGenerateMethod(Goods.GenerateMethod.eager);
		return pGoods;
	}

	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods update(Goods goods, List<Product> products, Admin operator) {
		Assert.notNull(goods);
		Assert.isTrue(!goods.isNew());
		Assert.isTrue(goods.hasSpecification());
		Assert.notEmpty(products);

		final List<SpecificationItem> specificationItems = goods.getSpecificationItems();
		if (CollectionUtils.exists(products, new Predicate() {
			private Set<List<Integer>> set = new HashSet<List<Integer>>();

			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product == null || !product.isNew() || !product.hasSpecification() || !set.add(product.getSpecificationValueIds()) || !specificationValueService.isValid(specificationItems, product.getSpecificationValues());
			}
		})) {
			throw new IllegalArgumentException();
		}

		Product defaultProduct = (Product) CollectionUtils.find(products, new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null && product.getIsDefault();
			}
		});
		if (defaultProduct == null) {
			defaultProduct = products.get(0);
			defaultProduct.setIsDefault(true);
		}

		Goods pGoods = goodsDao.find(goods.getId());
		for (Product product : products) {
			switch (pGoods.getType()) {
			case general:
				product.setExchangePoint(0L);
				break;
			case exchange:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				goods.setPromotions(null);
				break;
			case gift:
				product.setPrice(BigDecimal.ZERO);
				product.setRewardPoint(0L);
				product.setExchangePoint(0L);
				goods.setPromotions(null);
				break;
			}
			if (product.getMarketPrice() == null) {
				product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
			}
			if (product.getRewardPoint() == null) {
				product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
			}
			if (product != defaultProduct) {
				product.setIsDefault(false);
			}
			product.setAllocatedStock(0);
			product.setGoods(pGoods);
			product.setCartItems(null);
			product.setOrderItems(null);
			product.setShippingItems(null);
			product.setProductNotifies(null);
			product.setStockLogs(null);
			product.setGiftPromotions(null);
		}

		if (pGoods.hasSpecification()) {
			for (Product pProduct : pGoods.getProducts()) {
				if (!exists(products, pProduct.getSpecificationValueIds())) {
					productDao.remove(pProduct);
				}
			}
			for (Product product : products) {
				Product pProduct = find(pGoods.getProducts(), product.getSpecificationValueIds());
				if (pProduct != null) {
					pProduct.setPrice(product.getPrice());
					pProduct.setCost(product.getCost());
					pProduct.setMarketPrice(product.getMarketPrice());
					pProduct.setRewardPoint(product.getRewardPoint());
					pProduct.setExchangePoint(product.getExchangePoint());
					pProduct.setIsDefault(product.getIsDefault());
					pProduct.setSpecificationValues(product.getSpecificationValues());
				} else {
					if (product.getStock() == null) {
						throw new IllegalArgumentException();
					}
					setValue(product);
					productDao.persist(product);
					stockIn(product, operator);
				}
			}
		} else {
			productDao.remove(pGoods.getDefaultProduct());
			for (Product product : products) {
				if (product.getStock() == null) {
					throw new IllegalArgumentException();
				}
				setValue(product);
				productDao.persist(product);
				stockIn(product, operator);
			}
		}

		goods.setPrice(defaultProduct.getPrice());
		goods.setMarketPrice(defaultProduct.getMarketPrice());
		setValue(goods);
		copyProperties(goods, pGoods, "sn", "type", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "generateMethod", "reviews", "consultations", "favoriteMembers",
				"products");
		pGoods.setGenerateMethod(Goods.GenerateMethod.eager);
		return pGoods;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods save(Goods goods) {
		Assert.notNull(goods);

		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		setValue(goods);
		return super.save(goods);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods update(Goods goods) {
		Assert.notNull(goods);

		goods.setGenerateMethod(Goods.GenerateMethod.eager);
		setValue(goods);
		return super.update(goods);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public Goods update(Goods goods, String... ignoreProperties) {
		return super.update(goods, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "goods", "productCategory" }, allEntries = true)
	public void delete(Goods goods) {
		staticService.delete(goods);
		super.delete(goods);
	}

	/**
	 * 设置货品值
	 * 
	 * @param goods
	 *            货品
	 */
	private void setValue(Goods goods) {
		if (goods == null) {
			return;
		}

		productImageService.generate(goods.getProductImages());
		if (StringUtils.isEmpty(goods.getImage()) && StringUtils.isNotEmpty(goods.getThumbnail())) {
			goods.setImage(goods.getThumbnail());
		}
		if (goods.isNew()) {
			if (StringUtils.isEmpty(goods.getSn())) {
				String sn;
				do {
					sn = snDao.generate(Sn.Type.goods);
				} while (snExists(sn));
				goods.setSn(sn);
			}
		}
	}

	/**
	 * 设置商品值
	 * 
	 * @param product
	 *            商品
	 */
	private void setValue(Product product) {
		if (product == null) {
			return;
		}

		if (product.isNew()) {
			Goods goods = product.getGoods();
			if (goods != null && StringUtils.isNotEmpty(goods.getSn())) {
				String sn;
				int i = product.hasSpecification() ? 1 : 0;
				do {
					sn = goods.getSn() + (i == 0 ? "" : "_" + i);
					i++;
				} while (productDao.snExists(sn));
				product.setSn(sn);
			}
		}
	}

	/**
	 * 计算默认市场价
	 * 
	 * @param price
	 *            价格
	 * @return 默认市场价
	 */
	private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
		Assert.notNull(price);

		Setting setting = SystemUtils.getSetting();
		Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
		return defaultMarketPriceScale != null ? setting.setScale(price.multiply(new BigDecimal(String.valueOf(defaultMarketPriceScale)))) : BigDecimal.ZERO;
	}

	/**
	 * 计算默认赠送积分
	 * 
	 * @param price
	 *            价格
	 * @return 默认赠送积分
	 */
	private long calculateDefaultRewardPoint(BigDecimal price) {
		Assert.notNull(price);

		Setting setting = SystemUtils.getSetting();
		Double defaultPointScale = setting.getDefaultPointScale();
		return defaultPointScale != null ? price.multiply(new BigDecimal(String.valueOf(defaultPointScale))).longValue() : 0L;
	}

	/**
	 * 根据规格值ID查找商品
	 * 
	 * @param products
	 *            商品
	 * @param specificationValueIds
	 *            规格值ID
	 * @return 商品
	 */
	private Product find(Collection<Product> products, final List<Integer> specificationValueIds) {
		if (CollectionUtils.isEmpty(products) || CollectionUtils.isEmpty(specificationValueIds)) {
			return null;
		}

		return (Product) CollectionUtils.find(products, new Predicate() {
			public boolean evaluate(Object object) {
				Product product = (Product) object;
				return product != null && product.getSpecificationValueIds() != null && product.getSpecificationValueIds().equals(specificationValueIds);
			}
		});
	}

	/**
	 * 根据规格值ID判断商品是否存在
	 * 
	 * @param products
	 *            商品
	 * @param specificationValueIds
	 *            规格值ID
	 * @return 商品是否存在
	 */
	private boolean exists(Collection<Product> products, final List<Integer> specificationValueIds) {
		return find(products, specificationValueIds) != null;
	}

	/**
	 * 入库
	 * 
	 * @param product
	 *            商品
	 * @param operator
	 *            操作员
	 */
	private void stockIn(Product product, Admin operator) {
		if (product == null || product.getStock() == null || product.getStock() <= 0) {
			return;
		}

		StockLog stockLog = new StockLog();
		stockLog.setType(StockLog.Type.stockIn);
		stockLog.setInQuantity(product.getStock());
		stockLog.setOutQuantity(0);
		stockLog.setStock(product.getStock());
		stockLog.setOperator(operator);
		stockLog.setMemo(null);
		stockLog.setProduct(product);
		stockLogDao.persist(stockLog);
	}

}