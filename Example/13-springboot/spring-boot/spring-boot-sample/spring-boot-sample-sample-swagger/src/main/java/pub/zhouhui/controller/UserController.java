package pub.zhouhui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value="获取用户列表", notes="")
    @RequestMapping(value="/findall",method= RequestMethod.GET)
    public List<User> findall(){
        List<User> alluser = userRepository.findAll();
        return alluser;
    }
    @ApiOperation(value="通过ID查找用户", notes="")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long")
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public User getUser(@PathVariable String id) {
        // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
        // url中的id可通过@PathVariable绑定到函数的参数中
        return userRepository.findByName(id);
    }
    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String addUser(@RequestBody User user){
        User users = new User();
        users.setId(user.getId());
        users.setName(user.getName());
        users.setAge(user.getAge());
        userRepository.save(users);
        return "success";
    }
    @ApiOperation(value="更新用户详细信息", notes="根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    })
    @RequestMapping(value="/update/{id}", method=RequestMethod.POST)
    public String updateUser(@PathVariable long id,  @RequestBody User user){
        User users = new User();
        users.setId(id);
        users.setName(user.getName());
        users.setAge(user.getAge());
        userRepository.save(users);
        return "success";
    }
    @ApiOperation(value="删除用户", notes="根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public String deldeteUser(@PathVariable long id) {
        // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
        // url中的id可通过@PathVariable绑定到函数的参数中
        userRepository.delete(id);
        return "success";
    }
}
