package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

    private static JSONObject area = new JSONObject();
    private static JSONObject sqlLibs = new JSONObject();

    static {
        String insert = "insert into t_order (price, user_id,status) values(#{price}, #{userId}, #{status})"; //insert
        String delete = "delete from t_order where order_id in" +
                "<foreach item=\"item\" index=\"index\" collection=\"ids\"\n" +
                "      open=\"(\" separator=\",\" close=\")\">\n" +
                "        #{item}\n" +
                "</foreach>"; //delete
        String update = "update t_order set price = #{price} where order_id = #{id}";  //update
        String select = "select * from t_order where order_id = #{id}"; //selectList
        area.put("add", insert);
        area.put("delete", delete);
        area.put("update", update);
        area.put("select", select);
        sqlLibs.put("area", area);
    }

    @PostMapping("/{table}/add")
    public Object add(@PathVariable("table") String table, @RequestBody JSONObject request) {

        final Map<String, Object> wrapper = request.getJSONObject("wrapper");
        String sql = sqlLibs.getJSONObject(table).getString("add");
        return sqlMapper.delete("<script>" + sql +"</script>", wrapper);
    }

    @DeleteMapping("/{table}/delete/{ids}")
    public Object delete(@PathVariable("table") String table, @PathVariable("ids") String ids, @RequestBody JSONObject request) {

        Map<String, Object> wrapper = request.getJSONObject("wrapper");
        String[] idsS = ids.split(",");
        Long[] idsI = new Long[idsS.length];
        for (int i = 0; i < idsS.length; i++) {
            idsI[i] = Long.valueOf(idsS[i]);
        }
        wrapper.put("ids", idsI);
        String sql = sqlLibs.getJSONObject(table).getString("delete");
        return sqlMapper.delete("<script>" + sql +"</script>", wrapper);
    }

}
