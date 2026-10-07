package com.dingdingcatering.service.user.Impl;

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
        String openid = getOpenId(userLoginDTO.getCode());
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user = userMapper.login(openid);
        if (user == null) {
            user = User.builder().openid(openid).build();
            userMapper.createUser(user);
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
        Map<String, String> postMap = new HashMap<>();
        postMap.put(WechatApiConstant.APPID, weChatProperties.getAppid());
        postMap.put(WechatApiConstant.SECRET, weChatProperties.getSecret());
        postMap.put(WechatApiConstant.JS_CODE, code);
        postMap.put(WechatApiConstant.GRANT_TYPE, WechatApiConstant.GRANT_TYPE_VALUE);
        String response = HttpClientUtil.doGet(WechatApiConstant.WECHAT_LOGIN_URL, postMap);
        JSONObject jsonObject = JSONObject.parseObject(response);
        return jsonObject.getString("openid");
    }
}
