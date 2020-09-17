package ksnu.cie.hw;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;


public class JavaEnCryto {
    public static void main(String[] args) {
         try {
            String originalText="plain text";
            String key="key";
            String en=Encrypt(originalText,key);
            String de=Decrypt(en,key);
            System.out.println("Original Text is "+originalText);
            System.out.println("Encrypted Text is " + en);
            System.out.println("Decrypted Text is "+de);
         }catch(Exception ex) {
            System.out.println("¿À·ù");
         }      
      }
    public static String Decrypt(String text, String key) throws Exception{
         Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
         byte[] keyBytes=new byte[16];
         byte[] b=key.getBytes("UTF-8");
         int len=b.length;
         if(len>keyBytes.length) len=keyBytes.length;
         System.arraycopy(b, 0, keyBytes, 0, len);
         SecretKeySpec keySpec=new SecretKeySpec(keyBytes,"AES");
         IvParameterSpec ivSpec=new IvParameterSpec(keyBytes);
         cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
         
         Decoder decoder=Base64.getDecoder();
         byte[] results=decoder.decode(text.getBytes());
         return new String(results,"UTF-8");
      }
      public static String Encrypt(String text, String key) throws Exception{
         Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
         byte[] keyBytes=new byte[16];
         byte[] b=key.getBytes("UTF-8");
         int len=b.length;
         if(len>keyBytes.length) len=keyBytes.length;
         System.arraycopy(b, 0, keyBytes, 0, len);
         SecretKeySpec keySpec=new SecretKeySpec(keyBytes,"AES");
         IvParameterSpec ivSpec=new IvParameterSpec(keyBytes);
         cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
         
         Encoder encoder=Base64.getEncoder();
         byte[] results=encoder.encode(text.getBytes());
         return new String(results,"UTF-8");
      }
}