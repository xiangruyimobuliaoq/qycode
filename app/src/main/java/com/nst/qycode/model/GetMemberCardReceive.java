package com.nst.qycode.model;

import java.util.List;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/1 上午10:17
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GetMemberCardReceive extends DataReceive {

    public List<MemberCardInfo> MemberCardInfo;

    public static class MemberCardInfo {
        /**
         * UserName : pl8236448
         * BankName : 中国银行
         * AccountNumber : 6222222233334444567
         */

        public String UserName;
        public String BankName;
        public String AccountNumber;
    }
}
