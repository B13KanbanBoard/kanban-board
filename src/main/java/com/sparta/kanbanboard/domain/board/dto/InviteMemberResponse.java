package com.sparta.kanbanboard.domain.board.dto;

import com.sparta.kanbanboard.domain.member.dto.ProfileResponse;
import com.sparta.kanbanboard.domain.member.entity.Member;
import java.util.List;
import lombok.Builder;

@Builder
public record InviteMemberResponse(Long boardId, List<ProfileResponse> profileResponseList) {

    public static InviteMemberResponse of(Long boardId, List<Member> memberList) {
        return InviteMemberResponse.builder()
                .boardId(boardId)
                .profileResponseList(memberList.stream().map(ProfileResponse::of).toList())
                .build();
    }

}
