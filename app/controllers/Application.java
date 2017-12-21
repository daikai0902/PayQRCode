package controllers;

import cn.bran.play.JapidController;
import models.*;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.cache.Cache;
import utils.AuthUtils;

import java.net.URLEncoder;
import java.util.List;


public class Application extends JapidController {

    public static void index() {
        renderJapid();
    }

    /**
     * 回调接受token
     * @param authorizationCode
     */
    public static void notifyAccessToken(String authorizationCode) throws Exception{
        if(StringUtils.isBlank(authorizationCode)){
            renderJSON(ResultVO.failed("没有获取到accessToken"));
        }
        AuthResult authResult = AuthUtils.getAccessToken(authorizationCode);
        if(authResult == null){
            Logger.info("需要刷新accesstoken");
            authResult = AuthUtils.refreshToken((String) Cache.get("refreshkey"));
        }else{
            Cache.safeSet("refreshkey",authResult.refreshToken,"1d");
        }
        Logger.info("获取accessToken:"+authResult.accessToken+",,超时时间是:"+authResult.expiresIn);
        String schoolName = "";
        UserResult userResult = AuthUtils.getUserInfo(authResult.accessToken);
        if(userResult != null ){
            List<UserResult.Clazzinfo> clazzs = userResult.classList;
            if (clazzs != null && clazzs.size() > 0) {
                String schoolId = clazzs.get(0).schoolId;
                SchoolResult schoolResult = AuthUtils.getSchoolInfo(authResult.accessToken,schoolId);
                if (schoolResult != null){
                    schoolName = schoolResult.schoolInfo.schoolName;
                }
            }
        }else{
            Logger.info("用户信息返回为空！");
        }
        StringBuffer sb = new StringBuffer();
        sb.append("https://yxt.ngb.abchina.com/login_m2.aspx?");
        sb.append("id=").append(userResult.userInfo.sequence);
        sb.append("&name=").append(URLEncoder.encode(userResult.userInfo.realName,"utf-8"));
        sb.append("&school=").append(URLEncoder.encode(schoolName,"utf-8"));
        Logger.info("最终地址："+sb.toString());

        redirect(sb.toString(),true);
    }

    public static void payPage(String schoolName){
        School school = School.findByName(schoolName);
        renderJapid(school,schoolName);
    }
}