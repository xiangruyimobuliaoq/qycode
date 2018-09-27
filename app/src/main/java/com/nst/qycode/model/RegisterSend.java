package com.nst.qycode.model;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/21 上午11:53
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RegisterSend extends DataSend {

    public Register RegisterModel = new Register();

    public class Register {
        public String TrueName;
        public String Password;
        public String Tel;
        public int HeaderImg;
        public String AgentCode;
    }

}
