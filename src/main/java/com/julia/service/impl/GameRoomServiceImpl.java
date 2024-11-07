package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.GameEntity;
import com.julia.entity.GameRoomEntity;
import com.julia.entity.PlayersEntity;
import com.julia.entity.RoomPlayerEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.GameMapper;
import com.julia.mapper.GameRoomMapper;
import com.julia.mapper.PlayersMapper;
import com.julia.mapper.RoomPlayerMapper;
import com.julia.model.AliveGameRo;
import com.julia.model.PlayerRo;
import com.julia.model.vo.PlayersEntityVO;
import com.julia.service.IGameRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.service.IGameService;
import com.julia.service.IPlayersService;
import com.julia.socket.ChannelPond;
import com.julia.tool.*;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameRoomEntityVO;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 游戏房间 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2024-09-28
 */
@Log4j2
@Service
public class GameRoomServiceImpl extends ServiceImpl<GameRoomMapper, GameRoomEntity> implements IGameRoomService {

    @Resource
    IPlayersService playersService;

    @Resource
    RoomPlayerMapper roomPlayerMapper;

    @Resource
    IGameService gameService;

    @Resource
    RedisUtils redisUtils;

    private final ObjectMapper mapper = new ObjectMapper();

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
        GameRoomEntity entity = (GameRoomEntity) redisUtils.get(RedisKeyEnum.ROOMCACHE.getKey() + flag);
        if (ObjectUtils.isEmpty(entity)) {
            entity = this.getOne(new QueryWrapper<GameRoomEntity>().eq("flag", flag));
            if (ObjectUtils.isEmpty(entity)) {
                return null;
            } else {
                redisUtils.set(RedisKeyEnum.ROOMCACHE.getKey() + flag, entity, 3600 * 6);
            }
        }
        return JuliaUtils.convertTo(new GameRoomEntityVO(), entity);
    }

    @Override
    public GameRoomEntityVO saveGameRoomEntity(GameRoomEntityVO vo) {
        PlayersEntityVO player = playersService.findOneById(Long.valueOf(vo.getPlayerId()));
        if (ObjectUtils.isEmpty(player)) {
            throw new JuliaException("用户不存在");
        }
        if (player.getStatus() == 0) {
            throw new JuliaException("用户异常");
        }
        GameRoomEntityVO entityVO = findOneByFlag(vo.getFlag());

        if (!ObjectUtils.isEmpty(entityVO) && entityVO.getStatus() != 2) {
            throw new JuliaException("房间名称重复");
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
                return null;
            } else {
                //
                String ROOMPLAYERKEY = RedisKeyEnum.ROOMPLAYERS.getKey() + gameRoomEntity.getGameType() + ":" + gameRoomEntity.getFlag();
//                redisUtils.incr(RedisKeyEnum.ROOMMAXPLAYERS.getKey() + lastKey,
//                        gameRoomEntity.getPlayers() - 1);
                redisUtils.sSet( ROOMPLAYERKEY, JuliaUtils.convertTo(new PlayerRo(), player));
                return JuliaUtils.convertTo(new GameRoomEntityVO(), gameRoomEntity);
            }
        }

        return null;
    }

    @Override
    public GameRoomEntityVO saveRoomByLaba(GameRoomEntityVO vo) {
        PlayersEntityVO player = playersService.findOneById(Long.valueOf(vo.getPlayerId()));
        if (ObjectUtils.isEmpty(player)) {
            throw new JuliaException("用户不存在");
        }
        if (player.getStatus() == 0) {
            throw new JuliaException("用户异常");
        }
        GameRoomEntity room = JuliaUtils.convertTo(new GameRoomEntity(), vo);
        // 房间标识
        room.setFlag(checkRoomFlag());
        room.setPlayers(1);
        room.setStatus(0);
        if (save(room)) {
            return JuliaUtils.convertTo(new GameRoomEntityVO(), room);
        }
        return null;
    }

    @Override
    public GameRoomEntityVO joinGameRoomEntity(GameRoomEntityVO vo) {
        // todo
        PlayersEntityVO player = playersService.findOneById(Long.valueOf(vo.getPlayerId()));
        if (ObjectUtils.isEmpty(player)) {
            throw new JuliaException("用户不存在");
        }
        if (player.getStatus() == 0) {
            throw new JuliaException("用户异常");
        }
        String ROOMPLAYERKEY = RedisKeyEnum.ROOMPLAYERS.getKey() + vo.getGameType() + ":" + vo.getFlag();
        GameRoomEntityVO room = findOneByFlag(vo.getFlag());
        if (ObjectUtils.isEmpty(room)) {
            throw new JuliaException("房间不存在");
        }

        if (player.getCoin() < room.getLeast()) {
            Set<Object> roomPlayers = redisUtils.sGet(ROOMPLAYERKEY);
            for (Object element : roomPlayers) {
                PlayerRo t = (PlayerRo) element;
                long joinId = t.getPlayId();
                int userId = vo.getPlayerId();
                if (joinId == (long) userId) {
                    redisUtils.setRemove(ROOMPLAYERKEY, t);
                    break;
                }
            }
            throw new JuliaException("玩家金额小于最少金额");
        }


        boolean checkJoin = false;

        Set<Object> roomPlayers = redisUtils.sGet(ROOMPLAYERKEY);

        RoomPlayerEntity rPlayer = roomPlayerMapper.selectOne(new QueryWrapper<RoomPlayerEntity>()
                .eq("player_id", player.getPlayId()));
        if (!ObjectUtils.isEmpty(rPlayer) && rPlayer.getOnline() == 0) {
            // 此人再次加入房间
            log.info("此人再次加入房间");
            checkJoin = true;
            rPlayer.setOnline(1);
            roomPlayerMapper.updateById(rPlayer);
        }

        if (checkJoin) {
            //
            redisUtils.sSet(ROOMPLAYERKEY, JuliaUtils.convertTo(new PlayerRo(), player));
            cheeckPoker(room.getGameType(), room.getFlag());
            return JuliaUtils.convertTo(new GameRoomEntityVO(), room);
        }


        if (roomPlayers.size() == room.getPlayers()) {
            throw new JuliaException("房间人数已满");
        }
        RoomPlayerEntity roomPlayer = new RoomPlayerEntity();
        roomPlayer.setGameType(room.getGameType());
        roomPlayer.setRoomId(room.getRoomId());
        roomPlayer.setPlayerId(player.getPlayId());
        roomPlayer.setNickName(player.getNickName());
        roomPlayer.setRoomFlag(room.getFlag());
        if (roomPlayerMapper.insert(roomPlayer) < 1) {
            return null;
        } else {
            redisUtils.sSet(ROOMPLAYERKEY, JuliaUtils.convertTo(new PlayerRo(), player));
            long players = redisUtils.sGetSetSize(ROOMPLAYERKEY);
            if (players == room.getPlayers()) {
                //  房间满员-发牌
                gameService.createThirteennGame(vo.getGameType(), vo.getFlag());
            }
            return JuliaUtils.convertTo(new GameRoomEntityVO(), room);
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

    @Override
    public Boolean close(String flag, Integer gameType) {
        GameRoomEntity entity = this.getOne(new QueryWrapper<GameRoomEntity>()
                .eq("flag", flag)
                .eq("game_type", gameType));
        if (ObjectUtils.isEmpty(entity)) {
            return false;
        }
        entity.setStatus(2);
        return updateById(entity);
    }


    @Override
    public AliveGameRo findAliveByUserId(Integer userId) {
        // todo
        AliveGameRo ro = (AliveGameRo) redisUtils.get(RedisKeyEnum.ALIVEGAME.getKey() + userId);
        if (!ObjectUtils.isEmpty(ro)) {
            GameRoomEntityVO vo = findOneByFlag(ro.getRoomIde());
            ro.setLeast(vo.getLeast());
            ro.setAgame(vo.getAgame());
        }
        return ro;
    }

    /**
     * @Description: 如果都在线，没有牌，就发牌
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    private void cheeckPoker(int gameType, String gameFlag) {
        boolean fullPlayer = true;
        Set<Object> players = redisUtils.sGet(gameType + "_" + gameFlag);
        for (Object element : players) {
            PlayerRo t = (PlayerRo) element;
            Channel userChannel = ChannelPond.findChannel(String.valueOf(t.getPlayId()));
            if (ObjectUtils.isEmpty(userChannel)) {
                fullPlayer = false;
            }
        }
        if (fullPlayer) {
            GameEntity game = gameService.findGameByRoom(gameType, gameFlag);
            if (ObjectUtils.isEmpty(game)) {
                gameService.createThirteennGame(gameType, gameFlag);
            }
        }
    }

    private String checkRoomFlag() {
        String flag = "";
        while (true) {
            String t = JuliaUtils.randomNickName();
            GameRoomEntityVO room = findOneByFlag(t);
            if (ObjectUtils.isEmpty(room)) {
                flag = t;
                break;
            }
        }
        return flag;
    }

}

