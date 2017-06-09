package utils;

import com.google.gson.Gson;
import models.AuthResult;
import models.SchoolResult;
import models.UserResult;
import org.apache.commons.codec.digest.DigestUtils;
import play.libs.WS;

/**
 *
 *  授权工具类
 * Created by daikai on 2017/6/8.
 */
public class AuthUtils {

    private static final String GET_ACCESS_TOKEN = "http://oauth.weike.wanpeng.com/api/access_token.json";

    private static final  String REFRESH_ACCESS_TOKEN = "http://oauth.weike.wanpeng.com/api/refresh_token.json";

    private static final  String APP_ID = "4028819A5C856C99015C8618549E25CA";

    private static final  String APP_SECRET = "wk_zjjf";

    private static final String GET_USER_INFO = "http://openapi.weike.wanpeng.com/user/userInfo.json";

    private static final String GET_SCHOOL_INFO = "http://openapi.weike.wanpeng.com/user/schoolInfo.json";

    /**
     * 获取access_token
     * @param token
     * @return
     */
    public static AuthResult getAccessToken(String token){
        Long salt = System.currentTimeMillis();
        String secret = DigestUtils.md5Hex(salt+APP_SECRET);
        WS.HttpResponse response =  WS.url(GET_ACCESS_TOKEN).setParameter("appId",APP_ID).setParameter("appSecret",secret)
                .setParameter("authorizationCode",token).setParameter("Salt",salt).post();
        System.err.println(response.getJson().toString());
        return  new Gson().fromJson(response.getJson(),AuthResult.class);
    }


    /**
     * 刷新获取access_token
     * @param refreshToken
     * @return
     */
    public static AuthResult refreshToken(String refreshToken){
        WS.HttpResponse response =  WS.url(REFRESH_ACCESS_TOKEN).setParameter("refreshToken",refreshToken).post();
        return  new Gson().fromJson(response.getJson(),AuthResult.class);
    }


    /**
     * 获取学校信息
     * @param accessToken
     * @param schoolId
     * @return
     */
    public static SchoolResult getSchoolInfo(String accessToken,String schoolId){
        Long salt = System.currentTimeMillis();
        String securityKey = DigestUtils.md5Hex(accessToken+salt+APP_SECRET);
        WS.HttpResponse response =  WS.url(GET_SCHOOL_INFO).setParameter("accessToken",accessToken)
                .setParameter("appId",APP_ID).setParameter("salt",salt).setParameter("securityKey",securityKey)
                .setParameter("schoolId",schoolId).post();
        System.err.println(response.getJson().toString());
        return new Gson().fromJson(response.getJson(),SchoolResult.class);

    }


    /**
     * 获取用户基本信息
     * @param accessToken
     * @return
     */
    public static UserResult getUserInfo(String accessToken){
        Long salt = System.currentTimeMillis();
        String securityKey = DigestUtils.md5Hex(accessToken+salt+APP_SECRET);
        int needClass = 1;
        WS.HttpResponse response = WS.url(GET_USER_INFO).setParameter("accessToken",accessToken).setParameter("appId",APP_ID)
                .setParameter("salt",salt).setParameter("securityKey",securityKey).setParameter("needClass",needClass).post();
       System.err.println(response.getJson().toString());
        return  new Gson().fromJson(response.getJson(),UserResult.class);
    }




}
