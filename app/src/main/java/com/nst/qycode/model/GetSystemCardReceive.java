package com.nst.qycode.model;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/1 上午9:57
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GetSystemCardReceive extends DataReceive {

    public SystemCardInfo SystemCardInfo;
    public static class SystemCardInfo {
        /**
         * BankName : 工商银行
         * BankAlias : 工行1号
         * AccountName : 毛凤荣
         * AccountNumber : 6215 5932 0200 615 4911
         */

        public String BankName;
        public String BankAlias;
        public String AccountName;
        public String AccountNumber;
    }
}
