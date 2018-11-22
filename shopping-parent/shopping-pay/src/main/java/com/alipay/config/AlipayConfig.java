package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016092000552477";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCE+uZeagSzbwMz3ehBwHaFYM+dpWurnXalRUst99aCTotSnbaPnsWRcSaYEjdlnK1OldvYoSvc+8ABZipFfBE0YSxhezTfGiI/t3q/LElAILozo9LJp1qVEyOtDunbleKWNGcu9Zff/rP6EfvcdfJCBUkz4QGWxzEPqJmR6AtxrtWB3kWHzSZB2FbtHIv9Gyls0h5XjoFixyJ2aOl8PPrGLmkaF5UEm7nz8dvPevHRKvXLCgGJZAsywEHfNgDEnc4Cf9Rg3GtXDQ/9xAcGW546j1gzuvFhn8OufuHyGoAq4IXhwyI4Eu5++3GqjTDJuB4FDlA9kqPthh8E2Wy5iygPAgMBAAECggEAeMyrJK1OY+m+kQlOiRxbDkKpl13D93vtYakW6EUxmeHb8krOopzqgHq3QoGZzTaMwMylj56ph+8xtpOmZsSTpG95r3wKxXiqEBOO78c9K4IPZAW33/Ok97MRP45CpnT8BtLHwQLc2mA4Ajz//H8weQTwQK5vkRaV3NGd38tZSgTb2LXpqvhXJJ/7Mp/pOvUNQcHlpwsrmN4O+I3/FPBiYI1YikrxaajDjVb/I9I9EbKjtrIOwYW5w0dpjS0OxZK5qsS26yjSkc18pBJe5N3NQ4G4vsS4I7dgQfs7AR7o2eRfSwo/Vix5MMZbs58EC5DhxBepw77ENWATMFasmj/eoQKBgQDvRGc1dPcPVIuDpn+nP1dQxuw1Hw5NQVJv8Oisy2GSEhpGdDv6PXfG4oaM76PGOMUb6/GH7sJldHfO9A09f4RZ79k8+UlgiGf9HSBDknEJrMlEVA3EDFfl9St5Phlr2A1C9onrDEns1f/2PK2UbYtZ54urY7aUoLV7TFUllYmXlwKBgQCOR6Pb9iwJ615sAvE6HGq0fVfxUWc54k582G+3wTDlr2AlrTr5AcRI+WnyfQB9dc80hQk6to3RkLKAieXUBS95FsKNQclaanhk6ExN/KfGIL7lO67VP63l7DBtk/F8y2Bmz0id4WuypTV2T+y8aziaTBmEQB8W4EwgV+uwvNZCSQKBgQDfXw23c0W/Ex/YPRVJjzG14K/d++s/Jm+Ov+qIfqrNUocjJRplSQNMMmiIpKRRxugFZCUt0TG8w5miJGdD9Ysa53b4nIXrUA082Pco/kr3TE7tskbTsu5Qaa03WEklTCM9U6p4PBW7vCFkwUsPmaERwr+6trFaWhpzTFTlbJByawKBgQCKIHKrsKnAV4grRA/8YmoVHzqpRGX6hAG+okCTHDLkSQYGjc6av2RXlAuPJlu7cQvf6Oe04FV+BgPFVhqRYxdbbwUhLaTU6lhHXl31uDsI1fF9ihuJW3DvrgS13NKsXKUSKlpNK/LqVAk6+wzRPyaMZSTAh5+nDcnZ/IEL68f2QQKBgQCV4paorGPo6V26Ll0dj8F1A4lsiZF4Ng9EOPLpEML+sRH/KOJxemZw1LFv9wz3F+JQN1T8w6xOfNPLtI/8z39ZLW8gQcmcLZeookegWekyxFilg6j/As0tWy81e00OEZ+38HJEpbX4ZT+74j+h9dOFeSxl1T0f366BYz2N2FB5rg==";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhPrmXmoEs28DM93oQcB2hWDPnaVrq512pUVLLffWgk6LUp22j57FkXEmmBI3ZZytTpXb2KEr3PvAAWYqRXwRNGEsYXs03xoiP7d6vyxJQCC6M6PSyadalRMjrQ7p25XiljRnLvWX3/6z+hH73HXyQgVJM+EBlscxD6iZkegLca7Vgd5Fh80mQdhW7RyL/RspbNIeV46BYscidmjpfDz6xi5pGheVBJu58/Hbz3rx0Sr1ywoBiWQLMsBB3zYAxJ3OAn/UYNxrVw0P/cQHBlueOo9YM7rxYZ/Drn7h8hqAKuCF4cMiOBLufvtxqo0wybgeBQ5QPZKj7YYfBNlsuYsoDwIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "https://yepeiwei.tunnel.qydev.com/alibaba/notify_url";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://yepeiwei.tunnel.qydev.com/alibaba/return_url";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

