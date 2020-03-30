package com.travel.dao.impl;

import com.travel.dao.RouteDao;
import com.travel.domain.Route;
import com.travel.utils.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Summerday
 */
public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 计算总记录数
     * @param cid cid
     * @param rname
     * @return
     */
    @Override
    public int findTotalCount(int cid, String rname) {
        //String sql = "select count(*) from tab_route where cid = ?";

        //定义sql模板
        String sql = "select count(*) from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder();

        //条件们
        List params = new ArrayList();
        //判断参数是否有值

        //路线编号
        if(cid!=0){
            sb.append("and cid = ? ");
            //添加?对应的值
            params.add(cid);
        }
        //路线名称
        if(rname!=null&&rname.length()>0){

            sb.append("and rname like ? ");
            params.add("%"+rname+"%");
        }
        sql += sb.toString();
        //返回一个值,用forObject
        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    /**
     * 查询每页的数据信息
     * @param cid
     * @param start
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname ) {
        //String sql = "select * from tab_route where cid = ? limit ? , ?";

        String sql = " select * from tab_route where 1 = 1 ";

        StringBuilder sb = new StringBuilder();

        //条件们
        List params = new ArrayList();
        //判断参数是否有值

        if(cid!=0){
            sb.append("and cid = ? ");
            //添加?对应的值
            params.add(cid);
        }
        if(rname!=null&&rname.length()>0){

            sb.append("and rname like ?");
            params.add("%"+rname+"%");
        }
        //分页
        sb.append("limit ? , ? ");
        sql += sb.toString();

        params.add(start);
        params.add(pageSize);
        return template.query(sql,new BeanPropertyRowMapper<>(Route.class),params.toArray());
    }

    @Override
    public Route findByRid(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<>(Route.class),rid);
    }
}
