package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.MinaGameEntity;
import com.julia.enums.RedisKeys;
import com.julia.mapper.MinaGameMapper;
import com.julia.model.game.MinaGame;
import com.julia.service.IMinaGameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.GameUtils;
import com.julia.tool.RedisUtils;
import org.springframework.stereotype.Service;
import com.julia.model.vo.MinaGameEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * Game 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2025-10-27
 */
@Service
public class MinaGameServiceImpl extends ServiceImpl<MinaGameMapper, MinaGameEntity> implements IMinaGameService {


    @Resource
    RedisUtils redisUtils;


    @Override
    public Page<MinaGameEntityVO> findForPage(QueryPagement queryPagement) {
        Page<MinaGameEntity> p = new LambdaQueryChainWrapper<MinaGameEntity>(getBaseMapper()).page(new Page<MinaGameEntity>(queryPagement.getStartPage(),
                queryPagement.getPageSize()));
        Page<MinaGameEntityVO> page = JuliaUtils.convertTo(new Page<MinaGameEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new MinaGameEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public MinaGameEntityVO findOneById(Long id) {
        MinaGameEntity entity = getById(id);
        return JuliaUtils.convertTo(new MinaGameEntityVO(), entity);
    }

    @Override
    public Boolean saveMinaGameEntity(MinaGameEntityVO vo) {
        return save(JuliaUtils.convertTo(new MinaGameEntity(), vo));
    }

    @Override
    public Boolean alter(MinaGameEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new MinaGameEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

//    @Override
//    public List<MinaGame> genGameByPlayerId(Long playerId, int len, int type) {
//        List<MinaGame> genGames = new ArrayList<>(len);
//
//            for (int i = 0; i < len; i++) {
//                MinaGame g = new MinaGame();
//                g.setGameNo(JuliaUtils.genGameNo(playerId));
//                g.setPlayerId(playerId);
//                g.setStatus(type);
//                if(type == 1){
//                    g.setContents(GameUtils.genGameFri(15));
//                    redisUtils.set(RedisKeys.FRIDAYGAMEPOOL.getKey(g.getGameNo()), g, 3600 * 48);
//                }
//
//                if(type == 2){
//                    g.setContents(GameUtils.genGameSta(30));
//                    redisUtils.set(RedisKeys.STATURDAYGAMEPOOL.getKey(g.getGameNo()), g, 3600 * 48);
//                }
//                genGames.add(g);
//            }
//            return genGames;
//
//    }

}

