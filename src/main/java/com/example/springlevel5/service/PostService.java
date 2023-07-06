package com.example.springlevel5.service;

import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.dto.PostRequestDto;
import com.example.springlevel5.dto.PostResponseDto;
import com.example.springlevel5.entity.Like;
import com.example.springlevel5.entity.Post;
import com.example.springlevel5.entity.User;
import com.example.springlevel5.repository.LikeRepository;
import com.example.springlevel5.repository.PostRepository;
import com.example.springlevel5.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Post Service")
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final LikeRepository likeRepository;

    public ResponseEntity<PostResponseDto> createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = new Post(requestDto, userDetails.getUser());
        Post savedPost = postRepository.save(post);

        return ResponseEntity.status(201).body(new PostResponseDto(savedPost));
    }

    public List<PostResponseDto> getPosts() {
        return postRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostResponseDto::new)
                .toList();
    }

    public PostResponseDto getPost(Long id) {
        return new PostResponseDto(findPost(id));
    }

    @Transactional
    public ResponseEntity<PostResponseDto> updatePost(UserDetailsImpl userDetails, Long id, PostRequestDto requestDto) {
        User user = userDetails.getUser();
        Post post = findPost(id);

        if (!userService.isAdmin(user)) {
            if (!user.getUsername().equals(post.getUsername())) {
                throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
            }
        }
        post.update(requestDto);

        return ResponseEntity.status(200).body(new PostResponseDto(post));
    }

    public ResponseEntity<ErrorResponseDto> deletePost(UserDetailsImpl userDetails, Long id) {
        User user = userDetails.getUser();
        Post post = findPost(id);

        if (!userService.isAdmin(user)) {
            if (!user.getUsername().equals(post.getUsername())) {
                throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
            }
        }
        postRepository.delete(post);

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(200L)
                .error("게시물 삭제 성공")
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Transactional
    public ResponseEntity<PostResponseDto> likePost(UserDetailsImpl userDetails, Long id) {
        User user = userDetails.getUser();
        Post post = findPost(id);

        Optional<Like> optionalLike = likeRepository.findByUserAndPost(user, post);
        if (optionalLike.isPresent()) { // 이미 좋아요한 경우
            // 좋아요 취소
            likeRepository.delete(optionalLike.get());
            post.updateLike(false);
        } else {
            // 좋아요 하기
            Like like = Like.builder()
                    .user(user)
                    .post(post)
                    .build();
            likeRepository.save(like);
            post.updateLike(true);
        }

        return ResponseEntity.ok(new PostResponseDto(post));
    }

    protected Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다."));
    }

}
