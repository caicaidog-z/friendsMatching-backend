package com.qimu.jujiao.utils;

import com.qimu.jujiao.model.vo.CommentVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentVosUtils {
    /**
     * 构建评论树
     * @param list
     * @return
     */
    public static List<CommentVo> processCommentVos(List<CommentVo> list) {
        Map<Long, CommentVo> map = new HashMap<>();   // (id, CommentVo)
        List<CommentVo> result = new ArrayList<>();
        // 将所有根评论加入 map
        for(CommentVo comment : list) {
            if(comment.getParentId() == 0)
                result.add(comment);
            map.put(comment.getId(), comment);
        }
        // 子评论加入到父评论的 child 中
        for(CommentVo comment : list) {
            Long id = comment.getParentId();
            if(id != 0) {   // 当前评论为子评论
                CommentVo p = map.get(id);
                if(p.getChildren() == null)    // child 为空，则创建
                    p.setChildren(new ArrayList<>());
                p.getChildren().add(comment);
            }
        }
        return result;
    }

}
