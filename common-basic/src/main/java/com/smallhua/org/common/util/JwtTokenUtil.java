package com.smallhua.org.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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

        String payload = "{\"app\":\"2\",\"wxUserId\":null,\"user_name\":\"1015030\",\"userNo\":\"1015030\",\"user:nickname\":\"李娜\",\"user:name\":\"1015030\",\"version\":\"3\",\"authorities\":[\"ROLE_USER\",\"ROLE_ADMIN\"],\"platform\":\"1\",\"primarysid\":1000063,\"client_id\":\"web\",\"aud\":[\"Benlai.O2OERP.WebApi\"],\"scope\":[\"all\"],\"name\":\"李娜\",\"userType\":2,\"exp\":1661942324,\"jti\":\"f8f97e88-b783-4906-904f-3019d17fa80a\"}";
        Map<String, Object> payloadMap = JSONUtil.toBean(payload, Map.class);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(new BigInteger(modulus),
                new BigInteger(publicExponent));
        RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(new BigInteger(modulus),
                new BigInteger(privateExponent));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        String compact = Jwts.builder()
                .setHeader(headers)
                .setPayload(payload)
                .signWith(SignatureAlgorithm.RS256, factory.generatePrivate(privateSpec))
                .compact();
        System.out.println(compact);
    }

}
