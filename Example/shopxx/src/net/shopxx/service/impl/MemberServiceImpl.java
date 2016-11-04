/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.Principal;
import net.shopxx.Setting;
import net.shopxx.dao.DepositLogDao;
import net.shopxx.dao.MemberDao;
import net.shopxx.dao.MemberRankDao;
import net.shopxx.dao.PointLogDao;
import net.shopxx.entity.Admin;
import net.shopxx.entity.DepositLog;
import net.shopxx.entity.Member;
import net.shopxx.entity.MemberRank;
import net.shopxx.entity.PointLog;
import net.shopxx.service.MailService;
import net.shopxx.service.MemberService;
import net.shopxx.service.SmsService;
import net.shopxx.util.SystemUtils;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Service - 会员
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("memberServiceImpl")
public class MemberServiceImpl extends BaseServiceImpl<Member, Long> implements MemberService {

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;
	@Resource(name = "depositLogDaoImpl")
	private DepositLogDao depositLogDao;
	@Resource(name = "pointLogDaoImpl")
	private PointLogDao pointLogDao;
	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Resource(name = "smsServiceImpl")
	private SmsService smsService;

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return memberDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public boolean usernameDisabled(String username) {
		Assert.hasText(username);

		Setting setting = SystemUtils.getSetting();
		if (setting.getDisabledUsernames() != null) {
			for (String disabledUsername : setting.getDisabledUsernames()) {
				if (StringUtils.containsIgnoreCase(username, disabledUsername)) {
					return true;
				}
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return memberDao.emailExists(email);
	}

	@Transactional(readOnly = true)
	public boolean emailUnique(String previousEmail, String currentEmail) {
		if (StringUtils.equalsIgnoreCase(previousEmail, currentEmail)) {
			return true;
		}
		return !memberDao.emailExists(currentEmail);
	}

	@Transactional(readOnly = true)
	public Member find(String loginPluginId, String openId) {
		return memberDao.find(loginPluginId, openId);
	}

	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		return memberDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<Member> findListByEmail(String email) {
		return memberDao.findListByEmail(email);
	}

	@Transactional(readOnly = true)
	public Page<Member> findPage(Member.RankingType rankingType, Pageable pageable) {
		return memberDao.findPage(rankingType, pageable);
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		return requestAttributes != null && requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) != null;
	}

	@Transactional(readOnly = true)
	public Member getCurrent() {
		return getCurrent(false);
	}

	@Transactional(readOnly = true)
	public Member getCurrent(boolean lock) {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		Long id = principal != null ? principal.getId() : null;
		if (lock) {
			return memberDao.find(id, LockModeType.PESSIMISTIC_WRITE);
		} else {
			return memberDao.find(id);
		}
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION) : null;
		return principal != null ? principal.getUsername() : null;
	}

	public void addBalance(Member member, BigDecimal amount, DepositLog.Type type, Admin operator, String memo) {
		Assert.notNull(member);
		Assert.notNull(amount);
		Assert.notNull(type);

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(memberDao.getLockMode(member))) {
			memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(member.getBalance());
		Assert.state(member.getBalance().add(amount).compareTo(BigDecimal.ZERO) >= 0);

		member.setBalance(member.getBalance().add(amount));
		memberDao.flush();

		DepositLog depositLog = new DepositLog();
		depositLog.setType(type);
		depositLog.setCredit(amount.compareTo(BigDecimal.ZERO) > 0 ? amount : BigDecimal.ZERO);
		depositLog.setDebit(amount.compareTo(BigDecimal.ZERO) < 0 ? amount.abs() : BigDecimal.ZERO);
		depositLog.setBalance(member.getBalance());
		depositLog.setOperator(operator);
		depositLog.setMemo(memo);
		depositLog.setMember(member);
		depositLogDao.persist(depositLog);
	}

	public void addPoint(Member member, long amount, PointLog.Type type, Admin operator, String memo) {
		Assert.notNull(member);
		Assert.notNull(type);

		if (amount == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(memberDao.getLockMode(member))) {
			memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(member.getPoint());
		Assert.state(member.getPoint() + amount >= 0);

		member.setPoint(member.getPoint() + amount);
		memberDao.flush();

		PointLog pointLog = new PointLog();
		pointLog.setType(type);
		pointLog.setCredit(amount > 0 ? amount : 0L);
		pointLog.setDebit(amount < 0 ? Math.abs(amount) : 0L);
		pointLog.setBalance(member.getPoint());
		pointLog.setOperator(operator);
		pointLog.setMemo(memo);
		pointLog.setMember(member);
		pointLogDao.persist(pointLog);
	}

	public void addAmount(Member member, BigDecimal amount) {
		Assert.notNull(member);
		Assert.notNull(amount);

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		if (!LockModeType.PESSIMISTIC_WRITE.equals(memberDao.getLockMode(member))) {
			memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		}

		Assert.notNull(member.getAmount());
		Assert.state(member.getAmount().add(amount).compareTo(BigDecimal.ZERO) >= 0);

		member.setAmount(member.getAmount().add(amount));
		MemberRank memberRank = member.getMemberRank();
		if (memberRank != null && BooleanUtils.isFalse(memberRank.getIsSpecial())) {
			MemberRank newMemberRank = memberRankDao.findByAmount(member.getAmount());
			if (newMemberRank != null && newMemberRank.getAmount() != null && newMemberRank.getAmount().compareTo(memberRank.getAmount()) > 0) {
				member.setMemberRank(newMemberRank);
			}
		}
		memberDao.flush();
	}

	@Override
	@Transactional
	public Member save(Member member) {
		Assert.notNull(member);

		Member pMember = super.save(member);
		mailService.sendRegisterMemberMail(pMember);
		smsService.sendRegisterMemberSms(pMember);
		return pMember;
	}

}