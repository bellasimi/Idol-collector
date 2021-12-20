package com.idolcollector.idolcollector.web.controller;

import com.idolcollector.idolcollector.service.MemberService;
import com.idolcollector.idolcollector.service.PostService;
import com.idolcollector.idolcollector.web.dto.member.MemberResponseDto;
import com.idolcollector.idolcollector.web.dto.pageresponsedto.CardDetailPageDto;
import com.idolcollector.idolcollector.web.dto.pageresponsedto.MemberDetailPageDto;
import com.idolcollector.idolcollector.web.dto.pageresponsedto.RootPageDto;
import com.idolcollector.idolcollector.web.dto.post.HomePostListResponseDto;
import com.idolcollector.idolcollector.web.dto.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final PostService postService;
    private final MemberService memberService;


    @GetMapping("/home/{page}")
    public RootPageDto homePageList(@PathVariable(name = "page", required = false) Optional<Integer> page) {

        int pageNum = 0;
        if (page.isPresent()) pageNum = page.get();

        List<HomePostListResponseDto> homePostListResponseDtos = postService.scorePostList(pageNum);

        // 세션에서 멤버정보 받아오기
            MemberResponseDto memberResponseDto = memberService.testMember();


        return new RootPageDto(homePostListResponseDtos, memberResponseDto);
    }

    @GetMapping("/card/{id}")
    public CardDetailPageDto detail(@PathVariable("id") Long id) {

        PostResponseDto post = postService.detail(id);

        // 세션에서 멤버정보 받아오기
        MemberResponseDto member = memberService.testMember();

        return new CardDetailPageDto(post, member);
    }

    

}
