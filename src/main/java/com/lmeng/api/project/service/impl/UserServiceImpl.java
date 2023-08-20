package com.lmeng.api.project.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.lmeng.api.project.mapper.UserMapper;
import com.lmeng.api.project.exception.BusinessException;
import com.lmeng.api.project.service.UserService;
import com.lmeng.api.project.utils.JwtUtils;
import com.lmeng.api.project.utils.SqlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lmeng.apicommon.common.ErrorCode;
import com.lmeng.apicommon.constant.CommonConstant;
import com.lmeng.apicommon.constant.UserConstant;
import com.lmeng.api.project.model.dto.user.UserQueryRequest;
import com.lmeng.apicommon.entity.User;
import com.lmeng.api.project.model.enums.UserRoleEnum;
import com.lmeng.api.project.model.vo.LoginUserVO;
import com.lmeng.api.project.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.lmeng.apicommon.common.ErrorCode.SYSTEM_ERROR;

/**
 * 用户服务实现层
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "nimble-api";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private Gson gson;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            //3.分配accessKey和secretKey
            String accessKey = DigestUtils.md5DigestAsHex((SALT + userAccount + RandomUtil
                    .randomNumbers(5)).getBytes());
            String secretKey = DigestUtils.md5DigestAsHex((SALT + userAccount + RandomUtil
                    .randomNumbers(8)).getBytes());
            // 4. 插入数据
            User user = new User();
            long id = RandomUtils.nextLong(10000000000L, 99999999999L);
            user.setId(id);
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserName(CommonConstant.USER_PREINDEX + RandomUtil.randomString(4));
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            user.setUserAvatar("https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202307091458670.webp");
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request, HttpServletResponse response) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误!");
        }
        // 3. 记录用户的登录态
//        return setLoginUser(response, user);
        LoginUserVO loginUserVO = getLoginUserVO(user);
        request.getSession().setAttribute(UserConstant.LOGIN_USER_STATE,user);
        log.info(String.valueOf(request.getSession()));
        return loginUserVO;
    }

    /**
     * 记录用户的登录态，并返回脱敏后的登录用户
     * @param response
     * @param user
     * @return
     */
    private LoginUserVO setLoginUser(HttpServletResponse response, User user) {
        String token = JwtUtils.getJwtToken(user.getId(), user.getUserName());
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);
        String userJson = gson.toJson(user);
        stringRedisTemplate.opsForValue().set(UserConstant.LOGIN_USER_STATE + user.getId(),
                userJson, JwtUtils.EXPIRE, TimeUnit.MILLISECONDS);
        return this.getLoginUserVO(user);
    }


    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
//        Long userId = JwtUtils.getUserIdByToken(request);
//        if (userId == null){
//            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户未登录");
//        }
//        String userJson = stringRedisTemplate.opsForValue().get(UserConstant.LOGIN_USER_STATE + userId);
//        User user = gson.fromJson(userJson, User.class);
//        if (user == null){
//            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户不存在");
//        }
//        return user;

        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.LOGIN_USER_STATE);
        log.info("获取登录用户= " + userObj);
        User currentUser = (User) userObj;
        log.info("currentuser=" + currentUser);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户未登录");
        }
        // 从数据库查询
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户不存在");
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.LOGIN_USER_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.LOGIN_USER_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request, HttpServletResponse response) {
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("token")){
//                Long userId = JwtUtils.getUserIdByToken(request);
//                stringRedisTemplate.delete(UserConstant.LOGIN_USER_STATE + userId);
//                Cookie timeOutCookie = new Cookie(cookie.getName(),cookie.getValue());
//                timeOutCookie.setMaxAge(0);
//                response.addCookie(timeOutCookie);
//                return true;
//            }
//        }
//
//        throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");

        if (request.getSession().getAttribute(UserConstant.LOGIN_USER_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户未登录!");
        }
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.LOGIN_USER_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 更新用户密钥
     * @param id
     * @return
     */
    @Override
    public boolean updateSecretKey(Long id) {
        if(id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = getById(id);
        String userAccount = user.getUserAccount();
        //重新生成密钥信息
        String accessKey = DigestUtils.md5DigestAsHex((SALT + userAccount + RandomUtil.randomNumbers(5)).getBytes());
        String secretKey = DigestUtils.md5DigestAsHex((SALT + userAccount + RandomUtil.randomNumbers(8)).getBytes());
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        //更新用户密钥
        return this.updateById(user);
    }
}
