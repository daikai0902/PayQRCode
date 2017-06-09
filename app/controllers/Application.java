package controllers;

import cn.bran.play.JapidController;
import models.ResultVO;


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
        renderJSON(ResultVO.succeed());
    }

    public static void payPage(){
        renderJapid();
    }
}