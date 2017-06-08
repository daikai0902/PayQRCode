package controllers;

import cn.bran.play.JapidController;
import play.mvc.*;



public class Application extends JapidController {

    public static void index() {
        renderJapid();
    }

}