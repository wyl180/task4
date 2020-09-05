package com.jnshu.util;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;


/**
 * @ClassName Aliutil
 * @Description 邮箱、短信、图片上传工具类
 * @Author 韦延伦
 * @Date 2020/8/28 13:55
 * @Version 1.0
 */
@Component
public class AliUtil {
    private static Logger logger = LoggerFactory.getLogger(AliUtil.class);
    @Autowired
    private RedisService redisService;
    /**
     * 密钥
     */
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;
    /**
     * 短信业务
     */

    @Value("${signName}")
    private String signName;
    @Value("${templateCode}")
    private String templateCode;
    private static final String product = "Dysmsapi";
    //短信API产品域名（接口地址固定，无需修改）
    private static final String domain = "dysmsapi.aliyuncs.com";
    /**
     * 邮箱业务
     */
    @Value("${accountName}")
    private String accountName;
    @Value("${fromAlias}")
    private String fromAlias;
    @Value("${tagName}")
    private String tagName;
    @Value("${subject}")
    private String subject;

    /**
     * 验证码
     */
    public String getCode() {
        int newcode = (int) (Math.random() * 9999) + 100;

        String code = Integer.toString(newcode);
        return code;
    }

    /**
     * 发送短信
     */
    public void sendSMS(String telephone,String code) {


        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setSysMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(telephone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        //参考：request.setTemplateParam("{\"变量1\":\"值1\",\"变量2\":\"值2\",\"变量3\":\"值3\"}")
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            System.out.println("签名是==============="+signName);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (sendSmsResponse.getCode() != null && "OK".equals(sendSmsResponse.getCode())) {
            logger.info("发送成功");
            logger.info("短信接口返回的数据----------------");
            logger.info("Code=" + sendSmsResponse.getCode());
            logger.info("Message=" + sendSmsResponse.getMessage());
            logger.info("RequestId=" + sendSmsResponse.getRequestId());
            logger.info("BizId=" + sendSmsResponse.getBizId());

        } else {
            logger.error("发送失败");
            logger.info("短信接口返回的数据----------------");
            logger.info("Code=" + sendSmsResponse.getCode());
            logger.info("Message=" + sendSmsResponse.getMessage());
            logger.info("RequestId=" + sendSmsResponse.getRequestId());
            logger.info("BizId=" + sendSmsResponse.getBizId());
        }


    }

    /**
     * 邮件发送
     */
    public void sendMail(String mail,String code) {
        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的”cn-hangzhou”替换为”ap-southeast-1”、或”ap-southeast-2”。
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        //try {
        //DefaultProfile.addEndpoint(“dm.ap-southeast-1.aliyuncs.com”, “ap-southeast-1”, “Dm”,  “dm.ap-southeast-1.aliyuncs.com”);
        //} catch (ClientException e) {
        //e.printStackTrace();
        //}
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            //request.setVersion(“2017-06-22”);// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
//            “控制台创建的发信地址”
            request.setAccountName(accountName);
//            “发信人昵称”
            request.setFromAlias(fromAlias);
            request.setAddressType(1);
//            “控制台创建的标签”
            request.setTagName(tagName);
            request.setReplyToAddress(true);
//            “目标地址”
            request.setToAddress(mail);
            //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
            //request.setToAddress(“邮箱1,邮箱2”);
//            “邮件主题”
            request.setSubject(subject);
            //如果采用byte[].toString的方式的话请确保最终转换成utf-8的格式再放入htmlbody和textbody，若编码不一致则会被当成垃圾邮件。
            //注意：文本邮件的大小限制为3M，过大的文本会导致连接超时或413错误
            //“邮件正文”
            request.setHtmlBody(code);
            //SDK 采用的是http协议的发信方式, 默认是GET方法，有一定的长度限制。
            //若textBody、htmlBody或content的大小不确定，建议采用POST方式提交，避免出现uri is not valid异常
            request.setSysMethod(MethodType.POST);
            //开启需要备案，0关闭，1开启
            //request.setClickTrace(“0”);
            //如果调用成功，正常返回httpResponse；如果调用失败则抛出异常，需要在异常中捕获错误异常码；错误异常码请参考对应的API文档;
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);

        } catch (ServerException e) {
            //捕获错误异常码
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        } catch (ClientException e) {
            //捕获错误异常码
            System.out.println("ErrCode :" + e.getErrCode());
            e.printStackTrace();
        }

    }

    /**
     * 封装发送手机验证码的方法
     * @param phone
     * @param map
     */
    public void sendPhoneCode(final String phone, Map<String,Object> map){
        //判断今天是不是首次登录
        if (redisService.get("count" + phone) == null) {
            redisService.set("count" + phone, "5", 60 * 60 * 24);
            //生成验证码，加到redis中.设置失效时间为2分钟。
            String verify = getCode();
            //发送********
            sendSMS(phone,verify);
            System.out.println("验证码是：" + verify);
            redisService.set(phone, verify, 300);
            map.put("msg", "验证码发送成功,还可以发送" + redisService.get("count"+phone) + "次");
        } else {
            //过期时间
            long expire = redisService.getExpire(phone);
            System.out.println("失效时间：---" + expire);
            //判断今天还有没有剩余发送次数和间隔时间
            if (redisService.get(phone) == null) {
                int count=Integer.parseInt((String)redisService.get("count"+phone));
                if(count>0) {
                    //生成验证码，加到redis中.设置失效时间为2分钟。
                    String verify = getCode();
                    //发送********
                    sendSMS(phone,verify);
                    System.out.println("验证码是：" + verify);
                    redisService.set(phone, verify,300);
                    //可发送次数-1
                    redisService.decr("count" + phone, 1);
                    map.put("msg", "验证码发送成功,还可以发送" + redisService.get("count"+phone) + "次");
                } else {
                    logger.info(phone+ "====今天的发送验证码次数已使用完");
                    map.put("msg", "今天的发送次数已经用完");
                }
            } else {
                map.put("msg", "请隔" + expire + "秒再发送");
            }
        }
    }
    /**
     * 封装发送邮箱验证码的方法
     * @param  mail
     * @param map
     */
    public void sendMailCode(final String mail, Map<String,Object> map){
        //判断今天是不是首次登录
        if (redisService.get("count" + mail) == null) {
            redisService.set("count" + mail, "5", 60 * 60 * 24);
            //生成验证码，加到redis中.设置失效时间为2分钟。
            String verify = getCode();
            //发送*********
            sendMail(mail,verify);
            System.out.println("验证码是：" + verify);
            redisService.set(mail, verify, 300);
            map.put("msg", "验证码发送成功,还可以发送" + redisService.get("count"+mail) + "次");
        } else {
            //过期时间
            long expire = redisService.getExpire(mail);
            System.out.println("失效时间：---" + expire);
            if (redisService.get(mail) == null) {
                int count = Integer.parseInt((String) redisService.get("count" + mail));
                //判断今天还有没有剩余发送次数和间隔时间
                if (count > 0) {
                    //生成验证码，加到redis中.设置失效时间为2分钟。
                    String verify = getCode();
                    //发送*********
                    sendMail(mail,verify);
                    System.out.println("验证码是：" + verify);
                    redisService.set(mail, verify, 300);
                    //可发送次数-1
                    redisService.decr("count" + mail, 1);
                    map.put("msg", "验证码发送成功,还可以发送" + count + "次");
                } else {
                    logger.info(mail+ "====今天的发送验证码次数已使用完");
                    map.put("msg", "今天的发送次数已经用完");
                }
            } else {
                map.put("msg", "请隔" + expire + "秒再发送");
            }
        }
    }
}


