package com.spring.sharepod.v1.repository.Liked;

import java.util.Optional;

public interface LikedRepositoryCustom {

    Boolean existLikedBoolean(Long userId,Long boardId);
}
