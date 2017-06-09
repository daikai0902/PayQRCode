package controllers;

import cn.bran.play.JapidController;
import models.AuthResult;
import models.ResultVO;
import models.SchoolResult;
import models.UserResult;
import org.apache.commons.lang.StringUtils;
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
        System.err.println(authorizationCode);
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
                SchoolResult schoolResult = AuthUtils.getSchoolInfo(authorizationCode,schoolId);
                if (schoolResult != null){
                    schoolName = schoolResult.schoolInfo.schoolName;
                }
            }
        }
        renderJSON(ResultVO.succeed(schoolName));
    }

    public static void payPage(){
        renderJapid();
    }
}