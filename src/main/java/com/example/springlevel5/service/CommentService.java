package com.example.springlevel5.service;

import com.example.springlevel5.dto.CommentRequestDto;
import com.example.springlevel5.dto.CommentResponseDto;
import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.entity.Comment;
import com.example.springlevel5.entity.Like;
import com.example.springlevel5.entity.Post;
import com.example.springlevel5.entity.User;
import com.example.springlevel5.repository.CommentRepository;
import com.example.springlevel5.repository.LikeRepository;
import com.example.springlevel5.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;


    public ResponseEntity<CommentResponseDto> createComment(UserDetailsImpl userDetails, Long postId, CommentRequestDto requestDto){
        User currentUser = userDetails.getUser();
        Post currentPost = postService.findPost(postId);

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .post(currentPost)
                .user(currentUser)
                .build();

        comment = commentRepository.save(comment);

        CommentResponseDto responseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();

        return ResponseEntity.status(201).body(responseDto);
    }

    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(UserDetailsImpl userDetails, Long postId, Long id, CommentRequestDto requestDto) {
        User currentUser = userDetails.getUser();
        Comment comment = findComment(postId, id);

        if(!userService.isAdmin(currentUser)){
            if(!comment.getUser().getUsername().equals(currentUser.getUsername()))
                throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        comment.update(requestDto);

        return ResponseEntity.status(200).body(new CommentResponseDto(comment));
    }

    public ResponseEntity<ErrorResponseDto> deleteComment(UserDetailsImpl userDetails, Long postId, Long id) {
        User currentUser = userDetails.getUser();
        Comment comment = findComment(postId, id);
        if(!userService.isAdmin(currentUser)){
            if(!comment.getUser().getUsername().equals(currentUser.getUsername()))
                throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);

        return ResponseEntity.ok(
                ErrorResponseDto.builder(200,"댓글 삭제 완료")
                        .build()
        );
    }

    @Transactional
    public ResponseEntity<CommentResponseDto> likeComment(UserDetailsImpl userDetails, Long postId, Long id) {
        User user = userDetails.getUser();
        Comment comment = findComment(postId, id);

        Optional<Like> optionalLike = likeRepository.findByUserAndComment(user, comment);
        if (optionalLike.isPresent()) { // 이미 좋아요한 경우
            // 좋아요 취소
            likeRepository.delete(optionalLike.get());
            comment.updateLike(false);
        } else {
            // 좋아요 하기
            Like like = Like.builder()
                    .user(user)
                    .comment(comment)
                    .build();
            likeRepository.save(like);
            comment.updateLike(true);
        }

        return ResponseEntity.ok(new CommentResponseDto(comment));
    }

    private Comment findComment(Long postId, Long id) {
        return commentRepository.findByPostIdAndId(postId, id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다."));
    }

}
