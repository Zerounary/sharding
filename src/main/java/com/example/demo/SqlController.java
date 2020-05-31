package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.abel533.sql.SqlMapper;
import com.sun.applet2.AppletParameters;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
//@RequestMapping("sql")
public class SqlController {
    @Autowired
    SqlMapper sqlMapper;
    @Autowired
    SqlSession sqlSession;

    private static JSONObject area = new JSONObject();
    private static JSONObject sqlLibs = new JSONObject();

    static {
        String insert = "insert into t_order (price, user_id,status) values(#{price}, #{userId}, #{status})"; //insert
        String deleteByIds = "delete from t_order where order_id in" +
                "<foreach item=\"item\" index=\"index\" collection=\"ids\"\n" +
                "      open=\"(\" separator=\",\" close=\")\">\n" +
                "        #{item}\n" +
                "</foreach>"; //deleteByIds
        String deleteById = "delete from t_other where other_id = #{id}";
        String update = "update t_order set price = #{price} where order_id = #{id}";  //update
        String selectOne = "select * from t_order where order_id = #{id}"; //selectList
        String selectList = "select * from t_order ${ew.customSqlSegment}"; //selectList
        area.put("add", insert);
        area.put("deleteById", deleteById);
        area.put("deleteByIds", deleteByIds);
        area.put("update", update);
        area.put("selectOne", selectOne);
        area.put("selectList", selectList);
        sqlLibs.put("area", area);
    }

    public Wrapper getFilterSql(Map<String,Object> filter){
        QueryWrapper<Object> wrapper = Wrappers.query();
        //检查
        for (String key:
                filter.keySet()) {
            if(!key.matches("qp-\\w+-(eq|ne|gt|ge|lt|le|between|notBetween|like|notLike|likeRight|isNull|isNotNull|in|notIn)")){
                System.out.println("key " + key + "not right");
                break;
            }
            if(key.endsWith("-eq")){
                wrapper.eq(key.split("-")[1], filter.get(key));
            }
        }
        return wrapper;
    }

    @GetMapping("/{table}/list")
    public Object selectList(@PathVariable("table") String table, @RequestParam Map<String,Object> filter){
        final Map<String, Object> wrapper = new HashMap<>();
        wrapper.put(Constants.WRAPPER,getFilterSql(filter));
        String sql = sqlLibs.getJSONObject(table).getString("selectList");
        return sqlMapper.selectList("<script>" + sql +"</script>", wrapper, PageMapper.class);
    }

    @GetMapping("/{table}/id/{id}")
    public Object selectOne(@PathVariable("table") String table,@PathVariable("id") String id){
        final Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("id", Long.valueOf(id));
        String sql = sqlLibs.getJSONObject(table).getString("selectOne");
        return sqlMapper.selectList("<script>" + sql +"</script>", wrapper);
    }

    @PostMapping("/{table}/add")
    public Object add(@PathVariable("table") String table, @RequestBody JSONObject request) {

        final Map<String, Object> wrapper = request.getJSONObject("wrapper");
        String sql = sqlLibs.getJSONObject(table).getString("add");
        return sqlMapper.delete("<script>" + sql +"</script>", wrapper);
    }

//    @DeleteMapping("/{table}/delete/{id}")
//    public Object deleteById(@PathVariable("table") String table, @PathVariable("id") String id, @RequestBody JSONObject request){
//        final Map<String, Object> wrapper = request.getJSONObject("wrapper");
//        wrapper.put("id", Long.valueOf(id));
//        String sql = sqlLibs.getJSONObject(table).getString("deleteById");
//        return sqlMapper.delete("<script>" + sql +"</script>", wrapper);
//    }

    @DeleteMapping("/{table}/delete/{ids}")
    public Object deleteByIds(@PathVariable("table") String table, @PathVariable("ids") String ids, @RequestBody JSONObject request) {

        Map<String, Object> wrapper = request.getJSONObject("wrapper");
        String[] idsS = ids.split(",");
        Long[] idsI = new Long[idsS.length];
        for (int i = 0; i < idsS.length; i++) {
            idsI[i] = Long.valueOf(idsS[i]);
        }
        wrapper.put("ids", idsI);
        String sql = sqlLibs.getJSONObject(table).getString("deleteByIds");
        return sqlMapper.delete("<script>" + sql +"</script>", wrapper);
    }

}
