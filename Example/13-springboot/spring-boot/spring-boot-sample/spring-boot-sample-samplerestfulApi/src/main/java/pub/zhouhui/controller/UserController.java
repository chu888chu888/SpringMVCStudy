package pub.zhouhui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import pub.zhouhui.dao.User;
import pub.zhouhui.domin.UserRepository;

/**
 * Created by shane on 2016/11/1.
 */
@RestController
@RequestMapping(value="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/findall",method= RequestMethod.GET)
    public List<User> findall(){
        List<User> alluser = userRepository.findAll();
        return alluser;
    }
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public User getUser(@PathVariable String id) {
        // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
        // url中的id可通过@PathVariable绑定到函数的参数中
        return userRepository.findByName(id);
    }
    @RequestMapping(value="/add", method=RequestMethod.POST)
    @ResponseBody
    public String addUser(@RequestBody User jsonObj){
        User user = new User();
        user.setId(jsonObj.getId());
        user.setName(jsonObj.getName());
        user.setAge(jsonObj.getAge());
        userRepository.save(user);
        return "success";
    }

    @RequestMapping(value="/update/{id}", method=RequestMethod.POST)
    @ResponseBody
    public String updateUser(@PathVariable long id,  @RequestBody User jsonObj){
        User user = new User();
        user.setId(id);
        user.setName(jsonObj.getName());
        user.setAge(jsonObj.getAge());
        userRepository.save(user);
        return "success";
    }
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public String deldeteUser(@PathVariable long id) {
        // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
        // url中的id可通过@PathVariable绑定到函数的参数中
        userRepository.delete(id);
        return "success";
    }
}
