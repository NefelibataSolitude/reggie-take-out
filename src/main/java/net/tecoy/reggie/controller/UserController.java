/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/29 14:39
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.tecoy.reggie.common.R;
import net.tecoy.reggie.constant.Constants;
import net.tecoy.reggie.pojo.entity.User;
import net.tecoy.reggie.service.UserService;
import net.tecoy.reggie.utils.SmsUtils;
import net.tecoy.reggie.utils.ValidateCodeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 发送手机短信验证码
     * @param session
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(HttpSession session, @RequestBody User user) {
        // 获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            // 生成4位随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.error(code);
            // 调用阿里云短信服务发送短信验证码
            // SmsUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
            // 获取以前的code并删除
            try {
                session.removeAttribute(phone);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 存储验证码
            session.setAttribute(phone, code);
            R.success("手机验证码短信发送成功!");
        }

        return R.error("手机短信验证码发送失败!");
    }

    /**
     * 手机验证码登录
     * @param session
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(HttpSession session, @RequestBody Map map) {
        // 获取手机号
        String phone = map.get("phone").toString();
        // 获取验证码
        String code = map.get("code").toString();
        // 从session中获取验证码
        String realCode = null;
        try {
            realCode = session.getAttribute(phone).toString();
        } catch (Exception e) {
            R.error("验证码错误!");
        }

        session.removeAttribute(phone);
        if (realCode != null && realCode.equals(code)) {
            // 判断当前用户手机号是否注册, 新用户自动注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setSex(String.valueOf(0));
                user.setStatus(1);
                user.setName(UUID.randomUUID().toString());
                userService.save(user);
            }
            session.setAttribute(Constants.USER, user.getId());
            return R.success(user);
        }

        return R.error("验证码错误!");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute(Constants.USER);
        return R.success("退出登录成功!");
    }

}
