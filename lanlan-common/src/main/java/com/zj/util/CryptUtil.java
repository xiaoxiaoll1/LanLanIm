package com.zj.util;

import org.springframework.util.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author xiaozj
 */
public class CryptUtil {

    /** 算法名称 */
    private static final String ALGORITHM_RSA = "RSA";

    /** 密钥长度 */
    private static final int KEY_SIZE = 2048;

    public static String md5(String src) {
        return DigestUtils.md5DigestAsHex(src.getBytes());
    }

    public static String keyToBase64(Key key) {
        return new BASE64Encoder().encode(key.getEncoded());
    }

    /**
     * 随机生成密钥对（包含公钥和私钥）
     */
    public static KeyPair generateKeyPair() throws Exception {
        // 获取指定算法的密钥对生成器
        KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM_RSA);

        // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
        gen.initialize(KEY_SIZE);

        // 随机生成一对密钥（包含公钥和私钥）
        return gen.generateKeyPair();
    }

    /**
     * 将 公钥/私钥 编码后以 Base64 的格式保存到指定文件
     */
    public static void saveKeyForEncodedBase64(Key key, File keyFile) throws IOException {
        // 获取密钥编码后的格式
        byte[] encBytes = key.getEncoded();

        // 转换为 Base64 文本
        String encBase64 = new BASE64Encoder().encode(encBytes);

        // 保存到文件
        IoUtils.writeFile(encBase64, keyFile);
    }

    /**
     * 根据公钥的 Base64 文本创建公钥对象
     */
    public static PublicKey getPublicKey(String pubKeyBase64) throws Exception {
        // 把 公钥的Base64文本 转换为已编码的 公钥bytes
        byte[] encPubKey = new BASE64Decoder().decodeBuffer(pubKeyBase64);

        // 创建 已编码的公钥规格
        X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);

        // 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
        return KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(encPubKeySpec);
    }

    /**
     * 根据私钥的 Base64 文本创建私钥对象
     */
    public static PrivateKey getPrivateKey(String priKeyBase64) throws Exception {
        // 把 私钥的Base64文本 转换为已编码的 私钥bytes
        byte[] encPriKey = new BASE64Decoder().decodeBuffer(priKeyBase64);

        // 创建 已编码的私钥规格
        PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);

        // 获取指定算法的密钥工厂, 根据 已编码的私钥规格, 生成私钥对象
        return KeyFactory.getInstance(ALGORITHM_RSA).generatePrivate(encPriKeySpec);
    }

    /**
     * 公钥加密数据
     */
    public static byte[] encrypt(byte[] plainData, String pubKeyStr) throws Exception {
        PublicKey publicKey = getPublicKey(pubKeyStr);
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);

        // 初始化密码器（公钥加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 加密数据, 返回加密后的密文
        return cipher.doFinal(plainData);
    }

    /**
     * 私钥解密数据
     */
    public static byte[] decrypt(byte[] cipherData, String priKeyStr) throws Exception {
        PrivateKey privateKey = getPrivateKey(priKeyStr);
        // 获取指定算法的密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);

        // 初始化密码器（私钥解密模型）
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 解密数据, 返回解密后的明文
        return cipher.doFinal(cipherData);
    }


    /*
     * 加密
     * 1.构造密钥生成器
     * 2.根据ecnodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String aesEncode(String encodeRules,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key=keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte [] raw=original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key=new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher=Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte [] byte_encode=content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte [] byte_AES=cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode=new String(new BASE64Encoder().encode(byte_AES));
            //11.将字符串返回
            return AES_encode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }
    /*
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String aesDecode(String encodeRules,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key=keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte [] raw=original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key=new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher=Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte [] byte_decode=cipher.doFinal(byte_content);
            String AES_decode=new String(byte_decode,"utf-8");
            return AES_decode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    /**
     * 随机生成秘钥
     */
    public static String getKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            //要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = byteToHexString(b);
            System.out.println(s);
            System.out.println("十六进制密钥长度为"+s.length());
            System.out.println("二进制密钥的长度为"+s.length()*4);
            return s;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法。");
            return null;
        }
    }
    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex=Integer.toHexString(bytes[i]);
            if(strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if(strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
//        String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzmHZLmcUs0yzZJuxHAn3B1cS9EbXTy+A4OtDTv+qH/2Yc9u7payyYfI30Kettl0aQ7fNG5LcTCIIH3Z5HlusPzjwEorLKvR5zd5nkn8LhzILPLsELmoPCB2oDLsZ2Drc3ZNsHLF8DhN/HYaqsrskooHqiQTIU/2Ji/O5whqiizoYNm8nrjQJ9019507rDWb3Jl4B/QWI0lv96Si+v/RrGrUMY6YcfyTPguGJo5d3o4p97yArGqXHXn7dxGy0+7DkBOWVptQuKmsZom90+fIHdJTk//iB9tJr1HJ6mDWZSeES0f0WveEwbum6EQBk7Bsd9jSjNXP0+OwTxyWGif13jQIDAQAB";
//        // 得到私钥字符串
//        String privateKeyString = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOYdkuZxSzTLNkm7EcCfcHVxL0RtdPL4Dg60NO/6of/Zhz27ulrLJh8jfQp622XRpDt80bktxMIggfdnkeW6w/OPASissq9HnN3meSfwuHMgs8uwQuag8IHagMuxnYOtzdk2wcsXwOE38dhqqyuySigeqJBMhT/YmL87nCGqKLOhg2byeuNAn3TX3nTusNZvcmXgH9BYjSW/3pKL6/9GsatQxjphx/JM+C4Ymjl3ejin3vICsapcdeft3EbLT7sOQE5ZWm1C4qaxmib3T58gd0lOT/+IH20mvUcnqYNZlJ4RLR/Ra94TBu6boRAGTsGx32NKM1c/T47BPHJYaJ/XeNAgMBAAECggEAXXV2Ektf1mpPeqn+pEHm+g32aWSDMDrE1BX13xpsAhynIyBIc5gnF6/Gkti8E69Jq4zadzgkRt3Ka+UMqDC/acnw/ZSYuJUJa67hnDeoEssYx6GxHQuuTvCPH2TDKWZOipCuDrhZA07U65wGRPX2exj2CqJ9zXstBJGUd0/0d7NANcbDQUWve8P5iSi+9TiCnj7H1DWCcTCksGxEg2ABi6bjrdD0DXs5gug8MXJMR13N9AABC4cLylKLmpYwHhCSNtJ3YfSMpm898rOWk20Y6fAzSOz9q8q4Xgny7gzPfvvxsUYMocCkp0+m/jTCMOzeQyfrl5I8HUlr7sSgyaoywQKBgQD62NlkDI6BRm6Ywv7W/GGVDWBrSXkV6TznEkxIlAaFnrG9+hF1gU1tsgSvCvx+vBPdrWdZt37tc3zxACvlJVp8VF75Jo6cpd5A4nuPh5fSyQmG7guS8+RUivv22j7nCbP1ZeNytlDdWYlwzpWTvQgHnRqxfEcOJFRrZasEzpyMhQKBgQDSnysJEdyYZRIfLzsU9sBGEvmRgAF/OO+zEA9dG5ZaSEVaIzjkE5olkkILLhaZ74lZoR/9/MLmZYKJtsxsxmh7iM5k+yPRkLsc4LfMD3wjHbUbOgieIMojAvyJKLzNlFF52NujcddhoI+xDDbRENuCRnwrc86oWTGr7to9tJcRaQKBgD6aiAa0K4yP62IsqDU3X5M3d2zPNW0GfLCenHMwnkASzwE+u8S7tHtABnM5JrLqdXrJoBV/+imAvRnYlRvnKqIE3H8J5GWr77/5xiSYDogIrFwEZNcUD1QaK6/7398BSOxhtVYXsi7L/cUtiZ8JekuMJfFNtt4MqxTylq/ocKGRAoGBALZPwFihdWkUU5NatrF2xQfi9NPVEXamOhWtGR9m+cY8OIvFYUFLFmrfJvu2cSneBe3nYmHfoHT9+PhrmpetRlNoH/+Yw3Bq7wwYGAAyhyl+VX2zxjHaAB4+P4oaEROCBuSJqdTYfa6r5LVD9U8SYG9rw595JrDiQ/SnU5GF+B3BAoGBAOl9bJpynK2N6XAnEUXdnM/QCCBlMFjWC18184Ulyehz8rjfRcTm2hsaKQ4agqMBxUsfwvJQFkd7lltKTD9m+owQG6ARmj6U6FF9x2IYSHvt5V4Xdm2pDuo/Rnlna59px6Ucsr4d+EO5Oqu/XO4B+BAgVKit28ZLDYhjJxCfn94F";
//
//
//        String a = "{\"username\":\"xiaozj\",\"password\":\"123456\"}";
//        byte[] bytes = a.getBytes();
//        byte[] encrypt = encrypt(bytes, publicKeyString);
//        String s = Base64.getEncoder().encodeToString(encrypt);
//        System.out.println(s);
//        byte[] decrypt = decrypt(Base64.getDecoder().decode(s), privateKeyString);
//        String res = new String(decrypt);
//        System.out.println(res);
        String key = getKey();
        String s = aesEncode(key, "123abc");
        System.out.println(s);
        String s1 = aesDecode(key, s);
        System.out.println(s1);
    }



}
