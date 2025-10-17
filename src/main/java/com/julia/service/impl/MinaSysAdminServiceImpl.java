package com.julia.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.config.AdminToken;
import com.julia.entity.MinaSysAdminEntity;
import com.julia.mapper.MinaSysAdminMapper;
import com.julia.model.dto.LoginDto;
import com.julia.service.IMinaSysAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.springframework.stereotype.Service;
import com.julia.model.vo.MinaSysAdminEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;
/**
* <p>
    * 账号 服务实现类
    * </p>
*
* @author chowel
* @since 2025-10-17
*/
@Service
public class MinaSysAdminServiceImpl extends ServiceImpl<MinaSysAdminMapper, MinaSysAdminEntity> implements IMinaSysAdminService {
    @Override
    public Page<MinaSysAdminEntityVO> findForPage(QueryPagement queryPagement) {
        Page<MinaSysAdminEntity> p = new LambdaQueryChainWrapper<MinaSysAdminEntity>(getBaseMapper()).page(new Page<MinaSysAdminEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<MinaSysAdminEntityVO> page = JuliaUtils.convertTo(new Page<MinaSysAdminEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new MinaSysAdminEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public MinaSysAdminEntityVO findOneById(Long id) {
            MinaSysAdminEntity entity = getById(id);
            return JuliaUtils.convertTo(new MinaSysAdminEntityVO(), entity);
    }

    @Override
    public MinaSysAdminEntityVO login(LoginDto dto) {
        MinaSysAdminEntity admin = this.getOne(new QueryWrapper<MinaSysAdminEntity>().eq("login_name", dto.getName()));

        if (ObjectUtils.isEmpty(admin)) {
            throw new JuliaException("用户不存在");
        }

        if (admin.getCheDel() == 2) {
            throw new JuliaException("用户禁用");
        }

        if (!BCrypt.checkpw(dto.getPassword(), admin.getPassword())) {
            throw new JuliaException("密码错误");
        }

//        List<PactEntity> pactList  =  pactMapper.getPackByPowerId(yao.getRoleId());

        MinaSysAdminEntityVO vo = JuliaUtils.convertTo(new MinaSysAdminEntityVO(), admin);

//        vo.setMenus();
        AdminToken.login(admin.getYaoId());
        vo.setToken(AdminToken.getTokenValue());
        vo.setPassword("******");
        return vo;
    }

    @Override
    public Boolean saveMinaSysAdminEntity(MinaSysAdminEntityVO vo) {
            return save(JuliaUtils.convertTo(new MinaSysAdminEntity(), vo));
    }

    @Override
    public Boolean alter(MinaSysAdminEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new MinaSysAdminEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

