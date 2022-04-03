package com.spring.sharepod.v1.controller;

import com.spring.sharepod.entity.User;
import com.spring.sharepod.v1.dto.request.ChatRoomRequestDto;
import com.spring.sharepod.v1.dto.response.ChatRoomResponseDto;
import com.spring.sharepod.v1.service.ChatRoomSerivce;
import com.spring.sharepod.v1.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomSerivce chatRoomSerivce;
    private final TokenValidator tokenValidator;


    // 27번 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoomResponseDto.ChatRoomData createRoom(@RequestBody ChatRoomRequestDto.Create create, @AuthenticationPrincipal User user) {
        tokenValidator.userIdCompareToken(create.getBuyerId(), user.getId());
        return chatRoomSerivce.createChatRoom(create);
    }

    // 28번 채팅 리스트 받아오기
    @GetMapping("/room/{userId}")
    @ResponseBody
    public ChatRoomResponseDto.ChatRoomListData chatList(@PathVariable Long userId, @AuthenticationPrincipal User user) {
        tokenValidator.userIdCompareToken(userId,user.getId());
        return chatRoomSerivce.findChatList(userId);
    }

    // 29번 해당 채팅방 채팅내용 반환
    @GetMapping("/roomslist/{userId}/{chatroomId}")
    @ResponseBody
    public ChatRoomResponseDto.ChatMessageListData roomChatList(@PathVariable Long userId, @PathVariable Long chatroomId,
                                                                @RequestParam(value = "time", required = false) LocalDateTime localDateTime, @AuthenticationPrincipal User user) {
        tokenValidator.userIdCompareToken(userId,user.getId());
        return chatRoomSerivce.roomChatListService(userId, chatroomId, localDateTime);
    }
}
