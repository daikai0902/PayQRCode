package controllers;

import cn.bran.play.JapidController;
import models.ResultVO;
import models.School;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by daikai on 2017/6/7.
 */
public class BackController extends JapidController {

    public static final String URL_TEMPLATE = "http://static.daily.heyean.com/caibao/school.html?#/Home/";

    public static void index(){
        List<School> schools = School.fetchAll();
        renderJapid(schools);
    }


    public static void addSchool(String name,String merchantId){
        School.add(name,merchantId,URL_TEMPLATE+merchantId);
        renderJSON(ResultVO.succeed());
    }

    public static void editSchool(long schoolId,String merchantId){
        School school = School.findById(schoolId);
        if(StringUtils.equals(school.merchantId,merchantId)){
            renderJSON(ResultVO.succeed());
        }
        school.editSchool(merchantId,URL_TEMPLATE+merchantId);
        renderJSON(ResultVO.succeed());
    }

}
