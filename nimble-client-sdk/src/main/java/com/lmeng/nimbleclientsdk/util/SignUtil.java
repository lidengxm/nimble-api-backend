package com.lmeng.nimbleclientsdk.util;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/7/29
 */
public class SignUtil {

    /**
     * 生成签名
     * @param body
     * @param secretKey
     * @return
     */
    public static String getSign(String body, String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return md5.digestHex(content);
    }
}
