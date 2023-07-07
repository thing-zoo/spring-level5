package com.example.springlevel5.controller;

import com.example.springlevel5.dto.CommentRequestDto;
import com.example.springlevel5.dto.CommentResponseDto;
import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.security.UserDetailsImpl;
import com.example.springlevel5.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long postId,
                                                            @RequestBody @Valid CommentRequestDto requestDto) {
        return commentService.createComment(userDetails, postId, requestDto);
    }

    @PostMapping("/comments/{id}/comments")
    public ResponseEntity<CommentResponseDto> createCommentByComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long postId,
                                                            @PathVariable Long id,
                                                            @RequestBody @Valid CommentRequestDto requestDto) {
        return commentService.createCommentByComment(userDetails, postId, id, requestDto);
    }
    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long postId,
                                                            @PathVariable Long id,
                                                            @RequestBody @Valid CommentRequestDto requestDto) {
        return commentService.updateComment(userDetails, postId, id, requestDto);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ErrorResponseDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @PathVariable Long postId,
                                                          @PathVariable Long id) {
        return commentService.deleteComment(userDetails, postId, id);
    }

    @PostMapping("/comments/{id}/likes")
    public ResponseEntity<CommentResponseDto> likeComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @PathVariable Long postId,
                                                          @PathVariable Long id) {
        return commentService.likeComment(userDetails, postId, id);
    }
}
