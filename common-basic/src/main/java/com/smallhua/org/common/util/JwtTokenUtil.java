package com.smallhua.org.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;


/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * Created by macro on 2018/4/26.
 */
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取用户昵称
     */
    public Map<String, String> getSubjectFromToken(String token) {
        Map<String, String> subject;
        try {
            Claims claims = getClaimsFromToken(token);
            subject = (Map<String, String>) claims.get(CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            subject = null;
        }
        return subject;
    }

    /**
     * 从token中获取用户昵称
     */
    public String getUserNameFromToken(String token) {
        String userName;
        try {
            Map<String, String> map = getSubjectFromToken(token);
            userName = map.get(ConstUtil.PAYLOAD_KEY_USERNAME);
        } catch (Exception e) {
            userName = null;
        }
        return userName;
    }

    /**
     * 从token中获取用户昵称
     */
    public String getAccountFromToken(String token) {
        String account;
        try {
            Map<String, String> map = getSubjectFromToken(token);
            account = map.get(ConstUtil.PAYLOAD_KEY_ACCOUNT);
        } catch (Exception e) {
            account = null;
        }
        return account;
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String userName = getAccountFromToken(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(String userName, String account) {
        Map<String, String> map = new HashMap<>();
        map.put(ConstUtil.PAYLOAD_KEY_ACCOUNT, account);
        map.put(ConstUtil.PAYLOAD_KEY_USERNAME, userName);
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, map);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 当原来的token没过期时是可以刷新的
     *
     * @param oldToken 带tokenHead的token
     */
    public String refreshHeadToken(String oldToken) {
        if(StrUtil.isEmpty(oldToken)){
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if(StrUtil.isEmpty(token)){
            return null;
        }
        //token校验不通过
        Claims claims = getClaimsFromToken(token);
        if(claims==null){
            return null;
        }
        //如果token已经过期，不支持刷新
        if(isTokenExpired(token)){
            return null;
        }
        //如果token在30分钟之内刚刷新过，返回原token
        if(tokenRefreshJustBefore(token,30*60)){
            return token;
        }else{
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(claims);
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     * @param token 原token
     * @param time 指定时间（秒）
     */
    private boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();
        //刷新时间在创建时间的指定时间内
        if(refreshDate.after(created)&&refreshDate.before(DateUtil.offsetSecond(created,time))){
            return true;
        }
        return false;
    }

    public Map<String, String> getSubjectByToken(){
        HttpServletRequest request = ServletUtil.getRequest();
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String[] tokens = authHeader.split("\\s");// The part after "Bearer "
            String authToken = tokens[1];
            Map<String, String> subject = this.getSubjectFromToken(authToken);
            return subject;
        }
        return null;
    }


    /*测试rsa加密生成token*/
    private static String publicExponent = "65537";
    private static String privateExponent = "17229266893241928019407448538210767394314299831091924595141407231192097700085259794646635668667196594382333548862926951453552408374412828286121538469820555639179408599684718288543042181104921632781024582518681487594175261480330600707088202917746658509127479673682915944749732373968033990096280738372728055673";
    private static String modulus = "106423606445089183469171155028154482820092013951957724994512950585356880958575652324387988861210373534970310442208260472894577209013562066671776368208918942020783431571722667979490630586343538306223319468173581167993941500586382020805072004931124403103870349566932555032205872045530004847976880850153190269597";
    public static void main(String[] args) throws Exception {
        Map<String, Object> headers = Maps.newHashMap();
        headers.put("alg", "RS256");
        headers.put("typ", "JWT");

        String payload = "{\"app\":\"2\",\"wxUserId\":null,\"user_name\":\"1015030\",\"userNo\":\"1015030\",\"user:nickname\":\"李娜\",\"user:name\":\"1015030\",\"version\":\"3\",\"authorities\":[\"ROLE_USER\",\"ROLE_ADMIN\"],\"platform\":\"1\",\"primarysid\":1000063,\"client_id\":\"web\",\"aud\":[\"Benlai.O2OERP.WebApi\"],\"scope\":[\"all\"],\"name\":\"李娜\",\"userType\":2,\"exp\":1662188065,\"jti\":\"80f49900-df22-4ffd-8eca-cb569cc26e48\"}";
        RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(new BigInteger(modulus),
                new BigInteger(publicExponent));
        RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(new BigInteger(modulus),
                new BigInteger(privateExponent));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        String compact = CustomJwts.builder()
                .setHeader(headers)
                .setPayload(payload)
                .signWith(SignatureAlgorithm.RS256, factory.generatePrivate(privateSpec))
                .compact();
        System.out.println(compact);
    }



    public static final class CustomJwts {

        private CustomJwts(){}

        public static JwtBuilder builder() {
            return new CustomJwtBuilder();
        }

        public static class CustomJwtBuilder implements JwtBuilder{

            private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

            private String headerStr;
            private String payLoad;
            private SignatureAlgorithm alg;
            private Key key;

            @Override
            public JwtBuilder setHeader(Header header) {
                return null;
            }

            @Override
            public JwtBuilder setHeader(Map<String, Object> header) {
                this.headerStr = JSONUtil.parseObj(new TreeMap<>(header),false, true).toString();
                return this;
            }

            @Override
            public JwtBuilder setHeaderParams(Map<String, Object> params) {
                return null;
            }

            @Override
            public JwtBuilder setHeaderParam(String name, Object value) {
                return null;
            }

            @Override
            public JwtBuilder setPayload(String payload) {
                this.payLoad = payload;
                return this;
            }

            @Override
            public JwtBuilder setClaims(Claims claims) {
                return null;
            }

            @Override
            public JwtBuilder setClaims(Map<String, Object> claims) {
                return null;
            }

            @Override
            public JwtBuilder addClaims(Map<String, Object> claims) {
                return null;
            }

            @Override
            public JwtBuilder setIssuer(String iss) {
                return null;
            }

            @Override
            public JwtBuilder setSubject(String sub) {
                return null;
            }

            @Override
            public JwtBuilder setAudience(String aud) {
                return null;
            }

            @Override
            public JwtBuilder setExpiration(Date exp) {
                return null;
            }

            @Override
            public JwtBuilder setNotBefore(Date nbf) {
                return null;
            }

            @Override
            public JwtBuilder setIssuedAt(Date iat) {
                return null;
            }

            @Override
            public JwtBuilder setId(String jti) {
                return null;
            }

            @Override
            public JwtBuilder claim(String name, Object value) {
                return null;
            }

            @Override
            public JwtBuilder signWith(SignatureAlgorithm alg, byte[] secretKey) {
                return null;
            }

            @Override
            public JwtBuilder signWith(SignatureAlgorithm alg, String base64EncodedSecretKey) {
                return null;
            }

            @Override
            public JwtBuilder signWith(SignatureAlgorithm alg, Key key) {
                this.alg = alg;
                this.key = key;
                return this;
            }

            @Override
            public JwtBuilder compressWith(CompressionCodec codec) {
                return null;
            }

            @SneakyThrows
            @Override
            public String compact() {
                String base64UrlEncodedHeader = TextCodec.BASE64URL.encode(this.headerStr.getBytes());

                String base64UrlEncodedBody = TextCodec.BASE64URL.encode(this.payLoad.getBytes());

                byte[] concat = concat(Base64.getUrlEncoder().withoutPadding().encode(JSONUtil.toJsonStr(this.headerStr).getBytes()), ".".getBytes(), Base64.getUrlEncoder().withoutPadding().encode(this.payLoad.getBytes()));

//                Signature signature = Signature.getInstance(alg.getJcaName());
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initSign((RSAPrivateKey) key);
                signature.update(concat);
                byte[] sign = signature.sign();
                String base64UrlSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(sign);

                return base64UrlEncodedHeader + JwtParser.SEPARATOR_CHAR + base64UrlEncodedBody + JwtParser.SEPARATOR_CHAR + base64UrlSignature;
            }

            protected String base64UrlEncode(Object o, String errMsg) {
                byte[] bytes;
                try {
                    bytes = toJson(o);
                } catch (JsonProcessingException e) {
                    throw new IllegalStateException(errMsg, e);
                }

                return TextCodec.BASE64URL.encode(bytes);
            }


            protected byte[] toJson(Object object) throws  JsonProcessingException {
                return OBJECT_MAPPER.writeValueAsBytes(object);
            }

            public static byte[] concat(byte[]... arrays) {
                int size = 0;
                for (byte[] a: arrays) {
                    size += a.length;
                }
                byte[] result = new byte[size];
                int index = 0;
                for (byte[] a: arrays) {
                    System.arraycopy(a, 0, result, index, a.length);
                    index += a.length;
                }
                return result;
            }

            private static Charset UTF8 = Charset.forName("UTF-8");

            public static byte[] utf8Encode(CharSequence string) {
                try {
                    ByteBuffer bytes = UTF8.newEncoder().encode(CharBuffer.wrap(string));
                    byte[] bytesCopy = new byte[bytes.limit()];
                    System.arraycopy(bytes.array(), 0, bytesCopy, 0, bytes.limit());
                    return bytesCopy;
                }
                catch (CharacterCodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}
