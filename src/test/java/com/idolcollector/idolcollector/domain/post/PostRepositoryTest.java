package com.idolcollector.idolcollector.domain.post;

import com.idolcollector.idolcollector.domain.comment.Comment;
import com.idolcollector.idolcollector.domain.comment.CommentRepository;
import com.idolcollector.idolcollector.domain.member.Member;
import com.idolcollector.idolcollector.domain.member.MemberRepository;
import com.idolcollector.idolcollector.domain.member.MemberRole;
import com.idolcollector.idolcollector.domain.nestedcomment.NestedComment;
import com.idolcollector.idolcollector.domain.nestedcomment.NestedCommentRepository;
import com.idolcollector.idolcollector.domain.posttag.PostTag;
import com.idolcollector.idolcollector.domain.posttag.PostTagRepository;
import com.idolcollector.idolcollector.domain.scrap.Scrap;
import com.idolcollector.idolcollector.domain.scrap.ScrapRepository;
import com.idolcollector.idolcollector.domain.tag.Tag;
import com.idolcollector.idolcollector.domain.tag.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
class PostRepositoryTest {

    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired TagRepository tagRepository;
    @Autowired PostTagRepository postTagRepository;
    @Autowired ScrapRepository scrapRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired NestedCommentRepository nestedCommentRepository;


    @BeforeEach
    void before() {
        Member member = new Member(MemberRole.USER, "qqqqeeeererwr#@#wr13", "email", "1111", "steve", "dsfsdfdsfdsf", LocalDateTime.now());
        memberRepository.save(member);
        Tag tag = new Tag("qqqqeeeererwr#@#wr13");
        tagRepository.save(tag);
    }

    @Test
    void 저장_조회() {

        //Given
        Member member = memberRepository.findByNickName("qqqqeeeererwr#@#wr13").get();

        Post post = new Post(member, "title", "content", "storeFilename", "oriFileName");

        //When
        Post save = postRepository.save(post);
        Post find = postRepository.findById(save.getId()).get();
        postRepository.flush();

        //Then
        assertThat(find.getId()).isEqualTo(post.getId());
        assertThat(find.getTitle()).isEqualTo("title");
    }


    @Test
    void 삭제() {

        //Given
        Member member = memberRepository.findByNickName("qqqqeeeererwr#@#wr13").get();

        Post post = new Post(member, "title", "content", "storeFilename", "oriFileName");
        Post save = postRepository.save(post);
        postRepository.flush();

        //When
        postRepository.delete(save);
        Optional<Post> result = postRepository.findById(save.getId());

        //Then
        assertThat(result.isPresent()).isEqualTo(false);
    }


    @Test
    void 회원_게시글_모두조회() {

        //Given
        Member member = memberRepository.findByNickName("qqqqeeeererwr#@#wr13").get();

        Post post1 = new Post(member, "title1", "content1", "storeFilename1", "oriFileName1");
        Post post2 = new Post(member, "title2", "content2", "storeFilename2", "oriFileName2");

        postRepository.save(post1);
        postRepository.save(post2);

        //When
        List<Post> posts = postRepository.findAllInMember(member.getId(), PageRequest.of(0, 15));

        //Then
        assertThat(posts.size()).isEqualTo(2);
    }


    @Test
    void 태그로_모든_게시글_조회() {

        //Given
        Member member = memberRepository.findByNickName("qqqqeeeererwr#@#wr13").get();

        Post post = new Post(member, "title1", "content1", "storeFilename1", "oriFileName1");
        postRepository.save(post);

        Tag tag = tagRepository.findByName("qqqqeeeererwr#@#wr13").get();

        PostTag postTag = new PostTag(post, tag);
        postTagRepository.save(postTag);

        //When
        List<Post> posts = postRepository.findAllInTag(tag.getName());

        //Then
        assertThat(posts.size()).isEqualTo(1);
    }


    @Test
    void 회원이_스크랩한_모든_게시글_조회() {

        //Given
        Member member = memberRepository.findByNickName("qqqqeeeererwr#@#wr13").get();

        Post post = new Post(member, "title1", "content1", "storeFilename1", "oriFileName1");
        postRepository.save(post);

        Scrap scrap = new Scrap(member, post);
        scrapRepository.save(scrap);

        //When
        List<Post> posts = postRepository.findAllInScrap(member.getId());

        //Then
        assertThat(posts.size()).isEqualTo(1);
    }
    

    void testtest() {
        Member member = memberRepository.findByNickName("qqqqeeeererwr#@#wr13").get();

        Post post = postRepository.save(new Post(member, "title1", "content1", "storeFilename1", "oriFileName1"));

        Comment comment = commentRepository.save(new Comment(member, post, "content"));
        Comment comment2 = commentRepository.save(new Comment(member, post, "content2"));



        List<Object[]> objects = postRepository.multipleQ(post.getId());

        for (Object[] object : objects) {
            Post post1 = (Post) object[0];
            Comment comment1 = (Comment) object[1];
            System.out.println("post1.getTitle() = " + post1.getTitle());
            System.out.println("comment1.getContent() = " + comment1.getContent());
        }

    }


    void testtest2() {
        Member member = memberRepository.findByNickName("qqqqeeeererwr#@#wr13").get();

        Post post = postRepository.save(new Post(member, "title1", "content1", "storeFilename1", "oriFileName1"));

        Comment comment = commentRepository.save(new Comment(member, post, "content"));
        Comment comment2 = commentRepository.save(new Comment(member, post, "content2"));


        NestedComment nComment = new NestedComment(member, comment, "nContent");
        NestedComment nComment2 = new NestedComment(member, comment, "nContent2");

        NestedComment save = nestedCommentRepository.save(nComment);
        NestedComment save2 = nestedCommentRepository.save(nComment2);


        List<Object[]> objects = postRepository.multipleQ2(post.getId());

        for (Object[] object : objects) {



            Post post1 = (Post) object[0];
            Comment comment1 = (Comment) object[1];
            NestedComment nestedComment = (NestedComment) object[2];
            System.out.println("post1.getTitle() = " + post1.getTitle());
            System.out.println("comment1.getContent() = " + comment1.getContent());
            System.out.println("nestedComment.getContent() = " + nestedComment.getContent());
        }

    }
}