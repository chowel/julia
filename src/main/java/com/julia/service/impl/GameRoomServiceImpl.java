package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GameRoomEntity;
import com.julia.entity.PlayersEntity;
import com.julia.entity.RoomPlayerEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.GameRoomMapper;
import com.julia.mapper.PlayersMapper;
import com.julia.mapper.RoomPlayerMapper;
import com.julia.model.PlayerRo;
import com.julia.service.IGameRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import com.julia.tool.RedisUtils;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameRoomEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * <p>
 * 游戏房间 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2024-09-28
 */
@Service
public class GameRoomServiceImpl extends ServiceImpl<GameRoomMapper, GameRoomEntity> implements IGameRoomService {

    @Resource
    PlayersMapper playersMapper;

    @Resource
    RoomPlayerMapper roomPlayerMapper;

    @Resource
    RedisUtils redisUtils;

    @Override
    public Page<GameRoomEntityVO> findForPage(QueryPagement queryPagement) {
        Page<GameRoomEntity> p = new LambdaQueryChainWrapper<GameRoomEntity>(getBaseMapper()).page(new Page<GameRoomEntity>(queryPagement.getStartPage(),
                queryPagement.getPageSize()));
        Page<GameRoomEntityVO> page = JuliaUtils.convertTo(new Page<GameRoomEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new GameRoomEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public GameRoomEntityVO findOneById(Long id) {
        GameRoomEntity entity = getById(id);
        return JuliaUtils.convertTo(new GameRoomEntityVO(), entity);
    }

    @Override
    public GameRoomEntityVO findOneByFlag(String flag) {
        GameRoomEntity entity = this.getOne(new QueryWrapper<GameRoomEntity>().eq("flag", flag));
        return JuliaUtils.convertTo(new GameRoomEntityVO(), entity);
    }

    @Override
    public Boolean saveGameRoomEntity(GameRoomEntityVO vo) {
        PlayersEntity player = playersMapper.selectById(vo.getPlayerId());
        if (ObjectUtils.isEmpty(player)) {
            throw new JuliaException("用户不存在");
        }
        if (player.getStatus() == 0) {
            throw new JuliaException("用户异常");
        }
        GameRoomEntity gameRoomEntity = JuliaUtils.convertTo(new GameRoomEntity(), vo);

        if (save(gameRoomEntity)) {
            RoomPlayerEntity roomPlayer = new RoomPlayerEntity();
            roomPlayer.setGameType(gameRoomEntity.getGameType());
            roomPlayer.setRoomId(gameRoomEntity.getRoomId());
            roomPlayer.setPlayerId(player.getPlayId());
            roomPlayer.setNickName(player.getNickName());
            roomPlayer.setRoomFlag(gameRoomEntity.getFlag());
            roomPlayer.setInit(1);

            if (roomPlayerMapper.insert(roomPlayer) < 1) {
                return false;
            } else {
                //
                String lastKey = gameRoomEntity.getGameType() + "_" + gameRoomEntity.getFlag();
                redisUtils.incr(RedisKeyEnum.ROOMMAXPLAYERS.getKey() + lastKey,
                        gameRoomEntity.getPlayers() - 1);
                redisUtils.sSet(RedisKeyEnum.ROOMPLAYERS.getKey()+lastKey,JuliaUtils.convertTo(new PlayerRo(),player));
                return true;
            }
        }

        return false;
    }

    @Override
    public Boolean joinGameRoomEntity(GameRoomEntityVO vo) {
        // todo
        String lastKey = vo.getGameType() + "_" + vo.getFlag();
        int maxPlayer = (int) redisUtils.get(RedisKeyEnum.ROOMMAXPLAYERS.getKey() + lastKey);
        if(maxPlayer<1){
            throw new JuliaException("房间人数已满");
        }
        PlayersEntity player = playersMapper.selectById(vo.getPlayerId());
        if (ObjectUtils.isEmpty(player)) {
            throw new JuliaException("用户不存在");
        }
        if (player.getStatus() == 0) {
            throw new JuliaException("用户异常");
        }
        GameRoomEntity gameRoomEntity = getOne(new QueryWrapper<GameRoomEntity>().eq("flag",vo.getFlag()));
        if (ObjectUtils.isEmpty(gameRoomEntity)) {
            throw new JuliaException("房间不存在");
        }
        RoomPlayerEntity roomPlayer = new RoomPlayerEntity();
        roomPlayer.setGameType(gameRoomEntity.getGameType());
        roomPlayer.setRoomId(gameRoomEntity.getRoomId());
        roomPlayer.setPlayerId(player.getPlayId());
        roomPlayer.setNickName(player.getNickName());
        roomPlayer.setRoomFlag(gameRoomEntity.getFlag());
        if (roomPlayerMapper.insert(roomPlayer) < 1) {
            return false;
        } else {
            //
            redisUtils.decr(RedisKeyEnum.ROOMMAXPLAYERS.getKey() + lastKey, 1);
            redisUtils.sSet(RedisKeyEnum.ROOMPLAYERS.getKey()+lastKey,JuliaUtils.convertTo(new PlayerRo(),player));
            return true;
        }
    }

    @Override
    public Boolean alter(GameRoomEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new GameRoomEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }
}

