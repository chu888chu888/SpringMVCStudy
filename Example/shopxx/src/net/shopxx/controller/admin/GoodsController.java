/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.Admin;
import net.shopxx.entity.Attribute;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.Brand;
import net.shopxx.entity.Goods;
import net.shopxx.entity.Parameter;
import net.shopxx.entity.Product;
import net.shopxx.entity.ProductCategory;
import net.shopxx.entity.Promotion;
import net.shopxx.entity.Specification;
import net.shopxx.entity.Tag;
import net.shopxx.service.AdminService;
import net.shopxx.service.AttributeService;
import net.shopxx.service.BrandService;
import net.shopxx.service.GoodsService;
import net.shopxx.service.ParameterValueService;
import net.shopxx.service.ProductCategoryService;
import net.shopxx.service.ProductImageService;
import net.shopxx.service.ProductService;
import net.shopxx.service.PromotionService;
import net.shopxx.service.SpecificationItemService;
import net.shopxx.service.SpecificationService;
import net.shopxx.service.TagService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 货品
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Controller("adminGoodsController")
@RequestMapping("/admin/goods")
public class GoodsController extends BaseController {

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "parameterValueServiceImpl")
	private ParameterValueService parameterValueService;
	@Resource(name = "specificationItemServiceImpl")
	private SpecificationItemService specificationItemService;
	@Resource(name = "attributeServiceImpl")
	private AttributeService attributeService;
	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 检查编号是否存在
	 */
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkSn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}
		return !goodsService.snExists(sn);
	}

	/**
	 * 获取参数
	 */
	@RequestMapping(value = "/parameters", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> parameters(Long productCategoryId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getParameters())) {
			return data;
		}
		for (Parameter parameter : productCategory.getParameters()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("group", parameter.getGroup());
			item.put("names", parameter.getNames());
			data.add(item);
		}
		return data;
	}

	/**
	 * 获取属性
	 */
	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> attributes(Long productCategoryId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getAttributes())) {
			return data;
		}
		for (Attribute attribute : productCategory.getAttributes()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", attribute.getId());
			item.put("name", attribute.getName());
			item.put("options", attribute.getOptions());
			data.add(item);
		}
		return data;
	}

	/**
	 * 获取规格
	 */
	@RequestMapping(value = "/specifications", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> specifications(Long productCategoryId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getSpecifications())) {
			return data;
		}
		for (Specification specification : productCategory.getSpecifications()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", specification.getName());
			item.put("options", specification.getOptions());
			data.add(item);
		}
		return data;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("specifications", specificationService.findAll());
		return "/admin/goods/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Goods goods, ProductForm productForm, ProductListForm productListForm, Long productCategoryId, Long brandId, Long[] promotionIds, Long[] tagIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		productImageService.filter(goods.getProductImages());
		parameterValueService.filter(goods.getParameterValues());
		specificationItemService.filter(goods.getSpecificationItems());
		productService.filter(productListForm.getProductList());

		goods.setProductCategory(productCategoryService.find(productCategoryId));
		goods.setBrand(brandService.find(brandId));
		goods.setPromotions(new HashSet<Promotion>(promotionService.findList(promotionIds)));
		goods.setTags(new HashSet<Tag>(tagService.findList(tagIds)));

		goods.removeAttributeValue();
		for (Attribute attribute : goods.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(goods, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(goods.getSn()) && goodsService.snExists(goods.getSn())) {
			return ERROR_VIEW;
		}

		Admin admin = adminService.getCurrent();
		if (goods.hasSpecification()) {
			List<Product> products = productListForm.getProductList();
			if (CollectionUtils.isEmpty(products) || !isValid(products, getValidationGroup(goods.getType()), BaseEntity.Save.class)) {
				return ERROR_VIEW;
			}
			goodsService.save(goods, products, admin);
		} else {
			Product product = productForm.getProduct();
			if (product == null || !isValid(product, getValidationGroup(goods.getType()), BaseEntity.Save.class)) {
				return ERROR_VIEW;
			}
			goodsService.save(goods, product, admin);
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("goods", goodsService.find(id));
		return "/admin/goods/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Goods goods, ProductForm productForm, ProductListForm productListForm, Long id, Long productCategoryId, Long brandId, Long[] promotionIds, Long[] tagIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		productImageService.filter(goods.getProductImages());
		parameterValueService.filter(goods.getParameterValues());
		specificationItemService.filter(goods.getSpecificationItems());
		productService.filter(productListForm.getProductList());

		Goods pGoods = goodsService.find(id);
		goods.setType(pGoods.getType());
		goods.setProductCategory(productCategoryService.find(productCategoryId));
		goods.setBrand(brandService.find(brandId));
		goods.setPromotions(new HashSet<Promotion>(promotionService.findList(promotionIds)));
		goods.setTags(new HashSet<Tag>(tagService.findList(tagIds)));

		goods.removeAttributeValue();
		for (Attribute attribute : goods.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(goods, BaseEntity.Update.class)) {
			return ERROR_VIEW;
		}

		Admin admin = adminService.getCurrent();
		if (goods.hasSpecification()) {
			List<Product> products = productListForm.getProductList();
			if (CollectionUtils.isEmpty(products) || !isValid(products, getValidationGroup(goods.getType()), BaseEntity.Update.class)) {
				return ERROR_VIEW;
			}
			goodsService.update(goods, products, admin);
		} else {
			Product product = productForm.getProduct();
			if (product == null || !isValid(product, getValidationGroup(goods.getType()), BaseEntity.Update.class)) {
				return ERROR_VIEW;
			}
			goodsService.update(goods, product, admin);
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		model.addAttribute("types", Goods.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.goods));
		model.addAttribute("type", type);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("page", goodsService.findPage(type, productCategory, brand, promotion, tag, null, null, null, isMarketable, isList, isTop, isOutOfStock, isStockAlert, null, null, pageable));
		return "/admin/goods/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		goodsService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 根据类型获取验证组
	 * 
	 * @param type
	 *            类型
	 * @return 验证组
	 */
	private Class<?> getValidationGroup(Goods.Type type) {
		Assert.notNull(type);

		switch (type) {
		case general:
			return Product.General.class;
		case exchange:
			return Product.Exchange.class;
		case gift:
			return Product.Gift.class;
		}
		return null;
	}

	/**
	 * FormBean - 商品
	 * 
	 * @author SHOP++ Team
	 * @version 4.0
	 */
	public static class ProductForm {

		/** 商品 */
		private Product product;

		/**
		 * 获取商品
		 * 
		 * @return 商品
		 */
		public Product getProduct() {
			return product;
		}

		/**
		 * 设置商品
		 * 
		 * @param product
		 *            商品
		 */
		public void setProduct(Product product) {
			this.product = product;
		}

	}

	/**
	 * FormBean - 商品
	 * 
	 * @author SHOP++ Team
	 * @version 4.0
	 */
	public static class ProductListForm {

		/** 商品 */
		private List<Product> productList;

		/**
		 * 获取商品
		 * 
		 * @return 商品
		 */
		public List<Product> getProductList() {
			return productList;
		}

		/**
		 * 设置商品
		 * 
		 * @param productList
		 *            商品
		 */
		public void setProductList(List<Product> productList) {
			this.productList = productList;
		}

	}

}