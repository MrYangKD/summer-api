package com.cn.summer.api.common.util;

import ch.qos.logback.classic.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * .
 * 验签工具类.
 * @author YangYK
 * @create: 2019-11-15 16:56
 * @since 1.0
 */
public class RSAUtils {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(RSAUtils.class);
    private static final String RSA_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    private RSAUtils(){}

    /**
     * 验签生成.
     * @param content 待生产验签内容
     * @param privateKey 私钥
     * @param encode 编码
     * @return String
     */
    public static String sign(final String content, final String privateKey, final String encode){
        try {
            PKCS8EncodedKeySpec priPKCSS = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCSS);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(priKey);
            signature.update(content.getBytes(encode));

            byte[] signed = signature.sign();
            return Base64.encodeBase64URLSafeString(signed);

        } catch (Exception e) {
            LOGGER.error("签名发生异常", e);

        }
        return null;
    }

    /**
     * 验证签名.
     * @param content 待验签内容
     * @param sign 签名
     * @param publicKey 公钥
     * @param encode 编码
     * @return boolean
     */
    public static boolean verify(final String content, final String sign, final String publicKey, final String encode){
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));

            return signature.verify(Base64.decodeBase64(sign));
        } catch (final Exception e) {
            LOGGER.error("验签发生异常", e);
        }
        return false;
    }

    /**
     * 获取待验签的字符串.
     * @param params params
     * @return signStr
     */
    public static String getSignStr(final Map<String, Object> params) {
        StringBuilder signStr = new StringBuilder();
        char a = '=';
        char b = '&';
        params.entrySet().stream()
                .filter(e -> e.getValue() != null && !"".equals(e.getValue()) && !"sign".equals(e.getKey()))
                .sorted(Map.Entry.<String, Object>comparingByKey())
                .forEachOrdered(e -> signStr.append(e.getKey()).append(a).append(e.getValue()).append(b));

        signStr.deleteCharAt(signStr.length()-1);

        return signStr.toString();
    }

}
