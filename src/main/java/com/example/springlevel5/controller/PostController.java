package com.example.springlevel5.controller;

import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.dto.PostRequestDto;
import com.example.springlevel5.dto.PostResponseDto;
import com.example.springlevel5.security.UserDetailsImpl;
import com.example.springlevel5.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "Post Controller")
public class PostController {
    private final PostService postService;

    // 게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails);
    }

    // 전체 게시글 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    // 선택한 게시글 조회
    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 선택한 게시글 수정
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable Long id,
                                                      @RequestBody PostRequestDto requestDto) {
        return postService.updatePost(userDetails, id, requestDto);
    }

    // 선택한 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ErrorResponseDto> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable Long id) {
        return postService.deletePost(userDetails, id);
    }

    @PostMapping("/posts/{id}/likes")
    public ResponseEntity<PostResponseDto> likePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @PathVariable Long id) {
        return postService.likePost(userDetails, id);
    }
}
