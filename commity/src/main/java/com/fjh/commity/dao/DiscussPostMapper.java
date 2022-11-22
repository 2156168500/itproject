package com.fjh.commity.dao;

import com.fjh.commity.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectAllDiscussPost(int userId,int offset,int limit);
    int selectCount(@Param("userId") int userId);

}
