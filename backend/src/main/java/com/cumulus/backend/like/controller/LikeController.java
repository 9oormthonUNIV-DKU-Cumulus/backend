package com.cumulus.backend.like.controller;

import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.like.domain.Like;
import com.cumulus.backend.like.service.LikeService;
import com.cumulus.backend.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@Tag(name = "좋아요")
@Slf4j
public class LikeController {

    private final JwtUtil jwtUtil;
    private final LikeService likeService;

    @PostMapping("club/{id}")
    @Operation(summary = "동아리 좋아요 등록", description = "해당 유저가 동아리에 좋아요를 등록합니다.")
    public ResponseEntity<ApiResponse<?>> createClubLike(
            @Parameter(description = "동아리Id", required = true) @PathVariable("id") Long clubId,
            HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        Like clubLike = likeService.createClubLike(userId, clubId);
        log.info("동아리({}) 좋아요 등록합니다 - 좋아요 등록번호 : {}", clubId, clubLike.getId() );
        return ResponseEntity.ok(ApiResponse.success("동아리 좋아요 등록했습니다."));
    }
}
