package com.example.springlevel5.service;

import com.example.springlevel5.dto.ErrorResponseDto;
import com.example.springlevel5.dto.PostRequestDto;
import com.example.springlevel5.dto.PostResponseDto;
import com.example.springlevel5.entity.LikePost;
import com.example.springlevel5.entity.Post;
import com.example.springlevel5.entity.QPost;
import com.example.springlevel5.entity.User;
import com.example.springlevel5.repository.LikeRepository;
import com.example.springlevel5.repository.PostRepository;
import com.example.springlevel5.security.UserDetailsImpl;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.springlevel5.entity.Post.qPost;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Post Service")
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final JPAQueryFactory jpaQueryFactory;

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

    public Page<PostResponseDto> getPostPage(int page, int size,
                                             String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> postPage = postRepository.findAll(pageable);

        return postPage.map(PostResponseDto::new);
    }

    public PostResponseDto getPost(Long id) {
        return new PostResponseDto(findPost(id));
    }

    @Transactional
    public ResponseEntity<PostResponseDto> updatePost(UserDetailsImpl userDetails, Long id, PostRequestDto requestDto) {
        User user = userDetails.getUser();
        Post post = findPost(id);

        User.checkAuthority(post.getUser(), user);

        post.update(requestDto);

        return ResponseEntity.status(200).body(new PostResponseDto(post));
    }

    @Transactional
    public void updateJQueryPost(UserDetailsImpl userDetails, Long id, PostRequestDto requestDto, HttpServletResponse response) {
        User user = userDetails.getUser();
        QPost post = new QPost("post");
        Post post1 = jpaQueryFactory.selectFrom(post).fetchOne();

    }

    public ResponseEntity<ErrorResponseDto> deletePost(UserDetailsImpl userDetails, Long id) {
        User user = userDetails.getUser();
        Post post = findPost(id);

        User.checkAuthority(post.getUser(), user);

        postRepository.delete(post);

        ErrorResponseDto responseDto = ErrorResponseDto.builder(200, "게시물 삭제 성공")
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Transactional
    public ResponseEntity<ErrorResponseDto> deletePostQueryDslTest(Long user_id, Long id) {
//        User user = userDetails.getUser();
        ErrorResponseDto responseDto = null;
        Tuple post = jpaQueryFactory
                .select(qPost.id, qPost.user.id)
                .from(qPost)
                .where(qPost.id.eq(id))
                .fetchOne();

        if(user_id == post.get(qPost.user.id)){
            jpaQueryFactory.delete(qPost).where(qPost.id.eq(id)).execute();
            responseDto = ErrorResponseDto.builder(200, "게시물 삭제 성공")
                    .build();
        }
        else {
            responseDto = ErrorResponseDto.builder(200, "게시물 삭제 실패")
                    .build();
        }

        return ResponseEntity.ok(responseDto);
    }

    protected Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다."));
    }


    @Transactional
    public ResponseEntity<PostResponseDto> likePost(UserDetailsImpl userDetails, Long id) {
        Post currentPost = findPost(id);

        LikePost likePost = currentPost.changeLike(userDetails.getUser());
        if(likePost == null){
            likePost = new LikePost(currentPost, userDetails.getUser());
            likePost = likeRepository.save(likePost);
            currentPost.addLike(likePost);
            return ResponseEntity.ok(new PostResponseDto(currentPost));
        }
        currentPost.DeleteLike(likePost);
        likeRepository.delete(likePost);

        currentPost = postRepository.save(currentPost);

        return ResponseEntity.ok(new PostResponseDto(currentPost));
    }
}
