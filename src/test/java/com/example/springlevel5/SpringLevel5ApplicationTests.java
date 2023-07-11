package com.example.springlevel5;

import com.example.springlevel5.entity.Post;
import com.example.springlevel5.entity.QPost;
import com.example.springlevel5.entity.User;
import com.example.springlevel5.security.UserDetailsImpl;
import com.example.springlevel5.service.PostService;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringLevel5ApplicationTests {

    @Test
    void contextLoads() {
    }


    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Autowired
    PostService postService;

    @Test
    void Test1(){
        QPost qPost = new QPost("post");
        Long id = 1L;
        Tuple post = jpaQueryFactory
                .select(qPost.id, qPost.user.id, qPost.title, qPost.content)
                .from(qPost)
                .where(qPost.id.eq(id))
                .fetchFirst();
        System.out.println("tuple.get(qPost.id) = " + post.get(qPost.id));
        System.out.println("tuple.get(qPost.title) = " + post.get(qPost.title));
        System.out.println("tuple.get(qPost.content) = " + post.get(qPost.content));

    }

    @Test
    void Test2(){
        postService.deletePostQueryDslTest(1L, 1L);
    }
}
