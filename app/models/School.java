package models;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by daikai on 2017/6/7.
 */

@Entity
public class School extends BaseModel{

    public String name;

    public String merchantId;

    public String qrcode;

    public static School add(String name,String merchantId,String qrcode){
        School school = new School();
        school.name = name;
        school.merchantId = merchantId;
        school.qrcode = qrcode;
        return school.save();
    }

    public void editSchool(String merchantId,String qrcode){
        this.merchantId = merchantId;
        this.qrcode = qrcode;
        this.save();
    }

    public static School findByName(String name){
        return School.find(getDefaultContitionSql( " name = ? "),name).first();
    }

   public static  List<School> fetchAll(){
        return find("select s from School s where s.isDeleted = 0 ").fetch();
   }
}
