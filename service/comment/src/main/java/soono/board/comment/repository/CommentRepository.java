package soono.board.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soono.board.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = """
                SELECT COUNT(*)
                FROM (
                    SELECT comment_id
                    FROM comment
                    WHERE article_id = :articleId
                    AND parent_comment_id = :parentCommentId
                    LIMIT :limit
                ) t
            """, nativeQuery = true)
    Long countBy(@Param("articleId") Long articleId,
                 @Param("parentCommentId") Long parentCommentId,
                 @Param("limit") Long limit);
}
