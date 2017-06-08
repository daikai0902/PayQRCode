package models;

import java.util.List;

/**
 * Created by daikai on 2017/6/8.
 */
public class UserResult extends Result {

    public List<Clazzinfo> classList;

    public UserInfo userInfo;


    public class Clazzinfo{

        public String acadyear;

        public int graduate;

        public String id;

        public String name;

        public  String schoolId;

        public int schoolingLength;

        public int section;

        public int type;

        public int updatestampSp;

        public int updatestampT;
    }

    public class  UserInfo{

        public String avatarUrl;

        public Long sequence;

        public String openId;

        public  Long moreUpdatestamp;

        public int ownerType;

        public String realName;

        public int sex;

        public Long updatestamp;
    }
}
