package com.travel.service.impl;

import com.travel.dao.FavoriteDao;
import com.travel.dao.RouteDao;
import com.travel.dao.RouteImgDao;
import com.travel.dao.impl.FavoriteDaoImpl;
import com.travel.dao.impl.RouteDaoImpl;
import com.travel.dao.impl.RouteImgDaoImpl;
import com.travel.domain.Favorite;
import com.travel.domain.PageBean;
import com.travel.domain.Route;
import com.travel.domain.RouteImg;
import com.travel.service.FavoriteService;
import org.springframework.util.RouteMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Summerday
 */
public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();

    @Override
    public boolean isFavorite(String rid, int uid) {
        Favorite f = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);
        //有值代表收藏,true
        return f!=null;
    }

    @Override
    public void add(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid),uid);
    }

    @Override
    public PageBean<Route> pageFavorite(int uid, int currentPage, int pageSize) {
        //封装pagebean
        PageBean<Route> pb = new PageBean<>();

        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置pageSize
        pb.setPageSize(pageSize);

        //调用favoritedao获取总记录数
        int totalCount = favoriteDao.findTotalCountByUid(uid);
        pb.setTotalCount(totalCount);

        int start = (currentPage-1)*pageSize;
        List<Favorite> favoriteList = favoriteDao.findByPage(uid, start, pageSize);
        //[[favorite1,f2,f3]  遍历list，获取f.getrid,routedao.findbyrid,设置到pagebean中
        List<Route> routeList = new ArrayList<>();
        Route route;
        for (Favorite favorite : favoriteList) {

            Integer rid = favorite.getRid();
            //根据rid寻找route
            route = routeDao.findByRid(rid);
            //根据route找到imglist
            //List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
            //route.setRouteImgList(routeImgList);
            routeList.add(route);
        }
        pb.setList(routeList);
        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }
}
