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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "Comment Service")
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

    public ResponseEntity<CommentResponseDto> likeComment(UserDetailsImpl userDetails, Long postId, Long id) {
        Comment comment = findComment(postId, id);

        for (Like like : comment.getLikes()) {
            if(like.getUser().getId() == userDetails.getUser().getId()){
                likeRepository.delete(like);
                comment.updateLikeToComment(like);
                return ResponseEntity.ok(new CommentResponseDto(comment));
            }
        }
        Like like = Like.builder().user(userDetails.getUser()).comment(comment).build();
        like = likeRepository.save(like);
        comment.updateLikeToComment(like);

        return ResponseEntity.ok(new CommentResponseDto(comment));
    }

    private Comment findComment(Long postId, Long id) {
        return commentRepository.findByPostIdAndId(postId, id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다."));
    }

}
