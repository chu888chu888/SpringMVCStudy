/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.listener;

import javax.annotation.Resource;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopxx.entity.Article;
import net.shopxx.entity.Goods;
import net.shopxx.service.ArticleService;
import net.shopxx.service.GoodsService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Listener - 缓存
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Component("cacheEventListener")
public class CacheEventListener implements net.sf.ehcache.event.CacheEventListener {

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	/**
	 * 元素回收调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementEvicted(Ehcache ehcache, Element element) {
	}

	/**
	 * 元素过期调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementExpired(Ehcache ehcache, Element element) {
		String cacheName = ehcache.getName();
		if (StringUtils.equals(cacheName, Article.HITS_CACHE_NAME)) {
			Long id = (Long) element.getObjectKey();
			Long hits = (Long) element.getObjectValue();
			Article article = articleService.find(id);
			if (article != null && hits != null && hits > 0) {
				article.setHits(hits);
				articleService.update(article);
			}
		} else if (StringUtils.equals(cacheName, Goods.HITS_CACHE_NAME)) {
			Long id = (Long) element.getObjectKey();
			Long hits = (Long) element.getObjectValue();
			Goods goods = goodsService.find(id);
			if (goods != null && hits != null && hits > 0) {
				goods.setHits(hits);
				goodsService.update(goods);
			}
		}
	}

	/**
	 * 元素添加调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {
	}

	/**
	 * 元素移除调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
	}

	/**
	 * 元素更新调用
	 * 
	 * @param ehcache
	 *            缓存
	 * @param element
	 *            元素
	 */
	public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {
	}

	/**
	 * 删除调用
	 * 
	 * @param ehcache
	 *            缓存
	 */
	public void notifyRemoveAll(Ehcache ehcache) {
	}

	/**
	 * 释放
	 */
	public void dispose() {
	}

	/**
	 * 克隆
	 * 
	 * @return 对象
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}