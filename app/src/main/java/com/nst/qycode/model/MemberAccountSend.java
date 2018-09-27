package com.nst.qycode.model;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/20 下午3:02
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MemberAccountSend extends DataSend {
    public MemberAccount MemberAccountModel = new MemberAccount();
    public class MemberAccount {
        public String Tel;
        public String Password;
        public String WithdrawPass;
    }
}
