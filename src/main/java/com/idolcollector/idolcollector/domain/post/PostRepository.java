package com.idolcollector.idolcollector.domain.post;

import com.idolcollector.idolcollector.domain.member.Member;
import com.idolcollector.idolcollector.domain.nestedcomment.NestedComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByTitle(String title);

    @Query("select pt.post from PostTag pt join pt.tag t where t.name = :tagName")
    List<Post> findAllInTag(@Param("tagName") String tagName);

    @Query("select s.post from Scrap s where s.member.id = :memberId")
    List<Post> findAllInScrap(@Param("memberId") Long memberId);

    @Query("select p from Post p order by p.id desc")
    Page<Post> findAll(Pageable pageable);


    @Query(value = "(SELECT post.id, post.content, post.create_date, post.likes, post.modify_date, post.ori_file_name, post.store_file_name, post.title, post.views, post.member_id FROM trending LEFT JOIN post ON trending.post_id = post.id WHERE trending.create_date > :time GROUP BY post_id ORDER BY SUM(score) desc, post.id desc limit 1000000) UNION (SELECT * FROM post ORDER BY post.create_date desc limit 1000000)",
            nativeQuery = true)
    List<Post> findTrendingAll(@Param("time") LocalDateTime time, Pageable pageable);

    @Query(value = "select post.id, post.content, post.create_date, post.likes, post.modify_date, post.ori_file_name, post.store_file_name, post.title, post.views, post.member_id from post_tag left join tag on post_tag.tag_id = tag.id left join post on post.id = post_tag.post_id left join trending on trending.post_id = post.id where tag.name in :keywords and trending.create_date > :time group by post_tag.post_id ORDER BY SUM(score) desc, post_tag.post_id desc",
            nativeQuery = true)
    List<Post> findTrendingSearch(@Param("time") LocalDateTime time, @Param("keywords") List<String> keywords, Pageable pageable);

    @Query(value = "select * from (select * from post where post.member_id = :memberId UNION select post.id, post.content, post.create_date, post.likes, post.modify_date, post.ori_file_name, post.store_file_name, post.title, post.views, post.member_id from scrap left join post on scrap.post_id = post.id where scrap.member_id = :memberId) as b order by b.create_date",
            nativeQuery = true)
    List<Post> findAllInMember(@Param("memberId") Long memberId, Pageable pageable);

    /**
     *
     * 아래 코드는 사용하지 않는 코드입니다.
     *
     */
    @Query("select p, c from Comment c, Post p where p.id = :postId")
    List<Object[]> multipleQ(@Param("postId") Long postId);

    @Query("select p, c, nc from Comment c, Post p left outer join NestedComment nc on nc.comment.id = c.id where p.id = :postId")
    List<Object[]> multipleQ2(@Param("postId") Long postId);
}


