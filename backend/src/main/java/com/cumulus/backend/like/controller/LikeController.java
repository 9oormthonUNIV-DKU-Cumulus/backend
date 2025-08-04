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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@Tag(name = "좋아요 기능")
@Slf4j
public class LikeController {

    private final JwtUtil jwtUtil;
    private final LikeService likeService;

    @GetMapping("club/{id}")
    @Operation(summary = "동아리 좋아요 여부 확인",
            description = "해당 유저가 동아리에 좋아요를 등록했는지 확인합니다.(true/false값) 프론트에서 이거 호출해서 좋아요 등록된 상태면 취소 / 등록되지 않은 상태면 등록하세요")
    public ResponseEntity<ApiResponse<?>> checkClubLike(
            @Parameter(description = "동아리Id", required = true) @PathVariable("id") Long clubId,
            HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        boolean isClubLiked = likeService.checkClubLike(userId, clubId);
        return ResponseEntity.ok(ApiResponse.success(isClubLiked));
    }

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

    @PostMapping("club/{id}")
    @Operation(summary = "동아리 좋아요 등록취소(삭제)", description = "해당 유저가 동아리에 등록한 좋아요를 취소(삭제)합니다.")
    public ResponseEntity<ApiResponse<?>> deleteClubLike(
            @Parameter(description = "동아리Id", required = true) @PathVariable("id") Long clubId,
            HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        likeService.deleteClubLike(userId, clubId);
        log.info("동아리 좋아요 삭제요청 완료되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("동아리 좋아요 삭제했습니다."));
    }

    @GetMapping("activity/{id}")
    @Operation(summary = "모임글 좋아요 여부 확인",
            description = "해당 유저가 모임글에 좋아요를 등록했는지 확인합니다.(true/false값) 프론트에서 이거 호출해서 좋아요 등록된 상태면 취소 / 등록되지 않은 상태면 등록하세요")
    public ResponseEntity<ApiResponse<?>> checkActivityLike(
            @Parameter(description = "모임글Id", required = true) @PathVariable("id") Long activityId,
            HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        boolean isActivityLiked = likeService.checkActivityLike(userId, activityId);
        return ResponseEntity.ok(ApiResponse.success(isActivityLiked));
    }

    @PostMapping("activity/{id}")
    @Operation(summary = "모임글 좋아요 등록", description = "해당 유저가 모임글에 좋아요를 등록합니다.")
    public ResponseEntity<ApiResponse<?>> createActivityLike(
            @Parameter(description = "모임글Id", required = true) @PathVariable("id") Long activityId,
            HttpServletRequest request
    ){
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.extractUserId(token, false);

        Like activityLike = likeService.createActivityLike(userId, activityId);
        log.info("모임글({}) 좋아요 등록합니다 - 좋아요 등록번호 : {}", activityId, activityLike.getId() );
        return ResponseEntity.ok(ApiResponse.success("모임글 좋아요 등록했습니다."));
    }

}
