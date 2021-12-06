package com.idolcollector.idolcollector.service;

import com.idolcollector.idolcollector.domain.like.LikeType;
import com.idolcollector.idolcollector.domain.like.Likes;
import com.idolcollector.idolcollector.domain.like.LikesRepository;
import com.idolcollector.idolcollector.domain.member.Member;
import com.idolcollector.idolcollector.domain.member.MemberRepository;
import com.idolcollector.idolcollector.domain.post.Post;
import com.idolcollector.idolcollector.domain.post.PostRepository;
import com.idolcollector.idolcollector.domain.rank.Ranks;
import com.idolcollector.idolcollector.domain.rank.RanksRepository;
import com.idolcollector.idolcollector.domain.scrap.Scrap;
import com.idolcollector.idolcollector.domain.scrap.ScrapRepository;
import com.idolcollector.idolcollector.domain.tag.TagRepository;
import com.idolcollector.idolcollector.domain.trending.Trending;
import com.idolcollector.idolcollector.domain.trending.TrendingRepository;
import com.idolcollector.idolcollector.domain.trending.TrendingType;
import com.idolcollector.idolcollector.web.dto.post.HomePostListResponseDto;
import com.idolcollector.idolcollector.web.dto.post.PostResponseDto;
import com.idolcollector.idolcollector.web.dto.post.PostSaveRequestDto;
import com.idolcollector.idolcollector.web.dto.post.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {

    private final TagService tagService;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final RanksRepository ranksRepository;
    private final ScrapRepository scrapRepository;
    private final LikesRepository likesRepository;
    private final TrendingRepository trendingRepository;

    @Transactional
    public Long create(PostSaveRequestDto form) {
        Member member = memberRepository.findById(form.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + form.getMemberId()));

        // 지금은 form에 memberID있지만 세션에서 받아오는 걸로 수정할 것.

        // Post 저장.
        Post savedPost = postRepository.save(new Post(member, form.getTitle(), form.getContent(), form.getStoreFileName(), form.getOriFileName()));

        // 태그 저장.
        tagService.createPostTag(form.getTags(), savedPost);

        return savedPost.getId();
    }

    public PostResponseDto detail(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + id));

        // 조회수 증가.
        post.addView();

        /*
        양방향 연관관계 설정하기 전. 쿼리가 많이 나가 성능 저하 우려가있음.
        // 댓글 넣기.
        List<CommentResponseDto> comments = commentService.findAllInPost(id);

        // 태그 넣기.
        List<TagResponseDto> tags = tagService.findAllinPost(id);
        */

        PostResponseDto postResponseDto = new PostResponseDto(post);
        // 세션유저 좋아요 눌렀는지, 스크랩했는지 유무 확인.
        // postResponseDto.didLike();
        // postResponseDto.didScrap();

        return postResponseDto;
    }

    @Transactional
    public Long update(PostUpdateRequestDto form) {
        Post post = postRepository.findById(form.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + form.getPostId()));

        // 세션유저 일치 확인.

        post.update(form);

        tagRepository.deleteAllByPostId(form.getPostId());

        if (form.getTags() != null) tagService.createPostTag(form.getTags(), post);

        return post.getId();
    }

    @Transactional
    public Long delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + id));

        // 세션 유저일치 확인.

        postRepository.delete(post);

        return id;
    }

    public HomePostListResponseDto postList() {

        // 1주일내 좋아요를 많이 받은 카드 순으로 출력.

        /**
         * PK, 일자, postId, 점수가 담긴 테이블을 만든다.
         * 조회시 1점, 좋아요시 5점, 댓글 3점 등등 점수를 준다.
         * 주간 SUM(점수)로 순위를 매겨 받아온다.
         */

        return new HomePostListResponseDto();
    }

    @Transactional
    public int like(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + id));

                // 세션에서 로그인유저 받아올 것.
                    // 임시 코드
                    Ranks rank = new Ranks("ROLL_USER");
                    ranksRepository.save(rank);
                    Member member = new Member(rank, "pressLike", "email", "1111", "pressLike", "dsfsdfdsfdsf", LocalDateTime.now());
                    memberRepository.save(member);

        Optional<Likes> isDup = likesRepository.findLikeByMemberIdPostId(post.getId(), member.getId(), LikeType.POST);

        if (isDup.isPresent()) {
            return post.getLikes();
        }

        Likes likes = new Likes(post.getId(), member, LikeType.POST);
        likesRepository.save(likes);

        // 추천 기록 테이블
        trendingRepository.save(new Trending(post, TrendingType.COMMENT));

        return post.addLike();
    }

    @Transactional
    public int view(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + id));


        // 추천 기록 테이블
        trendingRepository.save(new Trending(post, TrendingType.VIEW));

        return post.addView();
    }

    @Transactional
    public Long scrap(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + id));

            // 세션에서 회원 정보 받아오기.
                // 임시 코드
                Ranks rank = new Ranks("ROLL_USER");
                ranksRepository.save(rank);
                Member member = new Member(rank, "scrapper", "email", "1111", "scrapper", "dsfsdfdsfdsf", LocalDateTime.now());
                memberRepository.save(member);

        Scrap scrap = new Scrap(member, post);
        Scrap save = scrapRepository.save(scrap);

        // 추천 기록 테이블
        trendingRepository.save(new Trending(post, TrendingType.SCRAP));

        return save.getId();
    }

}
