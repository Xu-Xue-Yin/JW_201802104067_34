//信管182 徐学印 201802104067
package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Service.DepartmentService;
import com.example.demo.domain.Department;
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
public class DepartmentController {
    @RequestMapping(value = "/department.ctl",method = RequestMethod.GET)
    public String List(@RequestParam(value = "id",required = false) String id_str){
        JSONObject message = new JSONObject();
        try{
            if (id_str == null){
                return responseDepartments();
            }else {
                return responseDepartment(Integer.parseInt(id_str));
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            return message.toString();
        } catch (Exception e){
            message.put("message","网络异常");
            return message.toString();
        }
    }
    @RequestMapping(value = "/department.ctl",method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String department_json = JSONUtil.getJSON(request);
        Department departmentToAdd = JSON.parseObject(department_json,Department.class);
        try {
            boolean result = DepartmentService.getInstance().add(departmentToAdd);
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
    @RequestMapping(value = "/department.ctl",method = RequestMethod.DELETE)
    public JSONObject delete(@RequestParam(value = "id",required = false) String id_str) throws IOException {
        JSONObject message = new JSONObject();
        int id = Integer.parseInt(id_str);
        try {
            boolean result = DepartmentService.getInstance().delete(id);
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
    @RequestMapping(value = "/department.ctl",method = RequestMethod.PUT)
    public JSONObject update(HttpServletRequest request) throws IOException {
        JSONObject message = new JSONObject();
        String department_json = JSONUtil.getJSON(request);
        Department departmentToAdd = JSON.parseObject(department_json,Department.class);
        try {
            boolean result = DepartmentService.getInstance().update(departmentToAdd);
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

    private String responseDepartment(int id) throws SQLException {
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);
        return department_json;
    }
    private String responseDepartments() throws SQLException {
        Collection<Department> departments = DepartmentService.getInstance().getAll();
        String departments_json = JSON.toJSONString(departments,SerializerFeature.DisableCircularReferenceDetect);
        return departments_json;
    }
}

