package controllers;

import cn.bran.play.JapidController;
import com.google.gson.JsonObject;
import models.*;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
import utils.AuthUtils;

import java.net.URLEncoder;
import java.util.List;


public class Application extends JapidController {

    public static void index(String token) throws Exception {

        UserResult userResult = AuthUtils.getUserInfoNoAuth(token);
        if(userResult != null){
            String schoolName = "";
            if(userResult.userInfo != null){//学生
                List<UserResult.Clazzinfo> clazzs = userResult.classList;
                if (clazzs != null && clazzs.size() > 0) {
                    String schoolId = clazzs.get(0).schoolId;
                    SchoolResult schoolResult = AuthUtils.getSchoolInfoNoAuth(token,schoolId);
                    if (schoolResult != null){
                        schoolName = schoolResult.schoolInfo.schoolName;
                    }
                }
                StringBuffer sb = new StringBuffer();
                sb.append("https://yxt.ngb.abchina.com/login_m2.aspx?");
                sb.append("id=").append(userResult.userInfo.sequence);
                sb.append("&name=").append(URLEncoder.encode(userResult.userInfo.realName,"utf-8"));
                sb.append("&school=").append(URLEncoder.encode(schoolName,"utf-8"));
                redirect(sb.toString(),true);
            }
        }
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
            if(userResult.userInfo != null && userResult.userInfo.ownerType == 1){//学生
                List<UserResult.Clazzinfo> clazzs = userResult.classList;
                if (clazzs != null && clazzs.size() > 0) {
                    String schoolId = clazzs.get(0).schoolId;
                    SchoolResult schoolResult = AuthUtils.getSchoolInfo(authResult.accessToken,schoolId);
                    if (schoolResult != null){
                        schoolName = schoolResult.schoolInfo.schoolName;
                    }
                }
                StringBuffer sb = new StringBuffer();
                sb.append("https://yxt.ngb.abchina.com/login_m2.aspx?");
                sb.append("id=").append(userResult.userInfo.sequence);
                sb.append("&name=").append(URLEncoder.encode(userResult.userInfo.realName,"utf-8"));
                sb.append("&school=").append(URLEncoder.encode(schoolName,"utf-8"));
                sb.append("&ip=").append(Http.Request.current().remoteAddress);
                redirect(sb.toString(),true);
            }

            if(userResult.userInfo != null && userResult.userInfo.ownerType == 3) {//家长
            }

            Logger.info("用户角色不对:"+userResult.userInfo.ownerType);
            renderJSON(ResultVO.failed("用户角色不对"));
        }else{
            Logger.info("用户信息返回为空！");
            renderJSON(ResultVO.failed("没有获取到用户信息"));
        }

    }

    public static void payPage(String schoolName){
        School school = School.findByName(schoolName);
        renderJapid(school,schoolName);
    }
}