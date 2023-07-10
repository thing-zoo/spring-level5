package com.example.springlevel5.service;

import com.example.springlevel5.dto.CommentRequestDto;
import com.example.springlevel5.dto.CommentResponseDto;
import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.dto.PostResponseDto;
import com.example.springlevel5.entity.*;
import com.example.springlevel5.repository.CommentRepository;
import com.example.springlevel5.repository.LikeRepository;
import com.example.springlevel5.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "Comment Service")
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final LikeRepository likeRepository;

    public ResponseEntity<CommentResponseDto> createComment(UserDetailsImpl userDetails, Long postId, CommentRequestDto requestDto){
        User currentUser = userDetails.getUser();
        Post currentPost = postService.findPost(postId);

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .post(currentPost)
                .user(currentUser)
                .build();

        comment = commentRepository.save(comment);

        CommentResponseDto responseDto = new CommentResponseDto(comment);

        return ResponseEntity.status(201).body(responseDto);
    }

    public ResponseEntity<CommentResponseDto> createCommentByComment(UserDetailsImpl userDetails,
                                                                     Long postId, Long id,
                                                                     CommentRequestDto requestDto){
        User currentUser = userDetails.getUser();
        Comment currentComment = findComment(postId, id);

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .parent(currentComment)
                .user(currentUser)
                .build();

        comment = commentRepository.save(comment);
        currentComment.addChild(comment);

        CommentResponseDto responseDto = new CommentResponseDto(currentComment);

        return ResponseEntity.status(201).body(responseDto);
    }

    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(UserDetailsImpl userDetails, Long postId, Long id, CommentRequestDto requestDto) {
        User currentUser = userDetails.getUser();
        Comment comment = findComment(postId, id);

        User.checkAuthority(comment.getUser(), currentUser);
        comment.update(requestDto);

        return ResponseEntity.status(200).body(new CommentResponseDto(comment));
    }

    public ResponseEntity<ErrorResponseDto> deleteComment(UserDetailsImpl userDetails, Long postId, Long id) {
        User currentUser = userDetails.getUser();
        Comment comment = findComment(postId, id);

        User.checkAuthority(comment.getUser(), currentUser);
        commentRepository.delete(comment);

        return ResponseEntity.ok(
                ErrorResponseDto.builder(200,"댓글 삭제 완료")
                        .build()
        );
    }

    @Transactional
    public ResponseEntity<CommentResponseDto> likeComment(UserDetailsImpl userDetails, Long postId, Long id) {
        Comment comment = findComment(postId, id);

        LikeComment like = comment.changeLike(userDetails.getUser());
        if(like == null){
            like = new LikeComment(comment, userDetails.getUser());
            like = likeRepository.save(like);
            comment.addLike(like);
            return ResponseEntity.ok(new CommentResponseDto(comment));
        }
        comment.DeleteLike(like);
        likeRepository.delete(like);

        comment = commentRepository.save(comment);

        return ResponseEntity.ok(new CommentResponseDto(comment));
    }

    private Comment findComment(Long postId, Long id) {
        return commentRepository.findByPostIdAndId(postId, id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다."));
    }

}
