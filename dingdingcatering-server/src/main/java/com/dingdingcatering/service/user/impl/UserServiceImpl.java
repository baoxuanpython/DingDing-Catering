package com.dingdingcatering.service.user.impl;

import com.alibaba.fastjson2.JSONObject;
import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.constant.WechatApiConstant;
import com.dingdingcatering.dto.UserLoginDTO;
import com.dingdingcatering.entity.User;
import com.dingdingcatering.exception.LoginFailedException;
import com.dingdingcatering.mapper.user.UserMapper;
import com.dingdingcatering.properties.JwtProperties;
import com.dingdingcatering.properties.WeChatProperties;
import com.dingdingcatering.service.user.UserService;
import com.dingdingcatering.utils.HttpClientUtil;
import com.dingdingcatering.utils.JwtUtil;
import com.dingdingcatering.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;
    private final WeChatProperties weChatProperties;

    public UserServiceImpl(UserMapper userMapper, JwtProperties jwtProperties, WeChatProperties weChatProperties) {
        this.userMapper = userMapper;
        this.jwtProperties = jwtProperties;
        this.weChatProperties = weChatProperties;
    }

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        log.info("用户登录尝试: code={}", userLoginDTO.getCode());
        String openid = getOpenId(userLoginDTO.getCode());
        if (openid == null) {
            log.warn("用户登录失败: 获取openid失败, code={}", userLoginDTO.getCode());
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user = userMapper.login(openid);
        if (user == null) {
            log.info("用户首次登录，自动注册: openid={}", openid);
            user = User.builder().openid(openid).build();
            userMapper.createUser(user);
            log.info("用户注册成功: id={}, openid={}", user.getId(), openid);
        } else {
            log.info("用户登录成功: id={}, openid={}", user.getId(), openid);
        }
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("openid", user.getOpenid());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        return UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .openid(openid)
                .build();
    }



    private String getOpenId(String code) {
        log.debug("调用微信接口获取openid: code={}", code);
        Map<String, String> postMap = new HashMap<>();
        postMap.put(WechatApiConstant.APPID, weChatProperties.getAppid());
        postMap.put(WechatApiConstant.SECRET, weChatProperties.getSecret());
        postMap.put(WechatApiConstant.JS_CODE, code);
        postMap.put(WechatApiConstant.GRANT_TYPE, WechatApiConstant.GRANT_TYPE_VALUE);
        String response = HttpClientUtil.doGet(WechatApiConstant.WECHAT_LOGIN_URL, postMap);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String openid = jsonObject.getString("openid");
        if (openid == null) {
            log.warn("获取openid失败: response={}", response);
        } else {
            log.debug("获取openid成功: openid={}", openid);
        }
        return openid;
    }
}
