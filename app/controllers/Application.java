package controllers;

import cn.bran.play.JapidController;
import models.*;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import utils.AuthUtils;

import java.util.List;


public class Application extends JapidController {

    public static void index() {
        renderJapid();
    }

    /**
     * 回调接受token
     * @param authorizationCode
     */
    public static void notifyAccessToken(String authorizationCode){
        if(StringUtils.isBlank(authorizationCode)){
            renderJSON(ResultVO.failed("没有获取到accessToken"));
        }
        AuthResult authResult = AuthUtils.getAccessToken(authorizationCode);
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
        }
        Logger.info("学校名称是:"+schoolName);
        payPage(schoolName);
    }

    public static void payPage(String schoolName){
        School school = School.findByName(schoolName);
        renderJapid(school);
    }
}