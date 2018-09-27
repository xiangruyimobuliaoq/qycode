package com.nst.qycode.model;

import com.nst.qycode.model.GetSystemCardReceive.SystemCardInfo;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/25 下午11:10
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MemberDepositSend extends DataSend {
    public MemberDeposit MemberDepositModel = new MemberDeposit();

    public static class MemberDeposit {
        public String DepositAccount;
        public double DepositMoney;
        public SystemCardInfo SystemCardInfo = new SystemCardInfo();
    }
}
