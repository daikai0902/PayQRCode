package controllers;

import cn.bran.play.JapidController;
import models.ResultVO;


public class Application extends JapidController {

    public static void index() {
        renderJapid();
    }

    /**
     * 回调接受token
     * @param token
     */
    public static void notifyAccessToken(String token){
        System.err.println(token);
        renderJSON(ResultVO.succeed());
    }
}