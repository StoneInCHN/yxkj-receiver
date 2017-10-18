package com.yxkj.commonenum;

public class CommonEnum {

		public enum CmdStatus {
				/**
				 * 发出指令
				 */
				SendOut,
				/**
				 * 执行完成
				 */
				Finished
		}

		/**
   * 命令类型
   */
  public enum CmdType {
    /**
     * 出货
     */
    SELL_OUT,
    /**
     * APP升级
     */
    APP_UPDATE,
    /**
     * 广告更新
     */
    AD_UPDATE,
    /**
     * 音量设置
     */
    VOLUME,
    /**
     * 支付成功指令
     */
    PAYMENT_SUCCESS
  }
}
