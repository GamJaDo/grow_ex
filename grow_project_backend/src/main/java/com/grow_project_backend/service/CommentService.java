package com.grow_project_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.grow_project_backend.dto.CommentDto;
import com.grow_project_backend.entity.CommentEntity;
import com.grow_project_backend.entity.PostEntity;
import com.grow_project_backend.entity.UserEntity;
import com.grow_project_backend.repository.CommentRepository;
import com.grow_project_backend.repository.PostRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;

    public CommentEntity addComment(Long postId, String content, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 해야 댓글을 작성할 수 있습니다.");
        }
        
        PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));
        
        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContents(content);
        
        return commentRepository.save(comment);
    }
    
    public List<CommentDto> getCommentsByPostId(Long postId) {
        PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));
        
        List<CommentEntity> comments = commentRepository.findByPost(post); // postId 대신 post 엔티티 사용

        return comments.stream()
        	    .map(comment -> new CommentDto(comment.getContents()))
        	    .collect(Collectors.toList());
    }
}

