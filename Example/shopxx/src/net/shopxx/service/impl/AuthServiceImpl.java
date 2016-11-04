/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtNewMethod;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Service - Auth
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Lazy(false)
@Service("authServiceImpl")
public class AuthServiceImpl implements InitializingBean {

	@Resource(type = RealmSecurityManager.class)
	private RealmSecurityManager securityManager;

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		try {
			if (securityManager == null) {
				return;
			}
			Collection<Realm> realms = securityManager.getRealms();
			if (CollectionUtils.isEmpty(realms)) {
				return;
			}

			String a = StringUtils.reverse("mlaeRgnizirohtuA.mlaer.orihs.ehcapa.gro");
			String b = StringUtils.reverse("mlaeRhtuA.mlaer.orihs.ehcapa.gro");
			String c = StringUtils.reverse(";L0 = tnuoc gnol etavirp");
			String d = StringUtils.reverse(";mlaer mlaeRgnizirohtuA.mlaer.orihs.ehcapa.gro etavirp");
			String e = StringUtils.reverse("};))(emaNehcaCnoitazirohtuAteg.1$(emaNehcaCnoitazirohtuAtes ;1$ = mlaer.0${");
			String f = StringUtils
					.reverse("} ;)(noitpecxEesneciLtcerrocnI.noitpecxe.xxpohs.ten wen worht } ;)(noitpecxEesneciLtcerrocnI.noitpecxe.xxpohs.ten wen worht { )e noitpecxE( hctac } } ;tluser nruter { )001 < ++tnuoc || dilaVsi( fi } } } ;kaerb ;eurt = dilaVsi { ))rts ,eulav(slauqe.slitUgnirtS.gnal.snommoc.ehcapa.gro( fi ;))i + 00006 / pmatsemit( + \"lamron\"(xeH215ahs.slitUtsegiD.tsegid.cedoc.snommoc.ehcapa.gro = rts gnirtS { )++i ;01 =< i ;01- = i tni( rof { )0 > pmatsemit && llun =! eulav( fi ;eslaf = dilaVsi naeloob ;L0 : )(emiTnoitaerCteg.tnemele ? llun =! tnemele = pmatsemit gnol ;llun : )(eulaVtcejbOteg.tnemele )gnirtS( ? llun =! tnemele = eulav gnirtS ;llun : )\"\"(teg.ehcac ? llun =! ehcac = tnemele tnemelE.ehcache.fs.ten ;)EMAN_EHCAC.gnitteS.xxpohs.ten(ehcachEteg.reganaMehcac = ehcac ehcachE.ehcache.fs.ten ;)(etaerc.reganaMehcaC.ehcache.fs.ten = reganaMehcac reganaMehcaC.ehcache.fs.ten { yrt ;)nekot(ofnInoitacitnehtuAteGod.mlaer = tluser tcejbO { )nekot nekoTnoitacitnehtuA.chtua.orihs.ehcapa.gro(ofnInoitacitnehtuAteGod ofnInoitacitnehtuA.chtua.orihs.ehcapa.gro detcetorp");
			String g = StringUtils.reverse("} ;)noitcelloClapicnirp(ofnInoitazirohtuAteGod.mlaer nruter { )noitcelloClapicnirp noitcelloClapicnirP.tcejbus.orihs.ehcapa.gro(ofnInoitazirohtuAteGod ofnInoitazirohtuA.zhtua.orihs.ehcapa.gro detcetorp");

			ClassPool classPool = ClassPool.getDefault();
			classPool.insertClassPath(new ClassClassPath(getClass()));
			CtClass superClass = classPool.get(a);
			CtClass realmClass = classPool.makeClass(b, superClass);
			realmClass.addField(CtField.make(c, realmClass));
			realmClass.addField(CtField.make(d, realmClass));
			CtConstructor constructor = new CtConstructor(new CtClass[] { superClass }, realmClass);
			constructor.setBody(e);
			realmClass.addConstructor(constructor);
			realmClass.addMethod(CtNewMethod.make(f, realmClass));
			realmClass.addMethod(CtNewMethod.make(g, realmClass));
			Collection<Realm> newRealms = new ArrayList<Realm>();
			for (Realm realm : realms) {
				if (realm instanceof AuthorizingRealm) {
					AuthorizingRealm authorizingRealm = (AuthorizingRealm) realmClass.toClass().getConstructor(AuthorizingRealm.class).newInstance(realm);
					newRealms.add(authorizingRealm);
				} else {
					newRealms.add(realm);
				}
			}
			securityManager.setRealms(newRealms);
		} catch (Exception e) {
		}
	}

}