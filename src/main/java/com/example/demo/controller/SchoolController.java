//信管182 徐学印 201802104067
package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Service.SchoolService;
import com.example.demo.domain.School;
import com.example.demo.util.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class SchoolController {
    @RequestMapping(value = "/school.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str){
        JSONObject message = new JSONObject();
        try{
            if (id_str == null){
                return responseSchools();
            }else {
                return responseSchool(Integer.parseInt(id_str));
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            return message.toString();
        } catch (Exception e){
            message.put("message","网络异常");
            return message.toString();
        }
    }
    @RequestMapping(value = "/school.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String school_json = JSONUtil.getJSON(request);
        School schoolToAdd = JSON.parseObject(school_json,School.class);
        try {
            boolean result = SchoolService.getInstance().add(schoolToAdd);
            if (result){
                message.put("message","添加成功");
            }else {
                message.put("message","添加失败");
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
        } catch (Exception e){
            message.put("message","网络异常");
        }
        return message;
    }
    @RequestMapping(value = "/school.ctl",method = RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false) String id_str) throws IOException {
        JSONObject message = new JSONObject();
        int id = Integer.parseInt(id_str);
        try {
            boolean result = SchoolService.getInstance().delete(id);
            if (result){
                message.put("message","删除成功");
            }else {
                message.put("message","删除失败");
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
        } catch (Exception e){
            message.put("message","网络异常");
        }
        return message;
    }
    @RequestMapping(value = "/school.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String school_json = JSONUtil.getJSON(request);
        School schoolToAdd = JSON.parseObject(school_json,School.class);
        try {
            boolean result = SchoolService.getInstance().update(schoolToAdd);
            if (result){
                message.put("message","修改成功");
            }else {
                message.put("message","修改失败");
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
        } catch (Exception e){
            message.put("message","网络异常");
        }
        return message;
    }

    private String responseSchool(int id) throws SQLException {
        School school = SchoolService.getInstance().find(id);
        String school_json = JSON.toJSONString(school);
        return school_json;
    }
    private String responseSchools() throws SQLException {
        Collection<School> schools = SchoolService.getInstance().findAll();
        String schools_json = JSON.toJSONString(schools, SerializerFeature.DisableCircularReferenceDetect);
        return schools_json;
    }
}
