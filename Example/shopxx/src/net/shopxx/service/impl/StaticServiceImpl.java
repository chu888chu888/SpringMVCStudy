/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.shopxx.Setting;
import net.shopxx.TemplateConfig;
import net.shopxx.dao.ArticleDao;
import net.shopxx.dao.BrandDao;
import net.shopxx.dao.GoodsDao;
import net.shopxx.dao.PromotionDao;
import net.shopxx.entity.Article;
import net.shopxx.entity.ArticleCategory;
import net.shopxx.entity.Goods;
import net.shopxx.entity.ProductCategory;
import net.shopxx.service.StaticService;
import net.shopxx.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Service - 静态化
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("staticServiceImpl")
public class StaticServiceImpl implements StaticService, ServletContextAware {

	/** Sitemap最大地址数 */
	private static final Integer SITEMAP_MAX_SIZE = 10000;

	/** ServletContext */
	private ServletContext servletContext;

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Resource(name = "articleDaoImpl")
	private ArticleDao articleDao;
	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;
	@Resource(name = "brandDaoImpl")
	private BrandDao brandDao;
	@Resource(name = "promotionDaoImpl")
	private PromotionDao promotionDao;

	/**
	 * 设置ServletContext
	 * 
	 * @param servletContext
	 *            ServletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Transactional(readOnly = true)
	public int generate(String templatePath, String staticPath, Map<String, Object> model) {
		Assert.hasText(templatePath);
		Assert.hasText(staticPath);

		Writer writer = null;
		try {
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templatePath);
			File staticFile = new File(servletContext.getRealPath(staticPath));
			File staticDir = staticFile.getParentFile();
			if (staticDir != null) {
				staticDir.mkdirs();
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(staticFile), "UTF-8"));
			template.process(model, writer);
			writer.flush();
			return 1;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	@Transactional
	public int generate(Article article) {
		if (article == null) {
			return 0;
		}
		delete(article);
		if (!article.getIsPublication()) {
			return 0;
		}
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("articleContent");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("article", article);
		article.setGenerateMethod(Article.GenerateMethod.none);
		int generateCount = 0;
		for (int i = 1; i <= article.getTotalPages(); i++) {
			model.put("pageNumber", i);
			generateCount += generate(templateConfig.getRealTemplatePath(), article.getPath(i), model);
		}
		return generateCount;
	}

	@Transactional
	public int generate(Goods goods) {
		if (goods == null) {
			return 0;
		}
		delete(goods);
		if (!goods.getIsMarketable()) {
			return 0;
		}
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("goodsContent");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("goods", goods);
		goods.setGenerateMethod(Goods.GenerateMethod.none);
		return generate(templateConfig.getRealTemplatePath(), goods.getPath(), model);
	}

	@Transactional
	public int generateArticle(ArticleCategory articleCategory, Boolean isPublication, Article.GenerateMethod generateMethod, Date beginDate, Date endDate) {
		int generateCount = 0;
		if (Article.GenerateMethod.eager.equals(generateMethod) || Article.GenerateMethod.lazy.equals(generateMethod)) {
			while (true) {
				List<Article> articles = articleDao.findList(articleCategory, isPublication, generateMethod, beginDate, endDate, null, 100);
				if (CollectionUtils.isNotEmpty(articles)) {
					for (Article article : articles) {
						generateCount += generate(article);
					}
					articleDao.flush();
					articleDao.clear();
				}
				if (articles.size() < 100) {
					break;
				}
			}
		} else {
			for (int i = 0;; i += 100) {
				List<Article> articles = articleDao.findList(articleCategory, isPublication, generateMethod, beginDate, endDate, i, 100);
				if (CollectionUtils.isNotEmpty(articles)) {
					for (Article article : articles) {
						generateCount += generate(article);
					}
					articleDao.flush();
					articleDao.clear();
				}
				if (articles.size() < 100) {
					break;
				}
			}
		}
		return generateCount;
	}

	@Transactional
	public int generateGoods(ProductCategory productCategory, Boolean isMarketable, Goods.GenerateMethod generateMethod, Date beginDate, Date endDate) {
		int generateCount = 0;
		if (Goods.GenerateMethod.eager.equals(generateMethod) || Goods.GenerateMethod.lazy.equals(generateMethod)) {
			while (true) {
				List<Goods> goodsList = goodsDao.findList(productCategory, isMarketable, generateMethod, beginDate, endDate, null, 100);
				if (CollectionUtils.isNotEmpty(goodsList)) {
					for (Goods goods : goodsList) {
						generateCount += generate(goods);
					}
					goodsDao.flush();
					goodsDao.clear();
				}
				if (goodsList.size() < 100) {
					break;
				}
			}
		} else {
			for (int i = 0;; i += 100) {
				List<Goods> goodsList = goodsDao.findList(productCategory, isMarketable, generateMethod, beginDate, endDate, i, 100);
				if (CollectionUtils.isNotEmpty(goodsList)) {
					for (Goods goods : goodsList) {
						generateCount += generate(goods);
					}
					goodsDao.flush();
					goodsDao.clear();
				}
				if (goodsList.size() < 100) {
					break;
				}
			}
		}
		return generateCount;
	}

	@Transactional(readOnly = true)
	public int generateIndex() {
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("index");
		return generate(templateConfig.getRealTemplatePath(), templateConfig.getRealStaticPath(), null);
	}

	@Transactional(readOnly = true)
	public int generateSitemap() {
		int generateCount = 0;
		TemplateConfig sitemapIndexTemplateConfig = SystemUtils.getTemplateConfig("sitemapIndex");
		TemplateConfig sitemapTemplateConfig = SystemUtils.getTemplateConfig("sitemap");
		List<SitemapUrl> sitemapUrls = new ArrayList<SitemapUrl>();

		Setting setting = SystemUtils.getSetting();
		SitemapUrl indexSitemapUrl = new SitemapUrl();
		indexSitemapUrl.setLoc(setting.getSiteUrl());
		indexSitemapUrl.setLastmod(new Date());
		indexSitemapUrl.setChangefreq(SitemapUrl.Changefreq.hourly);
		indexSitemapUrl.setPriority(1);
		sitemapUrls.add(indexSitemapUrl);

		for (int i = 0;; i += 100) {
			List<Article> articles = articleDao.findList(i, 100, null, null);
			if (CollectionUtils.isNotEmpty(articles)) {
				for (Article article : articles) {
					SitemapUrl articleSitemapUrl = new SitemapUrl();
					articleSitemapUrl.setLoc(article.getUrl());
					articleSitemapUrl.setLastmod(article.getModifyDate());
					articleSitemapUrl.setChangefreq(SitemapUrl.Changefreq.daily);
					articleSitemapUrl.setPriority(0.6F);
					sitemapUrls.add(articleSitemapUrl);
				}
				articleDao.flush();
				articleDao.clear();
			}
			if (articles.size() < 100) {
				break;
			}
		}
		for (int i = 0;; i += 100) {
			List<Goods> goodsList = goodsDao.findList(i, 100, null, null);
			if (CollectionUtils.isNotEmpty(goodsList)) {
				for (Goods goods : goodsList) {
					SitemapUrl goodsSitemapUrl = new SitemapUrl();
					goodsSitemapUrl.setLoc(goods.getUrl());
					goodsSitemapUrl.setLastmod(goods.getModifyDate());
					goodsSitemapUrl.setChangefreq(SitemapUrl.Changefreq.daily);
					goodsSitemapUrl.setPriority(0.8F);
					sitemapUrls.add(goodsSitemapUrl);
				}
				goodsDao.flush();
				goodsDao.clear();
			}
			if (goodsList.size() < 100) {
				break;
			}
		}

		List<String> sitemapPaths = new ArrayList<String>();
		for (int i = 0, index = 0; i < sitemapUrls.size(); i += SITEMAP_MAX_SIZE, index++) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("index", index);
			model.put("sitemapUrls", sitemapUrls.subList(i, i + SITEMAP_MAX_SIZE <= sitemapUrls.size() ? i + SITEMAP_MAX_SIZE : sitemapUrls.size()));
			String sitemapPath = sitemapTemplateConfig.getRealStaticPath(model);
			sitemapPaths.add(sitemapPath);
			generateCount += generate(sitemapTemplateConfig.getRealTemplatePath(), sitemapPath, model);
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sitemapPaths", sitemapPaths);
		generateCount += generate(sitemapIndexTemplateConfig.getRealTemplatePath(), sitemapIndexTemplateConfig.getRealStaticPath(), model);
		return generateCount;
	}

	@Transactional(readOnly = true)
	public int generateOther() {
		int generateCount = 0;
		TemplateConfig shopCommonJsTemplateConfig = SystemUtils.getTemplateConfig("shopCommonJs");
		TemplateConfig adminCommonJsTemplateConfig = SystemUtils.getTemplateConfig("adminCommonJs");
		generateCount += generate(shopCommonJsTemplateConfig.getRealTemplatePath(), shopCommonJsTemplateConfig.getRealStaticPath(), null);
		generateCount += generate(adminCommonJsTemplateConfig.getRealTemplatePath(), adminCommonJsTemplateConfig.getRealStaticPath(), null);
		return generateCount;
	}

	@Transactional
	public int generateAll() {
		int generateCount = 0;
		generateCount += generateArticle(null, null, null, null, null);
		generateCount += generateGoods(null, null, null, null, null);
		generateCount += generateIndex();
		generateCount += generateSitemap();
		generateCount += generateOther();
		return generateCount;
	}

	@Transactional(readOnly = true)
	public int delete(String staticPath) {
		if (StringUtils.isEmpty(staticPath)) {
			return 0;
		}
		File staticFile = new File(servletContext.getRealPath(staticPath));
		return FileUtils.deleteQuietly(staticFile) ? 1 : 0;
	}

	@Transactional
	public int delete(Article article) {
		if (article == null || StringUtils.isEmpty(article.getPath())) {
			return 0;
		}
		int deleteCount = 0;
		for (int i = 1;; i++) {
			int count = delete(article.getPath(i));
			if (count < 1) {
				break;
			}
			deleteCount += count;
		}
		return deleteCount;
	}

	@Transactional
	public int delete(Goods goods) {
		if (goods == null || StringUtils.isEmpty(goods.getPath())) {
			return 0;
		}
		return delete(goods.getPath());
	}

	@Transactional(readOnly = true)
	public int deleteIndex() {
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("index");
		return delete(templateConfig.getRealStaticPath());
	}

	@Transactional(readOnly = true)
	public int deleteOther() {
		int deleteCount = 0;
		TemplateConfig shopCommonJsTemplateConfig = SystemUtils.getTemplateConfig("shopCommonJs");
		TemplateConfig adminCommonJsTemplateConfig = SystemUtils.getTemplateConfig("adminCommonJs");
		deleteCount += delete(shopCommonJsTemplateConfig.getRealStaticPath());
		deleteCount += delete(adminCommonJsTemplateConfig.getRealStaticPath());
		return deleteCount;
	}

	/**
	 * SitemapUrl
	 * 
	 * @author SHOP++ Team
	 * @version 4.0
	 */
	public static class SitemapUrl {

		/**
		 * 更新频率
		 */
		public enum Changefreq {

			/** 经常 */
			always,

			/** 每小时 */
			hourly,

			/** 每天 */
			daily,

			/** 每周 */
			weekly,

			/** 每月 */
			monthly,

			/** 每年 */
			yearly,

			/** 从不 */
			never
		}

		/** 链接地址 */
		private String loc;

		/** 最后修改日期 */
		private Date lastmod;

		/** 更新频率 */
		private Changefreq changefreq;

		/** 权重 */
		private float priority;

		/**
		 * 获取链接地址
		 * 
		 * @return 链接地址
		 */
		public String getLoc() {
			return loc;
		}

		/**
		 * 设置链接地址
		 * 
		 * @param loc
		 *            链接地址
		 */
		public void setLoc(String loc) {
			this.loc = loc;
		}

		/**
		 * 获取最后修改日期
		 * 
		 * @return 最后修改日期
		 */
		public Date getLastmod() {
			return lastmod;
		}

		/**
		 * 设置最后修改日期
		 * 
		 * @param lastmod
		 *            最后修改日期
		 */
		public void setLastmod(Date lastmod) {
			this.lastmod = lastmod;
		}

		/**
		 * 获取更新频率
		 * 
		 * @return 更新频率
		 */
		public Changefreq getChangefreq() {
			return changefreq;
		}

		/**
		 * 设置更新频率
		 * 
		 * @param changefreq
		 *            更新频率
		 */
		public void setChangefreq(Changefreq changefreq) {
			this.changefreq = changefreq;
		}

		/**
		 * 获取权重
		 * 
		 * @return 权重
		 */
		public float getPriority() {
			return priority;
		}

		/**
		 * 设置权重
		 * 
		 * @param priority
		 *            权重
		 */
		public void setPriority(float priority) {
			this.priority = priority;
		}

	}

}