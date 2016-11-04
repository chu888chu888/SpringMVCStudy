/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.template.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.annotation.Resource;

import net.shopxx.entity.Member;
import net.shopxx.service.MemberService;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 当前会员
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Component("currentMemberDirective")
public class CurrentMemberDirective extends BaseDirective {

	/** 变量名称 */
	private static final String VARIABLE_NAME = "currentMember";

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 执行
	 * 
	 * @param env
	 *            环境变量
	 * @param params
	 *            参数
	 * @param loopVars
	 *            循环变量
	 * @param body
	 *            模板内容
	 */
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Member currentMember = memberService.getCurrent();
		if (body != null) {
			setLocalVariable(VARIABLE_NAME, currentMember, env, body);
		} else {
			if (currentMember != null) {
				Writer out = env.getOut();
				out.write(currentMember.getUsername());
			}
		}
	}

}