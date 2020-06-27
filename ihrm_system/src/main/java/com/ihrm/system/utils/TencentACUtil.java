package com.ihrm.system.utils;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.xml.ws.http.HTTPException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName
 * @Description
 * @Author He.Wang
 * @Date 2020/6/24 15:39
 * @Version 1.0
 **/

@Component
public class TencentACUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${ac.appId}")
    private int appId;
    @Value("${ac.appKey}")
    private String appKey;
    /**
     * 模板Id
     */
    @Value("${ac.templateId}")
    private int templateId;
    /**
     * 签名
     */
    @Value("${ac.smsSign}")
    private String smsSign;
    /**
     * 发送目标的手机号
     */
    private String mobile;

    public void sendMsgByTxPlatform(String mobile) {
        String randStrCode = RandomCodeUtil.getRandNumberCode();

        String msg =  randStrCode + "为您的注册验证码，请于" + 2 + "分钟内填写。如非本人操作，请忽略本短信。";
        try {
            SmsSingleSender sSender = new SmsSingleSender(appId, appKey);
            String[] params = {randStrCode};

            SmsSingleSenderResult smsSingleSenderResult = sSender.sendWithParam("86", mobile,
                    templateId,params, smsSign, "", "");
            System.out.println(smsSingleSenderResult);
            redisTemplate.opsForValue().set(mobile,randStrCode,2, TimeUnit.MINUTES);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
    }


}