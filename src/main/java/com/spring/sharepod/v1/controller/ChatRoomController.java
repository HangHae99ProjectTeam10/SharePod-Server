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

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomSerivce chatRoomSerivce;
    private final TokenValidator tokenValidator;

    // 채팅 클릭 화면
    @GetMapping("/clickbtn")
    public String clickbtn(Model model) {
        return "/chat/clickbtn";
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomid}")
    public String roomDetail(Model model, @PathVariable String roomid) {
        model.addAttribute("roomid", roomid);
        return "/chat/roomdetail";
    }

    /////////////////////////// api 시작

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoomResponseDto.ChatRoomData createRoom(@RequestBody ChatRoomRequestDto.Create create, @AuthenticationPrincipal User user) {
        tokenValidator.userIdCompareToken(create.getBuyerId(), user.getId());
        return chatRoomSerivce.createChatRoom(create);
    }

    // 채팅 리스트 받아오기
    // stress_test
    // userid = 2
    @GetMapping("/room/{userId}")
    @ResponseBody
    public ChatRoomResponseDto.ChatRoomListData chatList(@PathVariable Long userId, @AuthenticationPrincipal User user) {
        tokenValidator.userIdCompareToken(userId,user.getId());
        return chatRoomSerivce.findChatList(userId);
    }

    //해당 채팅방 채팅내용 반환
    // stress_test.
    // userid = 2, chatroomid = 7
    @GetMapping("/roomslist/{userId}/{chatroomId}")
    @ResponseBody
    public ChatRoomResponseDto.ChatMessageListData roomChatList(@PathVariable Long userId, @PathVariable Long chatroomId, @RequestParam(value = "startNum", defaultValue = "0") int startNum, @AuthenticationPrincipal User user) {
        tokenValidator.userIdCompareToken(userId,user.getId());
        return chatRoomSerivce.roomChatListService(userId, chatroomId, startNum);
    }
}
