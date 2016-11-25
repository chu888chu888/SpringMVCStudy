package com.chu.controller;

import com.chu.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by P70 on 2016/11/10.
 */

@RestController
@EnableAutoConfiguration
public class IndexController {

    @Value(value="${chu.secret}")
    private String SecretValue;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "hello world!";
    }

    @RequestMapping(value = "/getMapModel/{MapID}/{MapValue}",method = RequestMethod.GET)
    public Map getMapModel(@PathVariable String MapID,@PathVariable String MapValue)
    {
        Map<String,String> MapCollection=new HashMap<String,String>();
        MapCollection.put("MapID",MapID);
        MapCollection.put("MapValue",MapValue);
        MapCollection.put("MapSecretValue",SecretValue);
        return MapCollection;

    }

    @RequestMapping(value = "/getUserModel/{mapid}/{name}", method = RequestMethod.GET)
    public User getUserModel(@PathVariable int mapid, @PathVariable String name) {

        User u=new User();
        u.setDate(new Date());
        u.setId(mapid);
        u.setName(name);

        return u;

    }
}
