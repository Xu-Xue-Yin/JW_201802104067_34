//信管182 徐学印 201802104067
package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Service.DegreeService;
import com.example.demo.domain.Degree;
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
public class DegreeController {
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str){
        JSONObject message = new JSONObject();
        try{
            if (id_str == null){
                return responseDegrees();
            }else {
                return responseDegree(Integer.parseInt(id_str));
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            return message.toString();
        } catch (Exception e){
            message.put("message","网络异常");
            return message.toString();
        }
    }
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String degree_json = JSONUtil.getJSON(request);
        Degree degreeToAdd = JSON.parseObject(degree_json,Degree.class);
        try {
            boolean result = DegreeService.getInstance().add(degreeToAdd);
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
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false) String id_str) throws IOException {
        JSONObject message = new JSONObject();
        int id = Integer.parseInt(id_str);
        try {
            boolean result = DegreeService.getInstance().delete(id);
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
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String degree_json = JSONUtil.getJSON(request);
        Degree degreeToAdd = JSON.parseObject(degree_json,Degree.class);
        try {
            boolean result = DegreeService.getInstance().update(degreeToAdd);
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

    private String responseDegree(int id) throws SQLException {
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);
        return degree_json;
    }
    private String responseDegrees() throws SQLException {
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees,SerializerFeature.DisableCircularReferenceDetect);
        return degrees_json;
    }
}

