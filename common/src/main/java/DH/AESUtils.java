package DH;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * <p>Description: [AES对称加密和解密]</p>
 * Created on 2017年11月28日 下午4:14:38
 * @author <a href="mailto: 15175223269@163.com">全冉</a>
 * @version 1.0 Copyright (c) 2017 全冉公司
 */
public class AESUtils {

    /**
     * <p>Discription:[加密]</p>
     * Created on 2017年11月28日 下午4:16:30
     * @param content 明文 用JSON.toJSONString(Map<String, String> map)转换的json字符串
     * @param key 加解密规则 访客系统提供key
     * @return String 密文
     * @author:[全冉]
     */
    public static String ecodes(String content, String key) {
        if (content == null || content.length() < 1) {
            return null;
        }
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes());
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteRresult = cipher.doFinal(byteContent);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteRresult.length; i++) {
                String hex = Integer.toHexString(byteRresult[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>Discription:[解密]</p>
     * Created on 2017年11月28日 下午4:17:49
     * @param content 密文
     * @param key 加解密规则
     * @return String 明文
     * @author:[全冉]
     */
    public static String dcodes(String content, String key) {
        if (content == null || content.length() < 1) {
            return null;
        }
        if (content.trim().length() < 19) {
            return content;
        }
        byte[] byteRresult = new byte[content.length() / 2];
        for (int i = 0; i < content.length() / 2; i++) {
            int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
            byteRresult[i] = (byte) (high * 16 + low);
        }
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes());
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] result = cipher.doFinal(byteRresult);
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
//			样例一
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("name_name", "_@quanran");
//			map.put("age@()", "16");
//			String mapStr = JSON.toJSONString(map);
//			System.out.println(mapStr);
//
//			String miwen = AESUtils.ecodes(mapStr, "quanran");
//			System.out.println(miwen);
//
//			String mingwen = AESUtils.dcodes(miwen, "quanran");
//			System.out.println(mingwen);
//
//			Map<String, String> map2 = (Map<String, String>) JSON.parse(mingwen);
//			for (String key : map2.keySet()) {
//				System.out.println("key= "+ key + " and value= " + map.get(key));
//			}

//			样例二
            String miwen = AESUtils.ecodes("{\"id\":1759,\"reservationId\":1511867174251,\"visitingPurpose\":1,\"officeBuildingId\":1,\"officeBuildingName\":\"1\",\"cityId\":1,\"cityName\":\"1\",\"reservationStatus\":1,\"visitorName\":\"1 \",\"visitorAreaCode\":\"1\",\"visitorPhone\":\"1\",\"visitorEmail\":null,\"remark\":null,\"visitingTime\":1,\"visitorNum\":1,\"visitingUnit\":null,\"messageNum\":null,\"photoUrl\":null,\"receptionUserName\":\"1\",\"receptionUserDomainAccount\":\"1\",\"visitingInvitationCode\":null,\"createUser\":\"1\",\"createTime\":1,\"createUserDomainAccount\":null,\"updateTime\":null,\"signTime\":null,\"alreadySignedInNum\":null,\"alreadySignedOutNum\":null,\"accompanyPersons\":null,\"visitingReason\":null,\"field1\":null,\"field2\":null,\"field3\":null,\"field4\":null,\"field5\":null,\"approveStatus\":1,\"levelOneName\":null,\"levelTwoName\":null,\"createPlatform\":1}",
                    "2b72cc3d1426554818da16e13f6a249c");
            System.out.println("miwen : " + miwen);

            String mingwen = AESUtils.dcodes(miwen, "2b72cc3d1426554818da16e13f6a249c");
            System.out.println("mingwen : " + mingwen);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}