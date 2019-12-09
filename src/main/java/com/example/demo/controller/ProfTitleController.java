//信管182 徐学印 201802104067
package com.example.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Service.ProfTitleService;
import com.example.demo.domain.ProfTitle;
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
public class ProfTitleController {
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str){
        JSONObject message = new JSONObject();
        try{
            if (id_str == null){
                return responseProfTitles();
            }else {
                return responseProfTitle(Integer.parseInt(id_str));
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            return message.toString();
        } catch (Exception e){
            message.put("message","网络异常");
            return message.toString();
        }
    }
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String profTitle_json = JSONUtil.getJSON(request);
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json,ProfTitle.class);
        try {
            boolean result = ProfTitleService.getInstance().add(profTitleToAdd);
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
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false) String id_str) throws IOException {
        JSONObject message = new JSONObject();
        int id = Integer.parseInt(id_str);
        try {
            boolean result = ProfTitleService.getInstance().delete(id);
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
    @RequestMapping(value = "/profTitle.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String profTitle_json = JSONUtil.getJSON(request);
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json,ProfTitle.class);
        try {
            boolean result = ProfTitleService.getInstance().update(profTitleToAdd);
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

    private String responseProfTitle(int id) throws SQLException {
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);
        return profTitle_json;
    }
    private String responseProfTitles() throws SQLException {
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
        String profTitles_json = JSON.toJSONString(profTitles, SerializerFeature.DisableCircularReferenceDetect);
        return profTitles_json;
    }
}
