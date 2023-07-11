package com.example.springlevel5.entity;

import com.example.springlevel5.exception.CustomResponseException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) // Enum 타입 저장할때 사용
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    protected boolean isAdmin(){
        return this.getRole().equals(UserRoleEnum.ADMIN);
    }

    public boolean equalId(User user){
        return this.id == user.id;
    }

    public boolean equalId(Long id){
        return this.id == id;
    }

    /**
     * original is posting User, guest is Authority User
     * @param original
     * @param guest
     */
    public static void checkAuthority(User original, User guest)
    {
        if(original.equalId(guest))
            if(!guest.isAdmin())
                throw new CustomResponseException(HttpStatus.BAD_REQUEST, "해당 내용은 작성자만 수정, 삭제 할 수 있습니다.");
    }
}

