//信管182 徐学印 201802104067
package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Service.UserService;
import com.example.demo.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class LoginController {
    @RequestMapping(value = "/login.ctl", method = RequestMethod.POST)
    public JSONObject login(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request) throws SQLException {
        JSONObject message = new JSONObject();
        try {
            User loggedUser = UserService.getInstance().login(username, password);
            if (loggedUser != null){
                message.put("message", "登陆成功");
                HttpSession session = request.getSession();
                //十分钟内没有任何操作,则Session失效
                session.setMaxInactiveInterval(600);
                session.setAttribute("currentUser", loggedUser);
            }else
                message.put("message", "用户名或者密码错误");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch (Exception e){
            message.put("message", "其他异常");
        }
        return message;
    }
}

