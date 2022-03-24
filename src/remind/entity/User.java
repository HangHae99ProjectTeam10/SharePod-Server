package com.spring.sharepod.entity;

import com.spring.sharepod.dto.request.User.UserModifyRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class User extends Timestamped implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //유저 아이디 (이메일 형태)
    @Column(nullable = false, unique = true)
    private String username;

    // 유저 비밀번호
    @Column(nullable = false)
    private String password;

    //유저 닉네임
    @Column(nullable = false, unique = true)
    private  String nickname;

    // 유저 프로필 이미지
    @Column
    private String userimg;

    //유저 지역명
    @Column
    private String mapdata;

    //Reservation : User => 해당 userid의 요청 목록을 가져오기 위한 양방향 설정
    @OneToMany(mappedBy = "seller")
    private List<Reservation> reservation = new ArrayList<>();

    //Auth : User => 해당 authbuyer 요청 목록을 가져오기 위한 양방향 설정
    @OneToMany(mappedBy = "authbuyer")
    private List<Auth> authbuyerlist  = new ArrayList<>();

    //Auth : User => 해당 authseller 요청 목록을 가져오기 위한 양방향 설정
    @OneToMany(mappedBy = "authseller")
    private List<Auth> authsellerlist = new ArrayList<>();


    //Board: User => 회원탈퇴 시 글에 대한 삭제를 위한 양방향 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Board> boardlist = new ArrayList<>();

    //Notice: User => 회원탈퇴 시 글에 대한 삭제를 위한 양방향 설정
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.REMOVE)
    private List<Notice> noticeBuyerList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.REMOVE)
    private List<Notice> noticeSellerList = new ArrayList<>();










    //유저 이미지가 변경 되었을 때
    public void update1(UserModifyRequestDTO modifyRequestDTO){
        this.username = modifyRequestDTO.getUsername();
        this.nickname = modifyRequestDTO.getNickname();
        this.mapdata = modifyRequestDTO.getMapdata();
        this.userimg = modifyRequestDTO.getUserimg();
    }
    // 아닐때
    public void update2(UserModifyRequestDTO modifyRequestDTO){
        this.username = modifyRequestDTO.getUsername();
        this.nickname = modifyRequestDTO.getNickname();
        this.mapdata = modifyRequestDTO.getMapdata();
    }

    ///////////////////////////////////////////
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}



