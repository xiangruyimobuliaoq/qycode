package com.nst.qycode.model;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/25 上午10:40
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MemberWithdrawSend extends DataSend {
    public MemberWithdraw MemberWithdrawModel = new MemberWithdraw();

    public class MemberWithdraw {
        public String BankName;
        public String AccountNumber;
        public String TrueName;
        public double WithdrawMoney;
        public String WithdrawPass;
    }
}
